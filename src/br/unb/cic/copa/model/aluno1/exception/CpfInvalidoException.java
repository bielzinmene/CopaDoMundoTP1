package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando o CPF informado não tem formato válido
// Formato correto: 000.000.000-00
// Ex: "12345678900" — inválido, sem formatação
// Ex: "123.456.789-00" — válido
public class CpfInvalidoException extends CopaException {
    public CpfInvalidoException() {
        super("CPF inválido! Informe o CPF no formato correto: 000.000.000-00");
    }
}