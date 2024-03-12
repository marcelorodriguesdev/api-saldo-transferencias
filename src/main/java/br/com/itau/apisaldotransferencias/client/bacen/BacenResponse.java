package br.com.itau.apisaldotransferencias.client.bacen;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class BacenResponse {
    private String mensagem;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime horarioNotificacao;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getHorarioNotificacao() {
        return horarioNotificacao;
    }

    public void setHorarioNotificacao(LocalDateTime horarioNotificacao) {
        this.horarioNotificacao = horarioNotificacao;
    }
}
