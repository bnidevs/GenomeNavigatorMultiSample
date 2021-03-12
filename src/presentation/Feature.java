package presentation;

public class Feature {
	
	int sample;
	String sampleName;
	String type;
	int start;
	int end;
	double startPValue;
	double endPValue;
	
	public String toString(){
		return("sample " + sample + " sampleName " + sampleName + " type " + type + " start = " + start + " end " + end 
				+ " startPValue = " + startPValue + " endPValue " + endPValue);
	}

}
