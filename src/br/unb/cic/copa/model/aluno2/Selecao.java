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
