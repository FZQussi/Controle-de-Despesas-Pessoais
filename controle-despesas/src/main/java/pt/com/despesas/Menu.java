package pt.com.despesas;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.service.DespesaService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final DespesaService service;
    private final Scanner scanner;

    public Menu(DespesaService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n=== Controle de Despesas ===");
            System.out.println("1 - Adicionar despesa");
            System.out.println("2 - Listar despesas");
            System.out.println("3 - Relatório por categoria");
            System.out.println("4 - Relatório mensal");

            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> adicionarDespesa();
                case 2 -> listarDespesas();
                case 3 -> mostrarRelatorioPorCategoria();
                case 4 -> relatorioMensal();

                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
    private void relatorioMensal() {
    System.out.print("Ano (ex: 2026): ");
    int ano = lerInteiro();

    System.out.print("Mês (1-12): ");
    int mes = lerInteiro();

    YearMonth yearMonth = YearMonth.of(ano, mes);

    double total = service.totalDoMes(yearMonth);
    var despesas = service.listarPorMes(yearMonth);

    System.out.println("\n--- Relatório " + yearMonth + " ---");

    if (despesas.isEmpty()) {
        System.out.println("Nenhuma despesa encontrada.");
        return;
    }

    despesas.forEach(System.out::println);
    System.out.println("Total do mês: " + total);
}

    private void mostrarRelatorioPorCategoria() {
    var totais = service.totalPorCategoria();

    if (totais.isEmpty()) {
        System.out.println("Nenhuma despesa registrada.");
        return;
    }

    System.out.println("\n--- Total por Categoria ---");
    totais.forEach((categoria, total) ->
        System.out.println(categoria + ": " + total)
    );
}

    private void adicionarDespesa() {
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("Valor: ");
        double valor = lerDouble();

        Despesa despesa = new Despesa(descricao, categoria, valor, LocalDate.now());

        try {
            service.adicionarDespesa(despesa);
            System.out.println("Despesa adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarDespesas() {
        List<Despesa> despesas = service.listarDespesas();
        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa registrada.");
        } else {
            despesas.forEach(System.out::println);
        }
    }

    private int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Digite um valor válido: ");
            }
        }
    }
}
