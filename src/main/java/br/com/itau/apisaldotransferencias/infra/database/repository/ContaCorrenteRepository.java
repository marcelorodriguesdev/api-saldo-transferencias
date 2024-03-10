package br.com.itau.apisaldotransferencias.infra.database.repository;

import br.com.itau.apisaldotransferencias.infra.database.entity.ContaCorrenteEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ContaCorrenteRepository extends ReactiveCrudRepository<ContaCorrenteEntity, String> {
    Mono<ContaCorrenteEntity> findByNumContaCorrente(String numContaCorrente);
}
