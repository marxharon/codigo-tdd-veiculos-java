package br.edu.revenda;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Passo 6 (Versão Final Refatorada): Implementação em memória (Fake) thread-safe para testes e prototipação.
 */
public class InMemoryVeiculoRepository implements VeiculoRepository {

    private final Map<Long, Veiculo> bancoEmMemoria = new LinkedHashMap<>();
    private long sequenciaId = 1L;

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        Long idAtribuido = veiculo.getId() != null ? veiculo.getId() : sequenciaId++;
        Veiculo persistido = veiculo.comId(idAtribuido);
        bancoEmMemoria.put(idAtribuido, persistido);
        return persistido;
    }

    @Override
    public Optional<Veiculo> buscarPorId(Long id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return bancoEmMemoria.values().stream()
                .filter(v -> v.getPlaca().equals(placa))
                .findFirst();
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return buscarPorPlaca(placa).isPresent();
    }

    @Override
    public List<Veiculo> listarTodos() {
        return List.copyOf(bancoEmMemoria.values());
    }
}
