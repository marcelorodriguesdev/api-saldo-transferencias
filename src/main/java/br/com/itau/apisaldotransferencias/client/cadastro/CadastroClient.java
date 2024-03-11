package br.com.itau.apisaldotransferencias.client.cadastro;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
public class CadastroClient {

    private final RestTemplate restTemplate;

    public CadastroClient() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<?> getCadastro(String numeroConta) {
        return switch (numeroConta) {
            case "123" -> ResponseEntity.ok(new CadastroResponse("Fulano de Tal", "ATIVA", "2005-06-06"));
            case "456" -> ResponseEntity.ok(new CadastroResponse("Cliente Itau", "BLOQUEADA", "2020-03-03"));
            default -> ResponseEntity.notFound().build();
        };
    }
}
