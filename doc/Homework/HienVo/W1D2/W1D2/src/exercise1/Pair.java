package exercise1;

public class Pair<K extends Comparable<?>, V extends Comparable<?>> {

	private K key;

	private V value;

	public Pair() {
	}

	public Pair(K k, V v) {
		this.key = k;
		this.value = v;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String toString() {
		return "< " + getKey() + ", " + getValue() + " >";
	}

}
