package br.com.itau.apisaldotransferencias.core;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenRequest;
import br.com.itau.apisaldotransferencias.client.bacen.BacenResponse;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class EntityFactoryTest {
    public static TransferenciaContext createTransferenciaContext() {
        TransferenciaContext transferenciaContext = new TransferenciaContext();
        transferenciaContext.setBacenRequest(createBacenRequest());
        transferenciaContext.setTransferenciaBancaria(createTransferenciaBancariaEntity());
        transferenciaContext.setSaldoContaCorrente(createSaldoContaCorrenteEntity());
        transferenciaContext.setCadastroResponse(createCadastroResponse());
        return transferenciaContext;
    }

    public static BacenResponse createBacenResponse() {
        BacenResponse bacenResponse = new BacenResponse();
        bacenResponse.setMensagem("Notificação de transferência bancária processada com sucesso.");
        bacenResponse.setDataNotificacao(Instant.now().toString());
        return bacenResponse;
    }

    public static SaldoContaCorrenteEntity createSaldoContaCorrenteEntity() {
        SaldoContaCorrenteEntity saldo = new SaldoContaCorrenteEntity();
        saldo.setCodContaCorrente(UUID.randomUUID().toString());
        saldo.setNumContaCorrente("123456");
        saldo.setValLimiteDisponivel(new BigDecimal("5000.00"));
        saldo.setValLimiteDiario(new BigDecimal("1000.00"));
        saldo.setValSaldoContaCorrente(new BigDecimal("1000.00"));
        return saldo;
    }

    public static TransferenciaRequest createTransferenciaRequest() {
        TransferenciaRequest request = new TransferenciaRequest();
        request.setCodigoBancoDestino("001");
        request.setContaDestino("234553");
        request.setContaOrigem("123456");
        request.setValor(new BigDecimal("1000.00"));

        return request;
    }

    public static TransferenciaBancariaEntity createTransferenciaBancariaEntity() {
        TransferenciaBancariaEntity entity = new TransferenciaBancariaEntity();
        entity.setCodTransferenciaBancaria(UUID.randomUUID().toString());
        entity.setDatHorarioDaTransferencia(LocalDateTime.now().toString());
        entity.setValTransferencia(new BigDecimal("1000.00"));
        entity.setNumContaOrigem("123456");
        entity.setCodBancoDestino("001");
        entity.setNumContaDestino("234553");

        return entity;
    }

    public static BacenRequest createBacenRequest() {
        BacenRequest bacenRequest = new BacenRequest();
        bacenRequest.setIdTransferenciaBancaria(UUID.randomUUID().toString());
        bacenRequest.setNumeroContaOrigem("123456");
        bacenRequest.setCodigoBancoDestino("001");
        bacenRequest.setNumeroContaDestino("234553");
        bacenRequest.setDataHorarioDaTransferencia(Instant.now().toString());
        bacenRequest.setValTransferencia(new BigDecimal("1000.00"));
        bacenRequest.setNomeCliente("Cliente Exemplo");
        return bacenRequest;
    }

    public static CadastroResponse createCadastroResponse() {
        CadastroResponse cadastroResponse = new CadastroResponse();
        cadastroResponse.setNome("Cliente Exemplo");
        cadastroResponse.setSituacaoContaCorrente("ATIVA");
        cadastroResponse.setDataAberturaContaCorrente("2020-01-01");
        return cadastroResponse;
    }

}
