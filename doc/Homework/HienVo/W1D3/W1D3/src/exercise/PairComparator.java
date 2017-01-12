package exercise;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<String, Integer>> {

	@Override
	public int compare(Pair<String, Integer> p1, Pair<String, Integer> p2) {

		return p1.getKey().compareToIgnoreCase(p2.getKey());
	}

}
