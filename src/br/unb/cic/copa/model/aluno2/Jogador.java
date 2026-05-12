package br.unb.cic.copa.model.aluno2;

public class Jogador {
    private String nome;
    private int numeracao;
    private Posicao posicao;
    private Selecao selecao;
    private boolean titular;

    public Jogador(String nome, int numeracao, Posicao posicao, Selecao selecao, boolean titular) {
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
        if (numeracao > 0 && numeracao <= 26) {
            this.numeracao = numeracao;
        } else {
            System.out.println("Numeração inválida para a Copa de acordo com as regras da FIFA.");
        }
    }

    @Override
    public String toString() {
        return String.format("Jogador: %s (%d) | Posição: %s | Seleção: %s", nome, numeracao, posicao, selecao.getNome());
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public void setSelecao(Selecao selecao) {
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

    public Posicao getPosicao() {
        return posicao;
    }

    public Selecao getSelecao() {
        return selecao;
    }

    public boolean isTitular() {
        return titular;
    }
}
