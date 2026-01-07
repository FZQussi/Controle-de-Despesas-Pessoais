package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepository;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DespesaService implements DespesaServiceInterface {

    private final DespesaRepository repository = new DespesaRepository();

    @Override
    public void adicionarDespesa(Despesa despesa) {
        if (despesa.getValor() <= 0) {
            throw new IllegalArgumentException("O valor da despesa deve ser positivo");
        }
        repository.salvar(despesa);
    }

    @Override
    public List<Despesa> listarDespesas() {
        return repository.listar();
    }
    @Override
public Map<String, Double> totalPorCategoria() {
    Map<String, Double> totais = new HashMap<>();

    for (Despesa despesa : repository.listar()) {
        totais.merge(
            despesa.getCategoria(),
            despesa.getValor(),
            Double::sum
        );
    }

    return totais;
}
}
