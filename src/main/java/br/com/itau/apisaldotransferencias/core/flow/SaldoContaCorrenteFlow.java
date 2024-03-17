package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SaldoContaCorrenteFlow {

    private static final Logger log = LoggerFactory.getLogger(SaldoContaCorrenteFlow.class);
    private final SaldoContaCorrenteRepository repository;

    public SaldoContaCorrenteFlow(SaldoContaCorrenteRepository repository) {
        this.repository = repository;
    }

    public Mono<SaldoContaCorrenteEntity> fetchSaldoContaCorrente(String numeroContaCorrente) {
        log.info("Iniciando busca pelo saldo da conta corrente: {}", numeroContaCorrente);
        return Mono.just(repository.findByNumContaCorrente(numeroContaCorrente))
                .doOnSuccess(saldo -> log.info("Saldo da conta corrente {} encontrado com sucesso.", numeroContaCorrente))
                .doOnError(e -> log.error("Erro ao buscar o saldo da conta corrente {}.", numeroContaCorrente, e));
    }
}

