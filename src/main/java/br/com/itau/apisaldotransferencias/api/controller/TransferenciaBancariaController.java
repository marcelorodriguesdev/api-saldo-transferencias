package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
import br.com.itau.apisaldotransferencias.core.flow.TransferenciaBancariaFlow;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransferenciaBancariaController {

    private TransferenciaBancariaFlow transferenciaBancariaFlow;

    public TransferenciaBancariaController(TransferenciaBancariaFlow transferenciaBancariaFlow) {
        this.transferenciaBancariaFlow = transferenciaBancariaFlow;
    }

    @PostMapping("/transferencias")
    public Mono<ResponseEntity<TransferenciaResponse>> realizarTransferencia(@Valid @RequestBody TransferenciaRequest request) {

        return transferenciaBancariaFlow.realizaTransferenciaBancaria(request)
                .map(transferenciaResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transferenciaResponse));
    }

}
