package br.com.itau.apisaldotransferencias.core.service;

import br.com.itau.apisaldotransferencias.api.payload.TransferenciaBancariaRequest;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaBancariaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
public class TransferenciaBancariaValidator {
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaBancariaValidator.class);

    public Mono<TransferenciaBancariaContext> validateBankTransfer(TransferenciaBancariaRequest transferencia, TransferenciaBancariaContext context) {
        return Mono.zip(
                validateLimit(transferencia.getValor(), context.getSaldoContaCorrente().getValLimiteDiario(), "limiteDiario"),
                validateLimit(transferencia.getValor(), context.getSaldoContaCorrente().getValLimiteDisponivel(), "limiteDisponivel"),
                validateAccountStatus(context.getCadastroResponse().getSituacaoContaCorrente())
        ).flatMap(tuple -> {
            Boolean validDailyLimit = tuple.getT1();
            Boolean validAvailableLimit = tuple.getT2();
            Boolean blockedAccount = tuple.getT3();

            return verifyConditions(validDailyLimit, validAvailableLimit, blockedAccount, context);
        });
    }

    private Mono<TransferenciaBancariaContext> verifyConditions(Boolean limiteDiarioValido, Boolean limiteDisponivelValido, Boolean contaBloqueada, TransferenciaBancariaContext context) {
        if (!limiteDiarioValido) {
            logger.error("Valor da transferência excede o limite diário para a conta {}", context.getSaldoContaCorrente().getNumContaCorrente());
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da transferência excede o limite diário."));
        } else if (!limiteDisponivelValido) {
            logger.error("Valor da transferência excede o limite disponível para a conta {}", context.getSaldoContaCorrente().getNumContaCorrente());
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da transferência excede o limite disponível."));
        } else if (contaBloqueada) {
            logger.error("A transferência não pode ser realizada pois a conta corrente {} está bloqueada.", context.getSaldoContaCorrente().getNumContaCorrente());
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "A transferência não pode ser realizada pois a conta corrente está bloqueada."));
        }
        logger.info("Transferência validada para a conta {}", context.getSaldoContaCorrente().getNumContaCorrente());
        return Mono.just(context);
    }

    private Mono<Boolean> validateLimit(BigDecimal valorTransferencia, BigDecimal valorLimite, String campo) {
        return Mono.just(valorTransferencia.compareTo(valorLimite) <= 0);
    }

    public Mono<Boolean> validateAccountStatus(String situacaoContaCorrente) {
        return Mono.just("BLOQUEADA".equals(situacaoContaCorrente));
    }

}
