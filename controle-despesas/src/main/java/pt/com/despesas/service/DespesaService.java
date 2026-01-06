package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepository;

import java.util.List;

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
}
