package CopaDoMundoTP1.src.br.unb.cic.copa.model;

public class Resultado {
    private int gols1;
    private int gols2;

    public Resultado(int gols1, int gols2) {
        this.gols1 = gols1;
        this.gols2 = gols2;
    }

    public void exibirResultado() {
        System.out.println("Resultado: " + gols1 + " x " + gols2);
    }
}