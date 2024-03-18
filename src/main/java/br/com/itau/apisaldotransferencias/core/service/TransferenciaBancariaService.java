package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaResponse;
import br.com.itau.apisaldotransferencias.client.bacen.BacenClientMock;
import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaBancariaContext;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import br.com.itau.apisaldotransferencias.infra.database.repository.TransferenciaBancariaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TransferenciaBancariaService {

    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaService.class);
    private final TransferenciaBancariaRepository transferenciaRepository;
    private final SaldoContaCorrenteRepository saldoContaCorrenteRepository;
    private final BacenClientMock bacenClient;

    public TransferenciaBancariaService(TransferenciaBancariaRepository transferenciaRepository, SaldoContaCorrenteRepository saldoContaCorrenteRepository, BacenClientMock bacenClient) {
        this.transferenciaRepository = transferenciaRepository;
        this.saldoContaCorrenteRepository = saldoContaCorrenteRepository;
        this.bacenClient = bacenClient;
    }

        public Mono<TransferenciaBancariaContext> createTransferencia(TransferenciaBancariaRequest request, TransferenciaBancariaContext context) {
            return atualizaSaldoBancarioDoPagadorEPersiste(context.getSaldoContaCorrente(), request)
                    .doOnSuccess(saldo -> logger.info("Saldo do pagador atualizado com sucesso."))
                    .zipWith(persistTransferenciaBancaria(request))
                    .doOnSuccess(transf -> logger.info("Transferência bancária persistida com sucesso."))
                    .flatMap(tuple -> {
                        BacenRequest bacenRequest = montaBacenRequest(request, context.getCadastroResponse(), tuple);
                        notificaTransferenciaAoBacen(bacenRequest)
                                .doOnSuccess(bacenResp -> logger.info("Notificação ao BACEN realizada com sucesso."))
                                .doOnError(e -> logger.error("Erro ao notificar o BACEN.", e));

                        context.setTransferenciaBancariaEntity(tuple.getT2());
                        context.setBacenRequest(bacenRequest);

                        return Mono.just(context);
                    })
                    .doOnSuccess(ctx -> logger.info("Contexto de transferência criado com sucesso."))
                    .onErrorResume(e -> {
                        logger.error("Erro ao processar a transferência.", e);
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar a transferência", e));
                    });
        }

    public Mono<TransferenciaBancariaResponse> getTransferencia(String idTransferenciaBancaria) {
        return Mono.fromCallable(() -> transferenciaRepository.findByCodTransferenciaBancaria(idTransferenciaBancaria))
                .doOnSuccess(transferencia -> {
                    if (transferencia != null) {
                        logger.info("Consulta da transferência realizada com sucesso.");
                    } else {
                        logger.error("Transferência bancária não encontrada.");
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transferência bancária não encontrada");
                    }
                })
                .map(transferencia -> parseEntityToResponse().apply(transferencia))
                .doOnError(e -> logger.error("Erro ao consultar a transferência bancária no banco de dados.", e))
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao consultar a transferencia bancaria no banco de dados", e));
                });
    }

    private static Function<TransferenciaBancariaEntity, TransferenciaBancariaResponse> parseEntityToResponse() {
        return entity -> {
            TransferenciaBancariaResponse response = new TransferenciaBancariaResponse();
            response.setCodigoTransferenciaBancaria(entity.getCodTransferenciaBancaria());
            response.setContaOrigem(entity.getNumContaOrigem());
            response.setCodigoBancoDestino(entity.getCodBancoDestino());
            response.setContaDestino(entity.getNumContaDestino());
            response.setData(entity.getDatHorarioDaTransferencia());
            response.setValor(entity.getValTransferencia());
            return response;
        };
    }


    private Mono<SaldoContaCorrenteEntity> atualizaSaldoBancarioDoPagadorEPersiste(SaldoContaCorrenteEntity saldoContaCorrente, TransferenciaBancariaRequest request) {
        saldoContaCorrente.setValLimiteDisponivel(saldoContaCorrente.getValLimiteDisponivel().subtract(request.getValor()));
        saldoContaCorrente.setValLimiteDiario(saldoContaCorrente.getValLimiteDiario().subtract(request.getValor()));
        saldoContaCorrente.setValSaldoContaCorrente(saldoContaCorrente.getValSaldoContaCorrente().subtract(request.getValor()));

        return Mono.just(saldoContaCorrenteRepository.save(saldoContaCorrente));
    }

    private Mono<TransferenciaBancariaEntity> persistTransferenciaBancaria(TransferenciaBancariaRequest req) {
        TransferenciaBancariaEntity transferencia = new TransferenciaBancariaEntity();

        transferencia.setCodTransferenciaBancaria(UUID.randomUUID().toString());
        transferencia.setNumContaOrigem(req.getContaOrigem());
        transferencia.setCodBancoDestino(req.getCodigoBancoDestino());
        transferencia.setNumContaDestino(req.getContaDestino());
        transferencia.setDatHorarioDaTransferencia(Instant.now().toString());
        transferencia.setValTransferencia(req.getValor());

        return Mono.just(transferenciaRepository.save(transferencia));
    }

    public Mono<BacenResponse> notificaTransferenciaAoBacen(BacenRequest request) {
        return bacenClient.notificarBacen(request)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof ResponseStatusException)
                        .filter(throwable -> ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            // Em ultimo caso enviar para uma fila DLQ para reprocessar
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível processar a transação após várias tentativas.", retrySignal.failure());
                        }));
    }

    private static BacenRequest montaBacenRequest(TransferenciaBancariaRequest request, CadastroResponse cadastro, Tuple2<SaldoContaCorrenteEntity, TransferenciaBancariaEntity> tuple) {
        BacenRequest bacenRequest = new BacenRequest();
        bacenRequest.setNumeroContaOrigem(request.getContaOrigem());
        bacenRequest.setCodigoBancoDestino(request.getCodigoBancoDestino());
        bacenRequest.setNumeroContaDestino(request.getContaDestino());
        bacenRequest.setDataHorarioDaTransferencia(tuple.getT2().getDatHorarioDaTransferencia());
        bacenRequest.setValTransferencia(request.getValor());
        bacenRequest.setIdTransferenciaBancaria(tuple.getT2().getCodTransferenciaBancaria());
        bacenRequest.setNomeCliente(cadastro.getNome());

        return bacenRequest;
    }

}
