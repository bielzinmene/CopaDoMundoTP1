package br.unb.cic.copa.controller.aluno4;

import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.repository.aluno4.PartidaRepository;
import java.util.List;

public class PartidaController {

    private PartidaRepository repository;

    public PartidaController() {
        this.repository = new PartidaRepository();
    }

    public void salvarPartida(Partida partida) {
        if (partida.getId() == 0) {
            partida.setId(gerarNovoId());
        }
        repository.salvar(partida);
    }

    public Partida buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Partida> listarTodas() {
        return repository.listarTodos();
    }

    public void excluirPartida(int id) {
        repository.remover(id);
    }

    private int gerarNovoId() {
        List<Partida> partidas = repository.listarTodos();
        int maiorId = 0;
        for (Partida p : partidas) {
            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }
        return maiorId + 1;
    }
}