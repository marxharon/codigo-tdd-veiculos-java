package br.edu.revenda;

/**
 * Passo 2 ERRO (Fase RED): Classe de exceção criada para que o novo teste de validação de placa compile.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
