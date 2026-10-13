import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Carga productos desde un archivo CSV (formato: name,popularity)
 * y los inserta en un Trie.
 */
public class ProductLoader {

    /**
     * @param trie     el Trie donde se insertan los productos
     * @param filePath ruta del archivo CSV
     * @return cuántos productos se cargaron correctamente
     */
    public static int loadInto(Trie trie, String filePath) throws IOException {
        int loaded = 0;
        int lineNumber = 0;

        try (BufferedReader reader =
                     Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) {
                    continue;               // saltamos el encabezado
                }
                if (line.isBlank()) {
                    continue;               // saltamos líneas vacías
                }

                String[] parts = line.split(",");
                if (parts.length != 2) {
                    System.out.println("Línea " + lineNumber + " ignorada (formato inválido): " + line);
                    continue;
                }

                String name = parts[0].trim();
                try {
                    int popularity = Integer.parseInt(parts[1].trim());
                    trie.insert(name, popularity);
                    loaded++;
                } catch (NumberFormatException e) {
                    System.out.println("Línea " + lineNumber + " ignorada (popularidad no numérica): " + line);
                }
            }
        }
        return loaded;
    }
}