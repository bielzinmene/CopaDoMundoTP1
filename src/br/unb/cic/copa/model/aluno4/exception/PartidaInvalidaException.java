package br.unb.cic.copa.model.aluno4.exception;
import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class PartidaInvalidaException extends RuntimeException {
    public PartidaInvalidaException(String mensagem) {
        super(mensagem);
    }

}