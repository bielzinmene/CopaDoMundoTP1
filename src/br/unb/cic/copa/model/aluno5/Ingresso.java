package br.unb.cic.copa.model.aluno5;

import br.unb.cic.copa.model.aluno4.Partida;
import java.io.Serializable;


public class Ingresso implements Serializable {

    private int id;
    private int partidaId;
    private CategoriaIngresso categoria;
    private double preco;
    private boolean vendido;

    public Ingresso(
            int id,
            int partidaId,
            CategoriaIngresso categoria
    ) {
        this.id = id;
        this.partidaId = partidaId;
        this.categoria = categoria;
        this.preco = categoria.getPreco();
        this.vendido = false;
    }

    public int getId() {
        return id;
    }

    public int getPartidaId() {
        return partidaId;
    }

    public CategoriaIngresso getCategoria() {
        return categoria;
    }

    public double getPreco() {
        return preco;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void vender() {
        vendido = true;
    }

    @Override
    public String toString() {
        return "Ingresso " + id +
                " | Categoria: " + categoria +
                " | Preço: " + preco;
    }

}