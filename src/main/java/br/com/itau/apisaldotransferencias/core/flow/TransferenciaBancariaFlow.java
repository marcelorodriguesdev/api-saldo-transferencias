package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClientMock;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaService;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;

@Service
public class TransferenciaBancariaFlow {
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaFlow.class);

    private final CadastroClientMock cadastroClient;
    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;
    private final TransferenciaBancariaService transferenciaBancariaService;
    private final TransferenciaBancariaValidator transferenciaBancariaValidator;

    private final TransferenciaContext context;

    public TransferenciaBancariaFlow(CadastroClientMock cadastroClient, SaldoContaCorrenteFlow saldoContaCorrenteFlow, TransferenciaBancariaService transferenciaBancariaService, TransferenciaBancariaValidator transferenciaBancariaValidator, TransferenciaContext context) {
        this.cadastroClient = cadastroClient;
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
        this.transferenciaBancariaService = transferenciaBancariaService;
        this.transferenciaBancariaValidator = transferenciaBancariaValidator;
        this.context = context;
    }

    public Mono<TransferenciaResponse> realizaTransferenciaBancaria(TransferenciaRequest request) {
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

                    transferenciaBancariaValidator.validarTransferencia(request, context);

                    transferenciaBancariaService.createTransferencia(request, context);

                    return montaTransferenciaResponse(request);
                });
    }

    public Mono<TransferenciaResponse> consultarTransferenciaBancaria(String idTransferenciaBancaria) {
        return transferenciaBancariaService.getTransferencia(idTransferenciaBancaria);
    }

    private TransferenciaResponse montaTransferenciaResponse(TransferenciaRequest request) {
        return new TransferenciaResponse(
                context.getTransferenciaBancaria().getCodTransferenciaBancaria(),
                request.getContaOrigem(),
                request.getCodigoBancoDestino(),
                request.getContaDestino(),
                context.getTransferenciaBancaria().getDatHorarioDaTransferencia(),
                request.getValor(),
                context.getCadastroResponse().getNome()
        );
    }
}
