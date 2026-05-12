package br.unb.cic.copa.model.aluno3;

import java.util.ArrayList;
import java.util.List;
import br.unb.cic.copa.model.aluno4.Partida;

public class Arbitro {
    //atributos
    private int id;
    private String nome;
    private String nacionalidade;
    private int experiencia;
    private List<Partida> partidasDesignadas;

        //Construtor
    public Arbitro(int id, String nome, String nacionalidade, int experiencia) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.experiencia = experiencia;
        this.partidasDesignadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public int getId() {
        return id; }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade)
    {
        this.nacionalidade = nacionalidade;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia){
        this.experiencia = experiencia;
    }

    public List<Partida>consultarEscala(){
        return partidasDesignadas;
    }

    ///public boolean validarNacionalidade(Partida partida){
        ///return  !this.nacionalidade.equalsIgnoreCase(partida.getSelecao1())
          ///      && !this.nacionalidade.equalsIgnoreCase(partida.getSelecao2());
    ///}

    ///public void addPartidaAEscala( Partida partida){
      ///  if(validarNacionalidade(partida)){
         ///   this.partidasDesignadas.add(partida);
        ///}
    ///}



}
