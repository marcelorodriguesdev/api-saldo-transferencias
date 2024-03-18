package br.com.itau.apisaldotransferencias.core.flow;

import static org.mockito.Mockito.*;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.web.server.ResponseStatusException;

public class SaldoContaCorrenteFlowTest {

    @Mock
    private SaldoContaCorrenteRepository repository;

    @InjectMocks
    private SaldoContaCorrenteFlow flow;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnSaldoWhenFoundSaldoContaCorrente() {
        String numeroContaCorrente = "12345";
        SaldoContaCorrenteEntity expectedSaldo = new SaldoContaCorrenteEntity();
        when(repository.findByNumContaCorrente(numeroContaCorrente)).thenReturn(expectedSaldo);

        Mono<SaldoContaCorrenteEntity> result = flow.fetchSaldoContaCorrente(numeroContaCorrente);

        StepVerifier.create(result)
                .expectNext(expectedSaldo)
                .verifyComplete();

        verify(repository).findByNumContaCorrente(numeroContaCorrente);
    }

    @Test
    public void shouldReturnEmptyMonoWhenNotFoundSaldoContaCorrente() {
        String numeroContaCorrente = "00000";
        when(repository.findByNumContaCorrente(numeroContaCorrente)).thenReturn(null);

        Mono<SaldoContaCorrenteEntity> result = flow.fetchSaldoContaCorrente(numeroContaCorrente);

        StepVerifier.create(result)
                .expectError(ResponseStatusException.class)
                .verify();

        verify(repository).findByNumContaCorrente(numeroContaCorrente);
    }

}
