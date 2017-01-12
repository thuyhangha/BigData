package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount {

	String[] inputSplits = { "\"cat bat\" mat-pat mum.edu sat. \nfat 'rat eat cat' mum_cs mat",
			"\"bat-hat mat pat \"oat \nhat rat mum_cs eat oat-pat\"", "zat lat-cat pat jat. \nhat rat. kat sat wat" };
	private static int r = 4;
	List<Mapper> mappers;

	List<Reducer> reducers;

	public WordCount() {
		initialize();
	}

	public void initialize() {
		mappers = new ArrayList<Mapper>();
		reducers = new ArrayList<Reducer>();
		int m = inputSplits.length;
		for (int i = 0; i < m; i++) {
			Mapper mapper = new Mapper(Utils.getPairs(inputSplits[i]));
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
			List<Pair<String, Integer>> pairs = mapper.map();
			System.out.println("Mapper " + n + " output");
			pairs.stream().forEach(System.out::println);
			n++;
		}
	}

	public void sendToReducer() {

		Map<Integer, ArrayList<Pair<String, Integer>>> mapperList;

		for (int i = 0; i < mappers.size(); i++) {

			Mapper mapper = mappers.get(i);
			List<Pair<String, Integer>> pairs = mapper.map();

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
					list.stream().forEach(System.out::println);
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
