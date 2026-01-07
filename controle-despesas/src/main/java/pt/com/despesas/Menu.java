package pt.com.despesas;

import pt.com.despesas.config.Config;
import pt.com.despesas.export.RelatorioCsvExporter;
import pt.com.despesas.importer.CsvValidationException;
import pt.com.despesas.importer.RelatorioCsvImporter;
import pt.com.despesas.model.Despesa;
import pt.com.despesas.service.DespesaService;

import java.nio.file.Path;
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
            System.out.println("5 - Exportar relatório mensal (CSV)");
            System.out.println("6 - Importar despesas de CSV (rígido)");
            System.out.println("7 - Importar despesas de CSV (parcial)");

            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> adicionarDespesa();
                case 2 -> listarDespesas();
                case 3 -> mostrarRelatorioPorCategoria();
                case 4 -> relatorioMensal();
                case 5 -> exportarRelatorioMensal();
                case 6 -> importarCsvRigoroso();
                case 7 -> importarCsvParcial();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // -------------------------
    // Importação CSV rígida
    // -------------------------
    private void importarCsvRigoroso() {
        System.out.print("Caminho do ficheiro CSV: ");
        String caminho = scanner.nextLine();

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        try {
            List<Despesa> despesas = importer.importar(Path.of(caminho));
            despesas.forEach(service::adicionarDespesa);
            System.out.println(despesas.size() + " despesas importadas com sucesso.");

        } catch (CsvValidationException e) {
            System.out.println("Erro no CSV: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao importar CSV: " + e.getMessage());
        }
    }

    // -------------------------
    // Importação CSV parcial
    // -------------------------
    private void importarCsvParcial() {
        System.out.print("Caminho do ficheiro CSV: ");
        String caminho = scanner.nextLine();

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        try {
            List<Despesa> despesas = importer.importarParcial(Path.of(caminho));
            despesas.forEach(service::adicionarDespesa);
            System.out.println(despesas.size() + " despesas válidas importadas com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao importar CSV: " + e.getMessage());
        }
    }

    // -------------------------
    // Exportação CSV
    // -------------------------
    private void exportarRelatorioMensal() {
        System.out.print("Ano: ");
        int ano = lerInteiro();
        System.out.print("Mês: ");
        int mes = lerInteiro();

        YearMonth yearMonth = YearMonth.of(ano, mes);
        List<Despesa> despesas = service.listarPorMes(yearMonth);

        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa para exportar.");
            return;
        }

        RelatorioCsvExporter exporter = new RelatorioCsvExporter();
        try {
            String diretorioPadrao = Config.get("relatorios.diretorio", "relatorios");
            Path ficheiro = exporter.exportar(despesas, yearMonth, Path.of(diretorioPadrao));

            System.out.println("Relatório exportado para: " + ficheiro.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Erro ao exportar relatório: " + e.getMessage());
        }
    }

    // -------------------------
    // Relatório mensal
    // -------------------------
    private void relatorioMensal() {
        System.out.print("Ano (ex: 2026): ");
        int ano = lerInteiro();
        System.out.print("Mês (1-12): ");
        int mes = lerInteiro();

        YearMonth yearMonth = YearMonth.of(ano, mes);
        double total = service.totalDoMes(yearMonth);
        List<Despesa> despesas = service.listarPorMes(yearMonth);

        System.out.println("\n--- Relatório " + yearMonth + " ---");

        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        despesas.forEach(System.out::println);
        System.out.println("Total do mês: " + total);
    }

    // -------------------------
    // Relatório por categoria
    // -------------------------
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

    // -------------------------
    // Adicionar despesa
    // -------------------------
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

    // -------------------------
    // Listar despesas
    // -------------------------
    private void listarDespesas() {
        List<Despesa> despesas = service.listarDespesas();
        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa registrada.");
        } else {
            despesas.forEach(System.out::println);
        }
    }

    // -------------------------
    // Leitura de números
    // -------------------------
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
