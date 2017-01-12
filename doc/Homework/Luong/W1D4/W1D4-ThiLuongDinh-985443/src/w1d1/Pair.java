package w1d1;

import java.util.Comparator;

public class Pair<T extends Comparable<T>, S> implements Comparator<Pair<T,S>> {

	private T key;

	private S value;

	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public S getValue() {
		return value;
	}

	public void setValue(S value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return  "<" + this.getKey().toString() + " , " + this.getValue().toString() + ">";
	}
	
	@Override
	public int compare(Pair o1, Pair o2) {
		return o1.getKey().compareTo(o2.getKey());
	}

	public static final Comparator<Pair> MyComparator = new Comparator<Pair>(){

        @Override
        public int compare(Pair o1, Pair o2) {
            return o1.key.compareTo( o2.key);  // This will work because age is positive integer
        }
       
    };


}
