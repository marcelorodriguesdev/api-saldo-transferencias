package br.com.itau.apisaldotransferencias.client.bacen;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BacenRequest {

    private String idTransferenciaBancaria;
    private String numeroContaOrigem;

    private String codigoBancoDestino;
    private String numeroContaDestino;
    private LocalDateTime dataHorarioDaTransferencia;
    private BigDecimal valTransferencia;
    private String nomeClient;

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

    public LocalDateTime getDataHorarioDaTransferencia() {
        return dataHorarioDaTransferencia;
    }

    public void setDataHorarioDaTransferencia(LocalDateTime dataHorarioDaTransferencia) {
        this.dataHorarioDaTransferencia = dataHorarioDaTransferencia;
    }

    public BigDecimal getValTransferencia() {
        return valTransferencia;
    }

    public void setValTransferencia(BigDecimal valTransferencia) {
        this.valTransferencia = valTransferencia;
    }

    public String getNomeClient() {
        return nomeClient;
    }

    public void setNomeClient(String nomeClient) {
        this.nomeClient = nomeClient;
    }
}
