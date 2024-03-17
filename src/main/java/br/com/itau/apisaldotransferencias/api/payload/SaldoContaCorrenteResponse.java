package br.com.itau.apisaldotransferencias.api.payload;

import java.math.BigDecimal;

public class SaldoContaCorrenteResponse {
    private String numeroContaCorrente;
    private BigDecimal limiteDisponivel;
    private BigDecimal limiteDiario;
    private BigDecimal saldo;

    public SaldoContaCorrenteResponse(String numeroContaCorrente, BigDecimal limiteDisponivel, BigDecimal limiteDiario, BigDecimal saldo) {
        this.numeroContaCorrente = numeroContaCorrente;
        this.limiteDisponivel = limiteDisponivel;
        this.limiteDiario = limiteDiario;
        this.saldo = saldo;
    }

    public String getNumeroContaCorrente() {
        return numeroContaCorrente;
    }

    public void setNumeroContaCorrente(String numeroContaCorrente) {
        this.numeroContaCorrente = numeroContaCorrente;
    }

    public BigDecimal getLimiteDisponivel() {
        return limiteDisponivel;
    }

    public void setLimiteDisponivel(BigDecimal limiteDisponivel) {
        this.limiteDisponivel = limiteDisponivel;
    }

    public BigDecimal getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(BigDecimal limiteDiario) {
        this.limiteDiario = limiteDiario;
    }
}



