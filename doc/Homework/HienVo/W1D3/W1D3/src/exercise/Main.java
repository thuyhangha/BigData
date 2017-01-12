package exercise;

public class Main {

	public static void main(String[] args) {

		WordCount wc = new WordCount();
		wc.mapperOutput();
		wc.sendToReducer();
		wc.reducerInput();
		wc.reducerOutput();
		

	}

}
