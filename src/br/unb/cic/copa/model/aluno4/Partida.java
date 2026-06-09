package br.unb.cic.copa.model.aluno4;

import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Estadio;

import java.io.Serializable;

public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int arbitroId;
    private Selecao selecao1;
    private Selecao selecao2;
    private String data;
    private Estadio estadio;
    private Fase fase;
    private StatusPartida status;
    private Resultado resultado;

    public Partida(Selecao selecao1, Selecao selecao2, String data, Estadio estadio, Fase fase) {
        if (selecao1.getNome().equals(selecao2.getNome())) {
            throw new IllegalArgumentException("Erro: Uma partida deve ter duas seleções diferentes.");
        }
        this.selecao1 = selecao1;
        this.selecao2 = selecao2;
        this.data = data;
        this.estadio = estadio;
        this.fase = fase;
        this.status = StatusPartida.AGENDADA;
    }

    public Partida(Selecao selecao1, Selecao selecao2, String data, Estadio estadio) {
        this(selecao1, selecao2, data, estadio, Fase.GRUPOS);
    }

    public void iniciarPartida() {
        if (!(this.status.equals(StatusPartida.AGENDADA))) {
            System.out.println("Erro: somente partidas agendadas podem ser iniciadas.");
            return;
        }
        this.status = StatusPartida.EM_ANDAMENTO;
    }

    public void finalizarPartida(Resultado resultado) {
        if (this.status.equals(StatusPartida.FINALIZADA)) {
            System.out.println("Erro: Partida já finalizada!");
            return;
        }
        if (this.status.equals(StatusPartida.AGENDADA)) {
            System.out.println("Erro: partidas agendadas não podem ser finalizadas.");
            return;
        }
        if (resultado == null) {
            System.out.println("Erro: resultado não pode ser nulo.");
            return;
        }
        this.resultado = resultado;
        this.status = StatusPartida.FINALIZADA;
    }

    public void exibirPartida() {
        System.out.println(selecao1.getNome() + " x " + selecao2.getNome());
        System.out.println("Data: " + data);
        System.out.println("Estádio: " + estadio.getNome());
        System.out.println("Status: " + this.getStatus());
        if (resultado != null) resultado.exibirResultado();
    }

    public Selecao getVencedor() {
        if (status != StatusPartida.FINALIZADA || this.resultado == null) return null;
        if (this.resultado.getGols1() > this.resultado.getGols2()) return selecao1;
        else if (this.resultado.getGols2() > this.resultado.getGols1()) return selecao2;
        else return null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getArbitroId() { return arbitroId; }
    public void setArbitroId(int arbitroId) { this.arbitroId = arbitroId; }

    public Fase getFase() { return fase; }
    public void setFase(Fase fase) { this.fase = fase; }

    public Selecao getSelecao1() { return selecao1; }
    public Selecao getSelecao2() { return selecao2; }
    public String getData() { return data; }


    public int getGolsSelecao1() {
        if (this.resultado != null) {
            return this.resultado.getGols1();
        }
        return 0;
    }

    public int getGolsSelecao2() {
        if (this.resultado != null) {
            return this.resultado.getGols2();
        }
        return 0;
    }

    public boolean isFinalizada() {
        if (this.status != null) {
            return this.status.equals(StatusPartida.FINALIZADA);
        }
        return false;
    }

    public void setData(String data) {
        if (!(this.getStatus().equals(StatusPartida.AGENDADA))) {
            System.out.println("Erro: não é possivel alterar a data de uma partida " + this.getStatus());
            return;
        }
        this.data = data;
    }

    public Estadio getEstadio() { return estadio; }

    public void setEstadio(Estadio estadio) {
        if (!(this.status.equals(StatusPartida.AGENDADA))) {
            System.out.println("Erro: não é possível alterar o estádio de uma partida " + this.getStatus());
            return;
        }
        this.estadio = estadio;
    }

    public StatusPartida getStatus() { return status; }
    public Resultado getResultado() { return resultado; }

    @Override
    public String toString() {

        return getSelecao1().getNome()
                + " x "
                + getSelecao2().getNome()
                + " - "
                + getData();
    }
}

