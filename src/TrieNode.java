import java.util.HashMap;
import java.util.Map;

/**
 * Nodo del Trie.
 * - children: mapa letra -> siguiente nodo.
 * - isEndOfWord: true si aquí termina un producto guardado.
 * - product: el producto completo (solo si isEndOfWord es true).
 */
public class TrieNode {

    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
    Product product = null;
}