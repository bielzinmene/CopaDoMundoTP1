package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando o email informado não tem formato válido
// Ex: email sem @ ou sem ponto
public class EmailInvalidoException extends CopaException {
    public EmailInvalidoException() {
        super("Email inválido! Informe um email no formato correto. Ex: usuario@email.com");
    }
}