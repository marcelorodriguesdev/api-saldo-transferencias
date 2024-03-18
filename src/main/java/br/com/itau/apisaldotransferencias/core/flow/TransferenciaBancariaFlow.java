package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClientMock;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaBancariaContext;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaService;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransferenciaBancariaFlow {
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaFlow.class);

    private final CadastroClientMock cadastroClient;
    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;
    private final TransferenciaBancariaService transferenciaBancariaService;
    private final TransferenciaBancariaValidator transferenciaBancariaValidator;

    private final TransferenciaBancariaContext context;

    public TransferenciaBancariaFlow(CadastroClientMock cadastroClient, SaldoContaCorrenteFlow saldoContaCorrenteFlow, TransferenciaBancariaService transferenciaBancariaService, TransferenciaBancariaValidator transferenciaBancariaValidator, TransferenciaBancariaContext context) {
        this.cadastroClient = cadastroClient;
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
        this.transferenciaBancariaService = transferenciaBancariaService;
        this.transferenciaBancariaValidator = transferenciaBancariaValidator;
        this.context = context;
    }

    public Mono<TransferenciaBancariaResponse> performTransferenciaBancaria(TransferenciaBancariaRequest request) {
        return Mono.zip(
                        saldoContaCorrenteFlow.fetchSaldoContaCorrente(request.getContaOrigem()),
                        cadastroClient.getCadastro(request.getContaDestino())
                )
                .doOnSuccess(result -> logger.info("Dados de saldo e cadastro obtidos com sucesso para a transferência."))
                .doOnError(e -> logger.error("Erro ao obter dados para a transferência.", e))
                .map(resultados -> {
                    var saldoContaCorrente = resultados.getT1();
                    context.setSaldoContaCorrente(saldoContaCorrente);

                    var cadastro = resultados.getT2();
                    context.setCadastroResponse(cadastro);

                    transferenciaBancariaValidator.validateBankTransfer(request, context);

                    transferenciaBancariaService.createTransferencia(request, context);

                    return buildTransferenciaBancariaResponse(request);
                });
    }

    public Mono<TransferenciaBancariaResponse> fetchTransferenciaBancaria(String idTransferenciaBancaria) {
        return transferenciaBancariaService.getTransferencia(idTransferenciaBancaria);
    }

    private TransferenciaBancariaResponse buildTransferenciaBancariaResponse(TransferenciaBancariaRequest request) {
        return new TransferenciaBancariaResponse(
                context.getTransferenciaBancariaEntity().getCodTransferenciaBancaria(),
                request.getContaOrigem(),
                request.getCodigoBancoDestino(),
                request.getContaDestino(),
                context.getTransferenciaBancariaEntity().getDatHorarioDaTransferencia(),
                request.getValor(),
                context.getCadastroResponse().getNome()
        );
    }
}
