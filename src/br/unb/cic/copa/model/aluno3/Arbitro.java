package br.unb.cic.copa.model.aluno3;

import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno3.exception.ArbitroNacionalidadeException;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno4.Partida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Árbitro é um tipo de Usuario do sistema, com atributos e regras específicas de arbitragem
public class Arbitro extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nacionalidade;
    private int experiencia; // anos de experiência
    private List<Partida> partidasDesignadas; // partidas que este árbitro irá apitar

    // Construtor completo
    public Arbitro(int id, String nome, String email, String login,
                   String senha, String cpf, String pais,
                   String nacionalidade, int experiencia) throws ExperienciaInvalidaException {
        super(id, nome, email, login, senha, cpf, pais, "Arbitro");
        if (experiencia < 0) {
            throw new ExperienciaInvalidaException(experiencia);
        }
        this.nacionalidade = nacionalidade;
        this.experiencia = experiencia;
        this.partidasDesignadas = new ArrayList<>();
    }

    // Construtor simplificado (para ser chamado em partida)
    public Arbitro(int id, String nome, String email, String login,
                   String senha, String cpf, String pais) throws ExperienciaInvalidaException {
        this(id, nome, email, login, senha, cpf, pais, pais, 0);
    }

    //getters e setters
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

    // arbitro não pode apitar partida envolvendo sua própria seleção
    public boolean validarNacionalidade(Partida partida) {
        return !this.nacionalidade.equalsIgnoreCase(partida.getSelecao1().getNome())
                && !this.nacionalidade.equalsIgnoreCase(partida.getSelecao2().getNome());
    }

    // Designa o árbitro para a partida apenas se passar na validação de nacionalidade
    public void designarParaPartida(Partida partida) throws ArbitroNacionalidadeException {
        if (!validarNacionalidade(partida)) {
            throw new ArbitroNacionalidadeException(this.getNome(), this.nacionalidade);
        }
        this.partidasDesignadas.add(partida);
    }

    @Override
    public String toString() {
        return super.toString() + " | Nacionalidade: " + nacionalidade + " | Experiência: " + experiencia + " anos";
    }
}