package w1d2.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import w1d1.object.Pair;
import w1d2.object.GroupByPair;

public class Reducer {

	public List<GroupByPair> createReduceInput(List<Pair<String, Integer>> pairList) {
		List<GroupByPair> result = new ArrayList<>();
		Map<String, Long> counted = pairList.stream().map(pair -> pair.getKey())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		for (Map.Entry<String, Long> entry : counted.entrySet()) {
			GroupByPair g = new GroupByPair();
			g.setKey(entry.getKey());
			for(int i=0;i<entry.getValue();i++){
				g.addValue(1);
			}
			result.add(g);
		}
		return result;
	}
	
	public void reduce(List<GroupByPair>list ){
		for (GroupByPair groupByPair : list) {
			System.out.println("< "+groupByPair.getKey()+" , " + groupByPair.getValues().size()+" >");
			
		}
	}

}
