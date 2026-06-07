package br.unb.cic.copa.model.aluno4.exception;


import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class SelecaoNaoEncontradaException extends CopaException {
    public SelecaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}