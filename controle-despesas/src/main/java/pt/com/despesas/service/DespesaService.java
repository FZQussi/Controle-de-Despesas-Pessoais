package pt.com.despesas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepository;
import pt.com.despesas.repository.DespesaRepositoryInterface;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DespesaService implements DespesaServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(DespesaService.class);

    private final DespesaRepositoryInterface repository;

    // 👉 Construtor usado em PRODUÇÃO (Main / Menu)
    public DespesaService() {
        this.repository = new DespesaRepository();
        log.info("DespesaService inicializado com DespesaRepository padrão");
    }

    // 👉 Construtor usado em TESTES
    public DespesaService(DespesaRepositoryInterface repository) {
        this.repository = repository;
        log.info("DespesaService inicializado com repositório injetado: {}", repository.getClass().getSimpleName());
    }

    @Override
    public void adicionarDespesa(Despesa despesa) {
        log.debug("Tentativa de adicionar despesa: {} | {} | {}", despesa.getDescricao(), despesa.getCategoria(), despesa.getValor());

        if (despesa.getValor() <= 0) {
            log.warn("Falha ao adicionar despesa com valor inválido: {}", despesa.getValor());
            throw new IllegalArgumentException("O valor da despesa deve ser positivo");
        }

        repository.salvar(despesa);
        log.info("Despesa adicionada com sucesso: {} | {} | {}", despesa.getDescricao(), despesa.getCategoria(), despesa.getValor());
    }

    @Override
    public List<Despesa> listarDespesas() {
        List<Despesa> despesas = repository.listar();
        log.debug("Listando despesas, total encontrado: {}", despesas.size());
        return despesas;
    }

    @Override
    public Map<String, Double> totalPorCategoria() {
        log.debug("Calculando total por categoria");

        Map<String, Double> totais = new HashMap<>();
        for (Despesa despesa : repository.listar()) {
            totais.merge(despesa.getCategoria(), despesa.getValor(), Double::sum);
        }

        log.info("Total por categoria calculado: {}", totais);
        return totais;
    }

    @Override
    public double totalDoMes(YearMonth mes) {
        double total = repository.listar().stream()
                .filter(d -> YearMonth.from(d.getData()).equals(mes))
                .mapToDouble(Despesa::getValor)
                .sum();

        log.info("Total do mês {}: {}", mes, total);
        return total;
    }

    @Override
    public List<Despesa> listarPorMes(YearMonth mes) {
        List<Despesa> despesas = repository.listar().stream()
                .filter(d -> YearMonth.from(d.getData()).equals(mes))
                .collect(Collectors.toList());

        log.debug("Despesas listadas para o mês {}: {}", mes, despesas.size());
        return despesas;
    }
}
