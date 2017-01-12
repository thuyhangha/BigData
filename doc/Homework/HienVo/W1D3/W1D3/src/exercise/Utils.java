package exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {
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
				Pair<String, Integer> pair1 = new Pair<String, Integer>(key1, 1);
				Pair<String, Integer> pair2 = new Pair<String, Integer>(key2, 1);
				pairs.add(pair1);
				pairs.add(pair2);
			} else {
				Pair<String, Integer> pair = new Pair<String, Integer>(key, 1);
				pairs.add(pair);
			}

		}

		return pairs;

	}
}
