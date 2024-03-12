package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.SaldoContaCorrenteResponse;
import br.com.itau.apisaldotransferencias.core.flow.SaldoContaCorrenteFlow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SaldoContaCorrenteController {

    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;

    public SaldoContaCorrenteController(SaldoContaCorrenteFlow saldoContaCorrenteFlow) {
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
    }

    @GetMapping("/saldos/{numeroContaCorrente}")
    public Mono<ResponseEntity<SaldoContaCorrenteResponse>> getSaldoContaCorrente(@PathVariable String numeroContaCorrente) {

        return saldoContaCorrenteFlow.fetchSaldoContaCorrente(numeroContaCorrente)
                .map(cc -> ResponseEntity.ok(new SaldoContaCorrenteResponse(cc.getNumContaCorrente(), cc.getValLimiteDisponivel(), cc.getValLimiteDiario(), cc.getValSaldoContaCorrente())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
