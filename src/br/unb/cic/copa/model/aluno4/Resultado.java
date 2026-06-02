package br.unb.cic.copa.model.aluno4;

public class Resultado {
    private final int gols1;
    private final int gols2;

    public Resultado(int gols1, int gols2) {
        if(gols1 < 0 || gols2 < 0){
            throw new IllegalArgumentException("Erro: os gols não podem ser negativos. Valores recebidos: " + gols1 + " e " + gols2 + ".");
        }
        else {
            this.gols1 = gols1;
            this.gols2 = gols2;
        }
    }

    public void exibirResultado() {
        System.out.println("Resultado: " + gols1 + " x " + gols2);
    }

    public int getGols1() {
        return gols1;
    }

    public int getGols2() {
        return gols2;
    }

}