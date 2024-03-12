package br.com.itau.apisaldotransferencias.client.bacen;

import java.time.LocalDateTime;

public class BacenRequest {

    private String idTransferenciaBancaria;
    private String numeroContaOrigem;

    private String codigoBancoDestino;
    private String numeroContaDestino;
    private LocalDateTime dataHorarioDaTransferencia;
    private Double valTransferencia;

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

    public Double getValTransferencia() {
        return valTransferencia;
    }

    public void setValTransferencia(Double valTransferencia) {
        this.valTransferencia = valTransferencia;
    }
}
