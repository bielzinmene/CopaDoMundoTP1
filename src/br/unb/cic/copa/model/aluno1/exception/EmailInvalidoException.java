package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class EmailInvalidoException extends CopaException {
    public EmailInvalidoException() {
        super("Email inválido! Informe um email no formato correto. Ex: usuario@email.com");
    }
}//testando