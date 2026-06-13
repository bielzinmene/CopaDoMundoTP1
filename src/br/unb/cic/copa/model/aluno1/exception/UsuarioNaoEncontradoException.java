package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class UsuarioNaoEncontradoException extends CopaException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado!");
    }
}//testando