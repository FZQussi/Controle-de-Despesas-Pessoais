package pt.com.despesas.importer;

import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioCsvImporter {

    // -------------------------------
    // Importação com validação rígida
    // -------------------------------
    public List<Despesa> importar(Path ficheiro) throws IOException {

        if (!Files.exists(ficheiro)) {
            throw new IllegalArgumentException("Ficheiro CSV não encontrado");
        }

        List<String> linhas = Files.readAllLines(ficheiro);

        if (linhas.size() <= 1) {
            throw new CsvValidationException("CSV não contém dados");
        }

        List<Despesa> despesas = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) continue;

            String[] campos = linha.split(",");

            if (campos.length != 4) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": número inválido de colunas"
                );
            }

            String descricao = campos[0].trim();
            String categoria = campos[1].trim();
            String valorStr = campos[2].trim();
            String dataStr = campos[3].trim();

            if (descricao.isEmpty()) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": descrição vazia"
                );
            }

            if (categoria.isEmpty()) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": categoria vazia"
                );
            }

            double valor;
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": valor inválido"
                );
            }

            if (valor <= 0) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": valor deve ser positivo"
                );
            }

            LocalDate data;
            try {
                data = LocalDate.parse(dataStr);
            } catch (DateTimeParseException e) {
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": data inválida (yyyy-MM-dd)"
                );
            }

            despesas.add(new Despesa(descricao, categoria, valor, data));
        }

        return despesas;
    }

    // -------------------------------
    // Importação parcial: ignora erros
    // -------------------------------
    public List<Despesa> importarParcial(Path ficheiro) throws IOException {

        if (!Files.exists(ficheiro)) {
            throw new IllegalArgumentException("Ficheiro CSV não encontrado");
        }

        List<String> linhas = Files.readAllLines(ficheiro);

        if (linhas.size() <= 1) {
            System.out.println("CSV não contém dados");
            return List.of();
        }

        List<Despesa> despesas = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) continue;

            String[] campos = linha.split(",");

            if (campos.length != 4) {
                System.out.println("Linha " + (i + 1) + " ignorada: número inválido de colunas");
                continue;
            }

            String descricao = campos[0].trim();
            String categoria = campos[1].trim();
            String valorStr = campos[2].trim();
            String dataStr = campos[3].trim();

            try {
                if (descricao.isEmpty() || categoria.isEmpty()) {
                    throw new IllegalArgumentException("Descrição ou categoria vazia");
                }

                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) throw new IllegalArgumentException("Valor deve ser positivo");

                LocalDate data = LocalDate.parse(dataStr);

                despesas.add(new Despesa(descricao, categoria, valor, data));

            } catch (Exception e) {
                System.out.println("Linha " + (i + 1) + " ignorada: " + e.getMessage());
            }
        }

        return despesas;
    }
}
