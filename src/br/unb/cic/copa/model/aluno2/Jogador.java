package br.unb.cic.copa.model.aluno2;

public class Jogador {
    private String nome;
    private int numeracao;
    private Posicao posicao;
    private Selecao selecao;
    private boolean titular;

    public Jogador(String nome, int numeracao, Posicao posicao, boolean titular) {
        this.nome = nome;
        this.posicao = posicao;
        this.titular = titular;
        setNumeracao((numeracao));//pra n correr o risco de construir um objeto com numeracao invalida
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumeracao(int numeracao) {
        if (numeracao < 1 || numeracao > 26) {//valida se o numero esta no intervalo de 1 a 26
            System.out.println("Erro: a numeração " + numeracao + " é inválida de acordo com as regras da FIFA.");
            return;
        }
        if(numeracao == 1 && !(this.getPosicao() == Posicao.GOLEIRO)){//o numero 1 é exclusivamente pra GOLEIRO
            System.out.println("Erro: a numeração >1< é reservada para a posição GOLEIRO.");
            return;
        }

        this.numeracao = numeracao;
    }


    @Override
    public String toString() {//formatar para exibir de acordo com o q quero
        String nomeSelecao = (selecao != null) ? selecao.getNome() : "Sem seleção";
        return String.format("Jogador: %s (%d) | Posição: %s | Seleção: %s", nome, numeracao, posicao, nomeSelecao);
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
