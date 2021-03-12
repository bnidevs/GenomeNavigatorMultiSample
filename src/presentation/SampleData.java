package presentation;

/**
 * @author James Kelley
 * Genome Navigator Data Processor
 */
public class SampleData {
	
	int start;
	int end;
	String startPValue;
	String endPValue;
	String type;
	String chr;
	String insertionStr;
	String vcfLine;
	
	public String toString(){
		return("start " + start + " end " + end + " startPValue " + startPValue + " endPValue " + endPValue
				+ " type " + type + " chr " + chr + " insertionStr " + insertionStr + " vcfLine " + vcfLine);
	}

}
