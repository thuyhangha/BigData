package exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {
	private static String PATTERN = "[A-Za-z-\"']+";

	public static List<Pair<String, Integer>> getPairs(String fileContent) {
		StringTokenizer st = new StringTokenizer(fileContent);
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>();

		while (st.hasMoreTokens()) {
			String key = st.nextToken();

			if (key.indexOf("\"") >= 0) {
				key = key.replaceAll("\"", "");
			}

			if (key.indexOf("'") >= 0) {
				key = key.replaceAll("'", "");
			}

			if (key.indexOf(".") > 0) {
				key = key.substring(0, key.length() - 1);
			}

			if (key.indexOf(",") > 0) {
				key = key.substring(0, key.length() - 1);
			}

			if (key.indexOf("-") > 0) {
				String tempKey = key;
				String key1 = key.substring(0, key.indexOf("-"));
				String key2 = tempKey.substring(key.indexOf("-") + 1, tempKey.length());
				if (isAValidWord(key1)) {
					Pair<String, Integer> pair1 = new Pair<String, Integer>(key1, 1);
					pairs.add(pair1);

				}

				if (isAValidWord(key)) {
					Pair<String, Integer> pair2 = new Pair<String, Integer>(key2, 1);
					pairs.add(pair2);
				}

			} else {
				if (isAValidWord(key)) {
					Pair<String, Integer> pair = new Pair<String, Integer>(key, 1);
					pairs.add(pair);
				}

			}

		}

		return pairs;

	}

	private static boolean isAValidWord(String token) {

		if (token == null || token.length() == 0)
			return false;

		return token.matches(PATTERN);

	}
}
