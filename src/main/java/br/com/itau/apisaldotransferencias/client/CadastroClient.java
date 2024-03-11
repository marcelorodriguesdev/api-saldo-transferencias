package br.com.itau.apisaldotransferencias.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
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

    static class CadastroResponse {
        private String nome;
        private String situacaoContaCorrente;
        private String dataAberturaContaCorrente;

        public CadastroResponse(String nome, String situacaoContaCorrente, String dataAberturaContaCorrente) {
            this.nome = nome;
            this.situacaoContaCorrente = situacaoContaCorrente;
            this.dataAberturaContaCorrente = dataAberturaContaCorrente;
        }
    }
}
