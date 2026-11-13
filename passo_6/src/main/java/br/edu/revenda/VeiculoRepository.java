package br.edu.revenda;

import java.util.List;
import java.util.Optional;

/**
 * Passo 6 (Versão Final Refatorada): Porta de saída para persistência de veículos no estoque.
 */
public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(Long id);

    Optional<Veiculo> buscarPorPlaca(String placa);

    boolean existePorPlaca(String placa);

    List<Veiculo> listarTodos();
}
