import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    private MinHeap<Integer> heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap<Integer>(Comparator.naturalOrder());
    }

    /** Ayudante: saca todos los elementos del heap en el orden en que salen. */
    private List<Integer> drain(MinHeap<Integer> h) {
        List<Integer> out = new ArrayList<>();
        while (!h.isEmpty()) {
            out.add(h.pop());
        }
        return out;
    }

    @Test
    @DisplayName("Un heap nuevo está vacío")
    void newHeapIsEmpty() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    @DisplayName("peek devuelve el mínimo y no lo elimina")
    void peekReturnsMinimumWithoutRemoving() {
        heap.push(5);
        heap.push(3);
        heap.push(8);
        heap.push(1);

        assertEquals(1, (int) heap.peek());
        assertEquals(4, heap.size());
    }

    @Test
    @DisplayName("pop entrega los elementos de menor a mayor")
    void popReturnsAscendingOrder() {
        for (int n : new int[]{5, 3, 8, 1, 9, 2}) {
            heap.push(n);
        }
        assertEquals(List.of(1, 2, 3, 5, 8, 9), drain(heap));
    }

    @Test
    @DisplayName("Maneja elementos repetidos")
    void handlesDuplicates() {
        for (int n : new int[]{3, 1, 3, 1}) {
            heap.push(n);
        }
        assertEquals(List.of(1, 1, 3, 3), drain(heap));
    }

    @Test
    @DisplayName("pop y peek en un heap vacío lanzan NoSuchElementException")
    void emptyHeapThrows() {
        assertThrows(NoSuchElementException.class, () -> heap.pop());
        assertThrows(NoSuchElementException.class, () -> heap.peek());
    }

    @Test
    @DisplayName("Intercalar push y pop mantiene el orden correcto")
    void interleavedPushAndPop() {
        heap.push(5);
        heap.push(2);
        heap.push(8);
        assertEquals(2, (int) heap.pop());

        heap.push(1);
        assertEquals(1, (int) heap.pop());
        assertEquals(5, (int) heap.pop());
        assertEquals(8, (int) heap.pop());
        assertTrue(heap.isEmpty());
    }

    @Test
    @DisplayName("heapify convierte una lista desordenada en un heap válido")
    void heapifyBuildsValidHeap() {
        heap.heapify(List.of(9, 4, 7, 1, 8, 2));

        assertEquals(6, heap.size());
        assertEquals(1, (int) heap.peek());
        assertEquals(List.of(1, 2, 4, 7, 8, 9), drain(heap));
    }

    @Test
    @DisplayName("Con 500 números aleatorios, pop siempre sale ordenado")
    void randomNumbersComeOutSorted() {
        Random random = new Random(42);
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            numbers.add(random.nextInt(1000));
        }

        for (int n : numbers) {
            heap.push(n);
        }

        List<Integer> expected = new ArrayList<>(numbers);
        expected.sort(null);   // orden natural
        assertEquals(expected, drain(heap));
    }

    @Test
    @DisplayName("Con otro comparador funciona como max heap")
    void customComparatorWorksAsMaxHeap() {
        MinHeap<Integer> maxHeap = new MinHeap<Integer>(Comparator.reverseOrder());
        for (int n : new int[]{4, 9, 1, 7}) {
            maxHeap.push(n);
        }
        assertEquals(List.of(9, 7, 4, 1), drain(maxHeap));
    }

    @Test
    @DisplayName("toArray devuelve una copia: modificarla no daña el heap")
    void toArrayReturnsACopy() {
        heap.push(2);
        heap.push(1);

        List<Integer> copy = heap.toArray();
        copy.clear();

        assertEquals(2, heap.size());
        assertEquals(1, (int) heap.peek());
    }
}