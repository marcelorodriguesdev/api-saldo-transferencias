package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaResponse;
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

@RestController("/v1/transferencias")
public class TransferenciaBancariaController {

    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaController.class);
    private TransferenciaBancariaFlow transferenciaBancariaFlow;

    public TransferenciaBancariaController(TransferenciaBancariaFlow transferenciaBancariaFlow) {
        this.transferenciaBancariaFlow = transferenciaBancariaFlow;
    }

    @PostMapping
    public Mono<ResponseEntity<TransferenciaBancariaResponse>> performTransferenciaBancaria(@Valid @RequestBody TransferenciaBancariaRequest request) {
        logger.info("Iniciando transferência: {}", request);
        return transferenciaBancariaFlow.performTransferenciaBancaria(request)
                .doOnNext(response -> logger.info("Transferência realizada com sucesso: {}", response))
                .map(transferenciaResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transferenciaResponse));
    }

    @GetMapping("/{idTransferenciaBancaria}")
    public Mono<ResponseEntity<TransferenciaBancariaResponse>> getTransferenciaBancaria(@PathVariable String idTransferenciaBancaria) {
        logger.info("Consultando transferência: {}", idTransferenciaBancaria);
        return transferenciaBancariaFlow.fetchTransferenciaBancaria(idTransferenciaBancaria)
                .doOnNext(response -> logger.info("Consulta realizada com sucesso: {}", response))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
