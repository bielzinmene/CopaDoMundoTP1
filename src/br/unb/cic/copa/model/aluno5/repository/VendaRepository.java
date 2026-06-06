package br.unb.cic.copa.model.aluno5.repository;

import br.unb.cic.copa.model.aluno5.Venda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    private static final String ARQUIVO = "vendas.dat";

    @SuppressWarnings("unchecked")
    public List<Venda> carregar() {

        try(ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(ARQUIVO))) {

            return (List<Venda>) in.readObject();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void salvar(List<Venda> vendas) {

        try(ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(ARQUIVO))) {

            out.writeObject(vendas);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionar(Venda venda) {

        List<Venda> vendas = carregar();

        vendas.add(venda);

        salvar(vendas);
    }

    public void remover(int id) {

        List<Venda> vendas = carregar();

        vendas.removeIf(v -> v.getId() == id);

        salvar(vendas);
    }
}