package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 1 ERRO (Fase RED): Contrato definido a partir da necessidade do teste.
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);
}
