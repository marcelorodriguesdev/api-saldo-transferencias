package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import br.com.itau.apisaldotransferencias.infra.database.repository.TransferenciaBancariaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransferenciaBancariaService {

    private final TransferenciaBancariaRepository transferenciaRepository;
    private final SaldoContaCorrenteRepository saldoContaCorrenteRepository;

    public TransferenciaBancariaService(TransferenciaBancariaRepository transferenciaRepository, SaldoContaCorrenteRepository saldoContaCorrenteRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.saldoContaCorrenteRepository = saldoContaCorrenteRepository;
    }

    public Mono<TransferenciaBancariaEntity> createTransferencia(TransferenciaRequest request, SaldoContaCorrenteEntity saldoContaCorrente, CadastroResponse cadastro) {
        return Mono.just(request)
                .filter(req -> req.getCodigoBancoDestino().equals("184"))
                .flatMap(req -> {
                    atualizaSaldoBancarioDoPagador(saldoContaCorrente, req);

                    return saldoContaCorrenteRepository.save(saldoContaCorrente)
                            .then(persistTransferenciaBancaria(req));
                })
                .switchIfEmpty(persistTransferenciaBancaria(request));
    }

    private static void atualizaSaldoBancarioDoPagador(SaldoContaCorrenteEntity saldoContaCorrente, TransferenciaRequest request) {
        saldoContaCorrente.setValLimiteDisponivel(saldoContaCorrente.getValLimiteDisponivel().subtract(request.getValor()));
        saldoContaCorrente.setValLimiteDiario(saldoContaCorrente.getValLimiteDiario().subtract(request.getValor()));
        saldoContaCorrente.setValSaldoContaCorrente(saldoContaCorrente.getValSaldoContaCorrente().subtract(request.getValor()));
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
}
