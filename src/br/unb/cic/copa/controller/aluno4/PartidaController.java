package br.unb.cic.copa.controller.aluno4;

import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.repository.PartidaRepository;

import java.util.List;

public class PartidaController {

    private final PartidaRepository repository;

    public PartidaController() {
        this.repository = new PartidaRepository();
    }

    public void salvarPartida(Partida partida) {
        repository.salvar(partida);
    }

    public Partida buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Partida> listarTodas() {
        return repository.listarTodos();
    }

    public List<Partida> listarPorArbitro(int arbitroId) {
        return repository.listarPorArbitro(arbitroId);
    }

    public void excluirPartida(int id) {
        repository.remover(id);
    }

    public List<Partida> listarTodasPartidas() {
        return repository.listarTodos();
    }

    public int gerarNovoId() {
        List<Partida> lista = repository.listarTodos();
        int maior = 0;
        for (Partida p : lista) {
            if (p.getId() > maior) maior = p.getId();
        }
        return maior + 1;
    }
}