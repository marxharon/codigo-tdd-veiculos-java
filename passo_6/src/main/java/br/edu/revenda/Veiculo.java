package br.edu.revenda;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Passo 6 (Versão Final Refatorada): Entidade imutável de domínio representando um Veículo no estoque.
 */
public class Veiculo {

    private final Long id;
    private final String placa;
    private final String modelo;
    private final int ano;
    private final BigDecimal preco;

    public Veiculo(String placa, String modelo, int ano, BigDecimal preco) {
        this(null, placa, modelo, ano, preco);
    }

    public Veiculo(Long id, String placa, String modelo, int ano, BigDecimal preco) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
    }

    public Veiculo comId(Long novoId) {
        return new Veiculo(novoId, this.placa, this.modelo, this.ano, this.preco);
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo veiculo)) return false;
        return ano == veiculo.ano
                && Objects.equals(id, veiculo.id)
                && Objects.equals(placa, veiculo.placa)
                && Objects.equals(modelo, veiculo.modelo)
                && Objects.equals(preco, veiculo.preco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa, modelo, ano, preco);
    }
}
