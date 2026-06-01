package br.unb.cic.copa.model.aluno2;

import java.util.ArrayList;
import java.util.List;

public class Selecao {
    private String nome;
    private String grupo;
    private String tecnico;
    private List<Jogador> jogadores;

    public Selecao(String nome, String grupo, String tecnico) {
        this.nome = nome;
        this.grupo = grupo;
        this.tecnico = tecnico;
        this.jogadores = new ArrayList<>();
    }

    public boolean numeroJaExiste(int numeracao) {//verifica se a numeracao passada ja existe
        for (Jogador j : jogadores) {
            if (j.getNumeracao() == numeracao) {
                return true;
            }
        }
        return false;
    }

    public void adicionarJogador(Jogador novoJogador) {
        if(this.jogadores.size() >= 26) {//percorre a lista de jogadores e ve se tem mais de 26
            System.out.println("Erro: A seleção " + this.nome + " atingiu o máximo de jogadores.");
            return;
        }

        if(numeroJaExiste(novoJogador.getNumeracao())) {
            System.out.println("Erro: A numeração " + novoJogador.getNumeracao() + " já está sendo usada.");
            return;
        }

        this.jogadores.add(novoJogador);//adiciona o jogador na lista de jogadores - deu certo
        novoJogador.setSelecao(this); //o jogador sabe que pertence a essa selecao - adiciona o jogador a respectiva selecao
    }//end adicionar jogador

    public boolean ehEscalacaoValida(){
        int contTitular = 0;
        boolean temGoleiro = false;

        for(Jogador j : this.jogadores){//percorre a lista de jogadores
            if(j.isTitular()){
                contTitular++;//verifica os titulares na lista de jogadores
            }
            if(j.getPosicao() == Posicao.GOLEIRO) {//verifica se tem pelo menos um goleiro
                temGoleiro = true;
            }
        }
        if(contTitular != 11) {
            System.out.println("Erro na escalação da " + this.nome + ": Uma equipe precisa de exatamente 11 titulares (atualmente tem " + contTitular + ").");
            return false;
        }

        if (!temGoleiro) {
            System.out.println("Erro na escalação da " + this.nome + ": É obrigatório ter pelo menos 1 GOLEIRO entre os titulares.");
            return false;
        }

        System.out.println("A seleção " + this.nome + " está apta e escalada corretamente para a partida!");
        return true;
    }//end ehEscalacaoValida

    public void exibirIntegrantes() {
        System.out.println("=== Integrantes da Seleção: " + this.nome + " ===");
        System.out.println("Técnico: " + this.tecnico);
        System.out.println("--- Titulares ---");
        for (Jogador j : jogadores) {
            if (j.isTitular()) System.out.println(j); // Aciona o toString
        }
        System.out.println("--- Reservas ---");
        for (Jogador j : jogadores) {
            if (!j.isTitular()) System.out.println(j);
        }
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public String getNome() {
        return nome;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }
}
