package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class SenhaFracaException extends CopaException {

    public SenhaFracaException() {
        super("Senha fraca! A senha deve ter no mínimo 8 caracteres, letras e números.");
    }
}