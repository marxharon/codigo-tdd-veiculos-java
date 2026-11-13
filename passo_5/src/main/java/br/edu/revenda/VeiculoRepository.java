package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 5 (CA4): Contrato de persistência evoluído com verificação de existência por placa.
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);

    boolean existePorPlaca(String placa);
}
