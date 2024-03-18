package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class SaldoContaCorrenteFlow {

    private static final Logger log = LoggerFactory.getLogger(SaldoContaCorrenteFlow.class);
    private final SaldoContaCorrenteRepository repository;

    public SaldoContaCorrenteFlow(SaldoContaCorrenteRepository repository) {
        this.repository = repository;
    }

    public Mono<SaldoContaCorrenteEntity> fetchSaldoContaCorrente(String numeroContaCorrente) {
        log.info("Iniciando busca pelo saldo da conta corrente: {}", numeroContaCorrente);
        return Mono.justOrEmpty(repository.findByNumContaCorrente(numeroContaCorrente))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado.")))
                .onErrorResume(e -> {
                    log.error("Erro ao acessar o banco de dados", e);
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor."));
                });
    }

}
