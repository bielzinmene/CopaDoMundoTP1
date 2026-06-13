package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class AcessoNegadoException extends CopaException {
    public AcessoNegadoException() {
        super("Acesso negado! Você não tem permissão para realizar esta operação.");
    }
}
//testando