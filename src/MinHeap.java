import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Min Heap genérico implementado sobre una lista (arreglo dinámico).
 *
 * Fórmulas (índices desde 0):
 *   hijo izquierdo de i -> 2*i + 1
 *   hijo derecho de i   -> 2*i + 2
 *   padre de i          -> (i - 1) / 2
 */
public class MinHeap<T> {

    private final List<T> data = new ArrayList<>();
    private final Comparator<T> comparator;

    public MinHeap(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // ---------- Métodos auxiliares (diapositiva 20) ----------

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /** Devuelve una copia del contenido del heap como lista. */
    public List<T> toArray() {
        return new ArrayList<>(data);
    }

    /** Intercambia dos elementos de posición. */
    private void swap(int i, int j) {
        T temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    // ---------- Métodos principales (diapositivas 13 a 16) ----------

    /** peek: mira el mínimo sin sacarlo. Costo O(1). */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("El heap está vacío");
        }
        return data.get(0);
    }

    /** push: inserta al final y lo sube hasta su lugar. Costo O(log n). */
    public void push(T item) {
        data.add(item);                 // 1. se agrega al final
        percolateUp(data.size() - 1);   // 2. se sube si es menor que su padre
    }

    /** pop: saca el mínimo (la raíz). Costo O(log n). */
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("El heap está vacío");
        }
        T min = data.get(0);                         // 1. guardamos la raíz
        T last = data.remove(data.size() - 1);       // 2. quitamos el último
        if (!isEmpty()) {
            data.set(0, last);                       // 3. el último pasa a la raíz
            percolateDown(0);                        // 4. lo bajamos a su lugar
        }
        return min;
    }

    /** heapify: convierte una lista cualquiera en un heap. Costo O(n). */
    public void heapify(List<T> items) {
        data.clear();
        data.addAll(items);
        // Desde el último nodo con hijos hasta la raíz
        for (int i = data.size() / 2 - 1; i >= 0; i--) {
            percolateDown(i);
        }
    }

    // ---------- Reparación del heap (diapositivas 17 a 19) ----------

    /** percolateUp: sube el elemento mientras sea menor que su padre. */
    private void percolateUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (comparator.compare(data.get(i), data.get(parent)) < 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;  // ya está en su lugar
            }
        }
    }

    /** percolateDown: baja el elemento mientras algún hijo sea menor. */
    private void percolateDown(int i) {
        int n = data.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < n && comparator.compare(data.get(left), data.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < n && comparator.compare(data.get(right), data.get(smallest)) < 0) {
                smallest = right;
            }

            if (smallest == i) {
                break;  // ya cumple la propiedad del heap
            }
            swap(i, smallest);
            i = smallest;
        }
    }
}