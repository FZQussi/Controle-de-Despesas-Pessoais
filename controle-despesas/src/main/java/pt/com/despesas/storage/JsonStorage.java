package pt.com.despesas.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

    private final Path ficheiro;
    private final ObjectMapper mapper;

    public JsonStorage(Path ficheiro) {
        this.ficheiro = ficheiro;
        this.mapper = new ObjectMapper()
                .findAndRegisterModules() // suporte a LocalDate
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    // -------------------------
    // Guardar despesas em JSON
    // -------------------------
    public void guardar(List<Despesa> despesas) throws IOException {
        if (ficheiro.getParent() != null) {
            Files.createDirectories(ficheiro.getParent());
        }
        mapper.writeValue(ficheiro.toFile(), despesas);
    }

    // -------------------------
    // Carregar despesas do JSON
    // -------------------------
    public List<Despesa> carregar() throws IOException {
        if (!Files.exists(ficheiro)) {
            return new ArrayList<>();
        }

        return mapper.readValue(
                ficheiro.toFile(),
                new TypeReference<List<Despesa>>() {}
        );
    }
}
