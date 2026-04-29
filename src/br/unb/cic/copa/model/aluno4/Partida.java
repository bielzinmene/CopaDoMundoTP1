package br.unb.cic.copa.model.aluno4;

public class Partida {
    private String selecao1;
    private String selecao2;
    private String data;
    private String estadio;
    private String status; // agendada, em andamento, finalizada
    private Resultado resultado;

    public Partida(String selecao1, String selecao2, String data, String estadio) {
        this.selecao1 = selecao1;
        this.selecao2 = selecao2;
        this.data = data;
        this.estadio = estadio;
        this.status = "agendada";

        if (selecao1.equals(selecao2)) {
            System.out.println("Erro: uma partida deve ter duas seleções diferentes!");
        }
    }

    public void finalizarPartida(Resultado resultado) {
        if (this.status.equals("finalizada")) {
            System.out.println("Partida já finalizada!");
            return;
        }
        this.resultado = resultado;
        this.status = "finalizada";
    }

    public void exibirPartida() {
        System.out.println(selecao1 + " x " + selecao2);
        System.out.println("Data: " + data);
        System.out.println("Estádio: " + estadio);
        System.out.println("Status: " + status);

        if (resultado != null) {
            resultado.exibirResultado();
        }
    }
}