/**
 * Representa un producto del catálogo.
 * Un record genera automáticamente el constructor, los getters
 * (name(), popularity()), equals() y hashCode().
 */
public record Product(String name, int popularity) {

    @Override
    public String toString() {
        return name + " (" + popularity + ")";
    }
}