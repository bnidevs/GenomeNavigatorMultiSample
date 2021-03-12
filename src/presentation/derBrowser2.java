package presentation;

//import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Vector;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class derBrowser2 extends aBrowser
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean silent = false;

	public boolean enableNewMap = true;
	public String refSpec = "";
	
	public String frameFileName = "";
	public boolean isWindowActive = false;
	
	static JFrame frame;
	static String TITLE = "GenomeNavigator";
	String fileName;
	Image offI, winI, pic;
	Graphics offG, winG;
	boolean first = true, connected = true;
	
	final ArrayList<Image> icons = new ArrayList<Image>(); 
	
	public JFrame keyFrame = null;
//	private FindDialog findDialog = new FindDialog();

	//We do not need this in Genome Navigator
	//browserServio dataServer = null;

	/* sliders */

	aSlider vert, hori, zoom;

	final static int SLIDER_RESOLUTION = 2000;
	final static int SLIDER_W = 21, SLIDER_CAP = 17;
	int wText = 145, wZoom = 145;
	int x0 = 5, y0 = 5, wVis, hVis;
	int xText, yText;
	int xZoom, hBut=20, wBut=68, xBut = x0, yBut, betweenBut = 6, hField = 35;
	Color sliderColor = UtilityMethods.hexColor("666666");
	Color hiliteColor = UtilityMethods.hexColor("FFDCDC");
	int fixedHeight = 0;
	// correction for y position in fixed stripes
	int correction = 0;
	int exonCorrection = 0;
	
	boolean plotPresent = true;
	boolean exonPresent = Constants.EXON_DATA_EXISTS;
	boolean refSeqPresent = true;

	String Apply = "Apply", Cancel = "Cancel";

	String misc[] = {
			"Highlight Stripes when Selecting",
			"Draw Borders around Rectangles",
			"Show Cursor X Coordinate",
			//"Scale Sticky (Always Visible)",
			"Set Zoom Increment to X2",
			"Shift Text Down",
			Apply,
			Cancel
			//"Show Help Text"
	};

	String banner[] = {
			"DerBrowser",
			"Java Applet by Andrei Grigoriev",
			"Connecting to server..."
	};

	String custom[] = {
			"Select Fonts",
			"Scale Units",
			"Misc Options",
			Cancel
	};

	String customFont[] = {
			"Scale/Button Font (" + Constants.SCALE_FONT_TYPE_STRING + " " + Constants.SCALE_FONT_SIZE + "pt)",
			"Object Font (" + Constants.OBJ_FONT_TYPE_STRING + " " + Constants.OBJ_FONT_SIZE + "pt)",
			Cancel
	};

	String action[] = {
			Apply,
			Cancel
	};

	String objBut =  "ABOUT";
	String selBut =  "DISPLAY";
	String newBut =  "KEY";
	//String newBut =  "NEW MAP";
	//String findBut =  "FIND";
	String findBut =  "PAIR";

	aPopupMenu stripeMenu, newMapStripeMenu, customFontMenu, customMenu, miscMenu, fontMenu, foundMenu = new aPopupMenu(), moreMenu;
	aPopupInput findInput, scaleInput, newMapInput;
	aPopup naPopup;

	String overObj = "None"; 
	One selObj, none;

	/* contents */

	int LEFTEND=0, RIGHTEND=0, OBJECTID;
	int definedLeft=-1, definedRight=-1;
	int newLEFT=0, newRIGHT=0;
	// side where point was selected on previous screen, 0 = left, 1 = right
	public int side = 0;
	public int selectedPointFrom = 0;

	String USERID="", MAPID= "", TYPES= "", OTHERTYPES= "";

	More cgi = null;
	int moreCalls = 0;
	int port = 80;

	Stripe stripe;
	ScaleFixed scale = new ScaleFixed();
	Vector stripeVector = new Vector(), shadeVector = new Vector(), foundVector, moreVector = null;

	int MULTI, MULTISHADE, CLONES, CLONESHADE, LOCI, LOCISHADE, COMP, COMPSHADE, PLOT, PLOTSHADE, EXON, EXONSHADE, SEQ, SEQSHADE, CONDSEQ, CONDSEQSHADE, SECSTRU, SECSTRUSHADE, 
	STRIPES, STRIPESHADE, TOTALSTRIPES, ALLSTRIPES, ACTIONS = 2, curStripe, curpiece, oldhilite = 0;

	/* window */

	aWindow win;
	double zoo = 1.;
	int width, height, mapHeight, zooval=1;
	double zooval1 = 1;
	double zoom2Xmultiplier = 1;
	double ZOOMX2BP_SIZE = 22500.0;
	int range;
	double zoomX2corr;
	int resCorrectionCutoff = 1090;

	String FontList[];

	public static Font objF = new Font(Constants.OBJ_FONT, Constants.OBJ_FONT_TYPE, Constants.OBJ_FONT_SIZE);    
	public static Font scaleF = new Font(Constants.SCALE_FONT, Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);   
	public static Font menuF = new Font(Constants.MENU_FONT, Constants.MENU_FONT_TYPE, Constants.MENU_FONT_SIZE); 

	boolean winChange = true;
	public static boolean hiliteOn = true, zoomX2 = false, shiftText = false, showCoord = true, drawBorder = true;
	boolean scaleFontSelect = true;
	boolean DEBUG = true;
	boolean debugZoom = false;
	
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = new LinkedHashMap<String, ArrayList<Interval>>();
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMatesDiffChrMap = new LinkedHashMap<String, ArrayList<Interval>>();
	public ArrayList<Interval> intervals = new ArrayList<Interval>();
	public String dataDirectory = "";
	public String sampleName = "";
	public String chromosomeDisplayed = "";
	
	public HashMap<String, JFrame> openFramesMap = new HashMap<String, JFrame>();
	public HashMap<String, derBrowser2> openReadsBrowsersMap = new HashMap<String, derBrowser2>();
	
	public derBrowser2() {
		fileName = "etc/data2ce.txt";
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());
//		createFindDialog();
	}

	public void debug(String msg)
	{
		if(DEBUG) {
			if (!silent) {
				System.out.println("DB:"+msg);
			}
		}
	}


	public void init()
	{
//		String param;
		String file = fileName;
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
		
		width = Constants.READS_WINDOW_WIDTH;
		height = Constants.READS_WINDOW_HEIGHT;
		offI = createImage(width, height);
		offG = offI.getGraphics();
		repaint();

		/* set coords */

		wVis = width - 2*x0 - SLIDER_W;
		//if(width == 600){
		hVis = height - 3*y0 - 2*hBut - SLIDER_W;
		yText = 2*y0+hVis+SLIDER_W+10;
		yBut = y0+yText;
		xZoom = (width-wZoom)/2;
		winI = createImage(wVis, hVis);
		winG = winI.getGraphics();

		repaint();

		none = new One();
		none.name = "None";
		none.from = none.to = 0;
		selObj = none;

		scale.silent = silent;
		scale.factor = 1;

//		if(cgi == null && file == null)
//		{
//			debug("No data stream given");
//			System.exit(0);
//		}
		
		// read and parse input streams 
//		if(cgi != null) 
//			connected = fillStripeVector(cgi);

		if(file != null) 
			connected = fillStripeVector(file);
		
		else
			connected = true;
		
		initMapVisuals();
		// menus

		int miscItems = misc.length;
		boolean selected[] =  new boolean[miscItems];
		boolean special[] =  new boolean[miscItems];
		for(int i = 0; i < 2; i++)
		{
			selected[i] = true;
			special[i] = false;
		}

		selected[2] = showCoord;
		special[2] = false;
		selected[3] = zoomX2;
		special[3] = false;
		selected[4] = false;
		special[4] = false;

		for(int i = 5; i < 7; i++)
		{
			selected[i] = false;
			special[i] = true;
		}

		miscMenu = new aPopupMenu(misc, miscItems, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "miscMenu");
		miscMenu.centerPopup(offG, x0, y0, wVis, hVis);

		int customItems = custom.length;
		selected = new boolean[customItems];
		special = new boolean[customItems];

		for(int i = 0; i < customItems; i++)
		{
			selected[i] = false;
			special[i] = true;
		}

		customMenu = new aPopupMenu(custom, customItems, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "customMenu");
		customMenu.centerPopup(offG, x0, y0, wVis, hVis);

		int customFontItems = customFont.length;
		selected = new boolean[customFontItems];
		special = new boolean[customFontItems];

		for(int i = 0; i < customFontItems; i++)
		{
			selected[i] = false;
			special[i] = true;
		}

		customFontMenu = new aPopupMenu(customFont, customFontItems, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "customFontMenu");
		customFontMenu.centerPopup(offG, x0, y0, wVis, hVis);

		FontList=Toolkit.getDefaultToolkit().getFontList();
		int fontItems = FontList.length;
		selected = new boolean[fontItems];
		special = new boolean[fontItems];

		for(int i=0;i<fontItems;i++)
		{
			selected[i] = false;
			special[i] = true;
		}

		fontMenu = new aPopupMenu(FontList, fontItems, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "fontMenu");
		fontMenu.centerPopup(offG, x0, y0, wVis, hVis);

		if (moreCalls > 1)
		{
			selected =  new boolean[moreCalls+1];
			special =  new boolean[moreCalls+1];
			String moreList[] = new String[moreCalls+1];

			for(int i=0;i<moreCalls;i++)
			{
				selected[i] = false;
				special[i] = true;
				moreList[i] = ((More)moreVector.elementAt(i)).name;
			}

			selected[moreCalls] = false;
			special[moreCalls] = true;
			moreList[moreCalls] = Cancel;
			moreMenu = new aPopupMenu(moreList, moreCalls+1, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "moreMenu");
			moreMenu.centerPopup(offG, x0, y0, wVis, hVis);
		}

		String na[] = new String[1];
		na[0] = " Find Name(s) Containing ";
		findInput = new aPopupInput(na, 1, 15, Color.lightGray, sliderColor/*Color.lightGray*/, Color.black, Color.lightGray, Color.black, false, menuF, this, "findInput");
		findInput.centerPopup(offG, x0, y0, wVis, hVis,"Find");

		na[0] = " Scale Unit ";
		scaleInput = new aPopupInput(na, 1, 9, Color.lightGray, sliderColor/*Color.lightGray*/, Color.black, Color.lightGray, Color.black, false, menuF, this, "scaleInput");
		scaleInput.centerPopup(offG, x0, y0, wVis, hVis,"Set");
		scaleInput.setValue(""+scale.factor, 0);

		na = new String[2];
		na[0] = " Map Start ";
		na[1] = " Map End ";
		newMapInput = new aPopupInput(na, 2, 10, Color.lightGray, sliderColor/*Color.lightGray*/, Color.black, Color.lightGray, Color.black, false, menuF, this, "newMapInput");
		newMapInput.centerPopup(offG, x0, y0, wVis, hVis,"Done");

		na = new String[1];
		na[0] = "Nothing Available";
		naPopup = new aPopup(na, 1, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, false, menuF, this, "naPopup");
		naPopup.centerPopup(offG, x0, y0, wVis, hVis, "Dismiss");


		/* make sliders */

		int vertele = (mapHeight <= hVis) ? (hVis-2*SLIDER_CAP) : (int)((1.*hVis/mapHeight)*(hVis-2*SLIDER_CAP));
		vert = new aSlider(x0+wVis,y0,hVis,SLIDER_W,SLIDER_RESOLUTION,0.025,SLIDER_CAP,vertele,false,0,sliderColor,this,"vert");
		hori = new aSlider(x0,y0+hVis,SLIDER_W,wVis,SLIDER_RESOLUTION,0.025,SLIDER_CAP,wVis-2*SLIDER_CAP,true,0,sliderColor,this,"hori");
		//zoom = new aSlider(xZoom,yBut,SLIDER_W,wZoom,9,0.2,SLIDER_CAP,17,true,0,sliderColor,this,"zoom");
		range = RIGHTEND - LEFTEND;
		int resolution = 8;
		zoomX2 = false;
		if (range < resCorrectionCutoff) {
			resolution = (int) 8*range/870;			
		}
		if (range > ZOOMX2BP_SIZE/4) {
			zoomX2 = true;
		}
		zoomX2corr = range/ZOOMX2BP_SIZE;
		
		if (zoomX2) {
			resolution = 8;
			if (zoomX2corr <= 0.5) {
				zoom2Xmultiplier = 2;
				resolution = 7;
			}
			if (zoomX2corr <= 0.25) {
				zoom2Xmultiplier = 4;
				resolution = 6;
			}
		}
		
		if (debugZoom) {
			System.out.println("range " + range);
			System.out.println("resolution " + resolution);
			System.out.println("zoomX2 = " + zoomX2);
			System.out.println("zx2 cor " + zoomX2corr);
		}
		
		zoom = new aSlider(xZoom,yBut,SLIDER_W,wZoom,resolution,0.2,SLIDER_CAP,17,true,0,sliderColor,this,"zoom");
		
		if(connected) first = false;
		winChange = true;
		repaint();
		repaint();
	
	}


	public void update(Graphics g)
	{ 
		paint(g);
	}

	public void paint(Graphics g)
	{ 
		paintOff(offG); 
		g.drawImage(offI, 0, 0, null);
	}

	public void paintWin(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, win.width, win.fullheight);
		// below here, the order determines the layer of the highlight color.
		int lastStripe = STRIPES - 1;
		stripe = (Stripe) stripeVector.elementAt(lastStripe);
		stripe.visible = stripeMenu.selectedLine[lastStripe];
		if (stripe.visible)
		{
			for (curStripe = 0; curStripe < STRIPESHADE; curStripe++)
			{
				//stripe = (Stripe) shadeVector.elementAt(curStripe);
				Sequence stripe = (Sequence) shadeVector.elementAt(curStripe);
				//debug("The name of the stripe is " + stripe.shadename);
				if (stripe.visible)
				{				
					if (hiliteOn && stripe.hilite)
					{
						g.setColor(hiliteColor);
						g.fillRect(0, stripe.top - win.offY, win.width, stripe.height + 1);
					}
					stripe.drawShade(g, win, objF, selObj);
				}
			}
		}

		if (win.areaVisible)
		{
			g.setColor(hiliteColor);
			g.fillRect(win.areaFrom, 0, win.areaTo - win.areaFrom, win.fullheight);
		}

		fixedHeight = calculateFixedHeight();
		
		for (curStripe = STRIPES - 1; curStripe >= 0; curStripe--)
		//for (curStripe = 0; curStripe < STRIPES; curStripe++)
		{
			
			stripe = (Stripe) stripeVector.elementAt(curStripe);
			if (stripe instanceof Sequence) {
				((Sequence) stripe).fixedHeight = fixedHeight;
			}
			// debug("The name of the stripe is " + stripe.name);
			if (stripe.visible)
			{
				if (hiliteOn && stripe.hilite)
				{
					g.setColor(hiliteColor);
					g.fillRect(0, stripe.top - win.offY, win.width, stripe.height + 1);
				}
				stripe.draw(g, win, objF, selObj);
			}
			if (fixedHeight > 0) {
				boolean drawRect = false;
				if (exonPresent) {
					drawRect = true;
				} else if (plotPresent) {
					drawRect = true;
				}
				if (!exonPresent && !plotPresent && refSeqPresent) {
					drawRect = true;
				}
				
				if (drawRect) {
					int rectangleIndex = 2;
					if (curStripe == rectangleIndex) {
						drawBackgroundRectangle(g);
					}
					//System.out.println("rect " + rectangleIndex);
				}
			}
		}

		/*
		 * Se-qSec se-qSec = new Se-qSec();
		 * se-qSec.setData((Plot)(stripeVector.elementAt
		 * (3)),(Se-quence)(stripeVector.elementAt(5)));
		 * //**********************
		 * **********************************************************
		 * se-qSec.Output();
		 * 
		 * se-qSec.draw(g,win,objF,selObj);
		 */

		scale.draw(g, win, scaleF);
	}

	/**
	 * Draws rectangle behind Plot and/or Exon if either is present to obscure
	 * Reads behind these components
	 * @param g
	 */
	public void drawBackgroundRectangle(Graphics g) {	
		boolean moveUpRefBottomLine = false;
		if (!exonPresent && !plotPresent && refSeqPresent) {
			fixedHeight = 1;
			moveUpRefBottomLine = true;
		}
		g.setColor(Color.white);
		g.fillRect(0, 0, win.width, fixedHeight);
		g.setColor(new Color(248, 248, 248));
//		Graphics2D g2 = (Graphics2D) g;
//        g2.setStroke(new BasicStroke(2));
		if (!refSeqPresent) {
			g.drawLine(0, fixedHeight - 1, win.width, fixedHeight - 1);
			g.drawLine(0, fixedHeight, win.width, fixedHeight);
		} else {
			// draw rectangles above and below reference sequence
			g.setColor(Color.white);
			g.fillRect(0, fixedHeight - 1, win.width, Constants.ABOVE_REF_RECTANGLE_CORR);
			int startY = Constants.REF_SEQ_HEIGHT + fixedHeight - Constants.BELOW_REF_SEQ_HEIGHT_CORR;
			int lineStartY = startY + Constants.REF_SEQ_HEIGHT_CORRECTION;
			g.fillRect(0, startY, win.width, Constants.REF_SEQ_HEIGHT_CORRECTION);
			g.setColor(new Color(248, 248, 248));
			if (moveUpRefBottomLine) {
				lineStartY -= 4;
			}
			g.drawLine(0, lineStartY - 1, win.width, lineStartY - 1);
			g.drawLine(0, lineStartY, win.width, lineStartY);
		}
	}

	public void paintOff(Graphics g)
	{ 
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);

		if (first)
		{
			Font bannerF = new Font("Helvetica", Font.PLAIN, 18);
			g.setFont(bannerF);
			FontMetrics scaleFM = g.getFontMetrics(bannerF);

			g.setColor(Color.white);
			g.drawString(banner[0],(width - scaleFM.stringWidth(banner[0]))/2-1, height/2-1-35);
			g.setColor(Color.red);
			g.drawString(banner[0],(width - scaleFM.stringWidth(banner[0]))/2, height/2-35);

			if (!connected) 
				banner[2] = "Connection Failed!";

			for (int i = 1; i < 3; i++)
			{
				g.setColor(Color.white);
				g.drawString(banner[i],(width-scaleFM.stringWidth(banner[i]))/2-1, height/2-1+i*20);
				g.setColor(Color.red);
				g.drawString(banner[i],(width-scaleFM.stringWidth(banner[i]))/2, height/2+i*20);
			}

			return;
		}

		if (winChange)
		{
			paintWin(winG);
			winChange=false;
		}

		g.drawImage(winI, x0, y0, null);
		vert.drawSlider(g); 
		hori.drawSlider(g); 
		zoom.drawSlider(g);
		drawText(g); 

		/* draw menus, popups */

		if(stripeMenu.visible) stripeMenu.draw(g);
		else if(newMapStripeMenu.visible) newMapStripeMenu.draw(g);
		else if(foundMenu.visible) foundMenu.draw(g);
		else if(customFontMenu.visible) customFontMenu.draw(g);
		else if(miscMenu.visible) miscMenu.draw(g);
		else if(customMenu.visible) customMenu.draw(g);
		else if(fontMenu.visible) fontMenu.draw(g);
		else if(moreMenu != null && moreMenu.visible) moreMenu.draw(g);
		else if(scaleInput.visible) scaleInput.draw(g);
		else if(findInput.visible) findInput.draw(g);
		else if(newMapInput.visible) newMapInput.draw(g);
		else if(naPopup.visible) naPopup.draw(g);
	}

	/**
	 * Button text drawn
	 * @param g
	 */
	public void drawText(Graphics g)
	{ 
		FontMetrics scaleFM = g.getFontMetrics(scaleF);
		int fH = scaleFM.getHeight()+ (shiftText ? 2 : 0);

		g.setFont(scaleF);

		int x = xBut;
		g.setColor(sliderColor);
		g.fill3DRect(x,yBut, wBut, hBut, true);
		g.setColor(Constants.BUTTON_FONT_COLOR);
		g.drawString(selBut, x + 6, yBut + Constants.BUTTONS_Y_CORRECTION + fH - 2);//+1);

		/*	g.setColor(sliderColor);
		        g.fill3DRect(x,yBut-hBut-y0, wBut, hBut, true);
			g.setColor(Color.red);
		        g.drawString("LEFT",x+6,yBut-hBut +fH-2);//+1);
		 */
		x += wBut + betweenBut;
		g.setColor(sliderColor);
		g.fill3DRect(x, yBut, wBut, hBut, true);
		g.setColor(Constants.BUTTON_FONT_COLOR);
		g.drawString(objBut, x + 6, yBut + Constants.BUTTONS_Y_CORRECTION + fH - 2);//+1);

		x += wBut + betweenBut;
		g.setColor(sliderColor);
		g.fill3DRect(x, yBut, wBut, hBut, true);
		g.setColor(Constants.BUTTON_FONT_COLOR);
		g.drawString(newBut, x + 6, yBut + Constants.BUTTONS_Y_CORRECTION + fH - 2);//+1);

		x += wBut + betweenBut;
		g.setColor(sliderColor);
		g.fill3DRect(x, yBut, wBut, hBut, true);
		g.setColor(Constants.BUTTON_FONT_COLOR);
		g.drawString(findBut, x + 6, yBut + Constants.BUTTONS_Y_CORRECTION + fH - 2);//+1);

		g.setColor(Color.white);
		g.drawString("ZOOM: 1/"+zooval,xZoom,yText);
		g.drawString(zoomX2 ? "x2" : "+1",xZoom+wZoom-15,yText);

		x = width-wText-y0;
		g.drawString("MOUSE OVER",x,yText);
		g.drawString("SELECTED OBJECT",x-wText-betweenBut,yText);

		g.fill3DRect(x,yBut, wText, hField, false);
		g.fill3DRect(x-wText-betweenBut,yBut, wText, hField, false);
		
		g.setColor(Color.black);
		
		String mouseOverLine1 = "";
		String mouseOverLine2 = "";
		if (overObj != null) {
			mouseOverLine1 = overObj;
		}
		
		if (mouseOverLine1.contains(Constants.MATE_INFO_SPLIT_CHAR)) {
			String[] entries = mouseOverLine1.split(Constants.MATE_INFO_SPLIT_CHAR);
			mouseOverLine1 = entries[0];
			String mateInfo = entries[1];
			if (mateInfo.contains(Constants.READ_ID_SPLIT_CHAR)) {
				String[] allInfo = mateInfo.split(Constants.READ_ID_SPLIT_CHAR);
				mateInfo = allInfo[0];
			}
			if (mateInfo.contains(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR)) {
				String[] info = mateInfo.split(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR);
				mateInfo = info[0];
			}
			mouseOverLine2 = "Mate: " + mateInfo;
		}
		if (mouseOverLine1.length() >= 20)
		{
			String mouseOverText = mouseOverLine1;
			mouseOverLine1 = new String (mouseOverText.substring(0, 19));
			mouseOverLine1 = mouseOverLine1 + "...";
		}
		
		g.drawString(mouseOverLine1,x+2,yBut + Constants.TEXT_FIELDS_Y_CORRECTION +fH-2);//+2);
		g.drawString(mouseOverLine2,x+2,yBut + Constants.TEXT_FIELDS_Y_CORRECTION +2*fH-2);

		String selObjLine1 = "", selObjLine2 = "";
		if (selObj != null && selObj.id != null) {
			// Reference not selectable (this is spelled correctly) so it should not appear in selected object
			if (selObj.id.equals(Constants.REF_ID_READS_FILE)) {
				selObjLine1 = "";
			} else {
				selObjLine1 = selObj.id;
			}
		} 
		
		if (selObjLine1.contains(Constants.MATE_INFO_SPLIT_CHAR)) {
			String[] entries = selObjLine1.split(Constants.MATE_INFO_SPLIT_CHAR);
			selObjLine1 = entries[0];
			String mateInfo = entries[1];
			if (mateInfo.contains(Constants.READ_ID_SPLIT_CHAR)) {
				String[] allInfo = mateInfo.split(Constants.READ_ID_SPLIT_CHAR);
				mateInfo = allInfo[0];
			}
			if (mateInfo.contains(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR)) {
				String[] info = mateInfo.split(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR);
				mateInfo = info[0];
			}
			selObjLine2 = "Mate: " + mateInfo;
		}
		if (selObjLine1.length() >= 20)
		{
			String selObjText = selObjLine1;
			selObjLine1 = new String (selObjText.substring(0, 19));
			selObjLine1 = selObjLine1 + "...";
		}
		
		g.drawString(selObjLine1, x - wText - betweenBut, yBut + Constants.TEXT_FIELDS_Y_CORRECTION + fH - 2);//+2);
		g.drawString(selObjLine2, x - wText - betweenBut, yBut + Constants.TEXT_FIELDS_Y_CORRECTION + 2*fH - 2);//+2);
		
		// changed from name for testing - I forget why this is here ;)
//		String entry = selObj.name;
//		if (selObj.id != null) {
//			entry = selObj.id;
//		} 
//		//int maxLength = 20;
//		int maxLength = 21;
//		if (entry.length() >= maxLength)
//		{
//			substr2 = new String (entry.substring(0, maxLength - 1));
//			substr2 = substr2  + "...";
//		}
//		else 
//			substr2 = new String (entry);
//		g.drawString(substr2,x-wText-betweenBut,yBut +fH-2);//+2);

		g.setColor(sliderColor);
		g.fill3DRect(x0 + wVis + 1, y0 + hVis + 1, 20, 20, true);
		g.setColor(Color.red);
		
		//This is used to draw settings button near the slider
		g.drawString(" ", x0 + wVis +9, y0 + hVis + fH + Constants.BUTTONS_Y_CORRECTION - 2);//+1);
	}

	public boolean mouseMove(Event evt, int x, int y)
	{
		if(x > x0 && x < x0+wVis) {
			correction = y - fixedHeight;
			if (correction <= 0 && correction > Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {
				stripe = (Stripe) stripeVector.elementAt(1);
				exonCorrection = y - (Constants.PLOT_HEIGHT + Constants.EXON_HEIGHT);
				overObj = stripe.objID(win, x-x0, 10 + exonCorrection);
				//System.out.println("exon " + overObj);
			} else if (correction <= Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {
				//System.out.println("plot");
				overObj = showCoord ? String.valueOf(win.getX(x-x0) + 1) : "None";
			}
		}
		if(stripeMenu.visible) stripeMenu.mouseMoveSynchro(x,y);
		else if(newMapStripeMenu.visible) newMapStripeMenu.mouseMove(x,y);
		else if(miscMenu.visible) miscMenu.mouseMove(x,y);
		else if(customMenu.visible) customMenu.mouseMove(x,y);
		else if(customFontMenu.visible) customFontMenu.mouseMove(x,y);
		else if(fontMenu.visible) fontMenu.mouseMove(x,y);
		else if(moreMenu != null && moreMenu.visible) moreMenu.mouseMove(x,y);
		else if(foundMenu.visible) foundMenu.mouseMove(x,y);
		else if(x > x0 && x < x0+wVis && y > y0 && y < y0+hVis)
		{
			for(curStripe=STRIPES-1;curStripe>=0;curStripe--)
				//for(curStripe=0;curStripe<STRIPES;curStripe++)
				{
					stripe = (Stripe) stripeVector.elementAt(curStripe);
					if(stripe.visible && stripe.inside(win,y-y0))
					{
						// only show info for stripes other than exon if stripe is not within range of exon
						correction = y - fixedHeight;
						if (correction <= 0 && correction > Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {
//							if (curStripe == 1) {
//								//System.out.println("exon");
//							}
						} else if (correction <= Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {
							//System.out.println("plot");
							if (curStripe == 0) {
								overObj = showCoord ? String.valueOf(win.getX(x-x0) + 1) : "None";
							}
						} else {
							if (curStripe > 1) {
								overObj = stripe.objID(win, x-x0, y-y0);
							} 
						}
						
						//This will display the mouse coordinates 
						if(overObj == null)
							overObj = showCoord ? String.valueOf(win.getX(x-x0) + 1) : "None";

						//This will display border around stripes
						for(curStripe=0;curStripe<STRIPES;curStripe++)
						{
							stripe = (Stripe) stripeVector.elementAt(curStripe);
							stripe.setBorder(drawBorder);
							stripe.setShift(shiftText);
						}
						
						if(scaleInput.visible == true || findInput.visible == true) {
							derBrowser.editMenu.setEnabled(false);
							derBrowser.optionsMenu.setEnabled(false);
							derBrowser.helpMenuItem.setEnabled(false);
						}else {
							derBrowser.editMenu.setEnabled(true);
							derBrowser.optionsMenu.setEnabled(true);
							derBrowser.helpMenuItem.setEnabled(true);
						}
						
						if(scaleInput.visible == true || findInput.visible == true) {
							derBrowser1.editMenu.setEnabled(false);
							derBrowser1.optionsMenu.setEnabled(false);
							derBrowser1.helpMenuItem1.setEnabled(false);
						}else {
							derBrowser1.editMenu.setEnabled(true);
							derBrowser1.optionsMenu.setEnabled(true);
							derBrowser1.helpMenuItem1.setEnabled(true);
						}
						
						repaint();

							return true;
					}
				}

			overObj = showCoord ? String.valueOf(win.getX(x-x0)) : "None";

			repaint();
		}

		return true;
	}

	public boolean keyDown(Event evt, int key)
	{
		if(findInput.visible)
		{
			return findInput.keyDown(key);
		}

		else if(scaleInput.visible)
		{
			return scaleInput.keyDown(key);
		}

		else if(newMapInput.visible)
		{
			return newMapInput.keyDown(key);
		}

		return false;
	}


	public boolean mouseDown(Event ev, int x, int y)
	{
		// select exon if location is correct
		if(x > x0 && x < x0+wVis) {
			correction = y - fixedHeight;
			if (correction <= 0 && correction > Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {
				selObj = none;
				stripe = (Stripe) stripeVector.elementAt(1);
				exonCorrection = y - (Constants.PLOT_HEIGHT + Constants.EXON_HEIGHT);
				selObj = stripe.selectObject(win, x-x0, 10 + exonCorrection);
			}
		}
		if (vert.inside(x,y))
		{
			return vert.mouseDown(x,y);
		}

		else if (hori.inside(x,y))
		{
			return hori.mouseDown(x,y);
		}

		else if (zoom.inside(x,y))
		{
			return zoom.mouseDown(x,y);
		}

		else if(stripeMenu.visible)
		{
			return stripeMenu.mouseDown(x,y);
		}

		else if(newMapStripeMenu.visible)
		{
			return newMapStripeMenu.mouseDown(x,y);
		}

		else if(customFontMenu.visible)
		{
			return customFontMenu.mouseDown(x,y);
		}

		else if(customMenu.visible)
		{
			return customMenu.mouseDown(x,y);
		}

		else if(miscMenu.visible)
		{
			return miscMenu.mouseDown(x,y);
		}

		else if(foundMenu.visible)
		{
			return foundMenu.mouseDown(x,y);
		}

		else if(fontMenu.visible)
		{
			return fontMenu.mouseDown(x,y);
		}

		else if(moreMenu != null && moreMenu.visible)
		{
			return moreMenu.mouseDown(x,y);
		}

		else if(findInput.visible)
		{
			return findInput.mouseDown(x,y);
		}

		else if(scaleInput.visible)
		{
			return scaleInput.mouseDown(x,y);
		}

		else if(newMapInput.visible)
		{
			return newMapInput.mouseDown(x,y);
		}

		else if(naPopup.visible)
		{
			return naPopup.mouseDown(x,y);
		}

		/* customFont button --------*/
		else if(x > x0+wVis && y > y0+hVis && y < y0+hVis+20 && x < x0+wVis+20)
		{
			//customMenu.visible = true;
			
			//To disable sidebar menu
			customMenu.visible = false;
			//repaint();
			return true;
		}
		/* display button --------*/
		else if(x > xBut && y > yBut && y < yBut+hBut && x < xBut+wBut)
		{
			stripeMenu.visible = true;
			repaint();
			return true;
		}
		/* about button --------*/
		else if(x > xBut+betweenBut+wBut && y > yBut && y < yBut+hBut && x < xBut+2*wBut+betweenBut && !selObj.equals(none))
		{
//			if(moreVector == null)
//			{
//				naPopup.visible = true;
//				repaint();
//			}
//
//			else
//			{
//				if(moreCalls == 1)
//				{
//					String hmm = parseCall((More)moreVector.elementAt(0));
//					debug(hmm);
//					showDocument(hmm,"DerBrowserMore");
//				}
//
//				else
//				{
//					moreMenu.visible = true;
//					repaint();
//				}
//			}
			if (selObj != null && selObj != none) {
				if (UtilityMethods.isFeature(selObj)) {
					UtilityMethods.openURL(Constants.NCBI_BASE_URL + selObj.name + UtilityMethods.queryStringFromRefSpec(refSpec));
				} else {
					String[] idInfo = selObj.id.split("&");
					String readInfo = "Read Name: " + idInfo[0] + "\n";
//					if (idInfo.length > 1) {
//						readInfo += "Mate: " + idInfo[1] + "\n";
//					}
					System.out.println(readInfo);
//					JOptionPane.showMessageDialog(null,                
//							"No data exists for selected item.",                
//							"No Data Warning.",                                
//							JOptionPane.WARNING_MESSAGE);
				} 
			}

			return true;
		}
		/* key button --------*/
		/* new map button previously, still loads file if run from main --------*/
		else if(x > xBut+2*(betweenBut+wBut) && y > yBut && y < yBut+hBut && x < xBut+3*wBut+betweenBut)
		{
			if(cgi == null)
			{
//				naPopup.visible = true;
//				repaint();
				// This button opens files when using main
				if (enableNewMap) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Load File"); 
	 
					//... Open a file dialog.
					int retval = fileChooser.showOpenDialog(null);
					if (retval == JFileChooser.APPROVE_OPTION) {
						//... The user selected a file, get it, use it.
						File file = fileChooser.getSelectedFile();
						fileName = file.getAbsolutePath();
						clearData();
						frame.setTitle(TITLE + " - " + file.getName());
						init();
					}
				} else {
					if(!keyFrame.isVisible()) {						
						keyFrame.setVisible(true);
					} else {
						if (keyFrame.getState() == java.awt.Frame.ICONIFIED) {
							keyFrame.setState(java.awt.Frame.NORMAL);
						}
						keyFrame.toFront();
						keyFrame.repaint();
					}
				}
			}

			else
			{
				newMapInput.setValue(""+LEFTEND, 0);
				newMapInput.setValue(""+RIGHTEND, 1);
				newMapInput.visible = true;
				//	    naPopup.visible = true;
				repaint();
			}

			return true;
		}
		/* pair button --------*/
		/* formerly find button, find can be accessed in Edit menu */
		else if(x > xBut+3*(betweenBut+wBut) && y > yBut && y < yBut+hBut && x < xBut+4*wBut+betweenBut)
		{
			if (!selObj.equals(none)) {
				//System.out.println(chrIntervalsMatesDiffChrMap);
				if (UtilityMethods.isDataAvailableForObject(selObj)) {
//				if (selObj.color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
					//System.out.println(selObj.id);
					String[] allEntries = selObj.id.split(Constants.READ_ID_SPLIT_CHAR);
					String mateID = "";
					if (allEntries.length > 1) {
						mateID = allEntries[1];
					} else {
//						String[] entries = allEntries[0].split(Constants.MATE_INFO_SPLIT_CHAR);
//						mateID = entries[0].trim();
//						System.out.print(entries[0]);
//						System.out.print("mate " + mateID + " e");
////						if (allEntries[0].contains(Constants.MATE_INFO_SPLIT_CHAR)) {
////							String[] entries = allEntries[0].split(Constants.MATE_INFO_SPLIT_CHAR);
////							mateID = entries[0];
////							System.out.print("mate " + mateID);
////						}
					}
					String objInfo = allEntries[0];
					String[] objInfoEntries = objInfo.split(Constants.MATE_INFO_SPLIT_CHAR);
					String chrIntervalString = objInfoEntries[1];
					String chr = chromosomeDisplayed;
					Interval interval = new Interval(0, 0);
					if (chrIntervalString.contains(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR)) {
						String[] chrIntervalEntries = chrIntervalString.split(Constants.MATE_INFO_ENTRIES_SPLIT_CHAR);  
						chr = chrIntervalEntries[0];
						interval = new Interval(chrIntervalEntries[1]);
					} else {
						interval = new Interval(chrIntervalString);
					}
					//System.out.println(interval);
					int from = interval.start;
					int to = interval.end;
//					System.out.println(from);
//					System.out.println(to);
					
					if (selObj.type.equals(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
					//if (selObj.color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
						intervals = chrIntervalsMatesDiffChrMap.get(chr);
						for (int i = 0; i < intervals.size(); i++) {
							if (UtilityMethods.isIntervalPortionVisible(new Interval(from, to), intervals.get(i))) {
								interval = intervals.get(i);
							}
						}
					} else {
						intervals = chrIntervalsMap.get(chr);
						for (int i = 0; i < intervals.size(); i++) {
							if (UtilityMethods.isIntervalPortionVisible(new Interval(from, to), intervals.get(i))) {
								interval = intervals.get(i);
							}
						}
					}
					
					String description = "";
					if (interval != null) {	
						String filename = dataDirectory + Constants.READS_DIRECTORY + sampleName + "_" + chr + "_" + interval.keyString() + description + ".txt";
						if (filename.length() > 0) {
							System.out.println("filename " + filename);
							File f = new File(filename);					
							if (f.exists()) {
								//System.out.println("exists " + filename);		
								String title = Constants.TITLE_SINGLE_SAMPLE + " - " + f.getName()  + " - " + "Mate of " + objInfoEntries[0];
								if (selObj.type.equals(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
									title = Constants.TITLE_SINGLE_SAMPLE + " - " + mateID + " - " + "Mate of " + objInfoEntries[0] + " in " + f.getName();
								}
//								System.out.println("open frames " + browser1.openFramesMap);
								if (!openFramesMap.containsKey(title)) {
									final derBrowser2 browser2 = new derBrowser2();
									browser2.silent = silent;
									browser2.side = side;
									browser2.selectedPointFrom = selObj.from;
									browser2.dataDirectory = dataDirectory;
									browser2.sampleName = sampleName;
									browser2.chromosomeDisplayed = chr;
									final JFrame browserFrame = new JFrame();
									final JMenuBar menuBar = new JMenuBar();
									
									JMenu editMenu = new JMenu("Edit");
									//editMenu1.setMnemonic(KeyEvent.VK_E);
									editMenu.setOpaque(true);
									
									JMenuItem findItem = new JMenuItem("Find");
									//findItem1.setMnemonic(KeyEvent.VK_F);
									editMenu.add(findItem);

									findItem.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent a) {
											browser2.findInput.visible = true;
											browser2.repaint();
										}    	     
									});
									
									menuBar.add(editMenu);
									
									final JMenu optionsMenu = new JMenu("Options");
									//optionsMenu.setMnemonic(KeyEvent.VK_O);
									optionsMenu.setOpaque(true);
									menuBar.add(optionsMenu);
									//Adding Help menu item to menubar. Also using AboutDialog class to pass object in UtilityMethods function. 
									//menuBar.add(UtilityMethods.helpMenuFunc(aboutDialog));
									
									Container c = browserFrame.getContentPane();
									c.setBackground(Color.black);
									browserFrame.setTitle(title);
									browser2.frameFileName = f.getName();
									browserFrame.setIconImages(icons);
									browserFrame.setJMenuBar(menuBar);
									browserFrame.add(browser2);
									browserFrame.pack();
									browser2.enableNewMap = false;
									browser2.clearData();
									browser2.fileName = filename;
									browser2.refSpec = refSpec;
									browser2.keyFrame = keyFrame;
									browser2.chrIntervalsMap = chrIntervalsMap;
									browser2.chrIntervalsMatesDiffChrMap = chrIntervalsMatesDiffChrMap;
									browser2.init();
									browserFrame.setSize(Constants.READS_WINDOW_FRAME_WIDTH, Constants.READS_WINDOW_FRAME_HEIGHT);
									browserFrame.setResizable(false);
									//browserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
									browserFrame.addWindowListener(new WindowAdapter() {
										public void windowClosing(WindowEvent evt) {
											openReadsBrowsersMap.get(browserFrame.getTitle()).stop();
											openReadsBrowsersMap.get(browserFrame.getTitle()).clearData();
											openReadsBrowsersMap.get(browserFrame.getTitle()).destroy();
											openReadsBrowsersMap.remove(browserFrame.getTitle());
											openFramesMap.remove(browserFrame.getTitle());
											keyFrame.setVisible(false);
											browserFrame.dispose();
										}
									});
									browserFrame.addWindowFocusListener(new WindowAdapter() {
									    //To check window gained focus
									    public void windowGainedFocus(WindowEvent e) {
									        //set flag
									        browser2.isWindowActive = true;
									    }

									    //To check window lost focus
									    public void windowLostFocus(WindowEvent e) {
									        //set flag
									    	browser2.isWindowActive = false;							   
									    }
									});

									Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

									int xpos = (screenSize.width - browserFrame.getSize().width)/2; 
									int ypos = (screenSize.height - browserFrame.getSize().height)/2;

									Random random = new Random();

									int randomX = (int )(Math.random() * Constants.READS_WINDOW_RANDOM_X_RANGE + 1);
									if (random.nextBoolean()) {
										randomX = -randomX;
									}
									int randomY = (int )(Math.random() * Constants.READS_WINDOW_RANDOM_Y_RANGE + 1);
									if (random.nextBoolean()) {
										randomY = -randomY;
									}

									browserFrame.setLocation(xpos + randomX, ypos + randomY);
									openFramesMap.put(browserFrame.getTitle(), browserFrame);
									openReadsBrowsersMap.put(browserFrame.getTitle(), browser2);
									
									Vector list = new Vector();
									
									
									String[] entries = allEntries[0].split(Constants.MATE_INFO_SPLIT_CHAR);
									//System.out.println("entr " + chrIntervalEntries[2]);
									if (selObj.type.equals(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
									//if (selObj.color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
										Stripe st = ((Stripe) browser2.stripeVector.elementAt(browser2.stripeVector.size() - 1));
										list = st.objFind(mateID, list);
									} else {
										Stripe st = ((Stripe) browser2.stripeVector.elementAt(browser2.stripeVector.size() - 3));
										list = st.objFind(entries[0], list);
										//System.out.println("l " + list);
									}
									
									for (int i = 0; i < list.size(); i++) {
										One obj = (One)list.elementAt(i);
										//System.out.println(obj.id);
										if (selObj.type.equals(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
										//if (selObj.color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
											String[] idEntries = obj.id.split(Constants.MATE_INFO_SPLIT_CHAR);
											String id = idEntries[0];
											if (id.equals(mateID)) {
												browser2.selObj = obj;
												//System.out.println("derBrowser sel obj " + browser2.selObj);
												browser2.findAction(browser2.selObj);
											}
										} else {
											String[] idEntries = obj.id.split(Constants.MATE_INFO_SPLIT_CHAR);
											String id = idEntries[0];
											
//											System.out.println("id " + id);
//											System.out.println(entries[0]);
											if (id.equals(entries[0])) {
												browser2.selObj = obj;
												//System.out.println("derBrowser sel obj " + browser2.selObj);
												browser2.findAction(browser2.selObj);
											}
										}
									}
									browserFrame.setVisible(true);
								} else {
									JFrame openFrame = openFramesMap.get(title);
									if (openFrame.getState() == java.awt.Frame.ICONIFIED) {
										openFrame.setState(java.awt.Frame.NORMAL);
									}
									openFrame.toFront();
									openFrame.repaint();
								}
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(null,                
							"No data exists for selected item.",                
							"No Data Warning.",                                
							JOptionPane.WARNING_MESSAGE);
				}
			}
//			findInput.visible = true;
//			repaint();
			
//			findDialog.setTitle(Constants.READS_WINDOW_TITLE + " - " + "Find" + " - " + frameFileName);
//			findDialog.setVisible(true);
			
			return true;
		}
		/* select object */
		else if(x > x0 && x < x0+wVis && y > y0 && y < y0+hVis)	
		{
			winChange = true;
			for(curStripe=STRIPES-1;curStripe>=0;curStripe--)
				//for(curStripe=0;curStripe<STRIPES;curStripe++)
				{
					//System.out.println("**********************************right click1*********************************");
					stripe = (Stripe) stripeVector.elementAt(curStripe);
					if(stripe.visible && stripe.inside(win,y-y0))
					{
						// only select stripes other than exon if stripe is not within range of exon
						correction = y - fixedHeight;
						if (correction <= 0 && correction > Constants.PLOT_HEIGHT - Constants.EXON_Y_POS_CORRECTION_READS - fixedHeight) {

						} else {
							if (curStripe != 1) {
								selObj = stripe.selectObject(win, x-x0, y-y0);
							} 
						}
							
						if(selObj == null) 
							selObj=none;

						if((ev.modifiers & Event.META_MASK) != 0)
						{
							//System.out.println("**********************************right click2*********************************");
							if(selObj==none) {
								win.unsetArea();
							} else {
								win.setArea(selObj);
							}
							//			win.areaVisible = true;
						}

						repaint();

					return true;

					/* right mouse - select area - ??
								                  Two someObj = (Two)(stripe.selectObject(win, x-x0, y-y0), curStripe);
										  if(someObj.one != null && ev.modifiers & Event.META_MASK){ 
											win.setArea(someObj);
											win.areaVisible = true;
										  }
										  else{
											selObj = (someObj == null) ? none : someObj;
										  }
										  repaint();
									          return true;
					 */
				}
			}

			selObj = none;
			repaint();
			return true;
		}

		else return true;

	}


	public boolean mouseDrag(Event ev, int x, int y)
	{
		if (vert.inside(x,y))
		{
			return vert.mouseDrag(x,y);
		}

		if (hori.inside(x,y))
		{
			return hori.mouseDrag(x,y);
		}

		if (zoom.inside(x,y))
		{
			return zoom.mouseDrag(x,y);
		}

		else return true;
	}


	public void MenuAction(int line,String name)
	{
		if(name.equals("newMapStripeMenu"))
		{
			line -= ALLSTRIPES;
			//debug("line "+line+" ST "+STRIPES);
			newMapStripeMenu.visible = false;

			switch(line)
			{
			case 0:
				StringBuffer newTYPES = new StringBuffer(), newOTHERTYPES = new StringBuffer();
				//		newMapStripeMenu.updateSelections();

				for(curStripe=0;curStripe<ALLSTRIPES;curStripe++)
				{
					if(newMapStripeMenu.selectedLine[curStripe])
					{
						newTYPES.append(newMapStripeMenu.line[curStripe]);
						newTYPES.append(',');
					}

					else
					{
						newOTHERTYPES.append(newMapStripeMenu.line[curStripe]);
						newOTHERTYPES.append(',');
					}
				}

				if(newTYPES.length()>0)
					newTYPES.setLength(newTYPES.length()-1);

				if(newOTHERTYPES.length()>0)  
					newOTHERTYPES.setLength(newOTHERTYPES.length()-1);

				TYPES = newTYPES.toString();
				OTHERTYPES = newOTHERTYPES.toString();
				first = true;

				repaint();
				newMap();
				break;

			case 1:
				newMapStripeMenu.restoreSelections();
				repaint();
				break;
			}
		}

		else if(name.equals("stripeMenu"))
		{
			exonPresent = false;
			plotPresent = false;
			refSeqPresent = false;
			line -= STRIPES;
			//debug("line "+line+" ST "+STRIPES);
			switch(line)
			{
			case 0:
				stripeMenu.updateSelections();
//				int curY = 0;
//
//				for(curStripe=0;curStripe<STRIPES;curStripe++)
//				{
//					stripe = (Stripe) stripeVector.elementAt(curStripe);
//					stripe.visible = stripeMenu.selectedLine[curStripe];
//					if (stripe.visible) {
//						if (stripe instanceof Plot) {
//							plotPresent = true;
//						} 
//						if (stripe instanceof Exon) {
//							exonPresent = true;
//							if (plotPresent) {
//								((Exon) stripe).plotPresent = true;
//							} else {
//								((Exon) stripe).plotPresent = false;
//							}
//						} 
//						if (stripe instanceof Sequence && stripe.name.equals(Constants.REF_NAME_READS_FILE)) {
//							refSeqPresent = true;
//							if (plotPresent) {
//								((Sequence) stripe).plotPresent = true;
//							} else {
//								((Sequence) stripe).plotPresent = false;
//							}
//							if (exonPresent) {
//								((Sequence) stripe).exonPresent = true;
//							} else {
//								((Sequence) stripe).exonPresent = false;
//							}
//						}
//					}
//					
//					//debug("curStripe: " + curStripe + " stripe.visible :" + stripe.visible );
//
//					if(stripe.visible)
//					{
//						stripe.setTop(curY);
//						curY += stripe.height;
//					}
//
//					stripe.hilite = false;
//				}
//				
//				scale.setTop(curY);
//
//				/* draw shades of selected stripes */
//				int lastStripe = STRIPES-1;
//				stripe = (Stripe) stripeVector.elementAt(lastStripe);
//				stripe.visible = stripeMenu.selectedLine[lastStripe];
//
//				if (stripe.visible)
//				{	
//					//curY = 0;
//					curY -= SECSTRU*stripe.height; 
//
//					for(curStripe=0;curStripe<STRIPESHADE;curStripe++)
//					{
//						stripe = (Stripe)shadeVector.elementAt(curStripe);
//						stripe.visible = stripeMenu.selectedLine[curStripe];
//
//						if(stripe.visible)
//						{
//							stripe.setTop(curY);
//							curY += stripe.height;
//						}
//
//						stripe.hilite = false;
//					}
//				}
				redrawStripes();
				break;

			case 1:
				if(hiliteOn && STRIPES > 0)
				{
					stripe = (Stripe) stripeVector.elementAt(oldhilite);
					stripe.hilite = false;
				}

				stripeMenu.restoreSelections();
				
				//int curY = 0;
//				curY = 0;
//
//				for(curStripe=0;curStripe<STRIPES;curStripe++)
//				{
//					stripe = (Stripe) stripeVector.elementAt(curStripe);
//					stripe.visible = stripeMenu.selectedLine[curStripe];
//					if (stripe.visible) {
//						if (stripe instanceof Plot) {
//							plotPresent = true;
//						} 
//						if (stripe instanceof Exon) {
//							exonPresent = true;
//							if (plotPresent) {
//								((Exon) stripe).plotPresent = true;
//							} else {
//								((Exon) stripe).plotPresent = false;
//							}
//						} 
//						if (stripe instanceof Sequence && stripe.name.equals(Constants.REF_NAME_READS_FILE)) {
//							refSeqPresent = true;
//							if (plotPresent) {
//								((Sequence) stripe).plotPresent = true;
//							} else {
//								((Sequence) stripe).plotPresent = false;
//							}
//							if (exonPresent) {
//								((Sequence) stripe).exonPresent = true;
//							} else {
//								((Sequence) stripe).exonPresent = false;
//							}
//						}
//					}
//					
//					//debug("curStripe: " + curStripe + " stripe.visible :" + stripe.visible );
//
//					if(stripe.visible)
//					{
//						stripe.setTop(curY);
//						curY += stripe.height;
//					}
//
//					stripe.hilite = false;
//				}
//				
//				scale.setTop(curY);
//
//				/* draw shades of selected stripes */
//				//int lastStripe = STRIPES-1;
//				lastStripe = STRIPES-1;
//				stripe = (Stripe) stripeVector.elementAt(lastStripe);
//				stripe.visible = stripeMenu.selectedLine[lastStripe];
//
//				if (stripe.visible)
//				{	
//					//curY = 0;
//					curY -= SECSTRU*stripe.height; 
//
//					for(curStripe=0;curStripe<STRIPESHADE;curStripe++)
//					{
//						stripe = (Stripe)shadeVector.elementAt(curStripe);
//						stripe.visible = stripeMenu.selectedLine[curStripe];
//
//						if(stripe.visible)
//						{
//							stripe.setTop(curY);
//							curY += stripe.height;
//						}
//
//						stripe.hilite = false;
//					}
//				}
				redrawStripes();
				break;
				
				//break;								

			}

			winChange = true;
			stripeMenu.visible = false;
			// objects visible behind histogram bug occurs here?
			repaint();
		}

		else if(name.equals("miscMenu"))
		{
			switch(line-5)
			{
			case 0:
				miscMenu.updateSelections();
				hiliteOn = miscMenu.selectedLine[0];
				showCoord = miscMenu.selectedLine[2];
				zoomX2 = miscMenu.selectedLine[3];
				shiftText = miscMenu.selectedLine[4];

//				for(curStripe=0;curStripe<STRIPES;curStripe++)
//				{
//					stripe = (Stripe) stripeVector.elementAt(curStripe);
//					stripe.setBorder(miscMenu.selectedLine[1]);
//					stripe.setShift(shiftText);
//				}

				winChange = true;
				break;

			case 1:
				miscMenu.restoreSelections();
				break;

			default:
				break;
			}

			miscMenu.visible = false;
			repaint();
		}

		else if(name.equals("customFontMenu"))
		{
			switch(line)
			{
			case 0:
				scaleFontSelect =  true;
				fontMenu.visible = true;
				break;

			case 1:
				scaleFontSelect =  false;
				fontMenu.visible = true;
				break;

			default:
				break;
			}

			customFontMenu.visible = false;
			repaint();
		}

		else if(name.equals("fontMenu"))
		{
			if(scaleFontSelect)
			{
				scaleF = new Font(FontList[line], Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}

			else
				objF = new Font(FontList[line], Constants.OBJ_FONT_TYPE, Constants.OBJ_FONT_SIZE);

			winChange = true;
			fontMenu.visible = false;
			repaint();
		}

		else if(name.equals("moreMenu"))
		{
			if(line != moreCalls)
			{
				String hmm = parseCall((More)moreVector.elementAt(line));
				debug(hmm);
				if(hmm != null) showDocument(hmm,"DerBrowserMore");
				else naPopup.visible = true;
			}

			moreMenu.visible = false;
			repaint();
		}

		else if(name.equals("customMenu"))
		{
			switch(line)
			{
			case 0:
				customFontMenu.visible = true;
				break;

			case 1:
				scaleInput.visible = true;
				break;

			case 2:
				miscMenu.visible = true;
				break;

			default:
				break;
			}

			customMenu.visible = false;
			repaint();
		}

		else if(name.equals("foundMenu"))
		{
			selObj = (One)foundVector.elementAt(line);
			findAction(selObj);
//			double delta = win.centerAt((selObj.to+selObj.from)/2);
//			scale.makeTicks(offG, win, scaleF);
//			scale.arrangeObjects(scale.top);
//			winChange = true;
//			foundMenu.visible = false;
//			hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
//			repaint();
		}

		return;
	}

	public void findAction(One selObj) {
		double delta = win.centerAt((selObj.to+selObj.from)/2);
		if (mapHeight > hVis) {
			//win.setY(selObj.y - 3);
			win.setY(findScrollYPos(selObj.y));
			//win.setY((int)((mapHeight-hVis)*(1.*selObj.y/SLIDER_RESOLUTION)));
			winChange = true;
		}
		scale.makeTicks(offG, win, scaleF);
		scale.arrangeObjects(scale.top);
		winChange = true;
		foundMenu.visible = false;
		hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
		int vertele = (mapHeight <= hVis) ? (hVis-2*SLIDER_CAP) : (int)((1.*hVis/mapHeight)*(hVis-2*SLIDER_CAP));
		vert.changeEle(vertele, findScrollYPos(selObj.y));
		//vert.changeEle(vertele, selObj.y);
//		System.out.println("sel y " + selObj.y);
//		System.out.println(mapHeight);
//		System.out.println(hVis);
		repaint();
	}
	
	public int findScrollYPos(int y) {
		// if y <= 3 selection rectangle of read when scrolled into position is cut off on top
		int yPos = 0;
		int endPos = mapHeight + 4 - hVis;
		if (y > 3 && y < endPos) {
			yPos = y - 3;
		} else if (y > endPos) {
			yPos = endPos;
		}

		//System.out.println(yPos);
		return yPos;
	}

	public void MenuPosition(int line,String name)
	{
		if(name.equals("stripeMenu"))
		{
			//debug("line "+line+" old "+oldhilite);
			if(hiliteOn && line < STRIPES && line != oldhilite)
			{
				stripe = (Stripe) stripeVector.elementAt(oldhilite);
				stripe.hilite = false;
				stripe = (Stripe) stripeVector.elementAt(line);
				stripe.hilite = true;
				oldhilite = line;	   
				winChange = true;
			}
		}

		repaint();
		return;

	}


	public void SliderValue(int val,String name)
	{
		if(name.equals("vert") && mapHeight>hVis)
		{
			win.setY((int)((mapHeight-hVis)*(1.*val/SLIDER_RESOLUTION)));
			winChange = true;
		} 

		else if(name.equals("hori"))
		{
			win.setX((int)((1/zoo-1)*wVis*val/SLIDER_RESOLUTION));
			winChange = true;
			scale.makeTicks(offG, win, scaleF);
			scale.arrangeObjects(scale.top);
		}

		else if(name.equals("zoom"))
		{
			int maxZoom = range/88;
			double zoomCorr = maxZoom/10.5;
			if (range >= resCorrectionCutoff && range < 1144) {
				zoomCorr = maxZoom/9.3;
			}
			if (range >= 1144 && range < 1232) {
				zoomCorr = maxZoom/9.6;
			}
			if (range > 1319 && range < 1408) {
				zoomCorr = maxZoom/10.9;
			}
			if (range >= 1408) {
				zoomCorr = maxZoom/11.3;
			}
			if (range >= 1584) {
				zoomCorr = maxZoom/11.0;
			}
			
			if (debugZoom) {
				System.out.println("zoomCorr " + zoomCorr);
				System.out.println("range " + range);
				System.out.println("range/88 " + range/88);
			}
			
			if (zoomX2) {
				zooval = 1 << val;
				if (debugZoom) {
					System.out.println("val " + val);
					System.out.println("zooval " + zooval);
					System.out.println("z x2 corr x mult " + zoomX2corr * zoom2Xmultiplier);
				}
							
				if (val <= 2) {
					zooval = (int)Math.ceil(zooval * zoomX2corr * zoom2Xmultiplier);
				} else {
					zooval = (int)(zooval * zoomX2corr * zoom2Xmultiplier);
				}
				if (debugZoom) {
					System.out.println("zooval<=2 " + zooval);
				}
			} else {
				zooval = val+1;
				if (debugZoom) {
					System.out.println("zooval " + zooval);	
				}		
				if (range >= resCorrectionCutoff) {
					zooval1 = val+1;
					if (range > 1300) {
						if (zooval1 > 7)
						{
							val += 1;
							zooval1 += 1;
						}
					}
					if (debugZoom) {
						System.out.println("zooval1 " + zooval1);
					}
					double corr3 = 1.33;
					double corr4 = 1.0;
					if (range > 4000) {
						corr3 = 1.67;
						corr4 = 1.33;
					}
					// smoothes the zoom at low factors for ranges too small for zoomx2 and
					// too large for regular zoom
					if (zooval > 2) {
						if (zooval == 3) {
							if (range > 1140) {
								zooval1*=zoomCorr/corr3;
								zooval = (int)zooval1;
							} else {
								zooval = (int)zooval1;
							}
						} else if (zooval == 4) {
							zooval1*=zoomCorr/corr4;
							zooval = (int)zooval1;
						} else {
							zooval1*=zoomCorr;
							zooval = (int)zooval1;
						}
					}			
				}
				if (debugZoom) {
					System.out.println("zooval after corr " + zooval);
				}
			}
				
			if (zoo == 1.0/zooval) 
				return;
			
			zoo = 1.0/zooval;
			double delta=win.changeScale(zoo);
			winChange = true;
			scale.makeTicks(offG, win, scaleF);
			scale.arrangeObjects(scale.top);
			hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
		}

		else return;
		repaint();
	}


	public void textInput(StringBuffer line[],String name)
	{
		if(name.equals("findInput"))
		{
			if(line[0].length() < 1)
			{
				naPopup.visible = true;
			}

			else
			{
				objSearch(findInput.getValue(0));
			}
		} 

		else if(name.equals("scaleInput"))
		{
			try
			{
				scale.factor = Integer.parseInt(scaleInput.getValue(0));
				scale.makeTicks(offG, win, scaleF);
				scale.arrangeObjects(scale.top);
				winChange = true;
			}

			catch (Exception e)
			{
				debug(""+e);
				scaleInput.visible = true;
			}
		} 

		else if(name.equals("newMapInput"))
		{
			try
			{
				newLEFT = Integer.parseInt(newMapInput.getValue(0));
				newRIGHT = Integer.parseInt(newMapInput.getValue(1));
			}

			catch (Exception e)
			{
				debug(""+e);
				newMapInput.visible = true;
				repaint();
				return;
			}

			if(newLEFT >= newRIGHT)
			{
				newMapInput.visible = true;
			}

			else newMapStripeMenu.visible = true;
		}

		repaint();
		return;
	}


	public void objSearch(String partname)
	{
		foundVector = new Vector();
		for(curStripe=0;curStripe<STRIPES;curStripe++)
		{
			stripe = (Stripe) stripeVector.elementAt(curStripe);
			foundVector = stripe.objFind(partname, foundVector);
		}

		int foundTotal = foundVector.size();

		if(foundTotal == 0)
		{
			naPopup.visible = true;
		}

		else if(foundTotal == 1)
		{
			selObj = (One)foundVector.elementAt(0);
			findAction(selObj);
//			double delta = win.centerAt((selObj.to+selObj.from)/2);
//			scale.makeTicks(offG, win, scaleF);
//			scale.arrangeObjects(scale.top);
//			hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
//			winChange = true;
		}

		else
		{
			boolean selected[] =  new boolean[foundTotal];
			boolean special[] =  new boolean[foundTotal];
			String foundLine[] = new String[foundTotal];

			for(int i=0;i<foundTotal;i++)
			{
				//	     foundLine[i] = (String)foundVector.elementAt(i);
				String id = ((One)foundVector.elementAt(i)).id;
				String name = ((One)foundVector.elementAt(i)).name;
				if (name.contains(Constants.SEQUENCE_PLACEHOLDER))  {
					name = name.replace(Constants.SEQUENCE_PLACEHOLDER, " ");
					name = name.trim();
				}
				if (name.length() > 50) {
					name = name.substring(0, 50) + "...";
				}
				foundLine[i] = id + Constants.SAMPLE_NAME_TYPE_CONNECTOR + name;  
				selected[i] = false;
				special[i] = true;
			}

			foundMenu = new aPopupMenu(foundLine, foundTotal, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "foundMenu");
			foundMenu.centerPopup(offG, x0, y0, wVis, hVis);
			foundMenu.visible = true;
		}

		repaint();

		return;
	}

	public Stripe readStripe(DataInputStream s, Stripe set) throws IOException
	{
		String inputString;
		repaint();

		if((inputString = s.readLine()) != null)
		{ // line 1 id|name
			int next = inputString.indexOf('|');
			set.id = inputString.substring(0, next);
			set.name = inputString.substring(next+1, inputString.length());
		}

		if((inputString = s.readLine()) != null)
		{ // line 2 objs
			try
			{
				set.numObj = Integer.parseInt(inputString);
			}

			catch (Exception e)
			{
				debug("Can't read objects. "+set.name);
			}
		}

		set.piece = new One[set.numObj];

		for(int curpiece=0;curpiece<set.numObj;curpiece++)
		{
			set.piece[curpiece] = new One();
			if((inputString = s.readLine()) != null)
			{ // numbers
				int col = 0;
				for(int i = 0; i < inputString.length(); )
				{
					int next = inputString.indexOf('|', i);

					if(next == -1)
					{
						next = inputString.length();
					}

					switch(col)
					{
					case 0:
						try
						{
							set.piece[curpiece].from = Integer.parseInt(inputString.substring(i, next)); // got number
						}

						catch (Exception e)
						{
							debug("Can't read from piece "+curpiece+" set "+set.name);
						}
						break;

					case 1:
						try
						{
							set.piece[curpiece].to = Integer.parseInt(inputString.substring(i, next)); // got number
						}

						catch (Exception e) 
						{
							debug("Can't read to piece "+curpiece+" set "+set.name);
						}
						break;

					case 2:
						set.piece[curpiece].name = inputString.substring(i, next);
						break;

					case 3:
						set.piece[curpiece].id = inputString.substring(i, next);
						break;

					case 4:
						set.piece[curpiece].color = UtilityMethods.hexColor(inputString.substring(i, next));
						break;

					case 5:
						set.piece[curpiece].type = inputString.substring(i, next);
						break;

					default:
						debug("Too many params piece "+curpiece+" set "+set.name);
						break;
					}

					col++;
					i = next + 1;
				}

			}
			//debug(set.piece[curpiece].toString());
		}

		return set;
	}


	public Stripe readStripeShade(DataInputStream s, Stripe set) throws IOException
	{
		String inputString;
		repaint();

		if((inputString = s.readLine()) != null)
		{ // line 1 id|name
			int next = inputString.indexOf('|');
			set.shadeid = inputString.substring(0, next);
			set.shadename = inputString.substring(next+1, inputString.length());
		}

		if((inputString = s.readLine()) != null)
		{ // line 2 objs
			try
			{
				set.numShade = Integer.parseInt(inputString);
			}

			catch (Exception e)
			{
				debug("Can't read objects. "+set.name);
			}
		}

		set.shade = new One[set.numShade];

		for(int curpiece=0;curpiece<set.numShade;curpiece++)
		{
			set.shade[curpiece] = new One();
			if((inputString = s.readLine()) != null)
			{
				int col = 0;
				for(int i = 0; i < inputString.length(); )
				{
					int next = inputString.indexOf('|', i);

					if(next == -1)
					{
						next = inputString.length();
					}

					switch(col)
					{
					case 0:
						try
						{
							set.shade[curpiece].from = Integer.parseInt(inputString.substring(i, next)); // got number
						}

						catch (Exception e)
						{
							debug("Can't read from shade "+curpiece+" set "+set.shadename);
						}
						break;

					case 1:
						try
						{
							set.shade[curpiece].to = Integer.parseInt(inputString.substring(i, next)); // got number
						}

						catch (Exception e) 
						{
							debug("Can't read to shade "+curpiece+" set "+set.shadename);
						}
						break;

					case 2:
						set.shade[curpiece].name = inputString.substring(i, next);
						break;

					case 3:
						set.shade[curpiece].id = inputString.substring(i, next);
						break;

					case 4:
						set.shade[curpiece].color = UtilityMethods.hexColor(inputString.substring(i, next));
						break;

					case 5:
						set.shade[curpiece].type = inputString.substring(i, next);
						break;

					default:
						debug("Too many params shade "+curpiece+" set "+set.shadename);
						break;
					}

					col++;
					i = next + 1;
				}
			}
		}
		return set;
	}

	// Visit a URL.
	public void showDocument(String u, String name)
	{
		URL go;
		try
		{
			go = new URL(u);
		}

		catch (java.net.MalformedURLException e)
		{
			debug("u "+u+" name "+name);
			go = null;
		};

		if(go != null)
		{
			try
			{ 
				getAppletContext().showDocument(go,name); 
			}

			catch (Exception e)
			{

			};
		}
	}

	// resort stripes.
	public void sortStripes()
	{
		StringTokenizer st = new StringTokenizer(TYPES, ",");
		String next;
		int i = 0, count = -1;

		while(st.hasMoreTokens())
		{
			next =  st.nextToken();
			count++;
			for(i=count;i<STRIPES;i++)
			{
				Stripe cur = (Stripe) stripeVector.elementAt(i);
				if(next.equals(cur.name))
				{
					if(i==count) 
						break;

					else
					{
						Stripe mov = (Stripe) stripeVector.elementAt(count);
						stripeVector.setElementAt(cur, count);
						stripeVector.setElementAt(mov, i);
						break;
					}
				}
			}

			if(i==STRIPES) 
				count--;
		}
	}

	// read and parse file stream
	public boolean fillStripeVector(String file)
	{ 
		DataInputStream s;

		try
		{
			//URL url = new URL(getDocumentBase(), file);
			//s = new DataInputStream(url.openStream());
			s = new DataInputStream(new FileInputStream(file));

			if(s == null) 
				return false;

			parseDerFormat(s);
			s.close();
			return true;
		}

		catch (Exception e)
		{ 
			debug("Can't read file data "+e);
			return false;
		}
	}
	// read and parse cgi stream
//	public boolean fillStripeVector(More source)
//	{ 
//		DataInputStream s;
//
//		try
//		{
//			//dataServer = new browserServio(getDocumentBase().getHost(),port,this);
//			dataServer = new browserServio(getCodeBase().getHost(),port,this);
//			String x = parseCall(source);
//			debug(x);
//			s = dataServer.openServerConnection(x);
//			/*
//							    	"?USERID="+USERID+
//								"&MAPID="+MAPID+
//								"&LEFTEND="+LEFTEND+
//								"&RIGHTEND="+RIGHTEND+
//								"&TYPES="+URLEncoder.encode(TYPES)
//								,source);
//			 */
//			if(s == null) 
//				return false;
//
//			parseDerFormat(s);
//			dataServer.closeServerConnection();
//			return true;
//		}
//
//		catch (Exception e)
//		{ 
//			debug("Can't read cgi data "+e);
//			return false;
//		}
//	}

	public boolean parseDerFormat(DataInputStream s)
	{ 
		String inputString;
		try
		{
//			inputString = s.readLine();
//			inputString = s.readLine();

			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					LEFTEND = Integer.parseInt(inputString);
					if (definedLeft > -1) {
						LEFTEND = definedLeft;
					}
				}

				catch (Exception e) 
				{
					debug("Can't read LEFTEND");
				}
			}

			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					RIGHTEND = Integer.parseInt(inputString);
					if (definedRight > -1) {
						RIGHTEND = definedRight;
					}
				}

				catch (Exception e) 
				{
					debug("Can't read RIGHTEND");
				}
			}

			if(RIGHTEND <= LEFTEND)
			{
				debug("RIGHTEND <= LEFTEND, won't run");
				System.exit(0);
			}

			//********************************************************************************************


			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					PLOT = Integer.parseInt(inputString);
				}
				catch (Exception e)
				{debug("Can't read PLOT");}
			}
			for(curStripe=0;curStripe<PLOT;curStripe++)
			{
				Plot z  = new Plot();
				z = (Plot)readStripe(s, z);
				stripeVector.addElement(z);
			}

			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					PLOTSHADE = Integer.parseInt(inputString);
				}
				catch (Exception e)
				{debug("Can't read PLOTSHADE");}
			}
			for(curStripe=0;curStripe<PLOTSHADE;curStripe++)
			{
				Plot z  = new Plot();
				z = (Plot)readStripeShade(s, z);
				shadeVector.addElement(z);
			}
			
			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					EXON = Integer.parseInt(inputString);
				}
				catch (Exception e)
				{debug("Can't read EXON");}
			}
			for(curStripe=0;curStripe<EXON;curStripe++)
			{
				Exon z  = new Exon();
				z = (Exon)readStripe(s, z);
				stripeVector.addElement(z);
			}
			
			if((inputString = s.readLine()) != null)
				{ 
					try
					{
						EXONSHADE = Integer.parseInt(inputString);
					}
					catch (Exception e)
					{debug("Can't read EXONSHADE");}
				}
				for(curStripe=0;curStripe<EXONSHADE;curStripe++)
				{
					Exon z  = new Exon();
					z = (Exon)readStripeShade(s, z);
					shadeVector.addElement(z);
				}
			
			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					SEQ = Integer.parseInt(inputString);
					//System.out.println("SEQ = " + SEQ);
				}
				catch (Exception e)
				{debug("Can't read SEQ");}
			}
			for(curStripe=0;curStripe<SEQ;curStripe++)
			{
				Sequence z  = new Sequence();
				z = (Sequence)readStripe(s, z);
				stripeVector.addElement(z);
				z.LEFTEND = LEFTEND;
				z.RIGHTEND = RIGHTEND;
			}

			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					SEQSHADE = Integer.parseInt(inputString);
					//System.out.println("SEQSHADE = " + SEQSHADE);
				}
				catch (Exception e)
				{debug("Can't read SEQSHADE");}
			}
			for(curStripe=0;curStripe<SEQSHADE;curStripe++)
			{
				Sequence z  = new Sequence();
				z = (Sequence)readStripeShade(s, z);
				shadeVector.addElement(z);
			}
			
			return true;
		}

		catch (Exception e) 
		{ 
			debug("Can't parse data "+e);
			return false;
		}
	}

	/* create visuals */
	public void initMapVisuals()
	{
		win = new aWindow(wVis, hVis, LEFTEND, RIGHTEND, zoo, !silent); 
		win.name = Constants.READS_WINDOW_TITLE;
		int curY = 0;
		STRIPES = MULTI +  CLONES +  LOCI +  COMP +  PLOT +  EXON +  SEQ +  CONDSEQ +  SECSTRU;
		//STRIPESHADE = MULTISHADE +CLONESHADE +LOCISHADE + COMPSHADE + PLOTSHADE + EXONSHADE + SEQSHADE + SEQSHADE + SECSTRUSHADE;
		STRIPESHADE = SEQSHADE;
		TOTALSTRIPES = STRIPES + STRIPESHADE;
		StringTokenizer st = new StringTokenizer(OTHERTYPES, ",");
		int OTHER = st.countTokens();
		ALLSTRIPES = STRIPES+OTHER;

		boolean selected[] =  new boolean[ALLSTRIPES+ACTIONS];
		boolean special[] =  new boolean[ALLSTRIPES+ACTIONS];
		String stripeName[] = new String[ALLSTRIPES+ACTIONS];
		String shadeName[] = new String[ALLSTRIPES+ACTIONS];

		if(zoom != null)
		{ // reset zoom
			SliderValue(0,"zoom");
			zoom.changeEle(17, 0);
		}

		sortStripes();
		int multiStripeHeight = 0;
		for(curStripe=0;curStripe<STRIPES;curStripe++)
		{
			stripe = (Stripe) stripeVector.elementAt(curStripe);

			if(stripe.visibleName)
			{ 
				stripe.makeTicks(offG, win, objF);
			}

			if (curStripe == 0)
			{
				multiStripeHeight = stripe.height;
			}
			curY = stripe.arrangeObjects(curY);
			stripeName[curStripe] = stripe.name;
			selected[curStripe] = true;
			special[curStripe] = false;
			/*	debug("Stripe "+stripe.name+" has "+stripe.numObj);
							for(curpiece=0;curpiece<stripe.numObj;curpiece++) {
							   debug("Obj "+stripe.piece[curpiece].name+" from "+stripe.piece[curpiece].from+" to "+stripe.piece[curpiece].to+" Y "+stripe.piece[curpiece].y);
							}
			 */
		}
		//multiStripeHeight = curY;
		curY -= SECSTRU*stripe.height;

		//curY = 0;

		for(curStripe=0;curStripe<STRIPESHADE;curStripe++)
		{
			stripe = (Stripe)shadeVector.elementAt(curStripe);

			if(stripe.visibleName)
			{ 
				stripe.makeTicks(offG, win, objF);
			}
			curY = stripe.arrangeShades(curY);
			shadeName[curStripe] = stripe.shadename;
			selected[curStripe] = true;
			special[curStripe] = false;
		}
		/* draw scale */
		//curY = multiStripeHeight;

		scale.makeTicks(offG, win, scaleF);
		mapHeight = scale.arrangeObjects(curY);

		int vertele = (mapHeight <= hVis) ? (hVis-2*SLIDER_CAP) : (int)((1.*hVis/mapHeight)*(hVis-2*SLIDER_CAP));
		if(vert != null)
		{
			vert.changeEle(vertele, 0);
		}

		/* stripe menu */

		for(curStripe=0;curStripe<ACTIONS;curStripe++)
		{
			selected[STRIPES+curStripe] = false;
			special[STRIPES+curStripe] = true;
			stripeName[STRIPES+curStripe] = action[curStripe];
		}

		stripeMenu = new aPopupMenu(stripeName, STRIPES+ACTIONS, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "stripeMenu");
		stripeMenu.centerPopup(offG, x0, y0, wVis, hVis);

		/* new map menu */

		curStripe = 0;
		while(st.hasMoreTokens())
		{
			stripeName[STRIPES+curStripe] =  st.nextToken();
			selected[STRIPES+curStripe] = false;
			special[STRIPES+curStripe] = false;
			curStripe++;
		}

		for(curStripe=0;curStripe<ACTIONS;curStripe++)
		{
			selected[ALLSTRIPES+curStripe] = false;
			special[ALLSTRIPES+curStripe] = true;
			stripeName[ALLSTRIPES+curStripe] = action[curStripe];
		}

		newMapStripeMenu = new aPopupMenu(stripeName, ALLSTRIPES+ACTIONS, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "newMapStripeMenu");
		newMapStripeMenu.centerPopup(offG, x0, y0, wVis, hVis);
	}


	public boolean newMap()
	{ 
		stripeVector = new Vector();
		//connected = fillStripeVector(cgi);
		//    if(file != null) fillStripeVector(file);

		if(connected)
		{
			initMapVisuals();
			first = false;
			winChange = true;
		}

		repaint();
		return true;
	}


	public String parseCall(More x)
	{ 
		StringTokenizer st = new StringTokenizer(x.call, " ");
		StringBuffer result = new StringBuffer();
		String tok;

		for(int count = st.countTokens();count > 0;count-=2)
		{
			result.append(st.nextToken()); // text
			try
			{
				tok =  st.nextToken();  // value
			}
			catch(Exception e) 
			{ break;}

			if(tok.equals("USERID"))
				result.append(x.encode ? URLEncoder.encode(USERID) : USERID);

			else if(tok.equals("MAPID"))
				result.append(x.encode ? URLEncoder.encode(MAPID) : MAPID);

			else if(tok.equals("TYPES"))
				result.append(x.encode ? URLEncoder.encode(TYPES) : TYPES);

			else if(tok.equals("LEFTEND"))
				result.append(String.valueOf(newLEFT));

			else if(tok.equals("RIGHTEND"))
				result.append(String.valueOf(newRIGHT));

			else if(tok.startsWith("OBJID"))
			{
				int field = tok.indexOf('D')+1;
				if(field == tok.length())
					result.append(x.encode ? URLEncoder.encode(selObj.id) : selObj.id);

				else
				{
					String tokid = null;
					try
					{
						int field_val = Integer.parseInt(tok.substring(field, tok.length()));
						int col = 0;
						for(int i = 0; i < selObj.id.length(); )
						{
							int next = selObj.id.indexOf(' ', i);
							if(next == -1)
							{
								next = selObj.id.length();
							}

							if(col == field_val)
							{
								tokid = selObj.id.substring(i, next);
								break;
							}

							col++;
							i = next + 1;
						}	
					}

					catch(Exception e) 
					{ return null;}

					debug(tokid); 

					if(tokid == null || tokid.equals("")) 
						return null;

					result.append(x.encode ? URLEncoder.encode(tokid) : tokid);
				}
			}

			else if(tok.equals("OBJNAME"))
				result.append(x.encode ? URLEncoder.encode(selObj.name) : selObj.name);

		}

		return result.toString();

	}
	
	/**
	 * method called before loading a new file to clear old data
	 */
	public void clearData() {
		stripeVector = new Vector(); 
		shadeVector = new Vector(); 
		foundVector = null; 
		moreVector = null;
		scale = new ScaleFixed();
		scale.silent = silent;
	}
	
	// Find dialog using standard Java instead of Find drawn by Java graphics
	// in derBrowser versions. Each implementation has its pluses and minuses!
//	ActionListener findButtonActionListener = new ActionListener() {
//		public void actionPerformed(ActionEvent prodActionEvent) {
//			objSearch(findDialog.textField.getText());
//			findDialog.setVisible(false);
//			findDialog.dispose();
//		}
//	};
	
//	private void createFindDialog() {
//		final ArrayList<Image> icons = new ArrayList<Image>(); 
//		icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_16).getImage()); 
//		icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_32).getImage());
//
//		findDialog.setTitle(Constants.READS_WINDOW_TITLE + " - " + "Find");
//		findDialog.setIconImages(icons);
//		findDialog.pack();
//		findDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		findDialog.setForeground(Color.lightGray);
//		//findDialog.setFont(new Font("Courier", Font.PLAIN, 14));
//		findDialog.setAlwaysOnTop(true);
//		findDialog.setLocationRelativeTo(null);
//		
//		findDialog.selectButton.addActionListener(findButtonActionListener);
//	}
	
	public int calculateFixedHeight() {
		checkStripeVisibility();
		// plot height is constant. exon stripe can vary in height
		if (plotPresent) {
			fixedHeight = Constants.PLOT_HEIGHT;
			if (refSeqPresent) {
				fixedHeight += Constants.EXON_Y_POS_CORRECTION_READS;
			}
		} else {
			if (exonPresent) {
				fixedHeight = Constants.EXON_Y_POS_CORRECTION_READS;
				stripe = (Stripe) stripeVector.elementAt(1);
				fixedHeight += stripe.height;
			}
		}

		if (exonPresent && plotPresent) {
			fixedHeight = Constants.PLOT_HEIGHT + Constants.EXON_Y_POS_CORRECTION_READS;
			stripe = (Stripe) stripeVector.elementAt(1);
			//System.out.println(stripe.name + " " + stripe.height);
			fixedHeight += stripe.height;
		}
		
		if (!plotPresent) {
			fixedHeight -= 7;
		}
		
		if (!exonPresent && !plotPresent && refSeqPresent) {
			fixedHeight = Constants.REF_SEQ_HEIGHT + Constants.BELOW_REF_SEQ_HEIGHT_CORR;
		}
		return fixedHeight; 
	}
	
	public void checkStripeVisibility() {
		for(curStripe=0;curStripe<STRIPES;curStripe++)
		{
			stripe = (Stripe) stripeVector.elementAt(curStripe);
			if (stripe instanceof Plot) {
				plotPresent = stripe.visible;
			} 
			if (stripe instanceof Exon) {
				exonPresent = stripe.visible;
			} 
			if (stripe instanceof Sequence && stripe.name.equals(Constants.REF_NAME_READS_FILE)) {
				refSeqPresent = stripe.visible;
			}
		}
	}
	
	public void redrawStripes() {
		int curY = 0;

		for(curStripe=0;curStripe<STRIPES;curStripe++)
		{
			stripe = (Stripe) stripeVector.elementAt(curStripe);
			stripe.visible = stripeMenu.selectedLine[curStripe];
			if (stripe.visible) {
				if (stripe instanceof Plot) {
					plotPresent = true;
				} 
				if (stripe instanceof Exon) {
					exonPresent = true;
					if (plotPresent) {
						((Exon) stripe).plotPresent = true;
					} else {
						((Exon) stripe).plotPresent = false;
					}
				} 
				if (stripe instanceof Sequence && stripe.name.equals(Constants.REF_NAME_READS_FILE)) {
					refSeqPresent = true;
					if (plotPresent) {
						((Sequence) stripe).plotPresent = true;
					} else {
						((Sequence) stripe).plotPresent = false;
					}
					if (exonPresent) {
						((Sequence) stripe).exonPresent = true;
					} else {
						((Sequence) stripe).exonPresent = false;
					}
				}
			} 
			
			//debug("curStripe: " + curStripe + " stripe.visible :" + stripe.visible );

			if(stripe.visible)
			{
				stripe.setTop(curY);
				System.out.println(stripe.name + " " + curY);
				curY += stripe.height;
			}

			stripe.hilite = false;
		}
		
		scale.setTop(curY);

		/* draw shades of selected stripes */
		int lastStripe = STRIPES-1;
		stripe = (Stripe) stripeVector.elementAt(lastStripe);
		stripe.visible = stripeMenu.selectedLine[lastStripe];

		if (stripe.visible)
		{	
			//curY = 0;
			curY -= SECSTRU*stripe.height; 

			for(curStripe=0;curStripe<STRIPESHADE;curStripe++)
			{
				stripe = (Stripe)shadeVector.elementAt(curStripe);
				stripe.visible = stripeMenu.selectedLine[curStripe];

				if(stripe.visible)
				{
					stripe.setTop(curY);
					curY += stripe.height;
				}

				stripe.hilite = false;
			}
		}
	}
	
	public static void main(String[] args) { 
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_32).getImage());
		
		frame = new JFrame(); 
		frame.setTitle(Constants.TITLE + " - " + Constants.READS_WINDOW_TITLE);
		frame.setIconImages(icons);
		Container c = frame.getContentPane();
		c.setBackground(Color.black);
		derBrowser2 browser = new derBrowser2();
		frame.add(browser);
		frame.pack();
		browser.init();
		frame.setSize(Constants.READS_WINDOW_FRAME_WIDTH, Constants.READS_WINDOW_FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	} 
	
	class KeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	int key = e.getKeyCode();
				if (key == 27) { // Esc
					keyFrame.setVisible(false);
//					findDialog.setVisible(false);
					stripeMenu.visible = false;
					customMenu.visible = false;
					customFontMenu.visible = false;
					scaleInput.visible = false;
					miscMenu.visible = false;
					findInput.visible = false;
					// Prevents empty character at end of input from Esc key
					if(findInput.inputLine[0].length() > 0) {
						String text = findInput.getValue(0).trim();
						findInput.inputLine[0].setLength(text.length());
					}
					foundMenu.visible = false;
					naPopup.visible = false;
					repaint();
				} 
            } else if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) { 
            	if (isWindowActive) {
            		if (!findInput.visible) {
            			// remove blank character at end from Ctrl key
            			if(findInput.inputLine[0].length() > 0) {					
    						String text = findInput.getValue(0).trim();
    						findInput.inputLine[0].setLength(text.length());				
    					} else {
    						findInput.inputLine[0].setLength(0);
    					}
            		}
					findInput.visible = true;
					repaint();
				}
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                //System.out.println("key release");
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                //System.out.println("key typed");
            }
            return false;
        }
    }
}



