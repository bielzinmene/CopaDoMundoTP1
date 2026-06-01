package br.unb.cic.copa.model.aluno2;

import br.unb.cic.copa.model.aluno2.exception.NumeroCamisaInvalidoException;

import java.io.Serializable;

public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private int numeracao;
    private Posicao posicao;
    private Selecao selecao;
    private boolean titular;
    private StatusJogador status;

    //construtor sem seleçao para nao criar um jogador com uma seleção já, pois pode ser trocada posteriormente
    public Jogador(String nome, int numeracao, Posicao posicao, boolean titular) throws NumeroCamisaInvalidoException {
        this.nome = nome;
        this.posicao = posicao;
        this.titular = titular;
        this.status = StatusJogador.ATIVO;//padrão
        setNumeracao((numeracao));//pra n correr o risco de construir um objeto com numeracao invalida
    }

    public void setNumeracao(int numeracao) throws NumeroCamisaInvalidoException {
        if (numeracao < 1 || numeracao > 26) {//valida se o numero esta no intervalo de 1 a 26
            throw new NumeroCamisaInvalidoException("Numeração " + numeracao + " inválida. Deve ser entre 1 e 26.");
        }
        if(numeracao == 1 && this.posicao != Posicao.GOLEIRO){//o numero 1 é exclusivamente pra GOLEIRO
            throw new NumeroCamisaInvalidoException("A numeração 1 é reservada para a posição GOLEIRO.");
        }
        this.numeracao = numeracao;
    }


    @Override
    public String toString() {//formatar para exibir de acordo com o q quero
        String nomeSelecao = (selecao != null) ? selecao.getNome() : "Sem seleção";
        return String.format("Jogador: %s (%d) | Posição: %s | Seleção: %s", nome, numeracao, posicao, nomeSelecao);
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPosicao(Posicao posicao) {this.posicao = posicao;}

    public void setSelecao(Selecao selecao) {this.selecao = selecao;}

    public void setTitular(boolean titular) {this.titular = titular;}

    public String getNome() {return nome;}

    public int getNumeracao() {return numeracao;}

    public Posicao getPosicao() {return posicao;}

    public Selecao getSelecao() {return selecao;}

    public boolean isTitular() {return titular;}

    public StatusJogador getStatus() {
        return status;
    }
}
