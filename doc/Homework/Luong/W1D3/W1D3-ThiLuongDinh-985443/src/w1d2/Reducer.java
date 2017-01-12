package w1d2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import w1d1.Pair;

public class Reducer {

	List<GroupByPair> groupByPairList = new ArrayList<>();
	
	public void createReduceInput(List<Pair<String, Integer>> pairList) {
		
		Map<String, Long> counted = pairList.stream().map(pair -> pair.getKey())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		for (Map.Entry<String, Long> entry : counted.entrySet()) {
			GroupByPair g = new GroupByPair();
			g.setKey(entry.getKey());
			for(int i=0;i<entry.getValue();i++){
				g.addValue(1);
			}
			groupByPairList.add(g);
		}
		
		Collections.sort(groupByPairList,GroupByPair.MyComparator);
	}
	
	public void printInput(){
		for (GroupByPair groupByPair : groupByPairList) {
			System.out.println("< " + groupByPair.getKey() + " , " + groupByPair.getValues().toString() + " >");
		}
	}
	
	public void reduce( ){
		for (GroupByPair groupByPair : groupByPairList) {
			System.out.println("< "+groupByPair.getKey()+" , " + groupByPair.getValues().size()+" >");
			
		}
	}

}
