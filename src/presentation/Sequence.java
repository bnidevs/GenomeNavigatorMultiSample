package presentation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

public class Sequence extends Stripe
{
	boolean dna = true;
	boolean protein = true;
	boolean plus = true;
	private int seqlen = 0;
	private double sequnit = 1.;
	
	private ArrayList<Integer> positionList = new ArrayList<Integer>();
	public int LEFTEND = 0;
	public int RIGHTEND = 0;
	
	boolean drawMismatches = false;
	boolean plotPresent = true;
	boolean exonPresent = true;
	int fixedHeight = 0;
	
	Sequence()
	{
		objHeight = 15;
		objPad = 3;

	}

	Sequence(int pieces, String name, String type)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		this.name = name;
		this.type = type;
	}

	private void convert()
	{
		seqlen = piece[0].name.length();
		sequnit = (1. * piece[0].to - piece[0].from + 1) / seqlen;
	}
	
	private void convert1(aWindow win)
	{
		seqlen = win.getX(win.width) - win.getX(0);
		sequnit = (1. * win.getX(win.width) - win.getX(0) + 1) / seqlen;
	}
	
	private void createPositionList(aWindow win, double seqstep) {
		positionList = new ArrayList<Integer>();
		int count = 0;
		int prev = -1;
		boolean zeroDiff = false;
		for (int i = win.getX(0); i <= win.getX(win.width) + 2; i++) {
			int from = (int) (win.scale * i) - win.offX;
			int x = (int) (seqstep * count) + from;
			positionList.add(x);
			// fixes bug where at small sequence steps some rectangles have a width of 0
			// and some mismatches are drawn but others are not
			if (prev > -1) {
				if ((x - prev) < 1) {
					zeroDiff = true;
				} 
			}
			prev = x;
		}
		if (zeroDiff) {
			drawMismatches = false;
		} else {
			drawMismatches = true;
		}
	}
	
	private void renamePairedSequences() {
		for (int i = 0; i < numObj; i++)
		{
			if (piece[i].type.startsWith("dna-pair")) {
				if (piece[i].name.contains(Constants.SEQUENCE_PLACEHOLDER)) {
					String[] sequences = piece[i].name.split(Constants.SEQUENCE_PLACEHOLDER);
					String spacesString = piece[i].type.substring(8);
					String[] spaces = spacesString.split("-");
					String seq = sequences[0];
					for (int j = 0; j < spaces.length; j++) {
						if (!spaces[j].startsWith("i")) {
							// from https://stackoverflow.com/questions/2804827/create-a-string-with-n-characters
							char[] charArray = new char[Integer.parseInt(spaces[j])];
							Arrays.fill(charArray, '&');
							String str = new String(charArray);
							if (sequences.length >= (j + 1)) {
								seq += str + sequences[j + 1];
							}
						}
					}
					piece[i].name = seq;
				}
			} else if (piece[i].type.endsWith("pl_off")) {
				piece[i].from -= Constants.OFFSCREEN_MATE_INDICATOR_LENGTH;
				char[] charArray = new char[Constants.OFFSCREEN_MATE_INDICATOR_LENGTH];
				Arrays.fill(charArray, '&');
				String str = new String(charArray);
				piece[i].name = str + piece[i].name;
			}
			if (piece[i].type.endsWith("pr_off")) {
				piece[i].to += Constants.OFFSCREEN_MATE_INDICATOR_LENGTH;
				char[] charArray = new char[Constants.OFFSCREEN_MATE_INDICATOR_LENGTH];
				Arrays.fill(charArray, '&');
				String str = new String(charArray);
				piece[i].name += str;
			} 
		}
	}

	public int arrangeObjects(int curY)
	{
		renamePairedSequences();
		
		convert();
		
		int padding = (int) (Constants.PADDING_PERCENT*(RIGHTEND - LEFTEND));
		
		int nextRowIndex, moreObj = numObj, curObj = 0;

		setTop(curY);
		curY = objPad;
		piece[0].y = curY;
		piece[0].placed = true;
		moreObj--;
		
		while (moreObj > 0)
		{
			nextRowIndex = -1;
			for (int i = curObj; i < numObj; i++)
			{
				if (piece[i].placed)
					continue;
				
				int from = piece[i].from - padding;
				int to = piece[curObj].to + padding;
				
				if (piece[i].type.endsWith("pl_off")) {
					from = piece[i].from - Constants.OFFSCREEN_MATE_PADDING_LENGTH;
				}
				if (piece[curObj].type.endsWith("pr_off")) {
					to = piece[curObj].to + Constants.OFFSCREEN_MATE_PADDING_LENGTH;
				}
				
				if (from > to)
				//if (piece[i].from > piece[curObj].to)
				{
					piece[i].y = curY;
					piece[i].placed = true;
					curObj = i;
					moreObj--;
				}

				else if (nextRowIndex == -1)
					nextRowIndex = i;
			}

			if (nextRowIndex > -1)
			{
				curObj = nextRowIndex;
				curY += objHeight + objPad;
				piece[curObj].y = curY;
				piece[curObj].placed = true;
				moreObj--;
			}

		}

		curY += objHeight + objPad;
		height = curY;
		return curY + top;
	}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		convert1(win);
		int offY = top - win.offY;
	
		boolean seqShow = false;
		int len;
		int charOff = 0;
		font = new Font(Constants.SEQ_FONT, Constants.SEQ_FONT_TYPE, Constants.SEQ_FONT_SIZE);
		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = Constants.SEQ_FH;

		for (int i = numObj - 1; i >= 0; i--)
		//for (int i = 0; i < numObj; i++)
		{
			//boolean seqShow = false;
			if (piece[i].type.startsWith("dna"))	
				dna = true;
			else
				dna = false;

			if (piece[i].type.equals("protein"))
				protein = true;
			else
				protein = false;

			if (piece[i].type.equals("+"))
				plus = true;
			else
				plus = false;
			
			if (piece[i].id.equals(Constants.REF_ID_READS_FILE)) {
				if (plotPresent && exonPresent) {
					offY = fixedHeight;
				} else if (plotPresent) {
					offY = Constants.PLOT_HEIGHT + Constants.EXON_Y_POS_CORRECTION_READS;
				} else if (exonPresent) {
					offY = fixedHeight;
				} else {
					offY = 0;
				}
			} else {
				if (offY > win.height - 1 || offY + height < 1)
					return;
			}
			
			String letters = piece[i].name;
			String seq = letters;
			if (seq.contains(Constants.SEQUENCE_PLACEHOLDER)) {
				seq = letters.replace(Constants.SEQUENCE_PLACEHOLDER, " ");
			}
			if (seq.contains(Constants.N_PLACEHOLDER)) {
				seq = letters.replace(Constants.N_PLACEHOLDER, " ");
			}
			if (seq.contains(Constants.P_PLACEHOLDER)) {
				seq = letters.replace(Constants.P_PLACEHOLDER, " ");
			}
			int from = (int) (win.scale * piece[i].from) - win.offX, to = (int) (win.scale * piece[i].to) - win.offX;
			
			double seqstep = sequnit * win.scale;
			if (to + seqstep < 0 || from >= win.width)
			{
				// System.out.println("ERROR ERROR--------------------------");
				continue;
			}

//			 if (to > win.width)
//			 to = win.width;
			//double seqstep = sequnit * win.scale;
			
			if (seqstep > Constants.SEQ_SHOW_STEP_LIMIT) {
				seqShow = true;
			} else {
				seqShow = false;
			}
			
			if (to == from)
			{
				if (from == 0)
				{
					from =1;
					to = from + (int)seqstep;
					len = (int)seqstep;
				}
				else
				{
					to = from + (int)seqstep;
					len = (int)seqstep;
				}			
			}
			else
				len = to -from+1;
						
			//System.out.println("i=" + i + " from=" + from + " to=" + to + " len=" + len + " win.offX =" + win.offX);
			//System.out.println(seqstep);
			//if (seqstep < 2.)
		    if (seqstep < Constants.SEQSTEP_CUTOFF)
			{
//				System.out.println("111111");
				if (plus)
				{
//					g.setColor(Color.white);
//					g.fillRect(from, piece[i].y + offY, pluspacewid, objHeight);		
//					
//					g.setColor(Color.lightGray);
//					g.fillRect(from + pluspacewid, piece[i].y + offY, to - from - pluspacewid, objHeight);
//					
//					int tlen = fmetrics.stringWidth(piece[i].name);
//
//					if (tlen < len)
//					{
//						g.setColor(Color.black);
//						g.drawString(piece[i].name, from + (len - tlen) / 2, piece[i].y + offY + fh);
//					}
//
//					if (border)
//					{
//						g.setColor(Color.black);
//						g.drawRect(from + pluspacewid, piece[i].y + offY, to - from - pluspacewid-1, objHeight - 1);
//					}
				}
				else
				{
					if (piece[i].type.startsWith("dna-pair") && piece[i].name.contains(Constants.SEQUENCE_PLACEHOLDER)) {
						ArrayList<Integer> readLengths = new ArrayList<Integer>();
						ArrayList<Integer> connectorLengths = new ArrayList<Integer>();
						// split by & does not work
						StringTokenizer st = new StringTokenizer(piece[i].name, Constants.SEQUENCE_PLACEHOLDER);
						while (st.hasMoreTokens()) {
							String seq1 = st.nextToken();
							readLengths.add(seq1.length());
						}
						String[] lengths = piece[i].type.substring(8).split("-");
						for (int p = 0; p < lengths.length; p++) {
							if (!lengths[p].startsWith("i")) {
								connectorLengths.add(Integer.parseInt(lengths[p]));
							}
						}
						drawRectangles(g, win, piece[i].from, readLengths, connectorLengths, piece[i].color, seqstep, piece[i].y, offY, to, piece[i]);
					} else if (piece[i].type.endsWith("pl_off")) {
						ArrayList<Integer> readLengths = new ArrayList<Integer>();
						ArrayList<Integer> connectorLengths = new ArrayList<Integer>();
						readLengths.add(0);
						connectorLengths.add(Constants.OFFSCREEN_MATE_INDICATOR_LENGTH);
						int length = piece[i].name.length() - Constants.OFFSCREEN_MATE_INDICATOR_LENGTH;
						readLengths.add(length);
						drawRectangles(g, win, piece[i].from, readLengths, connectorLengths, piece[i].color, seqstep, piece[i].y, offY, to, piece[i]);
					} else if (piece[i].type.endsWith("pr_off")) {
						ArrayList<Integer> readLengths = new ArrayList<Integer>();
						ArrayList<Integer> connectorLengths = new ArrayList<Integer>();
						int length = piece[i].name.length() - Constants.OFFSCREEN_MATE_INDICATOR_LENGTH;
						readLengths.add(length);
						connectorLengths.add(Constants.OFFSCREEN_MATE_INDICATOR_LENGTH);
						readLengths.add(0);
						drawRectangles(g, win, piece[i].from, readLengths, connectorLengths, piece[i].color, seqstep, piece[i].y, offY, to, piece[i]);
					} else if (piece[i].name.contains(Constants.DELETION_PLACEHOLDER) || 
							piece[i].name.contains(Constants.N_PLACEHOLDER) ||
							piece[i].name.contains(Constants.P_PLACEHOLDER)) {
						String name = piece[i].name;
						Color pieceColor = Color.lightGray;
						if (piece[i].color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING))) {
							pieceColor = UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING);
						} 
						Color color = UtilityMethods.hexColor(Constants.DELETION_CIGAR_COLOR_STRING);
						if (piece[i].name.contains(Constants.DELETION_PLACEHOLDER)) {

						} else if (piece[i].name.contains(Constants.N_PLACEHOLDER)) {
							name = piece[i].name.replace(Constants.N_PLACEHOLDER, " ");
							color = UtilityMethods.hexColor(Constants.N_COLOR_STRING);
						} else if (piece[i].name.contains(Constants.P_PLACEHOLDER)) {
							name = piece[i].name.replace(Constants.P_PLACEHOLDER, " ");
							color = UtilityMethods.hexColor(Constants.P_COLOR_STRING);
						}
						ArrayList<Integer> readLengths = new ArrayList<Integer>();
						ArrayList<Integer> spaceLengths = new ArrayList<Integer>();
						int spaceCount = 1;
						StringBuilder sb = new StringBuilder(name);
						for (int s = 0; s < sb.length() - 1; s++) {
							if (sb.charAt(s) == ' ') {
								if (sb.charAt(s) == sb.charAt(s + 1)) {
									spaceCount += 1;
								} else {
									spaceLengths.add(spaceCount);
									spaceCount = 1;
								}
							}
						}
						String[] splitted = name.split("\\s+");
						for (int s = 0; s < splitted.length; s++) {
							readLengths.add(splitted[s].length());
						}
						drawRectangles(g, win, piece[i].from, readLengths, spaceLengths, color, seqstep, piece[i].y, offY, to, piece[i]);
					} else {
						//System.out.println("222222");
						if (piece[i].color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING))) {
							g.setColor(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING));
//						} else if (piece[i].color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
//							g.setColor(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING));
						} else {
							g.setColor(Color.lightGray);
						}
						g.fillRect(from, piece[i].y + offY, to - from + (int)seqstep, objHeight);
					}
				}
			}

			else
			{
				//System.out.println("333333");
				// System.out.println(" To: "+to + " From: "+from+" len: "+(len)+" len2: "+fmetrics.stringWidth("A") + ":"+
				 //letters.length());
				int ccc = fmetrics.stringWidth("A");
				//System.out.println( ccc + "----" + letters.length() + ":::" + len);
				if (fmetrics.stringWidth("A") * letters.length() <= len)
				{
					charOff = ((int) seqstep - fmetrics.stringWidth("A")) / 2;
				}
				createPositionList(win, seqstep);
				for (int j = 0; j < letters.length(); j++)
				{
					if (plus)
					{
//						switch (letters.charAt(j))
//						{
//						case ' ':
//							g.setColor(Color.white);
//							break;
//						default:
//							g.setColor(Color.magenta);
//							break;
//						}
					}
					else if (dna)
					{
						switch (letters.charAt(j))
						{
						case 'n':
							g.setColor(Constants.SEQ_N_COLOR);
							break;
						case 'N':
							g.setColor(Constants.SEQ_N_COLOR);
							break;
						case 'a':
							g.setColor(Constants.SEQ_A_MISMATCH_COLOR);
							break;
						case 't':
							g.setColor(Constants.SEQ_T_MISMATCH_COLOR);
							break;
						case 'c':
							g.setColor(Constants.SEQ_C_MISMATCH_COLOR);
							break;
						case 'g':
							g.setColor(Constants.SEQ_G_MISMATCH_COLOR);
							break;
						case '+':
							g.setColor(Constants.SEQ_PLUS_COLOR);
							break;
						case '-':
							g.setColor(Constants.SEQ_DASH_COLOR);
							break;
						case Constants.DELETION_PLACEHOLDER_CHAR:
							g.setColor(UtilityMethods.hexColor(Constants.DELETION_CIGAR_COLOR_STRING));
							break;
						case Constants.N_PLACEHOLDER_CHAR:
							g.setColor(UtilityMethods.hexColor(Constants.N_COLOR_STRING));
							break;
						case Constants.P_PLACEHOLDER_CHAR:
							g.setColor(UtilityMethods.hexColor(Constants.P_COLOR_STRING));
							break;	
						case Constants.SEQUENCE_PLACEHOLDER_CHAR:
							g.setColor(piece[i].color);
							break;
						default:
							if (piece[i].color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING))) {
								g.setColor(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING));
							} else {
								g.setColor(Color.lightGray);
							}
							break;
						}
					}

					else if (protein)
					{
//						switch (Character.toUpperCase(letters.charAt(j)))
//						{
//						case 'A': // non-polar aliphatic
//						case 'V':
//						case 'L':
//						case 'I':
//						case 'M':
//							g.setColor(Color.lightGray);
//							break;
//						case 'S': // polar neutral
//						case 'T':
//						case 'N':
//						case 'Q':
//							g.setColor(Color.green);
//							break;
//						case 'D': // polar negative
//						case 'E':
//							g.setColor(Color.yellow);
//							break;
//						case 'H': // polar positive
//						case 'K':
//						case 'R':
//							g.setColor(Color.cyan);
//							break;
//						case 'C': // non-polar aromatic
//							g.setColor(Color.yellow);
//							break;
//						case 'F': // non-polar aromatic
//						case 'Y':
//						case 'W':
//							g.setColor(Color.magenta);
//							break;
//						case 'P': // non-polar aromatic
//						case 'G':
//							g.setColor(Color.pink);
//							break;
//						case 'B': // special char
//						case 'Z':
//						case 'X':
//						case '-':
//							g.setColor(Color.white);
//							break;
//						default:
//							g.setColor(Color.blue);
//							break;
//						}
					}
		
					int xPos1 = piece[i].from + j;
					if (xPos1 >= win.getX(0) && xPos1 <= win.getX(win.width) + 2) {
						int xIndex = xPos1 - win.getX(0);
						if (positionList.size() > xIndex + 1) {
							int startPos = positionList.get(xIndex);
							int endPos = positionList.get(xIndex + 1);
							if (!drawMismatches) {
								g.setColor(Color.lightGray);
							}
							g.fillRect(startPos, piece[i].y + offY, endPos - startPos, objHeight);
							if (UtilityMethods.isDataAvailableForObject(piece[i])) {
								g.setColor(Constants.READ_WITH_PAIR_DATA_BORDER_COLOR);
								g.drawLine(startPos, piece[i].y + offY, endPos, piece[i].y + offY);
								int bottomY = piece[i].y + offY + objHeight - 1;
								g.drawLine(startPos, bottomY, endPos, bottomY);
								if (xPos1 == piece[i].from) {
									g.setColor(Constants.READ_WITH_PAIR_DATA_BORDER_COLOR);
									g.drawLine(startPos - 1, piece[i].y + offY, startPos - 1, bottomY);
								}
								if (xPos1 == piece[i].to) {
									g.setColor(Constants.READ_WITH_PAIR_DATA_BORDER_COLOR);
									int rightEnd = endPos;
									g.drawLine(rightEnd, piece[i].y + offY, rightEnd, bottomY);
								}
							}
						}
					}
					
					if (seqShow)
					{
						//System.out.println("777777");
						// used to check that seqstep is the same in ref and reads
//						if (i == 0 && j == 0) {
//							System.out.println(piece[0].id);
//							System.out.println(seqstep);
//						}
						g.setColor(Color.black);
						// this is what was slowing down display when letters visible,
						// now done once 
//						String seq = letters;
//						if (seq.contains(Constants.SEQUENCE_PLACEHOLDER)) {
//							seq = letters.replace(Constants.SEQUENCE_PLACEHOLDER, " ");
//						}
//						if (seq.contains(Constants.N_PLACEHOLDER)) {
//							seq = letters.replace(Constants.N_PLACEHOLDER, " ");
//						}
//						if (seq.contains(Constants.P_PLACEHOLDER)) {
//							seq = letters.replace(Constants.P_PLACEHOLDER, " ");
//						}
						//System.out.println(seq);
						int xPos = piece[i].from + j;
						if (xPos >= win.getX(0) && xPos <= win.getX(win.width) + 2) {
							int xIndex = xPos - win.getX(0);
							if (positionList.size() > xIndex) {
								int charPos = positionList.get(xIndex);
								//System.out.println(charPos);
								g.drawChars(seq.toCharArray(), j, 1, charPos + charOff + 1, piece[i].y + offY + fh);
							}
						}
						//g.drawChars(seq.toCharArray(), j, 1, (int) (seqstep * j + charOff) + from, piece[i].y + offY + fh);
					}
				}
				if (piece[i].type.contains("-i")) {
					String[] spaces = piece[i].type.split("-");
					for (int k = 0; k < spaces.length; k++) {
						if (spaces[k].startsWith("i")) {
							int index = Integer.parseInt(spaces[k].substring(1));
							int xPos = piece[i].from + index;
							int xIndex = xPos - win.getX(0);
							if (xIndex >= 0 && positionList.size() > xIndex + 1) {
								int insertionPos = positionList.get(xIndex);
								g.setColor(UtilityMethods.hexColor(Constants.INDEL_INSERTION_LINES_COLOR_STRING));
								g.drawLine(insertionPos - 2, piece[i].y + offY - 1, insertionPos + 2, piece[i].y + offY -1);
								g.drawLine(insertionPos, piece[i].y + offY, insertionPos, piece[i].y + offY + objHeight - 1);
							}
						}
					}
				}
			}

			if (selected.equals(piece[i]))
			{
				//System.out.println(piece[i]);
				int corr1 = 3;
				int corr2 = 3;
				g.setColor(Color.red);
				int dataObjCorrLeft = 0;
				int dataObjCorrRight = 0;
				if (UtilityMethods.isDataAvailableForObject(piece[i])) {
					dataObjCorrLeft = 1;
					dataObjCorrRight = 3;
				}
				if (to == from + (int)seqstep)
				{
					// does this ever occur?
					int rectWidth = (int) seqstep + corr1 + dataObjCorrRight;
					g.drawRect(from - (dataObjCorrLeft + 2), piece[i].y + offY - 2, rectWidth, objHeight + 3);
				}
				else
				{
					//if (seqstep < 2.)
					if (seqstep < Constants.SEQSTEP_CUTOFF)
					{
						int rectWidth3 = to + (int) seqstep - from + corr1 + dataObjCorrRight;
						if (piece[i].type.endsWith("pl_off")) {
							rectWidth3 -= 1;
						}
						g.drawRect(from - (dataObjCorrLeft + 2), piece[i].y + offY - 2, rectWidth3, objHeight + 3);
					} else {
						int startIndex = piece[i].from - win.getX(0);
						int endIndex = piece[i].to + 1 - win.getX(0);
						int startPos = 0;
						int endPos = win.width;
						if (startIndex > 0) {
							startPos = positionList.get(startIndex);
						}
						if (positionList.size() > endIndex + 1) {
							endPos = positionList.get(endIndex);
						}
						int rectWidth2 = endPos - startPos + corr2 + dataObjCorrRight;
						if (piece[i].type.endsWith("pl_off")) {
							rectWidth2 -= 1;
						} else if (piece[i].type.endsWith("pr_off")) {
							rectWidth2 -= 1;
						} else if (piece[i].type.endsWith(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
							rectWidth2 -= 1;
						}
						g.drawRect(startPos - (dataObjCorrLeft + 2), piece[i].y + offY - 2, rectWidth2, objHeight + 3);
					}
				}
				// System.out.println("This is the *****No.:" + i +
				// " *****piece");
			}
		}
	}

	public String objName(aWindow win, int x, int y)
	{
		y += win.offY - top;
		x = (int) ((x + win.offX) / win.scale);

		for (int i = 0; i < numObj; i++)
		{
			if (y > piece[i].y && y < piece[i].y + objHeight && x >= piece[i].from && x <= piece[i].to)
				return (piece[i].name + ", SEQ");
		}

		return null;
	}
	
	/**
	 * Draws rectangles for reads which contain deletions or other features
	 * or region joining read pairs at zoom factors where individual rectangles
	 * for each unit position are not drawn
	 * @param pos
	 * @param readLengths
	 * @param featureLengths
	 * @param color
	 */
	private void drawRectangles(Graphics g, aWindow win, int pos, ArrayList<Integer> readLengths, 
			ArrayList<Integer> featureLengths, Color featureColor, double seqstep, 
			int pieceY, int offY, int to, One one) {
		for (int q = 0; q < featureLengths.size(); q++) {
			int lastPos = pos;
			int lastFrom = (int) (win.scale * lastPos) - win.offX;
			pos += readLengths.get(q);
			int posFrom = (int) (win.scale * pos) - win.offX;
			// draw read rectangle
			g.setColor(Color.lightGray);
			g.fillRect(lastFrom, pieceY + offY, posFrom - lastFrom + (int)seqstep, objHeight);
			int lastPos1 = pos;
			int lastFrom1 = (int) (win.scale * lastPos1) - win.offX;
			pos += featureLengths.get(q);
			int posFrom1 = (int) (win.scale * pos) - win.offX;
			
			// draw feature rectangle
			g.setColor(featureColor);
			g.fillRect(lastFrom1, pieceY + offY, posFrom1 - lastFrom1 + (int)seqstep, objHeight);
			if (UtilityMethods.isDataAvailableForObject(one)) {
				g.setColor(Constants.READ_WITH_PAIR_DATA_BORDER_COLOR);
				int bottomY = pieceY + offY + objHeight - 1;
				if (q == 0) {
					// start line
					g.drawLine(lastFrom - 1, pieceY + offY, lastFrom - 1, bottomY);
				}
				// top line
//				g.drawLine(lastFrom - 1, pieceY + offY, lastFrom + 1 + (int)seqstep, pieceY + offY);
//				// bottom line
//				g.drawLine(lastFrom - 1, bottomY, lastFrom + 1 + (int)seqstep, bottomY);
			}
		}
		// draw last read rectangle
		int lastFrom = (int) (win.scale * pos) - win.offX;
		g.setColor(Color.lightGray);
		g.fillRect(lastFrom, pieceY + offY, to - lastFrom + (int)seqstep, objHeight);
		if (UtilityMethods.isDataAvailableForObject(one)) {
			g.setColor(Constants.READ_WITH_PAIR_DATA_BORDER_COLOR);
			// end line
			int lastTo = (int) (win.scale * one.to) - win.offX;
			int firstFrom = (int) (win.scale * one.from) - win.offX;
			if (one.type.endsWith("pr_off")) {
				g.drawLine(lastFrom, pieceY + offY, lastFrom, pieceY + offY + objHeight - 1);
			} else if (one.type.endsWith("pl_off")) {				
				g.drawLine(lastTo, pieceY + offY, lastTo, pieceY + offY + objHeight - 1);				
			} 
			// top line
			g.drawLine(firstFrom, pieceY + offY, lastTo, pieceY + offY);
			// bottom line
			int bottomY = pieceY + offY + objHeight - 1;
			g.drawLine(firstFrom, bottomY, lastTo, bottomY);
		}
	}
	
	/**
	 * Draws pre-arranged shades
	 * 
	 * @see Stripe for parameters
	 */
	public void drawShade(Graphics g, aWindow win, Font font, One selected)
	{
		/* offset of the y-coordinate of stripe */
		int offY = top - win.offY;
//		if (offY > win.height - 1 || offY + height < 1)
//			return;

		int j = 0;

		int from = (int) (win.scale * shade[j].from) - win.offX, to = (int) (win.scale * shade[j].to) - win.offX;
		if (from >= win.width)
		{
			//continue;
		} else {
			if ((from >= 0) && (from <= win.width) && (to >= win.width))
			{
				to = win.width;
			}

			if ((from <= 0) && (to <= win.width) && (to >= 0))
			{
				from = 0;
			}

			/* sets colors and draws shade elements */
			g.setColor(shade[j].color);
			g.fillRect(from, shade[j].y + offY, to - from + (int) win.scale, numShade*(objHeight + 6));
		}
	}
	
	/**
     * Finds elements with matching partial name.
     * Returns filled list    
     * @param namepart search string
     * @param list list to fill
     */

  public Vector objFind(String namepart, Vector list) {
    for(int i=0;i<numObj;i++){
	if(piece[i].name.indexOf(namepart) != -1 || piece[i].id.indexOf(namepart) != -1)
			list.addElement(piece[i]);
   }
   return list;
  }
	
	/**
	 * Draw filled triangle. Direction = 1 if forward, -1 if reverse
	 * @param g
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param direction
	 */
	public void drawTriangle(Graphics g, int x1, int y1, int y2, int seqstep, int direction, Color color) {
		int xpoints[] = {x1, x1 + seqstep*direction, x1};
	    int ypoints[] = {y1, (y1 + y2)/2, y2};
	    int npoints = 3;

	    g.setColor(color);
	    g.fillPolygon(xpoints, ypoints, npoints);
	}
	
}
