# Challenge 11 - Smart Search Engine (Trie + Heap)

Buscador de productos por prefijo que devuelve los Top K más populares.

## Estructuras usadas
- **Trie**: almacena los nombres de producto y permite buscar por prefijo.
- **Min Heap** (implementado a mano): mantiene los K productos más populares.

## Cómo funciona `searchTopK(prefix, k)`
1. Se recorre el Trie hasta el nodo del prefijo.
2. Se recorre el subárbol (DFS) buscando productos.
3. Cada producto pasa por un Min Heap de tamaño K.
4. Se devuelven los resultados ordenados de mayor a menor popularidad.

## Complejidad
O(M log K), donde M es la cantidad de productos que empiezan con el prefijo.

## Ejecutar
```
javac -d bin src/*.java
java -cp bin Main
```