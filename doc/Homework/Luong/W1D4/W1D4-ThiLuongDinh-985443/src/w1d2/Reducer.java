package w1d2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import w1d1.Pair;

public class Reducer {

	List<GroupByPair> groupByPairList = new ArrayList<>();

	public void createReduceInput(List<Pair<String, Integer>> pairList) {

		HashMap<String, List<Integer>> groupByPairMap = new HashMap<>();
		List<Integer> list;

		for (Pair<String, Integer> pair : pairList) {
			if (groupByPairMap.containsKey(pair.getKey())) {
				list = groupByPairMap.get(pair.getKey());
				list.add(pair.getValue());
				groupByPairMap.put(pair.getKey(), list);
			} else {
				list = new ArrayList<>();
				list.add(pair.getValue());
				groupByPairMap.put(pair.getKey(), list);
			}

		}

		groupByPairList = createListGroupByPairFromHashMap(groupByPairMap);

		Collections.sort(groupByPairList, GroupByPair.MyComparator);
	}

	private List<GroupByPair> createListGroupByPairFromHashMap(HashMap<String, List<Integer>> map) {
		List<GroupByPair> groupByPairList = new ArrayList<>();

		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			GroupByPair p = new GroupByPair();
			p.setKey(mentry.getKey().toString());
			p.setValues((List<Integer>) mentry.getValue());
			groupByPairList.add(p);
		}

		return groupByPairList;
	}

	public void printInput() {
		for (GroupByPair groupByPair : groupByPairList) {
			System.out.println("< " + groupByPair.getKey() + " , " + groupByPair.getValues().toString()
					+ " >");
		}
	}

	public void reduce() {
		for (GroupByPair groupByPair : groupByPairList) {
			int sum = 0;
			for (Integer count : groupByPair.getValues()) {
				sum += count;
			}
			System.out.println("< " + groupByPair.getKey() + " , " + sum + " >");

		}
	}

}
