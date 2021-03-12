package presentation;

//import java.applet.Applet;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class derBrowserKey extends aBrowser
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JFrame frame;
	static String TITLE = "Key";
	Image offI, winI, pic;
	Graphics offG, winG;

	int wText = 150, wZoom = 150;
	int x0 = 5, y0 = 5, wVis, hVis;
	int xText, yText;
	Color hiliteColor = UtilityMethods.hexColor("FFDCDC");

	LinkedHashMap<String, String> speciesColorMap = new LinkedHashMap<String, String>();
	String type = "";

	/* contents */

	int LEFTEND=0, RIGHTEND=0;
	
	Stripe stripe;
	Vector stripeVector = new Vector();

	int MULTI, MULTISHADE, CLONES, CLONESHADE, LOCI, LOCISHADE, COMP, COMPSHADE, PLOT, PLOTSHADE, EXON, EXONSHADE, SEQ, SEQSHADE, CONDSEQ, CONDSEQSHADE, SECSTRU, SECSTRUSHADE, 
	STRIPES, STRIPESHADE, TOTALSTRIPES, ALLSTRIPES, ACTIONS = 2, curStripe, curpiece, oldhilite = 0;

	/* window */

	aWindow win;
	double zoo = 1.;
	int width, height, mapHeight, zooval=1; 

	boolean winChange = true;
	boolean hiliteOn = true;
	
	public derBrowserKey() {
		
	}

	public void init()
	{
		// System.out.println("key color spec map " + speciesColorMap);
		width = Constants.KEY_WINDOW_WIDTH;
		int heightAdjustment = 0;
		if (speciesColorMap != null) {
			heightAdjustment = speciesColorMap.size() * 20;
		}
		height = Constants.KEY_WINDOW_HEIGHT + heightAdjustment;
		if (type.equals(Constants.READS_WINDOW_TITLE)) {
			height = Constants.KEY_WINDOW_READS_HEIGHT;
			width = Constants.KEY_WINDOW_READS_WIDTH;
		} else if (type.equals(Constants.P_VALUE_WINDOW_TITLE)) {
			height += Constants.P_VALUE_KEY_FRAME_TEXT_HEIGHT_CORRECTION;
		} else if (type.equals(Constants.VARIANTS_WINDOW_TITLE)) {
			height += Constants.VARIANTS_KEY_FRAME_TEXT_HEIGHT_CORRECTION;			
		}
		offI=createImage(width, height);
		offG=offI.getGraphics();
		repaint();
		
		wVis = width;
		hVis = height;
		
		winI=createImage(wVis, hVis);
		winG=winI.getGraphics();

		repaint();

		initMapVisuals(winG);
		repaint();
	}

	public void paintWin(Graphics g)
	{
		try {
			g.setColor(Color.white);
			g.fillRect(0, 0, win.width + 10, win.fullheight - 10);
		} catch (Exception e) {
			
		}
	}
	
	public void update(Graphics g)
	{ 
		paint(g);
	}

	public void paint(Graphics g)
	{ 
		paintOff(offG); 
		g.drawImage(offI, 0, 0, null);
		drawKeyType(g);
	}

	public void paintOff(Graphics g)
	{ 
		//g.setColor(Color.black);
		g.setColor(Color.white);
		g.fillRect(0,0,width,height);
		
		paintWin(winG);
		
		g.drawImage(winI, x0, y0, null);
	}

	/* create visuals */
	public void initMapVisuals(Graphics g)
	{
		win = new aWindow(wVis, hVis, LEFTEND, RIGHTEND, zoo, false); 
	}
	
	private void drawKeyType(Graphics g) {
		if (type.equals(Constants.VARIANTS_WINDOW_TITLE)) {
			drawMultiSampleChrKey(g);
		} else if (type.equals(Constants.P_VALUE_WINDOW_TITLE)) {
			drawMultiSampleKey(g);
		} else if (type.equals(Constants.READS_WINDOW_TITLE)) {
			drawSingleSampleReadsKey(g);
		}
	}
	
	private void drawMultiSampleChrKey(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("X axis = bp index", 50, 28);
		int objHeight = 15;
		int objWidth = 20;
		int startY = 40;
		int yIncrement = 25;
		
		int fontYOffset = 12;
		int startX = 50;
		int labelX = 100;
		
		if (Constants.EXON_DATA_EXISTS) {
			g.setColor(UtilityMethods.hexColor(Constants.EXON_COLOR_STRING));
			g.fillRect(startX, startY, objWidth, objHeight);
			g.setColor(Color.black);
			g.drawRect(startX, startY, objWidth, objHeight);
			g.drawString("Exon", labelX, startY + fontYOffset);
			startY += yIncrement;
		}
		ArrayList<String> keys = new ArrayList<String>(speciesColorMap.keySet());
		for (int i = 0; i < keys.size(); i++) {
			g.setColor(UtilityMethods.hexColor(speciesColorMap.get(keys.get(i))));
			g.fillRect(startX, startY, objWidth, objHeight);
			g.setColor(Color.black);
			g.drawRect(startX, startY, objWidth, objHeight);
			g.drawString(keys.get(i), labelX, startY + fontYOffset);
			startY += yIncrement;
		}
		
		//This will draw KEY frame inside GN - Variants window. Reduced the size of window and formatted inside text. OA
		g.drawString("Colors are for variants that have a size > 0."
				+ " ", 50, startY + 8);
		int lineIncrement = 15;
		startY += lineIncrement + 8;
		g.drawString("Insertions and indel insertions are dimensionless in this window and ", 50, startY);
		lineIncrement = 15;
		startY += lineIncrement;
		g.drawString("are drawn as small black rectangles.", 50, startY);
	}
	
	private void drawMultiSampleKey(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
		g.setColor(Color.BLACK);
		g.drawString("X axis = bp index, Y axis = -log(P-value)", 50, 28);
		
		int pointHeight = Constants.POINT_SIZE;
		int startY = 40;
		int yIncrement = 20;
		
		int borderPointX = 50;
		//int noBorderPointX = 60;
		//int labelX = 100;
		int labelX = 70;
		
		int fontYOffset = 8;
		
		ArrayList<String> keys = new ArrayList<String>(speciesColorMap.keySet());
		for (int i = 0; i < keys.size(); i++) {
			g2d.setColor(UtilityMethods.hexColor(speciesColorMap.get(keys.get(i))));
//			g2d.fillOval(noBorderPointX, startY, pointHeight, pointHeight);
//			// draw oval with no border, actually ovals with no border look jagged
//			// but drawing a border with the same color as the oval smoothes this out
//			g2d.drawOval(noBorderPointX, startY, pointHeight - 1, pointHeight - 1);
			g2d.fillOval(borderPointX, startY, pointHeight, pointHeight);
			// draw oval with black border
			g2d.setColor(Color.black);
			g2d.drawOval(borderPointX, startY, pointHeight - 1, pointHeight - 1);
			
			g.drawString(keys.get(i), labelX, startY + fontYOffset);
			startY += yIncrement;
		}
		//This will draw KEY frame inside GN - P-Values window. Reduced the size of window and formatted inside text. OA
		g.drawString("Points where -log(P-value) is undefined (P-value = 0) are drawn "
				+ "in gray", 50, startY + 8);
		int lineIncrement = 15;
		startY += lineIncrement + 8;
		g.drawString("without borders above the horizontal line at top and may correspond to ", 50, startY);
		startY += lineIncrement;
		g.drawString("multiple samples. Reads cannot be viewed by selection of these points. ", 50, startY);
		startY += lineIncrement;
		g.drawString("Points with non-zero P-values may also overlap. ", 50, startY);
		startY += lineIncrement;
		g.drawString("Please use Multi-Sample, Chromosome window to view reads for these samples. ", 50, startY);
		startY += lineIncrement;
		g.drawString("Points for samples where no Structural Variant is predicted are drawn  ", 50, startY);
		startY += lineIncrement;
		g.drawString("below the horizontal line at bottom immediately above the scale. ", 50, startY);
		startY += lineIncrement;
		g.drawString("X and Y positions for these points are meaningless.", 50, startY);
	}
	
	private void drawSingleSampleReadsKey(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Mismatch Colors:", 50, 28);
		int objHeight = 15;
		int objWidth = 3;
		int startY = 40;
		int yIncrement = 25;
		
		int mismatchStartX = 50;
		int mismatchLabelOffset = 5;
		int mismatchXOffset = 20;
		int fontYOffset = 12;
		
		drawLabeledMismatch(g, "a", Constants.SEQ_A_MISMATCH_COLOR, mismatchStartX, 
				startY, objWidth, objHeight, mismatchLabelOffset, fontYOffset);
		mismatchStartX += mismatchXOffset;
		drawLabeledMismatch(g, "t", Constants.SEQ_T_MISMATCH_COLOR, mismatchStartX, 
				startY, objWidth, objHeight, mismatchLabelOffset, fontYOffset);
		mismatchStartX += mismatchXOffset;
		drawLabeledMismatch(g, "g", Constants.SEQ_G_MISMATCH_COLOR, mismatchStartX, 
				startY, objWidth, objHeight, mismatchLabelOffset, fontYOffset);
		mismatchStartX += mismatchXOffset;
		drawLabeledMismatch(g, "c", Constants.SEQ_C_MISMATCH_COLOR, mismatchStartX, 
				startY, objWidth, objHeight, mismatchLabelOffset, fontYOffset);
		mismatchStartX += mismatchXOffset;
		// This must be last unless font metrics are used due to label length
		drawLabeledMismatch(g, "N or n", Constants.SEQ_N_COLOR, mismatchStartX, 
				startY, objWidth, objHeight, mismatchLabelOffset, fontYOffset);
		
		startY += yIncrement;
		int svWidth = 20;
		int startX = 50;
		int labelStartX = 100;
		
		g.setColor(Color.black);
		g.drawString("Structural Variant (SV) Colors:", startX, startY + 15);
		startY += yIncrement;
		for (int i = 0; i < Constants.SV_COLORS.length; i++) {
			g.setColor(UtilityMethods.hexColor(Constants.SV_COLORS[i]));
			g.fillRect(startX, startY, svWidth, objHeight);
			g.setColor(Color.black);
			g.drawString(Constants.SV_NAMES[i], labelStartX, startY + fontYOffset);
			startY += yIncrement;
		}
		
		g.setColor(Color.black);
		g.drawString("Structural Variant Indicators (color can be any of the SV colors above):", startX, startY + 15);
		startY += yIncrement;
		g.setColor(Color.lightGray);
		g.fillRect(startX, startY, svWidth, objHeight);
		g.setColor(UtilityMethods.hexColor(Constants.SV_COLORS[0]));
		g.fillRect(startX + 8, startY, 4, objHeight);
		g.setColor(Color.black);
		g.drawString("Read Pairs", labelStartX, startY + fontYOffset);
		startY += yIncrement;
		g.setColor(Color.lightGray);
		g.fillRect(startX, startY, svWidth, objHeight);
		g.setColor(UtilityMethods.hexColor(Constants.SV_COLORS[0]));
		g.fillRect(startX + 15, startY, 5, objHeight);
		g.setColor(Color.black);
		g.drawString("Paired Read - Mate Offscreen (can be on right or left)", labelStartX, startY + fontYOffset);
		
		startY = 40;
		int startXSecondColumn = 270;
		labelStartX = 40;
	
		g.setColor(Color.black);
		g.drawString("Symbols:", startXSecondColumn, 28);
		if (Constants.EXON_DATA_EXISTS) {
			g.setColor(UtilityMethods.hexColor(Constants.EXON_COLOR_STRING));
			g.fillRect(startXSecondColumn, startY, 13, objHeight);
			g.setColor(Color.black);
			g.drawRect(startXSecondColumn, startY, 13, objHeight);
			g.drawString("Exon", startXSecondColumn + labelStartX, startY + fontYOffset);
			startY += yIncrement;
		}
		
		g.setColor(Color.lightGray);
		g.fillRect(startXSecondColumn, startY, 13, objHeight);
		g.setColor(Color.black);
		g.drawString("Normal Read", startXSecondColumn + labelStartX, startY + fontYOffset);
		startY += yIncrement;
		
		g.setColor(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_COLOR_STRING));
		g.fillRect(startXSecondColumn, startY, 13, objHeight);
		g.setColor(Color.black);
		g.drawString("Read - Mate on Different Chromosome", startXSecondColumn + labelStartX, startY + fontYOffset);
		startY += yIncrement;
		
//		int secondLineCorrection = 7;
//		g.setColor(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING));
//		g.fillRect(startXSecondColumn, startY, 13, objHeight);
//		g.setColor(Color.black);
//		g.drawString("Read - Mate on Different Chromosome -", startXSecondColumn + labelStartX, startY + fontYOffset);
//		startY += (yIncrement - secondLineCorrection);
//		g.drawString("Mate Data Available", startXSecondColumn + labelStartX, startY + fontYOffset);
//		startY += (yIncrement + secondLineCorrection);
		
		g.setColor(Color.lightGray);
		g.fillRect(startXSecondColumn, startY, 13, objHeight);
		g.setColor(UtilityMethods.hexColor(Constants.INDEL_INSERTION_LINES_COLOR_STRING));
		g.drawLine(startXSecondColumn + 4, startY - 1, startXSecondColumn + 8, startY - 1);
		g.drawLine(startXSecondColumn + 6, startY, startXSecondColumn + 6, startY + objHeight);
		g.setColor(Color.black);
		g.drawString("Indel Insertion Position", startXSecondColumn + labelStartX, startY + fontYOffset);
	}
	
	private void drawLabeledMismatch(Graphics g, String label, Color color, int mismatchStartX, 
			int startY, int objWidth, int objHeight, int mismatchLabelOffset, int fontYOffset) {
		g.setColor(color);
		g.fillRect(mismatchStartX, startY, objWidth, objHeight);
		g.setColor(Color.black);
		g.drawString(label, mismatchStartX + mismatchLabelOffset, startY + fontYOffset);
	}

	public static void main(String[] args) { 
		 
	} 
}

