package br.unb.cic.copa.model.aluno4.exception;


import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class DataInvalidaException extends CopaException {
    public DataInvalidaException(String mensagem) {
        super(mensagem);
    }
}