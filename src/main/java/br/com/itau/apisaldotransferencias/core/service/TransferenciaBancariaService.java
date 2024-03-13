package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenClientMock;
import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import br.com.itau.apisaldotransferencias.infra.database.repository.TransferenciaBancariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class TransferenciaBancariaService {

    private final TransferenciaBancariaRepository transferenciaRepository;
    private final SaldoContaCorrenteRepository saldoContaCorrenteRepository;
    private final BacenClientMock bacenClient;

    public TransferenciaBancariaService(TransferenciaBancariaRepository transferenciaRepository, SaldoContaCorrenteRepository saldoContaCorrenteRepository, BacenClientMock bacenClient) {
        this.transferenciaRepository = transferenciaRepository;
        this.saldoContaCorrenteRepository = saldoContaCorrenteRepository;
        this.bacenClient = bacenClient;
    }

    public Mono<TransferenciaBancariaEntity> createTransferencia(TransferenciaRequest request, SaldoContaCorrenteEntity saldoContaCorrente, CadastroResponse cadastro) {
        return atualizaSaldoBancarioDoPagadorEPersiste(saldoContaCorrente, request)
                .zipWith(persistTransferenciaBancaria(request))
                //TODO adiciona o BacenRequest ao contexto
                .flatMap(tuple -> {
                    BacenRequest bacenRequest = montaBacenRequest(request, cadastro, tuple);

                    return notificaTransferenciaAoBacen(bacenRequest)
                            .then(Mono.just(tuple.getT2()));
                })
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao processar a transferência", e)));
    }

    private Mono<SaldoContaCorrenteEntity> atualizaSaldoBancarioDoPagadorEPersiste(SaldoContaCorrenteEntity saldoContaCorrente, TransferenciaRequest request) {
        saldoContaCorrente.setValLimiteDisponivel(saldoContaCorrente.getValLimiteDisponivel().subtract(request.getValor()));
        saldoContaCorrente.setValLimiteDiario(saldoContaCorrente.getValLimiteDiario().subtract(request.getValor()));
        saldoContaCorrente.setValSaldoContaCorrente(saldoContaCorrente.getValSaldoContaCorrente().subtract(request.getValor()));

        return saldoContaCorrenteRepository.save(saldoContaCorrente);
    }

    private Mono<TransferenciaBancariaEntity> persistTransferenciaBancaria(TransferenciaRequest req) {
        TransferenciaBancariaEntity transferencia = new TransferenciaBancariaEntity();

        transferencia.setCodTransferenciaBancaria(UUID.randomUUID().toString());
        transferencia.setNumContaOrigem(req.getContaOrigem());
        transferencia.setCodBancoDestino(req.getCodigoBancoDestino());
        transferencia.setNumContaDestino(req.getContaDestino());
        transferencia.setDatHorarioDaTransferencia(Instant.now().toString());
        transferencia.setValTransferencia(req.getValor());

        return transferenciaRepository.save(transferencia);
    }

    public Mono<BacenResponse> notificaTransferenciaAoBacen(BacenRequest request) {
        return bacenClient.notificarBacen(request)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof ResponseStatusException)
                        .filter(throwable -> ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            // Em ultimo caso enviar para uma fila DLQ
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível processar a transação após várias tentativas.", retrySignal.failure());
                        }));
    }

    private static BacenRequest montaBacenRequest(TransferenciaRequest request, CadastroResponse cadastro, Tuple2<SaldoContaCorrenteEntity, TransferenciaBancariaEntity> tuple) {
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
