package br.com.itau.apisaldotransferencias.infra.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Table(schema = "tb_saldo_conta_corrente")
public class SaldoContaCorrenteEntity {

    @Id
    @Column(name = "id_conta_corrente")
    private String idContaCorrente;

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
}
