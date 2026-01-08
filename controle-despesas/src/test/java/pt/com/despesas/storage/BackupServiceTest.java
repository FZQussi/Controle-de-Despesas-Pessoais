package pt.com.despesas.storage;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BackupServiceTest {

    @Test
    void deveCriarBackupDoFicheiro() throws Exception {

        Path tempDir = Files.createTempDirectory("dados");
        Path ficheiro = tempDir.resolve("despesas.json");
        Path backupDir = tempDir.resolve("backups");

        Files.writeString(ficheiro, "{}");

        BackupService backupService = new BackupService(backupDir);
        backupService.criarBackup(ficheiro);

        assertTrue(Files.exists(backupDir));
        assertEquals(1, Files.list(backupDir).count());
    }
}
