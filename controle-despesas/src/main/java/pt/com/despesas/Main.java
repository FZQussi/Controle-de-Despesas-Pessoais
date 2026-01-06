package pt.com.despesas;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.service.DespesaService;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        DespesaService service = new DespesaService();

        service.adicionarDespesa(
                new Despesa("Aluguel", "Moradia", 1200.00, LocalDate.now())
        );

        service.adicionarDespesa(
                new Despesa("Supermercado", "Alimentação", 350.50, LocalDate.now())
        );

        service.listarDespesas()
                .forEach(System.out::println);
    }
}
