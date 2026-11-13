package br.edu.revenda;

/**
 * Passo 6 (Versão Final Refatorada): Exceção de domínio lançada quando qualquer critério de aceitação é violado.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
