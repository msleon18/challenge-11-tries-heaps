import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductLoaderTest {

    @Test
    @DisplayName("Carga las líneas válidas e ignora encabezado, vacías y malas")
    void loadsOnlyValidLines(@TempDir Path tempDir) throws IOException {
        Path csv = tempDir.resolve("products.csv");
        Files.writeString(csv, String.join("\n",
                "name,popularity",      // encabezado: se salta
                "air max,90",           // válida
                "",                     // vacía: se salta
                "air force,95",         // válida
                "sin-popularidad",      // un solo campo: se ignora
                "zapato,abc",           // popularidad no numérica: se ignora
                "a,b,c",                // tres campos: se ignora
                "air jordan,85"         // válida
        ), StandardCharsets.UTF_8);

        Trie trie = new Trie();
        int loaded = ProductLoader.loadInto(trie, csv.toString());

        assertEquals(3, loaded);
        assertTrue(trie.search("air max"));
        assertTrue(trie.search("air force"));
        assertTrue(trie.search("air jordan"));
        assertFalse(trie.search("zapato"));
        assertFalse(trie.search("sin-popularidad"));
    }

    @Test
    @DisplayName("Un archivo que no existe lanza IOException")
    void missingFileThrows(@TempDir Path tempDir) {
        String missing = tempDir.resolve("no-existe.csv").toString();

        assertThrows(IOException.class, () -> ProductLoader.loadInto(new Trie(), missing));
    }

    @Test
    @DisplayName("El catálogo real (data/products.csv) carga 320 productos")
    void realCatalogLoads() throws IOException {
        Trie trie = new Trie();
        int loaded = ProductLoader.loadInto(trie, "data/products.csv");

        assertEquals(320, loaded);

        List<Product> top = trie.searchTopK("air max", 3);
        assertEquals("air max white", top.get(0).name());
        assertEquals("air max", top.get(1).name());
        assertEquals("air max purple", top.get(2).name());
    }
}