package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando tenta cadastrar um usuário com email que já existe no sistema
// Cada email deve ser único — é o identificador principal do usuário
public class UsuarioDuplicadoException extends CopaException {
    public UsuarioDuplicadoException() {
        super("Já existe um usuário cadastrado com este email!");
    }
}