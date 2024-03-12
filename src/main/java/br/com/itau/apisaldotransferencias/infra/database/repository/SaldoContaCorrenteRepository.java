package br.com.itau.apisaldotransferencias.infra.database.repository;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SaldoContaCorrenteRepository extends ReactiveCrudRepository<SaldoContaCorrenteEntity, String> {
    Mono<SaldoContaCorrenteEntity> findByNumContaCorrente(String numContaCorrente);
}
