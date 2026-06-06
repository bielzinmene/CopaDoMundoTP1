package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando a senha não atende à política mínima de segurança
// Regra: mínimo 8 caracteres, com letras E números
// Ex: "abc123" tem menos de 8 — inválida
// Ex: "abcdefgh" não tem número — inválida
// Ex: "Senha123" tem 8 chars, letras e número — válida
public class SenhaFracaException extends CopaException {
    public SenhaFracaException() {
        super("Senha fraca! A senha deve ter no mínimo 8 caracteres, letras e números.");
    }
}