import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Trie para guardar productos y buscarlos por prefijo.
 * Los nombres se normalizan a minúsculas para que "Air" y "air" sean iguales.
 */
public class Trie {

    private final TrieNode root = new TrieNode();   // raíz vacía (diapositiva 4)

    // ------------------------------------------------------------
    // INSERT (diapositiva 8)
    // ------------------------------------------------------------
    public void insert(String name, int popularity) {
        String key = normalize(name);
        TrieNode node = root;

        for (char ch : key.toCharArray()) {
            // Si no existe el hijo para esta letra, lo creamos
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }

        // En el último nodo marcamos el fin de palabra y guardamos el producto
        node.isEndOfWord = true;
        node.product = new Product(name.trim(), popularity);
    }

    // ------------------------------------------------------------
    // SEARCH (diapositiva 9): ¿existe esta palabra exacta?
    // ------------------------------------------------------------
    public boolean search(String name) {
        TrieNode node = findNode(normalize(name));
        return node != null && node.isEndOfWord;
    }

    // ------------------------------------------------------------
    // SEARCH TOP K (el reto)
    // ------------------------------------------------------------
    public List<Product> searchTopK(String prefix, int k) {
        List<Product> result = new ArrayList<>();
        if (k <= 0) {
            return result;
        }

        // 1. Llegar al nodo donde termina el prefijo
        TrieNode start = findNode(normalize(prefix));
        if (start == null) {
            return result;   // el prefijo no existe: lista vacía
        }

        // 2. Min heap de tamaño máximo k, ordenado por popularidad
        MinHeap<Product> heap = new MinHeap<>(Comparator.comparingInt(Product::popularity));

        // 3. Recorrer todo el subárbol y filtrar con el heap
        collect(start, heap, k);

        // 4. Sacar del heap: salen de menor a mayor popularidad...
        while (!heap.isEmpty()) {
            result.add(heap.pop());
        }
        // ...así que invertimos para dejar de mayor a menor
        Collections.reverse(result);
        return result;
    }

    // ------------------------------------------------------------
    // Métodos privados de apoyo
    // ------------------------------------------------------------

    /** Sigue el camino de letras. Devuelve el nodo final o null si no existe. */
    private TrieNode findNode(String key) {
        TrieNode node = root;
        for (char ch : key.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    /**
     * Recorrido en profundidad (DFS) desde un nodo.
     * Cada producto encontrado pasa por el heap, que solo conserva los k mejores.
     */
    private void collect(TrieNode node, MinHeap<Product> heap, int k) {
        if (node.isEndOfWord) {
            Product p = node.product;
            if (heap.size() < k) {
                heap.push(p);                                   // aún hay espacio
            } else if (p.popularity() > heap.peek().popularity()) {
                heap.pop();                                     // sale el menos popular
                heap.push(p);                                   // entra el nuevo
            }
            // si no es más popular que la raíz, se descarta
        }
        for (TrieNode child : node.children.values()) {
            collect(child, heap, k);
        }
    }

    private String normalize(String text) {
        return text.trim().toLowerCase();
    }
}