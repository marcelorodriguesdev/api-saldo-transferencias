package br.com.itau.apisaldotransferencias.core.flow;

import static org.mockito.Mockito.*;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClientMock;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.core.EntityFactoryTest;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaBancariaContext;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaService;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

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
    private TransferenciaBancariaContext context;

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
        TransferenciaBancariaRequest request = EntityFactoryTest.createTransferenciaRequest();
        TransferenciaBancariaEntity transferenciaEntity = EntityFactoryTest.createTransferenciaBancariaEntity();


        when(context.getTransferenciaBancariaEntity()).thenReturn(transferenciaEntity);
        doReturn(Mono.just(transferenciaEntity)).when(transferenciaBancariaService).createTransferencia(any(TransferenciaBancariaRequest.class), any(TransferenciaBancariaContext.class));

        Mono<TransferenciaBancariaResponse> result = flow.performTransferenciaBancaria(request);

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
