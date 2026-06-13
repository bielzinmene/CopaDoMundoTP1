package br.unb.cic.copa.model.aluno3.repository;

import java.io.IOException;
import java.util.List;

// Interface genérica que define as operações de persistência em arquivo
public interface Repositorio<T> {

    // salva uma nova entidade (ou atualiza, dependendo da implementação) no arquivo
    void salvar(T entidade) throws IOException;
    // busca uma entidade pelo seu id; retorna null se não encontrar
    T buscarPorId(int id) throws IOException;
    // retorna a lista com todas as entidades cadastradas no arquivo
    List<T> listarTodos() throws IOException;
    // remove a entidade que possui o id informado
    void remover(int id) throws IOException;


}