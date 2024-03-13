package br.com.itau.apisaldotransferencias.infra.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Table(schema = "tb_saldo_conta_corrente")
public class SaldoContaCorrenteEntity {

    @Id
    @Column(name = "cod_conta_corrente")
    private String codContaCorrente;

    @Column(name = "num_conta_corrente")
    private String numContaCorrente;

    @Column(name = "val_limite_disponivel")
    private BigDecimal valLimiteDisponivel;

    @Column(name = "val_limite_diario")
    private BigDecimal valLimiteDiario;

    @Column(name = "val_saldo_conta_corrente")
    private BigDecimal valSaldoContaCorrente;

    public String getNumContaCorrente() {
        return numContaCorrente;
    }

    public BigDecimal getValLimiteDisponivel() {
        return valLimiteDisponivel;
    }

    public BigDecimal getValLimiteDiario() {
        return valLimiteDiario;
    }

    public BigDecimal getValSaldoContaCorrente() {
        return valSaldoContaCorrente;
    }

    public void setCodContaCorrente(String codContaCorrente) {
        this.codContaCorrente = codContaCorrente;
    }

    public void setNumContaCorrente(String numContaCorrente) {
        this.numContaCorrente = numContaCorrente;
    }

    public void setValLimiteDisponivel(BigDecimal valLimiteDisponivel) {
        this.valLimiteDisponivel = valLimiteDisponivel;
    }

    public void setValLimiteDiario(BigDecimal valLimiteDiario) {
        this.valLimiteDiario = valLimiteDiario;
    }

    public void setValSaldoContaCorrente(BigDecimal valSaldoContaCorrente) {
        this.valSaldoContaCorrente = valSaldoContaCorrente;
    }
}
