package presentation;

import java.util.ArrayList;

public class Read {

	int pos;
	// since pos is changed for soft clipping, save original position in case
	// it is needed for other purposes
	int origPos;
	int tlen;
	String rname;
	String sequence;
	String rnext;
	int pnext;
	String id;
	String flag;
	//String binaryFlag;
	String cigar;
	String description;
	String type;
	String processedSeq;
	String replacedSeq;
	boolean paired;
	boolean sameChr;
	boolean mapped;
	boolean mateMapped;
	boolean primaryAlignment;
	boolean fails;
	boolean duplicate;
	boolean suppAlign;
	ArrayList<Integer> insertionPositions;
	// left, right, or mid when more than two and there is a deletion between two pieces
	String side = "";
	
	public String toString(){
		return("pos " + pos + " origPos " + origPos + " tlen " + tlen + " seq length = "+ sequence.length() + " rname " + rname 
				+ " rnext " + rnext + " pnext " + pnext + " flag " + flag + " cigar " + cigar 
				+ " description " + description + " paired " + paired + " sameChr " + sameChr 
				+ " mapped " + mapped + " mateMapped " + mateMapped + " primaryAlignment " + primaryAlignment 
				+ " fails " + fails + " dupl " + duplicate + " suppAlign " + suppAlign + " side " + side);
	}
	
}
