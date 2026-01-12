package pt.com.despesas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(Menu.class);

    private final DespesaService service;
    private final Scanner scanner;

    public Menu(DespesaService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        log.info("Menu iniciado");
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
            log.info("Opção escolhida: {}", opcao);

            switch (opcao) {
                case 1 -> adicionarDespesa();
                case 2 -> listarDespesas();
                case 3 -> mostrarRelatorioPorCategoria();
                case 4 -> relatorioMensal();
                case 5 -> exportarRelatorioMensal();
                case 6 -> importarCsvRigoroso();
                case 7 -> importarCsvParcial();
                case 0 -> log.info("Usuário optou por sair");
                default -> log.warn("Opção inválida selecionada: {}", opcao);
            }

        } while (opcao != 0);
    }

    // -------------------------
    // Importação CSV rígida
    // -------------------------
    private void importarCsvRigoroso() {
        System.out.print("Caminho do ficheiro CSV: ");
        String caminho = scanner.nextLine();
        log.info("Importação CSV rígida iniciada: {}", caminho);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        try {
            List<Despesa> despesas = importer.importar(Path.of(caminho));
            despesas.forEach(service::adicionarDespesa);
            log.info("{} despesas importadas com sucesso", despesas.size());
            System.out.println(despesas.size() + " despesas importadas com sucesso.");

        } catch (CsvValidationException e) {
            log.error("Erro no CSV: {}", e.getMessage());
            System.out.println("Erro no CSV: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao importar CSV", e);
            System.out.println("Erro ao importar CSV: " + e.getMessage());
        }
    }

    // -------------------------
    // Importação CSV parcial
    // -------------------------
    private void importarCsvParcial() {
        System.out.print("Caminho do ficheiro CSV: ");
        String caminho = scanner.nextLine();
        log.info("Importação CSV parcial iniciada: {}", caminho);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        try {
            List<Despesa> despesas = importer.importarParcial(Path.of(caminho));
            despesas.forEach(service::adicionarDespesa);
            log.info("{} despesas válidas importadas com sucesso", despesas.size());
            System.out.println(despesas.size() + " despesas válidas importadas com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao importar CSV parcial", e);
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
        log.info("Exportação de relatório para {}/{} iniciada, {} despesas encontradas", ano, mes, despesas.size());

        if (despesas.isEmpty()) {
            log.info("Nenhuma despesa para exportar");
            System.out.println("Nenhuma despesa para exportar.");
            return;
        }

        RelatorioCsvExporter exporter = new RelatorioCsvExporter();
        try {
            String diretorioPadrao = Config.get("relatorios.diretorio", "relatorios");
            Path ficheiro = exporter.exportar(despesas, yearMonth, Path.of(diretorioPadrao));
            log.info("Relatório exportado com sucesso para {}", ficheiro.toAbsolutePath());
            System.out.println("Relatório exportado para: " + ficheiro.toAbsolutePath());

        } catch (Exception e) {
            log.error("Erro ao exportar relatório", e);
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
        List<Despesa> despesas = service.listarPorMes(yearMonth);
        double total = service.totalDoMes(yearMonth);

        log.info("Gerando relatório mensal para {}-{}, {} despesas encontradas", ano, mes, despesas.size());
        System.out.println("\n--- Relatório " + yearMonth + " ---");

        if (despesas.isEmpty()) {
            log.info("Nenhuma despesa encontrada para o mês");
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        despesas.forEach(d -> log.info("Despesa: {}", d));
        despesas.forEach(System.out::println);
        System.out.println("Total do mês: " + total);
        log.info("Total do mês calculado: {}", total);
    }

    // -------------------------
    // Relatório por categoria
    // -------------------------
    private void mostrarRelatorioPorCategoria() {
        var totais = service.totalPorCategoria();
        log.info("Gerando relatório por categoria, {} categorias encontradas", totais.size());

        if (totais.isEmpty()) {
            log.info("Nenhuma despesa registrada");
            System.out.println("Nenhuma despesa registrada.");
            return;
        }

        System.out.println("\n--- Total por Categoria ---");
        totais.forEach((categoria, total) -> {
            log.info("Categoria: {}, Total: {}", categoria, total);
            System.out.println(categoria + ": " + total);
        });
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
        log.info("Tentando adicionar despesa: {}", despesa);

        try {
            service.adicionarDespesa(despesa);
            log.info("Despesa adicionada com sucesso");
            System.out.println("Despesa adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            log.error("Falha ao adicionar despesa: {}", e.getMessage());
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // -------------------------
    // Listar despesas
    // -------------------------
    private void listarDespesas() {
        List<Despesa> despesas = service.listarDespesas();
        log.info("Listando {} despesas", despesas.size());

        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa registrada.");
        } else {
            despesas.forEach(d -> log.info("Despesa: {}", d));
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
                log.warn("Entrada inválida para inteiro");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Digite um valor válido: ");
                log.warn("Entrada inválida para double");
            }
        }
    }
}
