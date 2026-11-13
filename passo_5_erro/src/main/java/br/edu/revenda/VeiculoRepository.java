package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 5 ERRO (Fase RED): Contrato ainda sem o método existePorPlaca(String placa).
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);
}
