package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 2 ERRO (Fase RED): Mesmo repositório do Passo 1.
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);
}
