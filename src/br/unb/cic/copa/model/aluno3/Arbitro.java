package br.unb.cic.copa.model.aluno3;

import br.unb.cic.copa.model.aluno3.exception.ArbitroNacionalidadeException;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno4.Partida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Representa um árbitro que pode ser designado para apitar partidas da Copa
public class Arbitro implements Serializable {

    //salvar os objetos em arquivo
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String nacionalidade;
    private int experiencia; // anos de experiência
    private List<Partida> partidasDesignadas; // partidas que este árbitro irá apitar

    // Valida experiência já na criação do objeto
    //construtor
    public Arbitro(int id, String nome, String nacionalidade, int experiencia) throws ExperienciaInvalidaException {
        if (experiencia < 0) {
            throw new ExperienciaInvalidaException(experiencia);
        }
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.experiencia = experiencia;
        this.partidasDesignadas = new ArrayList<>();
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

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) throws ExperienciaInvalidaException {
        if (experiencia < 0) {
            throw new ExperienciaInvalidaException(experiencia);
        }
        this.experiencia = experiencia;
    }

    public List<Partida> getPartidasDesignadas() {
        return partidasDesignadas;
    }

    // Retorna cópia imutável para evitar modificações externas na lista
    public List<Partida> consultarEscala() {
        return Collections.unmodifiableList(partidasDesignadas);
    }

    // Regra de negócio: arbitro não pode apitar partida envolvendo sua própria seleção
    public boolean validarNacionalidade(Partida partida) {
        return !this.nacionalidade.equalsIgnoreCase(partida.getSelecao1().getNome())
                && !this.nacionalidade.equalsIgnoreCase(partida.getSelecao2().getNome());
    }

    // Designa o árbitro para a partida apenas se passar na validação de nacionalidade
    public void designarParaPartida(Partida partida) throws ArbitroNacionalidadeException {
        if (!validarNacionalidade(partida)) {
            throw new ArbitroNacionalidadeException(this.nome, this.nacionalidade);
        }
        this.partidasDesignadas.add(partida);
    }

    @Override
    public String toString() {
        return "Arbitro{id=" + id + ", nome='" + nome + "', nacionalidade='" + nacionalidade
                + "', experiencia=" + experiencia + "}";
    }
}