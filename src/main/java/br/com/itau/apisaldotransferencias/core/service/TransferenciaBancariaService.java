package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.TransferenciaBancariaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransferenciaBancariaService {

    private final TransferenciaBancariaRepository repository;

    public TransferenciaBancariaService(TransferenciaBancariaRepository repository) {
        this.repository = repository;
    }

    public Mono<TransferenciaBancariaEntity> createTransferencia(TransferenciaRequest request) {
        TransferenciaBancariaEntity transferencia = new TransferenciaBancariaEntity();

        transferencia.setCodTransferenciaBancaria(UUID.randomUUID().toString());
        transferencia.setNumContaOrigem(request.getContaOrigem());
        transferencia.setCodBancoDestino(request.getCodigoBancoDestino());
        transferencia.setNumContaDestino(request.getContaDestino());
        transferencia.setDatHorarioDaTransferencia(Instant.now().toString());
        transferencia.setValTransferencia(request.getValor());
        return repository.save(transferencia);
    }

}
