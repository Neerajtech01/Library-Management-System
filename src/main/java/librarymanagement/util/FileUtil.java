package librarymanagement.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String DATA_DIR = "data";

    public static List<String> read(String fileName) {
        try {
            Path path = Paths.get(DATA_DIR, fileName);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + fileName + ": " + e.getMessage());
        }
    }

    public static void write(String fileName, List<String> lines) {
        try {
            Path path = Paths.get(DATA_DIR, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Could not save " + fileName + ": " + e.getMessage());
        }
    }

    public static void append(String fileName, String line) {
        List<String> lines = read(fileName);
        lines.add(line);
        write(fileName, lines);
    }
}
