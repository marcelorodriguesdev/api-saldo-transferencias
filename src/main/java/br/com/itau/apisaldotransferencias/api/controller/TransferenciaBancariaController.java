package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
import br.com.itau.apisaldotransferencias.core.flow.TransferenciaBancariaFlow;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("/transferencias")
public class TransferenciaBancariaController {

    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaController.class);
    private TransferenciaBancariaFlow transferenciaBancariaFlow;

    public TransferenciaBancariaController(TransferenciaBancariaFlow transferenciaBancariaFlow) {
        this.transferenciaBancariaFlow = transferenciaBancariaFlow;
    }

    @PostMapping
    public Mono<ResponseEntity<TransferenciaResponse>> realizarTransferencia(@Valid @RequestBody TransferenciaRequest request) {
        logger.info("Iniciando transferência: {}", request);
        return transferenciaBancariaFlow.realizaTransferenciaBancaria(request)
                .doOnNext(response -> logger.info("Transferência realizada com sucesso: {}", response))
                .map(transferenciaResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transferenciaResponse));
    }

    @GetMapping("/{idTransferenciaBancaria}")
    public Mono<ResponseEntity<TransferenciaResponse>> consultarTransferencia(@PathVariable String idTransferenciaBancaria) {
        logger.info("Consultando transferência: {}", idTransferenciaBancaria);
        return transferenciaBancariaFlow.consultarTransferenciaBancaria(idTransferenciaBancaria)
                .doOnNext(response -> logger.info("Consulta realizada com sucesso: {}", response))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
