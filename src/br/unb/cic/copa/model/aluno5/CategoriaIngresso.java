package br.unb.cic.copa.model.aluno5;

public enum CategoriaIngresso {
    VIP(300.0),
    PADRAO(150.0),
    MEIA(75.0);
    private final double preco;

    CategoriaIngresso(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }
}
