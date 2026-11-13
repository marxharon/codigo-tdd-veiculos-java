package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 4: Contrato de persistência de veículos.
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);
}
