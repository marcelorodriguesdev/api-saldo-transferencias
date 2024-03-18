package br.com.itau.apisaldotransferencias.api.payload;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferenciaBancariaRequestTest {

    private Validator validator;
    private TransferenciaBancariaRequest request;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        request = new TransferenciaBancariaRequest();
        request.setValor(new BigDecimal("100.00"));
    }

    private Set<ConstraintViolation<TransferenciaBancariaRequest>> validateRequest() {
        return validator.validate(request);
    }

    @Test
    public void shouldNotViolateWhenContaOrigemIsValid() {
        request.setContaOrigem("123456");
        assertThat(validateRequest()).isEmpty();
    }

    @Test
    public void shouldViolateWhenContaOrigemIsInvalid() {
        request.setContaOrigem("abc123");
        Set<ConstraintViolation<TransferenciaBancariaRequest>> violations = validateRequest();
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransferenciaBancariaRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("O numero da conta de origem deve conter apenas dígitos");
    }

    @Test
    public void shouldNotViolateWhenCodigoBancoDestinoIsValid() {
        request.setCodigoBancoDestino("001");
        assertThat(validateRequest()).isEmpty();
    }

    @Test
    public void shouldViolateWhenBankCodigoBancoDestinoIsInvalid() {
        request.setCodigoBancoDestino("00A");
        Set<ConstraintViolation<TransferenciaBancariaRequest>> violations = validateRequest();
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransferenciaBancariaRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("O codigo do banco de destino deve conter apenas dígitos");
    }

    @Test
    public void shouldNotViolateWhenContaDestinoIsValid() {
        request.setContaDestino("654321");
        assertThat(validateRequest()).isEmpty();
    }

    @Test
    public void shouldViolateWhenContaDestinoIsInvalid() {
        request.setContaDestino("6543AB");
        Set<ConstraintViolation<TransferenciaBancariaRequest>> violations = validateRequest();
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransferenciaBancariaRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("O numero da conta de destino deve conter apenas dígitos");
    }

    @Test
    public void shouldNotViolateWhenValorTransferenciaIsValid() {
        request.setValor(new BigDecimal("100.00"));
        assertThat(validateRequest()).isEmpty();
    }

}
