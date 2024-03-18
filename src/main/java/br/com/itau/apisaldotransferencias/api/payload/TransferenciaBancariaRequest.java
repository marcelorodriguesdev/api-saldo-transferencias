package br.com.itau.apisaldotransferencias.api.payload;

import br.com.itau.apisaldotransferencias.api.APIUtils;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class TransferenciaBancariaRequest {

    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O numero da conta de origem deve conter apenas dígitos")
    private String contaOrigem;

    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O codigo do banco de destino deve conter apenas dígitos")
    private String codigoBancoDestino;

    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O numero da conta de destino deve conter apenas dígitos")
    private String contaDestino;

    @DecimalMin(value = "0.01", message = "O valor da transferência deve ser maior que zero")
    private BigDecimal valor;

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getCodigoBancoDestino() {
        return codigoBancoDestino;
    }

    public void setCodigoBancoDestino(String codigoBancoDestino) {
        this.codigoBancoDestino = codigoBancoDestino;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
