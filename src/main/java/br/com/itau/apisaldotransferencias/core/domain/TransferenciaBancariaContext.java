package br.com.itau.apisaldotransferencias.core.domain;

import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;

public class TransferenciaBancariaContext {

    private BacenRequest bacenRequest;

    private TransferenciaBancariaEntity transferenciaBancariaEntity;

    private SaldoContaCorrenteEntity saldoContaCorrenteEntity;

    private CadastroResponse cadastroResponse;


    public BacenRequest getBacenRequest() {
        return bacenRequest;
    }

    public void setBacenRequest(BacenRequest bacenRequest) {
        this.bacenRequest = bacenRequest;
    }

    public TransferenciaBancariaEntity getTransferenciaBancariaEntity() {
        return transferenciaBancariaEntity;
    }

    public void setTransferenciaBancariaEntity(TransferenciaBancariaEntity transferenciaBancariaEntity) {
        this.transferenciaBancariaEntity = transferenciaBancariaEntity;
    }

    public SaldoContaCorrenteEntity getSaldoContaCorrente() {
        return saldoContaCorrenteEntity;
    }

    public void setSaldoContaCorrente(SaldoContaCorrenteEntity saldoContaCorrente) {
        this.saldoContaCorrenteEntity = saldoContaCorrente;
    }

    public CadastroResponse getCadastroResponse() {
        return cadastroResponse;
    }

    public void setCadastroResponse(CadastroResponse cadastroResponse) {
        this.cadastroResponse = cadastroResponse;
    }

}
