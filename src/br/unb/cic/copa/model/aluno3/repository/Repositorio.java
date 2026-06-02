package br.unb.cic.copa.model.aluno3.repository;

import java.io.IOException;
import java.util.List;

// Interface genérica que define as operações de persistência em arquivo
public interface Repositorio<T> {

    void salvar(T entidade) throws IOException;

    T buscarPorId(int id) throws IOException;

    List<T> listarTodos() throws IOException;

    void remover(int id) throws IOException;
}