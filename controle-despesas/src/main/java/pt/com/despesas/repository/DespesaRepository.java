package pt.com.despesas.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DespesaRepository implements DespesaRepositoryInterface {

    private static final Logger log =
            LoggerFactory.getLogger(DespesaRepository.class);

    private static final String ARQUIVO = "src/main/resources/despesas.csv";

    @Override
    public void salvar(Despesa despesa) {

        log.debug("A guardar despesa: {} | {} | {}",
                despesa.getDescricao(),
                despesa.getCategoria(),
                despesa.getValor()
        );

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

            log.info("Despesa guardada com sucesso em {}", ARQUIVO);

        } catch (IOException e) {
            log.error("Erro ao salvar despesa no arquivo {}", ARQUIVO, e);
            throw new RuntimeException("Erro ao salvar despesa", e);
        }
    }

    @Override
    public List<Despesa> listar() {

        log.debug("A ler despesas do arquivo {}", ARQUIVO);

        List<Despesa> despesas = new ArrayList<>();

        File ficheiro = new File(ARQUIVO);

        if (!ficheiro.exists()) {
            log.warn("Arquivo de despesas não encontrado: {}", ARQUIVO);
            return despesas;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(ficheiro),
                        StandardCharsets.UTF_8
                )
        )) {
            String linha;
            while ((linha = reader.readLine()) != null) {

                String[] dados = linha.split(",");

                try {
                    despesas.add(new Despesa(
                            dados[0],
                            dados[1],
                            Double.parseDouble(dados[2]),
                            LocalDate.parse(dados[3])
                    ));
                } catch (Exception e) {
                    log.warn("Linha ignorada por erro de parsing: {}", linha);
                }
            }

            log.info("Leitura concluída: {} despesas carregadas", despesas.size());

        } catch (IOException e) {
            log.error("Erro ao ler despesas do arquivo {}", ARQUIVO, e);
            throw new RuntimeException("Erro ao ler despesas", e);
        }

        return despesas;
    }
}

