package w1d2.object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import w1d1.object.Pair;

public class GroupByPair {

	private String key;
	
	private List<Integer> values = new ArrayList<>();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}
	
	public void addValue(int value){
		this.values.add(value);
	}
	
	public static final Comparator<GroupByPair> MyComparator = new Comparator<GroupByPair>(){

        @Override
        public int compare(GroupByPair o1, GroupByPair o2) {
            return o1.key.compareTo( o2.key); 
        }
       
    };
	
}
