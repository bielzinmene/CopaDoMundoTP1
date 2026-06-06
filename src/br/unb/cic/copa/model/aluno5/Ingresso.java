package br.unb.cic.copa.model.aluno5;

import br.unb.cic.copa.model.aluno4.Partida;
import java.io.Serializable;


public class Ingresso implements Serializable {

    private static final long serialVersionUID = 1L;


    private int id;
    private Partida partida;
    private CategoriaIngresso categoria;
    private double preco;
    private boolean vendido;

    public Ingresso(int id, Partida partida, CategoriaIngresso categoria) {
        this.id = id;
        this.partida = partida;
        this.categoria = categoria;
        this.preco = categoria.getPreco();
        this.vendido = false;
    }

    public int getId() {
        return id;
    }

    public Partida getPartida() {
        return partida;
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
        this.vendido = true;
    }

    @Override
    public String toString() {
        return "Ingresso " + id +
                " | Categoria: " + categoria +
                " | Preço: " + preco;
    }

}