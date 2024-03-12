package br.com.itau.apisaldotransferencias.client.cadastro;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class CadastroClient {

    private final RestTemplate restTemplate;

    public CadastroClient() {
        this.restTemplate = new RestTemplate();
    }

    public Mono<CadastroResponse> getCadastro(String numeroConta) {
        return Mono.just(numeroConta)
                .flatMap(conta -> {
                    switch (conta) {
                        case "123":
                            return Mono.just(new CadastroResponse("Fulano de Tal", "ATIVA", "2005-06-06"));
                        case "456":
                            return Mono.just(new CadastroResponse("Cliente Itau", "BLOQUEADA", "2020-03-03"));
                        default:
                            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cadastro do cliente com o número " + numeroConta + " não foi encontrado."));
                    }
                });
    }

}
