package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mapper {

	private Map<String, Integer> map;

	public Mapper() {
		map = new HashMap<String, Integer>();
	}

	public List<Pair<String, Integer>> map(String doc) {
		List<Pair<String, Integer>> pairs = Utils.getPairs(doc);
		for (Pair<String, Integer> pair : pairs) {
			String key = pair.getKey();
			if (map.get(key) == null) {
				map.put(key, pair.getValue());
			} else {
				Integer value = map.get(key);
				map.put(key, value.intValue() + pair.getValue().intValue());
			}

		}

		return emit();
	}

	public List<Pair<String, Integer>> emit() {
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>();

		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Pair<String, Integer> pair = new Pair<String, Integer>(key, map.get(key));
			pairs.add(pair);
		}

		return pairs;
	}

}
