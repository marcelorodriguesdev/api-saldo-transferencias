package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClientMock;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaService;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import reactor.core.publisher.Mono;

public class TransferenciaBancariaFlow {
    private final CadastroClientMock cadastroClient;
    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;
    private final TransferenciaBancariaService transferenciaBancariaService;
    private final TransferenciaBancariaValidator transferenciaBancariaValidator;

    public TransferenciaBancariaFlow(CadastroClientMock cadastroClient, SaldoContaCorrenteFlow saldoContaCorrenteFlow, TransferenciaBancariaService transferenciaBancariaService, TransferenciaBancariaValidator transferenciaBancariaValidator) {
        this.cadastroClient = cadastroClient;
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
        this.transferenciaBancariaService = transferenciaBancariaService;
        this.transferenciaBancariaValidator = transferenciaBancariaValidator;
    }


    public Mono<?> realizaTransferencia(TransferenciaRequest request) {
        return Mono.zip(
                        saldoContaCorrenteFlow.fetchSaldoContaCorrente(request.getContaOrigem()),
                        cadastroClient.getCadastro(request.getContaDestino())
                )
                .flatMap(resultados -> {
                    var saldoContaCorrente = resultados.getT1();
                    var cadastro = resultados.getT2();
                    transferenciaBancariaValidator.validarTransferencia(request, saldoContaCorrente, cadastro);
                    return transferenciaBancariaService.createTransferencia(request, saldoContaCorrente);
                });
    }

}
