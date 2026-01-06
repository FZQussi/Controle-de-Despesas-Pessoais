package pt.com.despesas.repository;

import pt.com.despesas.model.Despesa;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DespesaRepository {

    private static final String ARQUIVO = "src/main/resources/despesas.csv";

    public void salvar(Despesa despesa) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(ARQUIVO, true),
                        StandardCharsets.UTF_8
                )
        )) {
            writer.write(
                despesa.getDescricao() + "," +
                despesa.getCategoria() + "," +
                despesa.getValor() + "," +
                despesa.getData()
            );
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar despesa", e);
        }
    }

    public List<Despesa> listar() {
        List<Despesa> despesas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(ARQUIVO),
                        StandardCharsets.UTF_8
                )
        )) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                despesas.add(new Despesa(
                        dados[0],
                        dados[1],
                        Double.parseDouble(dados[2]),
                        java.time.LocalDate.parse(dados[3])
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler despesas", e);
        }

        return despesas;
    }
}
