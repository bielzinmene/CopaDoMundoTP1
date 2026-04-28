package br.unb.cic.copa.model;

public enum CategoriaIngresso {
    VIP(300.0),
    PADRAO(150.0),
    MEIA(75.0);
    private final double pReco;

    CategoriaIngresso(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }
}
