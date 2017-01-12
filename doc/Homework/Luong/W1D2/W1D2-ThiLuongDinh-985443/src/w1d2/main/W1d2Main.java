package w1d2.main;

import java.util.Collections;
import java.util.List;

import w1d1.main.Mapper;
import w1d1.object.Pair;
import w1d2.object.GroupByPair;

public class W1d2Main {
	public static void main(String[] args) {
		String inputFile = "e://workspace/CS522-W1D2/file/testDataForW1D1.txt";

		Mapper mapper = new Mapper();
		List<Pair<String, Integer>> list = mapper.createPairList(inputFile);

		Reducer reducer = new Reducer();
		List<GroupByPair> groupList = reducer.createReduceInput(list);
		
		System.out.println("Reducer Input: ");
		
		Collections.sort(groupList, GroupByPair.MyComparator);
		for (GroupByPair groupByPair : groupList) {
			System.out.println("< " + groupByPair.getKey() + " , " + groupByPair.getValues().toString() + " >");
		}
		
		System.out.println("Reducer Output: ");
		reducer.reduce(groupList);
		
	}
}
