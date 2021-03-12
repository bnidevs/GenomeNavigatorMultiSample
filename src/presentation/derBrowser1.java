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

public class derBrowser1 extends aBrowser
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean silent = false;
	
	public String dataDirectory = "";
	public String refSpec = "";
	public String frameFileName = "";
	
	private derBrowser2 browser2 = new derBrowser2();
	//private JFrame browserFrame = null;
	public JFrame keyFrame = null;
	public JFrame keyFrameReads = null;
	
	public boolean isWindowActive = false;
	
	public HashMap<String, JFrame> openFramesMap = new HashMap<String, JFrame>();
	public HashMap<String, derBrowser2> openReadsBrowsersMap = new HashMap<String, derBrowser2>();
	
	public ArrayList<String> samplesList = new ArrayList<String>();
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = new LinkedHashMap<String, ArrayList<Interval>>();
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMatesDiffChrMap = new LinkedHashMap<String, ArrayList<Interval>>();
	
	public String chromosomeDisplayed = "";
	public boolean enableNewMap = true;
	
	public ArrayList<Interval> readsIntervals = new ArrayList<Interval>();
	public int mergedIntervalStart = -1;
	public int mergedIntervalEnd = -1;
	public LinkedHashMap<String, String> speciesColorMap = null;
	
	static JFrame frame;
	static String TITLE = "DerBrowser";
	
	public static boolean optionsMenuValue;
	String fileName;
	Image offI, winI, pic;
	Graphics offG, winG;
	boolean first = true, connected = true;
	
	AboutDialog aboutDialog = new AboutDialog();
	
	//private FindDialog findDialog = new FindDialog();
	
	//We do not need browserServio class for Genome Navigator 
	//browserServio dataServer = null;

	/* sliders */

	aSlider vert, hori, zoom;

	final static int SLIDER_RESOLUTION = 2000;
	final static int SLIDER_W = 21, SLIDER_CAP = 17;
	int wText = 145, wZoom = 145;
	int x0 = 5, y0 = 5, wVis, hVis;
	int xText, yText;
	int xZoom, hBut=20, wBut=68, xBut = x0, yBut, betweenBut = 6;
	Color sliderColor = UtilityMethods.hexColor("666666");
	Color hiliteColor = UtilityMethods.hexColor("FFDCDC");

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

	String objBut =  "READS";
	//String objBut =  "ABOUT";
	String selBut =  "DISPLAY";
	String newBut =  "KEY";
	//String newBut =  "NEW MAP";
	String findBut =  "FIND";

	aPopupMenu stripeMenu, newMapStripeMenu, customFontMenu, customMenu, miscMenu, fontMenu, foundMenu = new aPopupMenu(), moreMenu;
	aPopupInput findInput, scaleInput, newMapInput;
	aPopup naPopup;

	String overObj = "None"; 
	One selObj, none;

	/* contents */

	int LEFTEND=0, RIGHTEND=0, OBJECTID;
	int newLEFT=0, newRIGHT=0;

	String USERID="", MAPID= "", TYPES= "", OTHERTYPES= "";

	More cgi = null;
	int moreCalls = 0;
	int port = 80;

	Stripe stripe;
	ScalePoints scale = new ScalePoints();
	Vector stripeVector = new Vector(), shadeVector = new Vector(), foundVector, moreVector = null;

	int MULTI, MULTISHADE, CLONES, CLONESHADE, LOCI, LOCISHADE, COMP, COMPSHADE, PLOT, PLOTSHADE, EXON, EXONSHADE, SEQ, SEQSHADE, CONDSEQ, CONDSEQSHADE, SECSTRU, SECSTRUSHADE, 
	STRIPES, STRIPESHADE, TOTALSTRIPES, ALLSTRIPES, ACTIONS = 2, curStripe, curpiece, oldhilite = 0;

	/* window */

	aWindow win;
	double zoo = 1.;
	int width, height, mapHeight, zooval=1;

	String FontList[];

	public static Font objF = new Font(Constants.OBJ_FONT, Constants.OBJ_FONT_TYPE, Constants.OBJ_FONT_SIZE);    
	public static Font scaleF = new Font(Constants.SCALE_FONT, Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);   
	public static Font menuF = new Font(Constants.MENU_FONT, Constants.MENU_FONT_TYPE, Constants.MENU_FONT_SIZE);     

	boolean winChange = true;
	public static boolean hiliteOn = true, zoomX2 = false, shiftText = false, showCoord = true, drawBorder = true;
	boolean scaleFontSelect = true;
	boolean DEBUG = true;
	
	public boolean showReadsMatesDiffChr = true;

	public static JMenu editMenu = new JMenu("Edit");
	public static JMenu optionsMenu = new JMenu("Options");
	public static Color uiBackgroundColor = Constants.UI_BACKGROUND_COLOR;
	public static Color uiForegroundColor = Constants.UI_FOREGROUND_COLOR;
	
	public static JMenu helpMenuItem1 = new JMenu();
	
	public derBrowser1() {
		fileName = "etc/data1.txt";
		//createFindDialog();
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
		
		width = Constants.P_VALUE_WINDOW_WIDTH;
		height = Constants.P_VALUE_WINDOW_HEIGHT;
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

		for (int i = 0; i < customFontItems; i++)
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

		for (int i = 0; i < fontItems; i++)
		{
			selected[i] = false;
			special[i] = true;
		}

		fontMenu = new aPopupMenu(FontList, fontItems, selected, special, Color.lightGray/*black*/, sliderColor/*Color.lightGray*/, Color.black, Color.red, false, menuF, this, "fontMenu");
		fontMenu.centerPopup(offG, x0, y0, wVis, hVis);

		if (moreCalls > 1)
		{
			selected = new boolean[moreCalls+1];
			special = new boolean[moreCalls+1];
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
		int width = RIGHTEND - LEFTEND;
		int resolution = 5;
		zoomX2 = true;
		if (width < 4000) {
			zoomX2 = false;
			resolution = (int) 8*width/800;
			if (resolution > 14) {
				resolution = 14;
			} else if (resolution < 5) {
				resolution = 5;
			}
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
				stripe = (Stripe) shadeVector.elementAt(curStripe);
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

		for (curStripe = 0; curStripe < STRIPES; curStripe++)
		{
			stripe = (Stripe) stripeVector.elementAt(curStripe);
			//debug("The name of the stripe is " + stripe.name);
			if (stripe.visible)
			{
				if (hiliteOn && stripe.hilite)
				{
					g.setColor(hiliteColor);
					g.fillRect(0, stripe.top - win.offY, win.width, stripe.height + 1);
				}
				stripe.draw(g, win, objF, selObj);
			}
		}

		scale.draw(g, win, scaleF);
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

		if(winChange)
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
		g.fill3DRect(x, yBut, wBut, hBut, true);
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

		g.fill3DRect(x,yBut, wText, hBut, false);
		g.fill3DRect(x-wText-betweenBut,yBut, wText, hBut, false);

		g.setColor(Color.black);
		String substr1 = ""; 
		String substr2 = "";

		if (overObj != null) {
			if (overObj.length() >= 20)
			{
				substr1 = new String (overObj.substring(0, 19));
				substr1 = substr1  + "...";
			}
			else 
				substr1 = new String (overObj);
			g.drawString(substr1, x + 2, yBut + Constants.TEXT_FIELDS_Y_CORRECTION + fH - 2);//+2);
		}
		

		if (selObj != null) {
			if (selObj.name.length() >= 20)
			{
				substr2 = new String (selObj.name.substring(0, 19));
				substr2 = substr2  + "...";
			}
			else 
				substr2 = new String (selObj.name);
			g.drawString(substr2, x - wText - betweenBut, yBut + Constants.TEXT_FIELDS_Y_CORRECTION +fH - 2);//+2);
		}
		

		g.setColor(sliderColor);
		g.fill3DRect(x0 + wVis + 1, y0 + hVis + 1, 20, 20, true);
		g.setColor(Color.red);
		
		//This is used to draw settings button near the slider
		g.drawString(" ", x0 + wVis +9, y0 + hVis + fH + Constants.BUTTONS_Y_CORRECTION - 2);//+1);

	}

	public boolean mouseMove(Event evt, int x, int y)
	{
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
			for(curStripe=0;curStripe<STRIPES;curStripe++)
			{
				stripe = (Stripe) stripeVector.elementAt(curStripe);
				if(stripe.visible && stripe.inside(win,y-y0))
				{
					overObj = stripe.objName(win, x-x0, y-y0);

					if(overObj == null)
						overObj = showCoord ? String.valueOf(win.getX(x-x0)) : "None";

						repaint();

						return true;
				}
			}

			//This will display the mouse coordinates 
			overObj = showCoord ? String.valueOf(win.getX(x-x0)) : "None";
			//This will display border around stripes
			for(curStripe=0;curStripe<STRIPES;curStripe++)
			{
				stripe = (Stripe) stripeVector.elementAt(curStripe);
				stripe.setBorder(drawBorder);
				stripe.setShift(shiftText);
			}
			
			if(scaleInput.visible == true || findInput.visible == true) {
				DataTableFrame.optionsMenu1.setEnabled(false);
				DataTableFrame.editMenu1.setEnabled(false);
			}else {
				DataTableFrame.optionsMenu1.setEnabled(true);
				DataTableFrame.editMenu1.setEnabled(true);
			}

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
		if(x > xBut+betweenBut+wBut && y > yBut && y < yBut+hBut && x < xBut+2*wBut+betweenBut && selObj.equals(none)) {
			JOptionPane.showMessageDialog(null,                
					"Please select an object to view reads.",                
					"No Selected Object Warning.",                                
					JOptionPane.WARNING_MESSAGE);
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
		/* reads button --------*/
		/* about button previously--------*/
		else if(x > xBut+betweenBut+wBut && y > yBut && y < yBut+hBut && x < xBut+2*wBut+betweenBut && !selObj.equals(none))
		{
			if (!selObj.equals(none)) {	
				//System.out.println(readsIntervals);
				boolean checkInterval = true;
				Interval interval = null;
				// x position for points below scale is meaningless since SV is not present.
				// Merged interval start and end are used.
				
				int midpoint = (RIGHTEND - LEFTEND)/2;
				int side = 0; 
				if (selObj.from > midpoint) {
					side = 1;
				}
				
				if (selObj.y > Constants.SCALE_POINTS_Y_POS) {
					if (mergedIntervalStart > -1 && mergedIntervalEnd > -1) {
						interval = new Interval(mergedIntervalStart, mergedIntervalEnd);
						checkInterval = false;
					}
				}
				
				if (checkInterval) {
					int from = selObj.from;
					int to = Integer.parseInt(selObj.type);
					if (Integer.parseInt(selObj.type) < selObj.from) {
						from = Integer.parseInt(selObj.type);
						to = selObj.from;
					}
					for (int i = 0; i < readsIntervals.size(); i++) {
						if (UtilityMethods.isIntervalPortionVisible(new Interval(from, to), readsIntervals.get(i))) {
							interval = readsIntervals.get(i);
						}
					}
				}
				
				if (interval != null) {
					String description = "";
					if (!showReadsMatesDiffChr) {
						description = Constants.MATES_SAME_CHR_DESCRIPTION;
					}
					if (selObj.y != Constants.POINTS_OBJ_PAD + Constants.ZERO_P_VALUE_Y_OFFSET) {
						String filename = dataDirectory + Constants.READS_DIRECTORY + selObj.name + "_" + chromosomeDisplayed + "_" + interval.keyString() + description + ".txt";
						
						if (filename.length() > 0) {
							File f = new File(filename);
							// System.out.println("derBrowser1 filename " + filename);
							if (f.exists()) {
								//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
								final ArrayList<Image> icons = new ArrayList<Image>(); 
								icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_16).getImage()); 
								icons.add(new ImageIcon(Constants.READS_WINDOW_IMAGE_PATH_32).getImage());
								
								//setting aboutDialog
								aboutDialog.setIconImages(icons);					
								aboutDialog.setSize(400, 180);
								aboutDialog.setResizable(false);
								aboutDialog.setLocationRelativeTo(null);		
								aboutDialog.setVisible(false);	
								aboutDialog.setModal(true);
								aboutDialog.licenseButton.addActionListener(UtilityMethods.licenseButtonActionListener);
								
								aboutDialog.addWindowListener(new WindowAdapter() {
									public void windowClosing(WindowEvent evt) {
										aboutDialog.setVisible(false);	
									}
								});
								
								String title = Constants.TITLE + " - " + Constants.READS_WINDOW_TITLE + " - " + f.getName();
//								System.out.println("open frames br1 " + openFramesMap);
//								System.out.println(title);
								if (!openFramesMap.containsKey(title)) {
									browser2 = new derBrowser2();
									browser2.silent = silent;
									browser2.side = side;
									browser2.selectedPointFrom = selObj.from;
									browser2.chrIntervalsMatesDiffChrMap = chrIntervalsMatesDiffChrMap;
									String sampleName = selObj.name;
									browser2.dataDirectory = dataDirectory;
									browser2.sampleName = UtilityMethods.getSampleNameFromVarName(sampleName);
									browser2.chromosomeDisplayed = chromosomeDisplayed;
									final JFrame browserFrame = new JFrame();
									final JMenuBar menuBar = new JMenuBar();
									
									//JMenu editMenu = new JMenu("Edit");
									//editMenu1.setMnemonic(KeyEvent.VK_E);
									editMenu.setOpaque(true);
									editMenu.setForeground(uiForegroundColor);
									editMenu.setBackground(uiBackgroundColor);
									
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
									
									//optionsMenu.setMnemonic(KeyEvent.VK_O);
									optionsMenu.setOpaque(true);
									optionsMenu.setForeground(uiForegroundColor);
									optionsMenu.setBackground(uiBackgroundColor);
									menuBar.add(optionsMenu);
									
//									JMenuItem scaleUnitsMenu = new JMenuItem("Scale Units");
//									scaleUnitsMenu.addActionListener(new ActionListener() {
//										
//										@Override
//										public void actionPerformed(ActionEvent e) {
//											// TODO Auto-generated method stub
//											browser2.scaleInput.visible = true;
//											browser2.repaint();
//										}
//									});
									
									optionsMenu.add(UtilityMethods.selectFontsMenuFuncBrowser2());
									//optionsMenu.add(scaleUnitsMenu);
									optionsMenu.add(UtilityMethods.miscOptionsMenuFuncBrowser2(browser2));
									
									//Adding Help menu item to menubar. Also using AboutDialog class to pass object in UtilityMethods function. 
									JMenu helpMenuItemTemp1 = UtilityMethods.helpMenuFunc(aboutDialog);
									helpMenuItem1 = helpMenuItemTemp1;
									menuBar.add(helpMenuItem1);
									
									Container c = browserFrame.getContentPane();
									c.setBackground(Color.black);
									browserFrame.setTitle(title);
									browser2.frameFileName = f.getName();
									browserFrame.setJMenuBar(menuBar);
									browserFrame.setIconImages(icons);
									browserFrame.add(browser2);
									browserFrame.pack();
									browser2.enableNewMap = false;
									browser2.clearData();
									browser2.fileName = filename;
									browser2.refSpec = refSpec;
									browser2.keyFrame = keyFrameReads;
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
											browser2.keyFrame.setVisible(false);
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
									//System.out.println(openFramesMap);

									browserFrame.setVisible(true);
								} else {
									JFrame openFrame = openFramesMap.get(title);
									if (openFrame.getState() == java.awt.Frame.ICONIFIED) {
										openFrame.setState(java.awt.Frame.NORMAL);
									}
									openFrame.toFront();
									openFrame.repaint();
								}
							} else {
								JOptionPane.showMessageDialog(null,                
										"No data exists for selected item.",                
										"No Data Warning.",                                
										JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null,                
								"<html>The point selected may correspond to multiple samples.<br>"
								+ "Please use the " + Constants.VARIANTS_WINDOW_TITLE +
								" window.</html>",               
								"Zero P-value point selection error.",                                
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null,                
							"No data exists for selected item.",                
							"No Data Warning.",                                
							JOptionPane.WARNING_MESSAGE);
//					System.out.println("derBrowser1 reads intervals " + readsIntervals);
//					System.out.println("derBrowser1 error matching interval " + selObj.from + "-" + selObj.to);
				}
			} else {
				JOptionPane.showMessageDialog(null,                
						"Please select an object to view reads.",                
						"No Selected Object Warning.",                                
						JOptionPane.WARNING_MESSAGE);
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
		/* find button --------*/
		else if(x > xBut+3*(betweenBut+wBut) && y > yBut && y < yBut+hBut && x < xBut+4*wBut+betweenBut)
		{
			findInput.visible = true;
			repaint();
			
//			findDialog.setVisible(true);
			
			return true;
		}
		/* select object */
		else if(x > x0 && x < x0+wVis && y > y0 && y < y0+hVis)
		{
			winChange = true;
			for(curStripe=0;curStripe<STRIPES;curStripe++)
			{
				//System.out.println("**********************************right click1*********************************");
				stripe = (Stripe) stripeVector.elementAt(curStripe);
				if(stripe.visible && stripe.inside(win,y-y0))
				{
					selObj = stripe.selectObject(win, x-x0, y-y0);
					if(selObj == null) 
						selObj=none;

					if((ev.modifiers & Event.META_MASK) != 0)
					{
						//System.out.println("**********************************right click2*********************************");
						if(selObj==none)
							win.unsetArea();
						else 
							win.setArea(selObj);
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
			line -= STRIPES;
			//debug("line "+line+" ST "+STRIPES);
			switch(line)
			{
			case 0:
				stripeMenu.updateSelections();
				int curY = 0;

				for(curStripe=0;curStripe<STRIPES;curStripe++)
				{
					stripe = (Stripe) stripeVector.elementAt(curStripe);
					stripe.visible = stripeMenu.selectedLine[curStripe];

					//debug("curStripe: " + curStripe + " stripe.visible :" + stripe.visible );

					if(stripe.visible)
					{
						stripe.setTop(curY);
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
				break;

			case 1:
				if(hiliteOn && STRIPES > 0)
				{
					stripe = (Stripe) stripeVector.elementAt(oldhilite);
					stripe.hilite = false;
				}

				stripeMenu.restoreSelections();
				break;								

			}

			winChange = true;
			stripeMenu.visible = false;
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

				for(curStripe=0;curStripe<STRIPES;curStripe++)
				{
					stripe = (Stripe) stripeVector.elementAt(curStripe);
					stripe.setBorder(miscMenu.selectedLine[1]);
					stripe.setShift(shiftText);
				}

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
//			scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
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
		scale.makeTicks(offG, win, scaleF);
		scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
		scale.arrangeObjects(scale.top);
		winChange = true;
		foundMenu.visible = false;
		hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
		repaint();
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
			scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
			scale.arrangeObjects(scale.top);
		}

		else if(name.equals("zoom"))
		{
			if(zoomX2)
				zooval = 1 << val;

			else
				zooval = val+1;

			if(zoo == 1.0/zooval) 
				return;

			zoo = 1.0/zooval;
			double delta=win.changeScale(zoo);
			winChange = true;
			scale.makeTicks(offG, win, scaleF);
			scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
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
				scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
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

		else if(foundTotal == 2)
		{
			selObj = (One)foundVector.elementAt(0);
			double delta = win.centerAt((selObj.to+selObj.from)/2);
			scale.makeTicks(offG, win, scaleF);
			scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
			scale.arrangeObjects(scale.top);
			hori.changeEle((int)((wVis-2*SLIDER_CAP)*zoo), delta);
			winChange = true;
		}

		else
		{
			boolean selected[] =  new boolean[foundTotal];
			boolean special[] =  new boolean[foundTotal];
			String foundLine[] = new String[foundTotal];

			for(int i=0;i<foundTotal;i++)
			{
				//	     foundLine[i] = (String)foundVector.elementAt(i);   
				foundLine[i] = ((One)foundVector.elementAt(i)).name + " " + 
						((One)foundVector.elementAt(i)).from;   
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
//	public void fillStripeVector(More source)
//	{ 
//		DataInputStream s;
//
//		try
//		{
//			//dataServer = new browserServio(getDocumentBase().getHost(),port,this);
//			//dataServer = new browserServio(getCodeBase().getHost(),port,this);
//			String x = parseCall(source);
//			debug(x);
//			//s = dataServer.openServerConnection(x);
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
//			//dataServer.closeServerConnection();
//			return true;
//		}
//
//		catch (Exception e)
//		{ 
//			debug("Can't read cgi data "+e);
//			//return false;
//		}
//	}

	public boolean parseDerFormat(DataInputStream s)
	{ 
		String inputString;
		try
		{
			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					LEFTEND = Integer.parseInt(inputString);
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
					CLONES = Integer.parseInt(inputString);
				}
				catch (Exception e)
				{debug("Can't read CLONES");}
			}
			for(curStripe=0;curStripe<CLONES;curStripe++)
			{
//				Clones z = new Clones();
//				z = (Clones)readStripe(s, z);
//				stripeVector.addElement(z);
				Points z = new Points();
				z.samplesList = samplesList;
				z = (Points)readStripe(s, z);
				stripeVector.addElement(z);
			}

			if((inputString = s.readLine()) != null)
			{ 
				try
				{
					CLONESHADE = Integer.parseInt(inputString);
				}
				catch (Exception e)
				{debug("Can't read CLONESHADE");}
			}
			for(curStripe=0;curStripe<CLONESHADE;curStripe++)
			{
				Points z = new Points();
				z = (Points)readStripeShade(s, z);
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
		win.name = Constants.P_VALUE_WINDOW_TITLE;
		int curY = 0;
		STRIPES = MULTI +  CLONES +  LOCI +  COMP +  PLOT +  EXON +  SEQ +  CONDSEQ +  SECSTRU;
		STRIPESHADE = MULTISHADE +CLONESHADE +LOCISHADE + COMPSHADE + PLOTSHADE + EXONSHADE + SEQSHADE + SEQSHADE + SECSTRUSHADE;
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
		scale.makeYTicks(offG, win, scaleF, (LEFTEND + RIGHTEND)/2);
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
		scale = new ScalePoints();
		scale.silent = silent;
	}

	/*
	 * This code was an experiment to use Java components to replace the Find dialog
	 * in derBrowser. This is currently not used.
	 * 
	 */
	
//	ActionListener findButtonActionListener = new ActionListener() {
//		public void actionPerformed(ActionEvent prodActionEvent) {
//			objSearch(findDialog.textField.getText());
//			findDialog.setVisible(false);
//			findDialog.dispose();
//		}
//	};
	
//	private void createFindDialog() {
//		final ArrayList<Image> icons = new ArrayList<Image>(); 
//		icons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_16).getImage()); 
//		icons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_32).getImage());
//
//		findDialog.setTitle(Constants.P_VALUE_WINDOW_TITLE + " - " + "Find");
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
	
	public void updateOpenFramesMap(String title, JFrame frame) {
		openFramesMap.put(title, frame);
		//System.out.println("update " + openFramesMap);
	}
	
	public static void main(String[] args) { 
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_32).getImage());
		
		
		
		frame = new JFrame(); 
		frame.setTitle(Constants.TITLE + " - " + Constants.P_VALUE_WINDOW_TITLE);
		frame.setIconImages(icons);
		Container c = frame.getContentPane();
		c.setBackground(Color.black);
		derBrowser1 browser = new derBrowser1();
		frame.add(browser);
		
		frame.pack();

		browser.init();
		frame.setSize(Constants.P_VALUE_WINDOW_FRAME_WIDTH, Constants.P_VALUE_WINDOW_FRAME_HEIGHT);
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

