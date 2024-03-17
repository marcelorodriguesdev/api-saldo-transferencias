package br.com.itau.apisaldotransferencias.infra.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;

@Table("tb_transferencia_bancaria")
public class TransferenciaBancariaEntity {

    @Id
    @Column("cod_transferencia_bancaria")
    private String codTransferenciaBancaria;

    @Column("num_conta_origem")
    private String numContaOrigem;

    @Column("cod_banco_destino")
    private String codBancoDestino;

    @Column("num_conta_destino")
    private String numContaDestino;

    @Column("dat_horario_da_transferencia")
    private String datHorarioDaTransferencia;

    @Column("val_transferencia")
    private BigDecimal valTransferencia;


    public TransferenciaBancariaEntity(String codTransferenciaBancaria, String numContaOrigem, String codBancoDestino, String numContaDestino, String datHorarioDaTransferencia, BigDecimal valTransferencia) {
        this.codTransferenciaBancaria = codTransferenciaBancaria;
        this.numContaOrigem = numContaOrigem;
        this.codBancoDestino = codBancoDestino;
        this.numContaDestino = numContaDestino;
        this.datHorarioDaTransferencia = datHorarioDaTransferencia;
        this.valTransferencia = valTransferencia;
    }

    public TransferenciaBancariaEntity() {

    }

    public String getCodTransferenciaBancaria() {
        return codTransferenciaBancaria;
    }

    public void setCodTransferenciaBancaria(String codTransferenciaBancaria) {
        this.codTransferenciaBancaria = codTransferenciaBancaria;
    }

    public String getNumContaOrigem() {
        return numContaOrigem;
    }

    public void setNumContaOrigem(String numContaOrigem) {
        this.numContaOrigem = numContaOrigem;
    }

    public String getCodBancoDestino() {
        return codBancoDestino;
    }

    public void setCodBancoDestino(String codBancoDestino) {
        this.codBancoDestino = codBancoDestino;
    }

    public String getNumContaDestino() {
        return numContaDestino;
    }

    public void setNumContaDestino(String numContaDestino) {
        this.numContaDestino = numContaDestino;
    }

    public String getDatHorarioDaTransferencia() {
        return datHorarioDaTransferencia;
    }

    public void setDatHorarioDaTransferencia(String datHorarioDaTransferencia) {
        this.datHorarioDaTransferencia = datHorarioDaTransferencia;
    }

    public BigDecimal getValTransferencia() {
        return valTransferencia;
    }

    public void setValTransferencia(BigDecimal valTransferencia) {
        this.valTransferencia = valTransferencia;
    }
}
