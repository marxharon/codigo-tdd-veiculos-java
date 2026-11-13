package br.edu.revenda;

/**
 * Passo 3: Exceção de domínio para violações das regras de negócio.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
