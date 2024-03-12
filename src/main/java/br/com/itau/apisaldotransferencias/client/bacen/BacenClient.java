package br.com.itau.apisaldotransferencias.client.bacen;

import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class BacenClient {

    private final RestTemplate restTemplate;
    private final Random random;

    public BacenClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.random = new Random();
    }

    public ResponseEntity<String> enviarTransferencia(BacenRequest request) {

        // Simula o rate limit
        if (random.nextInt(10) == 0) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit atingido.");
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("mensagem", "Transação notificada com sucesso ao banco central");
        responseMap.put("horarioNotificacao", LocalDateTime.now());
        return ResponseEntity.ok(responseMap.toString());
    }
}
