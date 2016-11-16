package tavonatti.stefano.model;

import java.util.Comparator;

public class ComaparatorMeasureDate implements Comparator<Measure>{

	@Override
	public int compare(Measure o1, Measure o2) {
		if(o1.getCreated().getTime()>o2.getCreated().getTime())
			return -1;
		return 1;
	}

}
