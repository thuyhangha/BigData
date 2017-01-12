package w1d1.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import w1d1.object.Pair;

public class Main {

	public static void main(String[] args) {
		String inputFile = "e://workspace/CS522-W1D1/file/testDataForW1D1.txt";
		String outputFile = "e://workspace/CS522-W1D1/file/outputDataForW1D1.txt";
		
		

		Mapper mapper = new Mapper();
		List<Pair> list = mapper.createPairList(inputFile);

		System.out.println("Writing to file: " + outputFile);
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
			for (Pair pair : list) {
				writer.write("(" + pair.getKey().toString() + "," + pair.getValue().toString() + ")");
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
