package w1d1.object;

import java.util.Comparator;

public class  MyComparator  implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Pair p1 = (Pair) o1;
		Pair p2 = (Pair) o2;
		
	   return  p1.getKey().compareTo(p2.getKey());

	  }

}
