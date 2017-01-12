package exercise1;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

	private static String PATTERN = "[A-Za-z-\"]+";

	public static List<Pair<String, Integer>> map(List<Pair<String, Integer>> pairs) {

		return pairs.stream().filter(p -> isAValidWord(p.getKey())).sorted(new PairComparator())
				.collect(Collectors.toList());
	}

	private static boolean isAValidWord(String token) {

		if (token == null || token.length() == 0)
			return false;

		return token.matches(PATTERN);

	}

}
