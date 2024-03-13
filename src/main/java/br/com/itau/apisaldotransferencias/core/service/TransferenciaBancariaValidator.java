package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaRequest;
import br.com.itau.apisaldotransferencias.client.cadastro.CadastroResponse;
import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
public class TransferenciaBancariaValidator {

    //TODO substitui os ultimos 2 parametros pelo contexto
    public Mono<TransferenciaRequest> validarTransferencia(TransferenciaRequest transferencia, SaldoContaCorrenteEntity saldo, CadastroResponse cadastro) {
        return Mono.zip(
                validarLimite(transferencia.getValor(), saldo.getValLimiteDiario(), "limiteDiario"),
                validarLimite(transferencia.getValor(), saldo.getValLimiteDisponivel(), "limiteDisponivel"),
                validarSituacaoConta(cadastro.getSituacaoContaCorrente())
        ).flatMap(tuple -> {
            Boolean limiteDiarioValido = tuple.getT1();
            Boolean limiteDisponivelValido = tuple.getT2();
            Boolean contaBloqueada = tuple.getT3();

            if (!limiteDiarioValido) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da transferência excede o limite diário."));
            } else if (!limiteDisponivelValido) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da transferência excede o limite disponível."));
            } else if (contaBloqueada) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "A transferência não pode ser realizada pois a conta corrente está bloqueada."));
            } else {
                return Mono.just(transferencia);
            }
        });
    }

    private Mono<Boolean> validarLimite(BigDecimal valorTransferencia, BigDecimal valorLimite, String campo) {
        return Mono.just(valorTransferencia.compareTo(valorLimite) <= 0);
    }

    public Mono<Boolean> validarSituacaoConta(String situacaoContaCorrente) {
        return Mono.just("BLOQUEADA".equals(situacaoContaCorrente));
    }

}
