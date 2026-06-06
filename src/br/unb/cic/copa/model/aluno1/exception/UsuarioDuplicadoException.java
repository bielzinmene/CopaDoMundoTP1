package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class UsuarioDuplicadoException extends CopaException {

    public UsuarioDuplicadoException() {
        super("Já existe um usuário cadastrado com este email!");
    }
}