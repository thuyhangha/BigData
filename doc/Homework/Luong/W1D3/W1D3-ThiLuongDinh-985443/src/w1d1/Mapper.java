package w1d1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mapper {

	List<Pair> pairList = new ArrayList<>();

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
		// Collections.sort(pairList, Pair.MyComparator);
	}

	public void printOutput() {
		for (Pair pair : pairList) {
			System.out.println(pair.toString());
		}
	}

	public List<Pair> getPairList() {
		return pairList;
	}

}
