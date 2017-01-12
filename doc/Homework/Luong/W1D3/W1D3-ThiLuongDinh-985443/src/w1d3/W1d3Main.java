package w1d3;

public class W1d3Main {

	public static void main(String[] args) {
		String inputMapper0File = "E://workspace/CS522-W1D3/file/mapper0input.txt";
		String inputMapper1File = "E://workspace/CS522-W1D3/file/mapper1input.txt";
		String inputMapper2File = "E://workspace/CS522-W1D3/file/mapper2input.txt";
		
		//Create a WordCount with 3 Mappers and 4 Producers
		WordCount wordCount = new WordCount(3, 4);
		
		//Read the input files and create the input for the Mappers
		wordCount.createInputForMapper(inputMapper0File, 0);
		wordCount.createInputForMapper(inputMapper1File, 1);
		wordCount.createInputForMapper(inputMapper2File, 2);
		//Print the Mapper Output
		wordCount.printMapperOuput();
		
		wordCount.createReducerInput();
		
		wordCount.printInput();
		
		wordCount.printOutput();
	}

}
