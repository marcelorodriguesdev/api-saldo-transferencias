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
}
