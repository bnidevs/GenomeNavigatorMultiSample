package presentation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code, may be used in Genome Navigator in DataTable load
 */
public class GeneralFeatureData {
	
	String chr;
	String featureName;
	int start;
	int end;
	String strand;
	HashMap<String, ArrayList<String>> attributesMap = new HashMap<String, ArrayList<String>>();
	
	public Interval createInterval() {
		Interval interval = null;
		if (start > end) {
			interval = new Interval(end, start);
		} else {
			interval = new Interval(start, end);
		}
		
		return interval;
		
	}
	
	public String toString(){
		return("chr " + chr + " featureName " + featureName + " start " + start + " end = "+ end 
				+ " strand " + strand + " attributesMap " + attributesMap);
	}

}
