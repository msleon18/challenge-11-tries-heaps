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
## Menú interactivo
```
javac -d bin src/*.java
java -cp bin App
```

## Pruebas (JUnit)
Las pruebas están en la carpeta `test/` (28 pruebas: MinHeap, Trie y ProductLoader).

1. Descargar JUnit en `lib/`:
```
mkdir lib
Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar" -OutFile "lib/junit-platform-console-standalone-1.11.4.jar"
```
2. Compilar y ejecutar:
```
javac -d bin -cp "lib/junit-platform-console-standalone-1.11.4.jar" src/*.java test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute --class-path bin --scan-class-path
```