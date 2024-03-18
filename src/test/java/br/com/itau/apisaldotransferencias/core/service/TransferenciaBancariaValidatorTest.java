package br.com.itau.apisaldotransferencias.core.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import org.springframework.web.server.ResponseStatusException;

class TransferenciaBancariaValidatorTest {

    private SaldoContaCorrenteEntity saldoContaCorrente;
    private CadastroResponse cadastroResponse;
    private TransferenciaContext context;
    private TransferenciaRequest transferenciaRequest;
    private TransferenciaBancariaValidator validator;

    @BeforeEach
    public void setUp() {
        saldoContaCorrente = mock(SaldoContaCorrenteEntity.class);
        cadastroResponse = mock(CadastroResponse.class);
        context = mock(TransferenciaContext.class);
        transferenciaRequest = mock(TransferenciaRequest.class);
        validator = new TransferenciaBancariaValidator();

        when(context.getSaldoContaCorrente()).thenReturn(saldoContaCorrente);
        when(context.getCadastroResponse()).thenReturn(cadastroResponse);
    }

    @Test
    public void shouldCompleteWithValidContext_whenTransferenciaIsValid() {
        // Arrange
        when(saldoContaCorrente.getValLimiteDiario()).thenReturn(BigDecimal.valueOf(2000));
        when(saldoContaCorrente.getValLimiteDisponivel()).thenReturn(BigDecimal.valueOf(1500));
        when(cadastroResponse.getSituacaoContaCorrente()).thenReturn("ATIVA");
        when(transferenciaRequest.getValor()).thenReturn(BigDecimal.valueOf(1000));

        // Act
        Mono<TransferenciaContext> result = validator.validarTransferencia(transferenciaRequest, context);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(contexto -> contexto.equals(context))
                .verifyComplete();
    }

    @Test
    public void shouldThrowBadRequest_whenLimiteDiarioIsExceeded() {
        when(saldoContaCorrente.getValLimiteDiario()).thenReturn(BigDecimal.valueOf(2000));
        when(saldoContaCorrente.getValLimiteDisponivel()).thenReturn(BigDecimal.valueOf(1500));
        when(cadastroResponse.getSituacaoContaCorrente()).thenReturn("ATIVA");
        when(transferenciaRequest.getValor()).thenReturn(BigDecimal.valueOf(3000));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            validator.validarTransferencia(transferenciaRequest, context).block();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Valor da transferência excede o limite diário."));
    }

    @Test
    public void shouldThrowBadRequest_whenLimiteDisponivelIsExceeded() {
        when(saldoContaCorrente.getValLimiteDiario()).thenReturn(BigDecimal.valueOf(2000));
        when(saldoContaCorrente.getValLimiteDisponivel()).thenReturn(BigDecimal.valueOf(1500));
        when(cadastroResponse.getSituacaoContaCorrente()).thenReturn("ATIVA");
        when(transferenciaRequest.getValor()).thenReturn(BigDecimal.valueOf(1600));

        Throwable exception = assertThrows(ResponseStatusException.class, () -> {
            validator.validarTransferencia(transferenciaRequest, context).block();
        });

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) exception).getStatusCode());
        assertTrue(exception.getMessage().contains("Valor da transferência excede o limite disponível."));
    }

    @Test
    public void shouldThrowForbidden_whenContaIsBlocked() {
        // Arrange
        when(saldoContaCorrente.getValLimiteDiario()).thenReturn(BigDecimal.valueOf(2000));
        when(saldoContaCorrente.getValLimiteDisponivel()).thenReturn(BigDecimal.valueOf(1500));
        when(cadastroResponse.getSituacaoContaCorrente()).thenReturn("BLOQUEADA");
        when(transferenciaRequest.getValor()).thenReturn(BigDecimal.valueOf(1000));

        Throwable exception = assertThrows(ResponseStatusException.class, () -> {
            validator.validarTransferencia(transferenciaRequest, context).block();
        });

        assertEquals(HttpStatus.FORBIDDEN, ((ResponseStatusException) exception).getStatusCode());
        assertTrue(exception.getMessage().contains("A transferência não pode ser realizada pois a conta corrente está bloqueada."));
    }
}

