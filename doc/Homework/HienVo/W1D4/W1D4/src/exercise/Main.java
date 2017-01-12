package exercise;

public class Main {
	
	
	public static void main(String[] args) {
		
		String[] inputSplits = { "\"cat bat\" mat-pat mum.edu sat. \nfat 'rat eat cat' mum_cs mat",
				"\"bat-hat mat pat \"oat \nhat rat mum_cs eat oat-pat\"", "zat lat-cat pat jat. \nhat rat. kat sat wat" };

		InMapperWordCount wc = new InMapperWordCount(inputSplits);
		wc.mapperOutput();
		wc.sendToReducer();
		wc.reducerInput();
		wc.reducerOutput();

	}

}
