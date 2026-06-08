package br.unb.cic.copa.controller.aluno5;

import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno5.CategoriaIngresso;
import br.unb.cic.copa.model.aluno5.Ingresso;
import br.unb.cic.copa.model.aluno5.Venda;
import br.unb.cic.copa.model.aluno5.exception.VendaIngressoException;
import br.unb.cic.copa.model.aluno5.repository.VendaRepository;

import java.util.List;

public class IngressosController {

    private final VendaRepository repository;

    public IngressosController() {
        repository = new VendaRepository();
    }

    public void registrarVenda(
            String comprador,
            Partida partida,
            CategoriaIngresso categoria,
            int quantidade
    ) throws VendaIngressoException {

        if (comprador == null || comprador.isBlank()) {
            throw new VendaIngressoException(
                    "Informe o nome do comprador."
            );
        }

        if (partida == null) {
            throw new VendaIngressoException(
                    "Selecione uma partida."
            );
        }

        if (categoria == null) {
            throw new VendaIngressoException(
                    "Selecione uma categoria."
            );
        }

        if (quantidade <= 0) {
            throw new VendaIngressoException(
                    "Quantidade inválida."
            );
        }

        Venda venda =
                new Venda(
                        gerarNovoIdVenda(),
                        comprador
                );

        for (int i = 0; i < quantidade; i++) {

            Ingresso ingresso =
                    new Ingresso(
                            gerarNovoIdIngresso(),
                            partida.getId(),
                            categoria
                    );

            ingresso.vender();

            venda.adicionarIngresso(ingresso);
        }

        repository.salvar(venda);
    }

    public List<Venda> listarVendas() {
        return repository.listarTodos();
    }

    public Venda buscarVenda(int id) {

        return repository.buscarPorId(id);
    }

    public void excluirVenda(int id)
            throws VendaIngressoException {

        Venda venda = repository.buscarPorId(id);

        if (venda == null) {
            throw new VendaIngressoException(
                    "Venda não encontrada."
            );
        }

        repository.remover(id);
    }

    private int gerarNovoIdVenda() {

        int maior = 0;

        for (Venda venda : repository.listarTodos()) {

            if (venda.getId() > maior) {
                maior = venda.getId();
            }
        }

        return maior + 1;
    }

    private int gerarNovoIdIngresso() {

        int maior = 0;

        for (Venda venda : repository.listarTodos()) {

            for (Ingresso ingresso : venda.getIngressos()) {

                if (ingresso.getId() > maior) {
                    maior = ingresso.getId();
                }
            }
        }

        return maior + 1;
    }
}