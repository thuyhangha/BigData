package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Reducer {

	List<Pair<String, Integer>> list;
	List<GroupByPair<String, Integer>> groups;
	
	public Reducer(){
		list = new ArrayList<Pair<String, Integer>>();
	}
	
	public List<GroupByPair<String, Integer>> reduce() {
		Map<String, ArrayList<Integer>> maps = new HashMap<String, ArrayList<Integer>>();
		groups = new ArrayList<GroupByPair<String, Integer>>();

		for (Pair<String, Integer> pair : list) {

			if (maps.get(pair.getKey()) == null) {
				maps.put(pair.getKey(), new ArrayList<Integer>());
			}

			maps.get(pair.getKey()).add(pair.getValue());

		}

		Set<String> keys = maps.keySet();
		for (String key : keys) {
			GroupByPair<String, Integer> groupPair = new GroupByPair<String, Integer>(key, maps.get(key));
			groups.add(groupPair);
		}

		return groups.stream().sorted(new PairGroupComparator()).collect(Collectors.toList());
	}

	public List<Pair<String, Integer>> sum() {

		List<Pair<String, Integer>> rets = new ArrayList<Pair<String, Integer>>();
		for (GroupByPair<String, Integer> groupPair : groups) {
			List<Integer> values = groupPair.getValues();
			Pair<String, Integer> pair = new Pair<String, Integer>(groupPair.getKey(),
					values.stream().mapToInt(i -> i.intValue()).sum());
			rets.add(pair);
		}

		return rets.stream().sorted(new PairComparator()).collect(Collectors.toList());

	}

	public List<Pair<String, Integer>> getList() {
		return list;
	}
	
	

}
