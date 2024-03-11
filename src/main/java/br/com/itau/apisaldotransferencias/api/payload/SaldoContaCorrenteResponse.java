package br.com.itau.apisaldotransferencias.api.payload;

import java.math.BigDecimal;

import java.math.BigDecimal;

public class SaldoContaCorrenteResponse {
    private String numContaCorrente;
    private BigDecimal valLimiteDisponivel;
    private BigDecimal valLimiteDiario;

    public SaldoContaCorrenteResponse(String numContaCorrente, BigDecimal valLimiteDisponivel, BigDecimal valLimiteDiario) {
        this.numContaCorrente = numContaCorrente;
        this.valLimiteDisponivel = valLimiteDisponivel;
        this.valLimiteDiario = valLimiteDiario;
    }

    public String getNumContaCorrente() {
        return numContaCorrente;
    }

    public void setNumContaCorrente(String numContaCorrente) {
        this.numContaCorrente = numContaCorrente;
    }

    public BigDecimal getValLimiteDisponivel() {
        return valLimiteDisponivel;
    }

    public void setValLimiteDisponivel(BigDecimal valLimiteDisponivel) {
        this.valLimiteDisponivel = valLimiteDisponivel;
    }

    public BigDecimal getValLimiteDiario() {
        return valLimiteDiario;
    }

    public void setValLimiteDiario(BigDecimal valLimiteDiario) {
        this.valLimiteDiario = valLimiteDiario;
    }
}



