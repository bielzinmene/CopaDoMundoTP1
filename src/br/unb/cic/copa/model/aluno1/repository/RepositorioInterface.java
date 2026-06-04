package br.unb.cic.copa.model.aluno1.repository;

import java.io.IOException;
import java.util.List;

public class RepositorioInterface {

    public interface Repositorio<T> {
        void salvar(T entity) throws IOException;
        T buscarPorId(int id) throws IOException;
        List<T> listarTodos() throws IOException;
        void remover(int id) throws IOException;
    }
}
