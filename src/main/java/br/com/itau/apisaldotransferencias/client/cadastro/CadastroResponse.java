package br.com.itau.apisaldotransferencias.client.cadastro;

public class CadastroResponse {
    private String nome;
    private String situacaoContaCorrente;
    private String dataAberturaContaCorrente;

    public CadastroResponse(String nome, String situacaoContaCorrente, String dataAberturaContaCorrente) {
        this.nome = nome;
        this.situacaoContaCorrente = situacaoContaCorrente;
        this.dataAberturaContaCorrente = dataAberturaContaCorrente;
    }

    public CadastroResponse() {

    }

    public String getSituacaoContaCorrente() {
        return situacaoContaCorrente;
    }

    public String getDataAberturaContaCorrente() {
        return dataAberturaContaCorrente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSituacaoContaCorrente(String situacaoContaCorrente) {
        this.situacaoContaCorrente = situacaoContaCorrente;
    }

    public void setDataAberturaContaCorrente(String dataAberturaContaCorrente) {
        this.dataAberturaContaCorrente = dataAberturaContaCorrente;
    }
}
