package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroClient;
import br.com.itau.apisaldotransferencias.core.service.TransferenciaBancariaValidator;
import reactor.core.publisher.Mono;

public class TransferenciaBancariaFlow {
    private final CadastroClient cadastroClient;
    private final SaldoContaCorrenteFlow saldoContaCorrenteFlow;

    private final TransferenciaBancariaValidator transferenciaBancariaValidator;

    public TransferenciaBancariaFlow(CadastroClient cadastroClient, SaldoContaCorrenteFlow saldoContaCorrenteFlow, TransferenciaBancariaValidator transferenciaBancariaValidator) {
        this.cadastroClient = cadastroClient;
        this.saldoContaCorrenteFlow = saldoContaCorrenteFlow;
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
                    return transferenciaBancariaValidator.validarTransferencia(request, saldoContaCorrente, cadastro);
                });
    }

}
