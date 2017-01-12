package exercise;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

	private static String PATTERN = "[A-Za-z-\"']+";
	List<Pair<String, Integer>> pairs;

	public Mapper(List<Pair<String, Integer>> p) {
		this.pairs = p;
	}

	public List<Pair<String, Integer>> map() {
//
//		return pairs.stream().filter(p -> isAValidWord(p.getKey())).sorted(new PairComparator())
//				.collect(Collectors.toList());
		
		return pairs.stream().filter(p -> isAValidWord(p.getKey()))
				.collect(Collectors.toList());
	}

	private boolean isAValidWord(String token) {

		if (token == null || token.length() == 0)
			return false;

		return token.matches(PATTERN);

	}

}
