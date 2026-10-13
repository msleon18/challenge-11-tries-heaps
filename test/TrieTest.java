import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
        trie.insert("air max", 90);
        trie.insert("air force", 95);
        trie.insert("air jordan", 85);
        trie.insert("adidas boost", 80);
    }

    /** Ayudante: de una lista de productos, deja solo los nombres. */
    private List<String> names(List<Product> products) {
        List<String> out = new ArrayList<>();
        for (Product p : products) {
            out.add(p.name());
        }
        return out;
    }

    // ---------------- insert y search ----------------

    @Test
    @DisplayName("search encuentra un producto insertado")
    void searchFindsInsertedProduct() {
        assertTrue(trie.search("air max"));
        assertTrue(trie.search("adidas boost"));
    }

    @Test
    @DisplayName("search no confunde un prefijo con un producto")
    void searchRejectsPrefixOnly() {
        assertFalse(trie.search("air"));
        assertFalse(trie.search("air m"));
    }

    @Test
    @DisplayName("search devuelve false para algo que no existe")
    void searchRejectsMissing() {
        assertFalse(trie.search("nike"));
        assertFalse(trie.search("air max 90"));
    }

    @Test
    @DisplayName("Ignora mayúsculas y espacios sobrantes")
    void ignoresCaseAndOuterSpaces() {
        assertTrue(trie.search("AIR MAX"));
        assertTrue(trie.search("  air max  "));
    }

    @Test
    @DisplayName("Conserva el nombre original con sus mayúsculas")
    void keepsOriginalName() {
        trie.insert("Zapato Rojo", 10);
        List<Product> result = trie.searchTopK("zap", 1);

        assertEquals("Zapato Rojo", result.get(0).name());
    }

    @Test
    @DisplayName("Un Trie vacío no encuentra nada")
    void emptyTrieFindsNothing() {
        Trie empty = new Trie();
        assertFalse(empty.search("x"));
        assertTrue(empty.searchTopK("a", 3).isEmpty());
    }

    // ---------------- searchTopK ----------------

    @Test
    @DisplayName("El ejemplo del enunciado: top 2 de 'air'")
    void topTwoOfAir() {
        assertEquals(List.of("air force", "air max"), names(trie.searchTopK("air", 2)));
    }

    @Test
    @DisplayName("Si k es mayor que los resultados, devuelve todos ordenados")
    void kLargerThanResults() {
        assertEquals(
                List.of("air force", "air max", "air jordan"),
                names(trie.searchTopK("air", 10)));
    }

    @Test
    @DisplayName("Un prefijo corto recorre varias ramas")
    void shortPrefixCoversSeveralBranches() {
        assertEquals(
                List.of("air force", "air max", "air jordan"),
                names(trie.searchTopK("a", 3)));
    }

    @Test
    @DisplayName("Un prefijo inexistente devuelve lista vacía")
    void unknownPrefixReturnsEmpty() {
        assertTrue(trie.searchTopK("nike", 3).isEmpty());
    }

    @Test
    @DisplayName("k igual a 0 o negativo devuelve lista vacía")
    void nonPositiveKReturnsEmpty() {
        assertTrue(trie.searchTopK("air", 0).isEmpty());
        assertTrue(trie.searchTopK("air", -5).isEmpty());
    }

    @Test
    @DisplayName("Un prefijo vacío devuelve los más populares de todo el catálogo")
    void emptyPrefixReturnsGlobalTop() {
        assertEquals(List.of("air force", "air max"), names(trie.searchTopK("", 2)));
    }

    @Test
    @DisplayName("Un producto que también es prefijo de otro aparece en los resultados")
    void productThatIsAlsoAPrefix() {
        trie.insert("air max black", 72);

        assertEquals(
                List.of("air max", "air max black"),
                names(trie.searchTopK("air max", 10)));
    }

    @Test
    @DisplayName("Insertar de nuevo un nombre actualiza su popularidad")
    void reinsertUpdatesPopularity() {
        trie.insert("air jordan", 99);

        assertEquals("air jordan", trie.searchTopK("air", 1).get(0).name());
        assertEquals(3, trie.searchTopK("air", 10).size());   // sigue habiendo 3, no 4
    }

    @Test
    @DisplayName("Coincide con una búsqueda por fuerza bruta en 1000 productos aleatorios")
    void matchesBruteForce() {
        Random random = new Random(7);

        // Popularidades 1..1000 sin repetir, para que no haya empates
        List<Integer> popularities = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            popularities.add(i);
        }
        Collections.shuffle(popularities, random);

        // Nombres con letras a, b, c para que muchos compartan prefijo
        Trie big = new Trie();
        List<Product> all = new ArrayList<>();
        String letters = "abc";
        for (int i = 0; i < 1000; i++) {
            StringBuilder name = new StringBuilder();
            int length = 1 + random.nextInt(4);
            for (int j = 0; j < length; j++) {
                name.append(letters.charAt(random.nextInt(3)));
            }
            name.append("-").append(i);          // el sufijo garantiza nombres únicos

            Product p = new Product(name.toString(), popularities.get(i));
            all.add(p);
            big.insert(p.name(), p.popularity());
        }

        String[] prefixes = {"", "a", "b", "ab", "abc", "ca", "zzz"};
        int[] ks = {1, 5, 20};

        for (String prefix : prefixes) {
            for (int k : ks) {
                List<Product> expected = all.stream()
                        .filter(p -> p.name().startsWith(prefix))
                        .sorted(Comparator.comparingInt(Product::popularity).reversed())
                        .limit(k)
                        .toList();

                assertEquals(expected, big.searchTopK(prefix, k),
                        "Falló con prefijo \"" + prefix + "\" y k=" + k);
            }
        }
    }
}