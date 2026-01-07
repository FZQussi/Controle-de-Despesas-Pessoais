package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepository;
import pt.com.despesas.repository.DespesaRepositoryInterface;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DespesaService implements DespesaServiceInterface {

    private final DespesaRepositoryInterface repository;

    // 👉 Construtor usado em PRODUÇÃO (Main / Menu)
    public DespesaService() {
        this.repository = new DespesaRepository();
    }

    // 👉 Construtor usado em TESTES
    public DespesaService(DespesaRepositoryInterface repository) {
        this.repository = repository;
    }

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

    @Override
    public double totalDoMes(YearMonth mes) {
        return repository.listar().stream()
                .filter(d -> YearMonth.from(d.getData()).equals(mes))
                .mapToDouble(Despesa::getValor)
                .sum();
    }

    @Override
    public List<Despesa> listarPorMes(YearMonth mes) {
        return repository.listar().stream()
                .filter(d -> YearMonth.from(d.getData()).equals(mes))
                .collect(Collectors.toList());
    }
}
