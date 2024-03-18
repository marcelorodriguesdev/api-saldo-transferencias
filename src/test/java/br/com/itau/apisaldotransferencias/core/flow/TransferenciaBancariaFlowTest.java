package br.com.itau.apisaldotransferencias.core.flow;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClientMock;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.core.EntityFactoryTest;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaService;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransferenciaBancariaFlowTest {

    @Mock
    private CadastroClientMock cadastroClient;
    @Mock
    private SaldoContaCorrenteFlow saldoContaCorrenteFlow;
    @Mock
    private TransferenciaBancariaService transferenciaBancariaService;
    @Mock
    private TransferenciaBancariaValidator transferenciaBancariaValidator;
    @Mock
    private TransferenciaContext context;

    @InjectMocks
    private TransferenciaBancariaFlow flow;

    @BeforeEach
    public void setUp() {
        openMocks(this);

        when(saldoContaCorrenteFlow.fetchSaldoContaCorrente(anyString()))
                .thenReturn(Mono.just(EntityFactoryTest.createSaldoContaCorrenteEntity()));

        when(cadastroClient.getCadastro(anyString()))
                .thenReturn(Mono.just(EntityFactoryTest.createCadastroResponse()));

        CadastroResponse cadastroResponseMock = mock(CadastroResponse.class);
        when(cadastroResponseMock.getNome()).thenReturn("Nome de Teste");

        when(context.getCadastroResponse()).thenReturn(cadastroResponseMock);
    }

    @Test
    public void shouldCompleteSuccessfullyWhenIsValidTransferenciaBancaria() {
        TransferenciaRequest request = EntityFactoryTest.createTransferenciaRequest();
        TransferenciaBancariaEntity transferenciaEntity = EntityFactoryTest.createTransferenciaBancariaEntity();


        when(context.getTransferenciaBancaria()).thenReturn(transferenciaEntity);
        doReturn(Mono.just(transferenciaEntity)).when(transferenciaBancariaService).createTransferencia(any(TransferenciaRequest.class), any(TransferenciaContext.class));

        Mono<TransferenciaResponse> result = flow.realizaTransferenciaBancaria(request);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getContaOrigem().equals(request.getContaOrigem()) &&
                                response.getCodigoBancoDestino().equals(request.getCodigoBancoDestino()) &&
                                response.getContaDestino().equals(request.getContaDestino()) &&
                                response.getValor().equals(request.getValor())
                )
                .verifyComplete();
    }
}
