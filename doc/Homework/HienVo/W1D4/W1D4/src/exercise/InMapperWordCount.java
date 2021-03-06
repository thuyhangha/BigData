package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMapperWordCount {
	String[] inputSplits;
	private static int r = 4;
	private List<Mapper> mappers;
	private List<Reducer> reducers;

	public InMapperWordCount(){}
	
	public InMapperWordCount(String[] inputSplits) {
		this.inputSplits = inputSplits;
		initialize();
	}

	public void initialize() {
		mappers = new ArrayList<Mapper>();
		reducers = new ArrayList<Reducer>();
		int m = inputSplits.length;
		for (int i = 0; i < m; i++) {
			Mapper mapper = new Mapper();
			mappers.add(mapper);
		}

		for (int j = 0; j < r; j++) {
			Reducer reducer = new Reducer();
			reducers.add(reducer);
		}
	}

	public void mapperOutput() {
		int n = 0;
		for (Mapper mapper : mappers) {
			List<Pair<String, Integer>> pairs = mapper.map(inputSplits[n]);
			System.out.println("Mapper " + n + " output");
			pairs.stream().forEach(System.out::println);
			n++;
		}
	}

	public void sendToReducer() {

		Map<Integer, ArrayList<Pair<String, Integer>>> mapperList;

		for (int i = 0; i < mappers.size(); i++) {

			Mapper mapper = mappers.get(i);
			List<Pair<String, Integer>> pairs = mapper.emit();

			mapperList = new HashMap<Integer, ArrayList<Pair<String, Integer>>>();
			for (Pair<String, Integer> pair : pairs) {
				int partition = getPartition(pair.getKey());

				reducers.get(partition).getList().add(pair);

				if (mapperList.get(partition) == null) {
					mapperList.put(partition, new ArrayList<Pair<String, Integer>>());

				}
				mapperList.get(partition).add(pair);
			}

			for (int j = 0; j < r; ++j) {
				System.out.println("Pairs send from Mapper " + i + " Reducer " + j);
				List<Pair<String, Integer>> list = mapperList.get(j);
				if (list != null) {
					list.stream().sorted(new PairComparator()).forEach(System.out::println);
				}

			}
		}
	}

	public void reducerInput() {
		int n = 0;
		for (Reducer r : reducers) {
			List<GroupByPair<String, Integer>> groupPairs = r.reduce();
			System.out.println(" Reducer " + n + " input ");
			groupPairs.stream().forEach(System.out::println);
			n++;
		}
	}

	public void reducerOutput() {
		int n = 0;
		for (Reducer r : reducers) {
			List<Pair<String, Integer>> groupPairs = r.sum();
			System.out.println("Reducer " + n + " output ");
			groupPairs.stream().forEach(System.out::println);
			n++;
		}

	}

	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
}
