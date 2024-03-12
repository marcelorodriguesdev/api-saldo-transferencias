package br.com.itau.apisaldotransferencias.api.controller;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.core.flow.SaldoContaCorrenteFlow;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransferenciaBancariaController {

    @PostMapping("/transferencias")
    public Mono<ResponseEntity<?>> realizarTransferencia(@Valid @RequestBody TransferenciaRequest request) {
        // Aqui você pode realizar a lógica de negócios para a transferência
        // Por enquanto, apenas retornamos uma resposta de sucesso
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("Transferência realizada com sucesso"));
    }
}
