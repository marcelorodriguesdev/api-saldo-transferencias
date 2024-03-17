package br.com.itau.apisaldotransferencias.client.bacen;

import java.math.BigDecimal;

public class BacenRequest {

    private String idTransferenciaBancaria;
    private String numeroContaOrigem;
    private String codigoBancoDestino;
    private String numeroContaDestino;
    private String dataHorarioDaTransferencia;
    private BigDecimal valTransferencia;
    private String nomeCliente;

    public String getIdTransferenciaBancaria() {
        return idTransferenciaBancaria;
    }

    public void setIdTransferenciaBancaria(String idTransferenciaBancaria) {
        this.idTransferenciaBancaria = idTransferenciaBancaria;
    }

    public String getNumeroContaOrigem() {
        return numeroContaOrigem;
    }

    public void setNumeroContaOrigem(String numeroContaOrigem) {
        this.numeroContaOrigem = numeroContaOrigem;
    }

    public String getCodigoBancoDestino() {
        return codigoBancoDestino;
    }

    public void setCodigoBancoDestino(String codigoBancoDestino) {
        this.codigoBancoDestino = codigoBancoDestino;
    }

    public String getNumeroContaDestino() {
        return numeroContaDestino;
    }

    public void setNumeroContaDestino(String numeroContaDestino) {
        this.numeroContaDestino = numeroContaDestino;
    }

    public String getDataHorarioDaTransferencia() {
        return dataHorarioDaTransferencia;
    }

    public void setDataHorarioDaTransferencia(String dataHorarioDaTransferencia) {
        this.dataHorarioDaTransferencia = dataHorarioDaTransferencia;
    }

    public BigDecimal getValTransferencia() {
        return valTransferencia;
    }

    public void setValTransferencia(BigDecimal valTransferencia) {
        this.valTransferencia = valTransferencia;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
