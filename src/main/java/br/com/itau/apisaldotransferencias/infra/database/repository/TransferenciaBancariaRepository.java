package br.com.itau.apisaldotransferencias.infra.database.repository;

import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransferenciaBancariaRepository extends ReactiveCrudRepository<TransferenciaBancariaEntity, String> {
    Mono<TransferenciaBancariaEntity> findByCodTransferenciaBancaria(String codTransferenciaBancaria);
}
