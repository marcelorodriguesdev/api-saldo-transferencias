package br.com.itau.apisaldotransferencias.core.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
import br.com.itau.apisaldotransferencias.client.bacen.BacenClientMock;
import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenResponse;
import br.com.itau.apisaldotransferencias.core.EntityFactoryTest;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import br.com.itau.apisaldotransferencias.infra.database.repository.SaldoContaCorrenteRepository;
import br.com.itau.apisaldotransferencias.infra.database.repository.TransferenciaBancariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.web.server.ResponseStatusException;

class TransferenciaBancariaServiceTest {

    @Mock
    private SaldoContaCorrenteRepository saldoContaCorrenteRepository;
    @Mock
    private TransferenciaBancariaRepository transferenciaBancariaRepository;
    @Mock
    private BacenClientMock bacenClient;
    @Mock
    private Logger logger;

    @InjectMocks
    private TransferenciaBancariaService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCompleteSuccessfullyWhenTransferenciaIsFound() {
        String idTransferenciaBancaria = "123";
        TransferenciaBancariaEntity foundEntity = EntityFactoryTest.createTransferenciaBancariaEntity();
        when(transferenciaBancariaRepository.findByCodTransferenciaBancaria(idTransferenciaBancaria))
                .thenReturn(foundEntity);

        Mono<TransferenciaResponse> result = service.getTransferencia(idTransferenciaBancaria);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCodigoTransferenciaBancaria().equals(foundEntity.getCodTransferenciaBancaria()))
                .verifyComplete();
    }

    @Test
    public void shouldThrowNotFoundWhenTransferenciaIsNotFound() {
        String idTransferenciaBancaria = "123";
        when(transferenciaBancariaRepository.findByCodTransferenciaBancaria(idTransferenciaBancaria))
                .thenReturn(null);

        StepVerifier.create(service.getTransferencia(idTransferenciaBancaria))
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }

    @Test
    public void shouldLogErrorAndThrowInternalServerErrorWhenDatabaseErrorOccurs() {
        String idTransferenciaBancaria = "123";
        RuntimeException databaseException = new RuntimeException("Database error");
        when(transferenciaBancariaRepository.findByCodTransferenciaBancaria(idTransferenciaBancaria))
                .thenThrow(databaseException);

        StepVerifier.create(service.getTransferencia(idTransferenciaBancaria))
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();
    }

    @Test
    public void shouldCreateTransferenciaContextWhenSuccessful() {
        TransferenciaRequest request = EntityFactoryTest.createTransferenciaRequest();
        TransferenciaContext context = EntityFactoryTest.createTransferenciaContext();
        SaldoContaCorrenteEntity saldoContaCorrente = EntityFactoryTest.createSaldoContaCorrenteEntity();
        TransferenciaBancariaEntity transferenciaBancaria = EntityFactoryTest.createTransferenciaBancariaEntity();
        BacenResponse bacenResponse = EntityFactoryTest.createBacenResponse();

        when(saldoContaCorrenteRepository.save(any(SaldoContaCorrenteEntity.class))).thenReturn(saldoContaCorrente);
        when(transferenciaBancariaRepository.save(any(TransferenciaBancariaEntity.class))).thenReturn(transferenciaBancaria);
        when(bacenClient.notificarBacen(any(BacenRequest.class))).thenReturn(Mono.just(bacenResponse));

        Mono<TransferenciaContext> result = service.createTransferencia(request, context);

        StepVerifier.create(result)
                .expectNextMatches(ctx -> ctx.getTransferenciaBancaria().equals(transferenciaBancaria) && ctx.getBacenRequest() != null)
                .verifyComplete();
    }

    @Test
    public void shouldEmitBadRequestErrorAfterRetriesFail() {
        TransferenciaRequest request = EntityFactoryTest.createTransferenciaRequest();
        TransferenciaContext context = EntityFactoryTest.createTransferenciaContext();
        when(saldoContaCorrenteRepository.save(any(SaldoContaCorrenteEntity.class)))
                .thenReturn(EntityFactoryTest.createSaldoContaCorrenteEntity());
        when(transferenciaBancariaRepository.save(any(TransferenciaBancariaEntity.class)))
                .thenReturn(EntityFactoryTest.createTransferenciaBancariaEntity());
        when(bacenClient.notificarBacen(any(BacenRequest.class)))
                .thenAnswer(invocation -> {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit atingido.");
                });

        StepVerifier.create(service.createTransferencia(request, context))
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();
    }
}
