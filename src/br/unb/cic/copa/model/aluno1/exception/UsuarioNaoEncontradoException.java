package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando tenta editar ou excluir um usuário que não existe no sistema
// Ex: buscar pelo ID 99 mas esse ID não está cadastrado
public class UsuarioNaoEncontradoException extends CopaException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado!");
    }
}