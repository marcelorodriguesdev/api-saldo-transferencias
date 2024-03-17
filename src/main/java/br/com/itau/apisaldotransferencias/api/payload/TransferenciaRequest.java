package br.com.itau.apisaldotransferencias.api.payload;

import br.com.itau.apisaldotransferencias.api.APIUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class TransferenciaRequest {

    @NotBlank(message = "O número da conta de origem é obrigatório")
    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O numero da conta de origem deve conter apenas dígitos")
    private String contaOrigem;

    @NotBlank(message = "O código do banco de destino é obrigatório")
    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O codigo do banco de destino deve conter apenas dígitos")
    private String codigoBancoDestino;

    @NotBlank(message = "O número da conta de destino é obrigatório")
    @Pattern(regexp = APIUtils.ONLY_NUMERIC_CHARACTERS, message = "O numero da conta de destino deve conter apenas dígitos")
    private String contaDestino;

    @NotBlank(message = "O valor da transferência é obrigatório")
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
