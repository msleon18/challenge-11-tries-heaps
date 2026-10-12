import java.util.List;

public class Main {

    public static void main(String[] args) {
        Trie trie = new Trie();

        // Datos del enunciado
        trie.insert("air max", 90);
        trie.insert("air force", 95);
        trie.insert("air jordan", 85);
        trie.insert("adidas boost", 80);

        // Prueba 1: el ejemplo del enunciado
        System.out.println("=== searchTopK(\"air\", 2) ===");
        print(trie.searchTopK("air", 2));
        // Esperado: air force (95), air max (90)

        // Prueba 2: k mayor que los resultados disponibles
        System.out.println("\n=== searchTopK(\"air\", 10) ===");
        print(trie.searchTopK("air", 10));
        // Esperado: air force (95), air max (90), air jordan (85)

        // Prueba 3: prefijo más corto (incluye adidas)
        System.out.println("\n=== searchTopK(\"a\", 3) ===");
        print(trie.searchTopK("a", 3));
        // Esperado: air force (95), air max (90), air jordan (85)

        // Prueba 4: prefijo que no existe
        System.out.println("\n=== searchTopK(\"nike\", 3) ===");
        print(trie.searchTopK("nike", 3));
        // Esperado: (sin resultados)

        // Prueba 5: mayúsculas
        System.out.println("\n=== searchTopK(\"AIR\", 1) ===");
        print(trie.searchTopK("AIR", 1));
        // Esperado: air force (95)

        // Prueba 6: search exacto
        System.out.println("\n=== search ===");
        System.out.println("air max existe? " + trie.search("air max"));   // true
        System.out.println("air existe? " + trie.search("air"));           // false (es prefijo, no producto)
    }

    private static void print(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("(sin resultados)");
            return;
        }
        for (Product p : products) {
            System.out.println(" - " + p);
        }
    }
}