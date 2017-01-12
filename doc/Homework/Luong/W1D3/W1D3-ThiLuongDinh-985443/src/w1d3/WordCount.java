package w1d3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import w1d1.Mapper;
import w1d1.Pair;
import w1d2.Reducer;

public class WordCount {

	List<Mapper> mapperList = new ArrayList<Mapper>();

	List<Reducer> reducerList = new ArrayList<Reducer>();

	int m; // number of Mapper

	int r; // number of Reducer

	public WordCount(int numberOfMapper, int numberOfReducer) {
		this.m = numberOfMapper;
		this.r = numberOfReducer;
		for (int i = 0; i < numberOfMapper; i++) {
			Mapper mapper = new Mapper();
			mapperList.add(mapper);
		}

		/*
		 * for (int j = 0; j < numberOfReducer; j++) { Reducer reducer =
		 * new Reducer(); reducerList.add(reducer); }
		 */
	}

	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}

	public void createInputForMapper(String fileName, int mapperNum) {
		Mapper mapper = new Mapper();
		mapper.createPairList(fileName);
		mapperList.set(mapperNum, mapper);
	}

	public void printMapperOuput() {
		for (Mapper mapper : mapperList) {
			System.out.println("Mapper " + mapperList.indexOf(mapper) + " Output");
			mapper.printOutput();
		}
	}

	public void createReducerInput() {
		int reducerOrd;
		List<List<Pair<String, Integer>>> pairListForReducer = new ArrayList<>();
		List<List<Pair<String, Integer>>> pairListForReducertoPrint = new ArrayList<>();
		List<Pair<String, Integer>> pairList;
		for (int i = 0; i < r; i++) {
			List<Pair<String, Integer>> list = new ArrayList<>();
			pairListForReducer.add(list);
			pairListForReducertoPrint.add(list);
		}

		for (Mapper mapper : mapperList) {
			// clear the pair list for print
			for (int j = 0; j < pairListForReducertoPrint.size(); j++)
				pairListForReducertoPrint.set(j, new ArrayList<>());

			System.out.print("Pair send from Mapper " + mapperList.indexOf(mapper));
			for (Pair pair : mapper.getPairList()) {
				reducerOrd = getPartition((String) pair.getKey());
				pairListForReducer.get(reducerOrd).add(pair);
				pairListForReducertoPrint.get(reducerOrd).add(pair);

				Collections.sort(pairListForReducer.get(reducerOrd), Pair.MyComparator);
				Collections.sort(pairListForReducertoPrint.get(reducerOrd), Pair.MyComparator);
			}

			for (List<Pair<String, Integer>> list : pairListForReducertoPrint) {
				System.out.println(" Reducer " + pairListForReducertoPrint.indexOf(list));
				for (Pair pair : list) {
					System.out.println(pair.toString());
				}

			}

		}

		// set input to Reducer list
		for (List<Pair<String, Integer>> list : pairListForReducer) {
			Collections.sort(list, Pair.MyComparator);
			Reducer reducer = new Reducer();
			reducer.createReduceInput(list);
			reducerList.add(reducer);
		}

	}

	// Print Producer Input
	public void printInput() {
		for (Reducer reducer : reducerList) {
			System.out.println("Reducer " + reducerList.indexOf(reducer) + " input");
			reducer.printInput();
		}

	}

	// Print Producer Input
	public void printOutput() {
		// Print Producer Output
		for (Reducer reducer : reducerList) {
			System.out.println("Reducer " + reducerList.indexOf(reducer) + " output");
			reducer.reduce();
		}
	}
}
