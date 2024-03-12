CREATE DATABASE IF NOT EXISTS db_saldo_transferencias;
USE db_saldo_transferencias;

CREATE TABLE IF NOT EXISTS tb_transferencia_bancaria (
    cod_transferencia_bancaria CHAR(36) NOT NULL,
    num_conta_origem VARCHAR(30) NOT NULL,
    cod_banco_destino VARCHAR(30) NOT NULL,
    num_conta_destino VARCHAR(30) NOT NULL,
    dat_horario_da_transferencia VARCHAR(255) NOT NULL,
    val_transferencia DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id_transferencia_bancaria)
);

CREATE TABLE IF NOT EXISTS tb_saldo_conta_corrente (
    cod_conta_corrente CHAR(36) NOT NULL,
    num_conta_corrente VARCHAR(30) NOT NULL,
    val_limite_disponivel DECIMAL(19, 2) NOT NULL,
    val_limite_diario DECIMAL(19, 2) NOT NULL DEFAULT 1000.00,
    PRIMARY KEY (id_conta_corrente)
);
