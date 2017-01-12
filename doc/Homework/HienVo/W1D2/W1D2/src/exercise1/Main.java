package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) {

		System.out.println("-------------------Mapper Output----------------------");
		List<Pair<String, Integer>> pairs = getPairs();
		List<Pair<String, Integer>> mapOutputs = Mapper.map(pairs);
		mapOutputs.stream().forEach(System.out::println);

		System.out.println("-------------------Reducer Input----------------------");
		List<GroupByPair<String, Integer>> list = Reducer.reduce(mapOutputs);
		list.stream().forEach(System.out::println);

		System.out.println("-------------------Reducer Output---------------------");
		List<Pair<String, Integer>> sumList = Reducer.sum(list);
		sumList.stream().forEach(System.out::println);

	}

	private static List<Pair<String, Integer>> getPairs() {
		String fileContent = readTextFile();
		StringTokenizer st = new StringTokenizer(fileContent);
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>();

		while (st.hasMoreTokens()) {
			String key = st.nextToken();

			if (key.indexOf("\"") >= 0) {
				key = key.replaceAll("\"", "");
			}

			if (key.indexOf(".") > 0) {
				key = key.substring(0, key.length() - 1);
			}

			if (key.indexOf(",") > 0) {
				key = key.substring(0, key.length() - 1);
			}

			if (key.indexOf("-") > 0) {
				String tempKey = key;
				String key1 = key.substring(0, key.indexOf("-"));
				String key2 = tempKey.substring(key.indexOf("-") + 1, tempKey.length());
				Pair<String, Integer> pair1 = new Pair<String, Integer>(key1, 1);
				Pair<String, Integer> pair2 = new Pair<String, Integer>(key2, 1);
				pairs.add(pair1);
				pairs.add(pair2);
			} else {
				Pair<String, Integer> pair = new Pair<String, Integer>(key, 1);
				pairs.add(pair);
			}

		}

		return pairs;

	}

	private static String readTextFile() {
		BufferedReader br = null;
		try {
			String filePath = new File(".").getAbsolutePath();
			br = new BufferedReader(new FileReader(filePath + "/src/exercise1/input123.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		} catch (IOException e) {

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}

		return "";
	}

}
