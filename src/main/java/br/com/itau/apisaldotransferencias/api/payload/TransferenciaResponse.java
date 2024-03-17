package br.com.itau.apisaldotransferencias.api.payload;

import java.math.BigDecimal;

public class TransferenciaResponse {
        private String codigoTransferenciaBancaria;
        private String contaOrigem;
        private String codigoBancoDestino;
        private String contaDestino;
        private String data;
        private BigDecimal valor;
        private String nomeCliente;

    public TransferenciaResponse(String codigoTransferenciaBancaria, String contaOrigem, String codigoBancoDestino,
                                 String contaDestino, String data, BigDecimal valor, String nomeCliente) {
        this.codigoTransferenciaBancaria = codigoTransferenciaBancaria;
        this.contaOrigem = contaOrigem;
        this.codigoBancoDestino = codigoBancoDestino;
        this.contaDestino = contaDestino;
        this.data = data;
        this.valor = valor;
        this.nomeCliente = nomeCliente;
    }

    public String getCodigoTransferenciaBancaria() {
        return codigoTransferenciaBancaria;
    }

    public void setCodigoTransferenciaBancaria(String codigoTransferenciaBancaria) {
        this.codigoTransferenciaBancaria = codigoTransferenciaBancaria;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getCodigoBancoDestino() {
        return codigoBancoDestino;
    }

    public void setCodigoBancoDestino(String codigoBancoDestino) {
        this.codigoBancoDestino = codigoBancoDestino;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setValor(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
