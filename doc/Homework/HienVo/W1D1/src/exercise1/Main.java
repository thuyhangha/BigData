package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

	private static String PATTERN = "[A-Za-z-\"]+";

	public static void main(String[] args) {

		String fileContent = readTextFile();
		StringTokenizer st = new StringTokenizer(fileContent);
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>();

		while (st.hasMoreTokens()) {
			String key = st.nextToken();

			if (key.indexOf("\"") >= 0) {
				key = key.replaceAll("\"", "");
			}

			if (key.indexOf(".") > 0) {
				key = key.substring(0, key.length()-1);
			}
			
			if (key.indexOf(",") > 0) {
				key = key.substring(0, key.length()-1);
			}
			
			Pair<String, Integer> pair = new Pair<String, Integer>(key, 1);
			pairs.add(pair);

		}

		pairs.stream().filter(p -> isAValidWord(p.getKey())).sorted(new PairComparator()).forEach(System.out::println);

	}

	private static boolean isAValidWord(String token) {

		if (token == null || token.length() == 0)
			return false;

		return token.matches(PATTERN);

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
