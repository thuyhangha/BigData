package w1d1.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import w1d1.object.Pair;

public class Mapper {

	private static final String REGEX = "[a-z]*|[a-z]*.$";

	private String[] readFileAndSeparate(String fileName) {

		try {
			return new String(Files.readAllBytes(Paths.get(fileName))).toLowerCase().replace("\"", "")
					.split(" |-|[\\r\\n]+");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Pair<String, Integer>> createPairList(String fileName) {
		List<Pair<String, Integer>> pairList = new ArrayList<>();
		String[] wordArray = readFileAndSeparate(fileName);

		Pair<String, Integer> p;

		for (String string : wordArray) {
			if (string.matches(REGEX) && !string.equals("")) {
				string = string.replace(".", "");
				string = string.replace(",", "");
				p = new Pair<>();
				p.setKey(string);
				p.setValue(1);
				pairList.add(p);
			}
		}
		Collections.sort(pairList, Pair.MyComparator);
		return pairList;
	}

}
