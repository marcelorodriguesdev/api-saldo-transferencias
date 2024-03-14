package br.com.itau.apisaldotransferencias.core.domain;

import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;

public class TransferenciaContext {

    private BacenRequest bacenRequest;

    private TransferenciaBancariaEntity transferenciaBancaria;

    private SaldoContaCorrenteEntity saldoContaCorrenteResponse;

    private CadastroResponse cadastroResponse;


    public BacenRequest getBacenRequest() {
        return bacenRequest;
    }

    public void setBacenRequest(BacenRequest bacenRequest) {
        this.bacenRequest = bacenRequest;
    }

    public TransferenciaBancariaEntity getTransferenciaBancaria() {
        return transferenciaBancaria;
    }

    public void setTransferenciaBancaria(TransferenciaBancariaEntity transferenciaBancaria) {
        this.transferenciaBancaria = transferenciaBancaria;
    }

    public SaldoContaCorrenteEntity getSaldoContaCorrente() {
        return saldoContaCorrenteResponse;
    }

    public void setSaldoContaCorrente(SaldoContaCorrenteEntity saldoContaCorrente) {
        this.saldoContaCorrenteResponse = saldoContaCorrente;
    }

    public CadastroResponse getCadastroResponse() {
        return cadastroResponse;
    }

    public void setCadastroResponse(CadastroResponse cadastroResponse) {
        this.cadastroResponse = cadastroResponse;
    }

}
