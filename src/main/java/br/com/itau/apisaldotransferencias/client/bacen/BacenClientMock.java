package br.com.itau.apisaldotransferencias.client.bacen;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class BacenClientMock {

    private final RestTemplate restTemplate;
    private final Random random;

    public BacenClientMock(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.random = new Random();
    }

    public Mono<BacenResponse> notificarBacen(BacenRequest request) {
        return Mono.fromCallable(() -> {
            if (isRateLimit()) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit atingido.");
            }

            BacenResponse response = new BacenResponse();
            response.setMensagem("Transação notificada com sucesso ao banco central");
            response.setDataNotificacao(LocalDateTime.now().toString());
            return response;
        });
    }

    private boolean isRateLimit() {
        return random.nextInt(10) == 0;
    }
}
