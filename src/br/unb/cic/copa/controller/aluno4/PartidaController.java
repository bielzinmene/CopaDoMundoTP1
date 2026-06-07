package br.unb.cic.copa.controller.aluno4;

import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.exception.PartidaInvalidaException;
import br.unb.cic.copa.repository.aluno4.PartidaRepository;


import java.io.IOException;
import java.util.List;

public class PartidaController {
    private br.unb.cic.copa.repository.aluno4.PartidaRepository repositorio;

    public PartidaController() {
        this.repositorio = new br.unb.cic.copa.repository.aluno4.PartidaRepository();
    }

    public void salvarPartida(Partida partida) throws PartidaInvalidaException, IOException {
        // Validação extra de negócio antes de salvar
        if (partida == null) {
            throw new PartidaInvalidaException("Erro: A partida não pode ser nula.");
        }

        repositorio.salvar(partida);
    }
    public List<Partida> listarPorArbitro(int arbitroId) {
        PartidaRepository repository = null;
        return repository.listarPorArbitro(arbitroId);
    }

    public List<Partida> listarPartidas() throws IOException {
        return repositorio.listarTodos();
    }

    public void removerPartida(int id) throws IOException {
        repositorio.remover(id);
    }
}