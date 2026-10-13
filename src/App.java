import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Menú interactivo del buscador inteligente.
 * Usa el Trie (búsqueda por prefijo) y el MinHeap (Top K) ya construidos.
 */
public class App {

    private static final String CSV_PATH = "data/products.csv";

    public static void main(String[] args) {
        Trie trie = new Trie();

        // 1. Cargar el catálogo desde el CSV
        try {
            int total = ProductLoader.loadInto(trie, CSV_PATH);
            System.out.println("Catálogo cargado: " + total + " productos.");
        } catch (IOException e) {
            System.out.println("No se pudo leer " + CSV_PATH + ": " + e.getMessage());
            System.out.println("Continúas con un catálogo vacío.");
        }

        // 2. Bucle del menú
        Scanner scanner = new Scanner(System.in);
        int k = 5;                      // cuántas sugerencias mostrar
        boolean running = true;

        while (running) {
            printMenu(k);

            if (!scanner.hasNextLine()) {   // entrada cerrada (Ctrl+Z / Ctrl+D)
                break;
            }
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> searchPrefix(scanner, trie, k);
                case "2" -> k = askK(scanner, k);
                case "3" -> addProduct(scanner, trie);
                case "4" -> exactSearch(scanner, trie);
                case "5" -> typingDemo(scanner, trie, k);
                case "0" -> running = false;
                default -> System.out.println("Opción no válida. Elige un número del menú.");
            }
        }

        System.out.println("¡Hasta pronto!");
        scanner.close();
    }

    // ------------------------------------------------------------
    // Menú
    // ------------------------------------------------------------
    private static void printMenu(int k) {
        System.out.println();
        System.out.println("========== BUSCADOR INTELIGENTE ==========");
        System.out.println("1. Buscar por prefijo (autocompletar)");
        System.out.println("2. Cambiar cantidad de sugerencias (ahora: " + k + ")");
        System.out.println("3. Agregar un producto");
        System.out.println("4. Comprobar si existe un producto exacto");
        System.out.println("5. Simular escritura letra por letra");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    // ------------------------------------------------------------
    // Opción 1: buscar por prefijo
    // ------------------------------------------------------------
    private static void searchPrefix(Scanner scanner, Trie trie, int k) {
        System.out.print("Escribe el prefijo: ");
        String prefix = scanner.nextLine();
        showSuggestions(trie, prefix, k);
    }

    /** Imprime las sugerencias numeradas, de más a menos popular. */
    private static void showSuggestions(Trie trie, String prefix, int k) {
        List<Product> results = trie.searchTopK(prefix, k);

        if (results.isEmpty()) {
            System.out.println("   (sin sugerencias para \"" + prefix.trim() + "\")");
            return;
        }

        int position = 1;
        for (Product p : results) {
            System.out.println("   " + position + ". " + p);
            position++;
        }
    }

    // ------------------------------------------------------------
    // Opción 2: cambiar K
    // ------------------------------------------------------------
    private static int askK(Scanner scanner, int current) {
        System.out.print("Nueva cantidad de sugerencias (1 a 20): ");
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            if (value < 1 || value > 20) {
                System.out.println("Debe estar entre 1 y 20.");
                return current;
            }
            System.out.println("Ahora se muestran " + value + " sugerencias.");
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Eso no es un número entero.");
            return current;
        }
    }

    // ------------------------------------------------------------
    // Opción 3: agregar producto
    // ------------------------------------------------------------
    private static void addProduct(Scanner scanner, Trie trie) {
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Popularidad (número entero): ");
        try {
            int popularity = Integer.parseInt(scanner.nextLine().trim());
            trie.insert(name, popularity);
            System.out.println("Producto agregado: " + name + " (" + popularity + ")");
        } catch (NumberFormatException e) {
            System.out.println("La popularidad debe ser un número entero.");
        }
    }

    // ------------------------------------------------------------
    // Opción 4: búsqueda exacta
    // ------------------------------------------------------------
    private static void exactSearch(Scanner scanner, Trie trie) {
        System.out.print("Nombre exacto del producto: ");
        String name = scanner.nextLine();
        if (trie.search(name)) {
            System.out.println("Sí existe en el catálogo.");
        } else {
            System.out.println("No existe como producto (puede ser solo un prefijo).");
        }
    }

    // ------------------------------------------------------------
    // Opción 5: simular escritura letra por letra
    // ------------------------------------------------------------
    private static void typingDemo(Scanner scanner, Trie trie, int k) {
        System.out.print("Escribe una palabra para simular (ej: air max): ");
        String text = scanner.nextLine().trim();

        for (int i = 1; i <= text.length(); i++) {
            String typed = text.substring(0, i);
            System.out.println();
            System.out.println("> " + typed + "|");
            showSuggestions(trie, typed, k);
        }
    }
}