package br.com.itau.apisaldotransferencias.core.flow;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.api.payload.TransferenciaResponse;
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
                    //TODO adicionar os resultados ao contexto
                    var saldoContaCorrente = resultados.getT1();
                    var cadastro = resultados.getT2();


                    //TODO passar somente a request e o contexto
                    transferenciaBancariaValidator.validarTransferencia(request, saldoContaCorrente, cadastro);

                    //TODO passar somente a request e o contexto
                    return transferenciaBancariaService.createTransferencia(request, saldoContaCorrente, cadastro);

                    //TODO mapear a resposta fazendo get no contexto
//                    return new TransferenciaResponse(
//                            tuple.getT2().getCodTransferenciaBancaria(),
//                            request.getContaOrigem(),
//                            request.getCodigoBancoDestino(),
//                            request.getContaDestino(),
//                            tuple.getT2().getDatHorarioDaTransferencia().toString(),
//                            request.getValor(),
//                            cadastro.getNome()
//                    );

                });
    }

}
