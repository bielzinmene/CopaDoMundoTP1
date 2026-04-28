package br.unb.cic.copa.model.aluno2;

public class Jogador {
    private String nome;
    private int numeracao;
    private String posicao;
    private String selecao;
    private boolean titular;

    public Jogador(String nome, int numeracao, String posicao, String selecao, boolean titular) {
        this.nome = nome;
        this.numeracao = numeracao;
        this.posicao = posicao;
        this.selecao = selecao;
        this.titular = titular;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumeracao(int numeracao) {
        this.numeracao = numeracao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public void setSelecao(String selecao) {
        this.selecao = selecao;
    }

    public void setTitular(boolean titular) {
        this.titular = titular;
    }

    public String getNome() {
        return nome;
    }

    public int getNumeracao() {
        return numeracao;
    }

    public String getPosicao() {
        return posicao;
    }

    public String getSelecao() {
        return selecao;
    }

    public boolean isTitular() {
        return titular;
    }
}
