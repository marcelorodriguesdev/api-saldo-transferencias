package br.com.itau.apisaldotransferencias.client.bacen;

public class BacenResponse {
    private String mensagem;

    private String dataNotificacao;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDataNotificacao() {
        return dataNotificacao;
    }

    public void setDataNotificacao(String dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }
}
