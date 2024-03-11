package br.com.itau.apisaldotransferencias.client.bacen;

import java.time.LocalDateTime;

public class BacenRequest {

    private String idTransferenciaBancaria;
    private String numContaOrigem;
    private String numContaDestino;
    private LocalDateTime datHorarioDaTransferencia;
    private Double valTransferencia;

    public String getIdTransferenciaBancaria() {
        return idTransferenciaBancaria;
    }

    public void setIdTransferenciaBancaria(String idTransferenciaBancaria) {
        this.idTransferenciaBancaria = idTransferenciaBancaria;
    }

    public String getNumContaOrigem() {
        return numContaOrigem;
    }

    public void setNumContaOrigem(String numContaOrigem) {
        this.numContaOrigem = numContaOrigem;
    }

    public String getNumContaDestino() {
        return numContaDestino;
    }

    public void setNumContaDestino(String numContaDestino) {
        this.numContaDestino = numContaDestino;
    }

    public LocalDateTime getDatHorarioDaTransferencia() {
        return datHorarioDaTransferencia;
    }

    public void setDatHorarioDaTransferencia(LocalDateTime datHorarioDaTransferencia) {
        this.datHorarioDaTransferencia = datHorarioDaTransferencia;
    }

    public Double getValTransferencia() {
        return valTransferencia;
    }

    public void setValTransferencia(Double valTransferencia) {
        this.valTransferencia = valTransferencia;
    }
}
