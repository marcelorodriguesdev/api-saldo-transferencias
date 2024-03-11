package br.com.itau.apisaldotransferencias.api.resource;

import br.com.itau.apisaldotransferencias.api.payload.SaldoContaCorrenteResponse;
import br.com.itau.apisaldotransferencias.infra.database.repository.ContaCorrenteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@RestController
public class SaldoContaCorrenteResource {

    private final ContaCorrenteRepository repository;

    public SaldoContaCorrenteResource(ContaCorrenteRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/conta-corrente/{numeroContaCorrente}")
    public Mono<ResponseEntity<SaldoContaCorrenteResponse>> getSaldoContaCorrente(@PathVariable String numeroContaCorrente) {

        return repository.findByNumContaCorrente(numeroContaCorrente)
                .map(cc -> ResponseEntity.ok(new SaldoContaCorrenteResponse(cc.getNumContaCorrente(), cc.getValLimiteDisponivel(), cc.getValLimiteDiario())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
