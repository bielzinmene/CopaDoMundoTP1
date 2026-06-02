package br.unb.cic.copa.model.aluno3;

import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;
import br.unb.cic.copa.model.aluno3.exception.EstadioIndisponivelException;
import br.unb.cic.copa.model.aluno4.Partida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Representa um estádio onde as partidas da Copa serão realizadas
public class Estadio implements Serializable {

    //permite que os objetos sejam salvos em arquivo
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private Localizacao localizacao;
    private int capacidade; // número máximo de espectadores
    private List<Partida> partidasSediadas; // partidas agendadas neste estádio

    // Valida capacidade já na criação do objeto
    //construtor
    public Estadio(int id, String nome, Localizacao localizacao, int capacidade) throws CapacidadeInvalidaException {
        if (capacidade <= 0) {
            throw new CapacidadeInvalidaException(capacidade);
        }
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
        this.partidasSediadas = new ArrayList<>();
    }
    //getters e setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return capacidade;
    }
    //checa se a capacidade informada é válida
    public void setCapacidade(int capacidade) throws CapacidadeInvalidaException {
        if (capacidade <= 0) {
            throw new CapacidadeInvalidaException(capacidade);
        }
        this.capacidade = capacidade;
    }
    //lista de partidas qe terão naquele estádio
    public List<Partida> getPartidasSediadas() {
        return partidasSediadas;
    }

    // Retorna cópia imutável para evitar modificações externas na lista
    public List<Partida> consultarPartidas() {
        return Collections.unmodifiableList(partidasSediadas);
    }

    //um estádio só pode sediar uma partida por data
    public boolean verificarDisponibilidade(String data) {
        for (Partida p : partidasSediadas) {
            if (p.getData().equals(data)) {
                return false; // já existe partida nessa data
            }
        }
        return true;
    }

    // Adiciona a partida apenas se o estádio estiver disponível na data
    public void adicionarPartida(Partida partida) throws EstadioIndisponivelException {
        if (!verificarDisponibilidade(partida.getData())) {
            throw new EstadioIndisponivelException(this.nome, partida.getData());
        }
        this.partidasSediadas.add(partida);
    }

    @Override
    public String toString() {
        return "Estadio{id=" + id + ", nome='" + nome + "', capacidade=" + capacidade
                + ", localizacao=" + localizacao + "}";
    }
}