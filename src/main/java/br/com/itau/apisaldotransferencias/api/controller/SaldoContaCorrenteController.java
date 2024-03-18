package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.SaldoContaCorrenteResponse;
import br.com.itau.apisaldotransferencias.core.flow.SaldoContaCorrenteFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
public class SaldoContaCorrenteController {

    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;
    private static final Logger logger = LoggerFactory.getLogger(SaldoContaCorrenteController.class);

    public SaldoContaCorrenteController(SaldoContaCorrenteFlow saldoContaCorrenteFlow) {
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
    }

    @GetMapping("/v1/saldos/{numeroContaCorrente}")
    public Mono<ResponseEntity<SaldoContaCorrenteResponse>> getSaldoContaCorrente(@PathVariable String numeroContaCorrente) {
        logger.info("Iniciando consulta para o número da conta corrente: {}", numeroContaCorrente);

        return saldoContaCorrenteFlow.fetchSaldoContaCorrente(numeroContaCorrente)
                .map(cc -> {
                    logger.info("Saldo da conta corrente encontrado para o número da conta: {}", cc.getNumContaCorrente());
                    return ResponseEntity.ok(new SaldoContaCorrenteResponse(cc.getNumContaCorrente(), cc.getValLimiteDisponivel(), cc.getValLimiteDiario(), cc.getValSaldoContaCorrente()));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    logger.error("Erro ao consultar o saldo da conta corrente: ", e);
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao consultar o saldo da conta corrente no banco de dados", e));
                });

    }
}
