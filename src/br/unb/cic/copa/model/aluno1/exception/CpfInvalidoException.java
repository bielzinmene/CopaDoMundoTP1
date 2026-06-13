package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class CpfInvalidoException extends CopaException {
    public CpfInvalidoException() {
        super("CPF inválido! Informe o CPF no formato correto: 000.000.000-00");
    }
}