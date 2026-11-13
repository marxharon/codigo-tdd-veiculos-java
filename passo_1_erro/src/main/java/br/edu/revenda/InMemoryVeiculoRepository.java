package br.edu.revenda;

import java.util.Optional;

/**
 * Passo 1 ERRO (Fase RED): Esqueleto inicial (stub) ainda sem lógica de persistência nem geração de ID.
 */
public class InMemoryVeiculoRepository implements VeiculoRepository {

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        return veiculo; // AINDA NÃO GERA ID NEM PERSISTE!
    }

    @Override
    public Optional<Veiculo> buscarPorId(Long id) {
        return Optional.empty();
    }
}
