package br.unb.cic.copa.model.aluno1.repository;

import java.io.IOException;
import java.util.List;

// Interface genérica — define o "contrato" que todo repositório deve seguir
// T é o tipo do objeto (no nosso caso, Usuario)
// Seguindo o mesmo padrão da Aluno 3
public interface Repositorio<T> {

    // Salva ou atualiza um objeto no arquivo
    void salvar(T entidade) throws IOException;

    // Busca um objeto pelo ID — lança exceção se não encontrar
    T buscarPorId(int id) throws IOException;

    // Retorna todos os objetos salvos no arquivo
    List<T> listarTodos() throws IOException;

    // Remove um objeto pelo ID — lança exceção se não encontrar
    void remover(int id) throws IOException;
}