package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SaldoContaCorrenteFlow {

    private final SaldoContaCorrenteRepository repository;

    public SaldoContaCorrenteFlow(SaldoContaCorrenteRepository repository) {
        this.repository = repository;
    }

    public Mono<SaldoContaCorrenteEntity> fetchSaldoContaCorrente(String numeroContaCorrente) {

        return repository.findByNumContaCorrente(numeroContaCorrente);

    }

}
