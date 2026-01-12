package pt.com.despesas.storage;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BackupServiceRestoreTest {

    @Test
    void deveRestaurarBackup() throws Exception {

        Path tempDir = Files.createTempDirectory("dados");
        Path ficheiro = tempDir.resolve("despesas.json");
        Path backupDir = tempDir.resolve("backups");

        Files.writeString(ficheiro, "ORIGINAL");

        BackupService service = new BackupService(backupDir);
        service.criarBackup(ficheiro);

        Files.writeString(ficheiro, "ALTERADO");

        Path backup = Files.list(backupDir).findFirst().get();
        service.restaurarBackup(backup, ficheiro);

        String conteudo = Files.readString(ficheiro);
        assertEquals("ORIGINAL", conteudo);
    }
}
