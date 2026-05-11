package br.unb.cic.copa.model.aluno3;

import java.util.ArrayList;
import java.util.List;
import br.unb.cic.copa.model.aluno4.Partida;

public class Estadio {

    private String nome;
    private String localizacao;
    private int capacidade;
    private List<Partida> partidasSediadas;

    public Estadio(String nome, String localizacao, int capacidade) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
        this.partidasSediadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao){
        this.localizacao = localizacao;
    }

    public int getCapacidade(){
        return capacidade;
    }

    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }

    public List<Partida> getPartidasSediadas(){
        return partidasSediadas;
    }

    /* public boolean verificarDisponibilidade(String data){
        for (Partida p: partidasSediadas){
            if (p.getData().equals(data)){
                return false;
            }
        }
        return true;
    } */

    /* public void adicionarPartida(Partida partida){
        if (verificarDisponibilidade(partida.getData())){
            this.partidasSediadas.add(partida);
        }
    } */

}
