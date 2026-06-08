package br.unb.cic.copa.model.aluno5;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String comprador;
    private LocalDateTime dataVenda;
    private int quantidadeIngressos;
    private double valorTotal;
    private List<Ingresso> ingressos;

    public Venda(int id, String comprador) {
        this.id = id;
        this.comprador = comprador;
        this.dataVenda = LocalDateTime.now();
        this.ingressos = new ArrayList<>();
    }

    public Venda(
            int id,
            String comprador,
            LocalDateTime dataVenda,
            int quantidadeIngressos,
            double valorTotal) {

        this.id = id;
        this.comprador = comprador;
        this.dataVenda = dataVenda;
        this.quantidadeIngressos = quantidadeIngressos;
        this.valorTotal = valorTotal;
        this.ingressos = new ArrayList<>();
    }

    public void adicionarIngresso(Ingresso ingresso) {
        ingressos.add(ingresso);
    }

    public double getValorTotal() {

        if (!ingressos.isEmpty()) {

            double total = 0;

            for (Ingresso ingresso : ingressos) {
                total += ingresso.getPreco();
            }

            return total;
        }

        return valorTotal;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public int getQuantidadeIngressos() {

        if (!ingressos.isEmpty()) {
            return ingressos.size();
        }

        return quantidadeIngressos;
    }
    public int getId() {
        return id;
    }

    public String getComprador() {
        return comprador;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }


    @Override
    public String toString() {
        return "Venda " + id +
                " | Comprador: " + comprador +
                " | Total: R$ " + getValorTotal();
    }
}
