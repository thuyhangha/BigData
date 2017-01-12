package w1d1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mapper {

	List<Pair<String, Integer>> pairList = new ArrayList<>();

	private static final String REGEX = "[a-z]*|[a-z]*.$";

	private String[] readFileAndSeparate(String fileName) {

		try {
			return new String(Files.readAllBytes(Paths.get(fileName))).toLowerCase().replace("\"", "")
					.replace("\'", "").split(" |-|[\\r\\n]+");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void createPairList(String fileName) {
		String[] wordArray = readFileAndSeparate(fileName);
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();

		for (String string : wordArray) {
			if (string.matches(REGEX) && !string.equals("")) {
				string = string.replace(".", "");
				string = string.replace(",", "");

				if (wordMap.containsKey(string))
					wordMap.put(string, wordMap.get(string) + 1);
				else
					wordMap.put(string, 1);
			}
		}

		pairList = createListPairFromHashMap(wordMap);
		// Collections.sort(pairList, Pair.MyComparator);
	}

	private List<Pair<String, Integer>> createListPairFromHashMap(HashMap<String, Integer> map) {
		List<Pair<String, Integer>> pairList = new ArrayList<>();

		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			Pair<String, Integer> p = new Pair<String, Integer>();
			p.setKey(mentry.getKey().toString());
			p.setValue(Integer.valueOf(mentry.getValue().toString()));
			pairList.add(p);
		}

		return pairList;
	}

	public void printOutput() {
		for (Pair pair : pairList) {
			System.out.println(pair.toString());
		}
	}

	public List<Pair<String, Integer>> getPairList() {
		return pairList;
	}

}
