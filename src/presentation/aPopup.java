package presentation;

/*
 * @(#)aPopup.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A popup window defined by x, y, width, height and text lines.
 *
 * @version 	14 Oct 1996
 * @author 	Andrei Grigoriev
 */
public class aPopup {

    /**
     * The parent browser.
     */
    public aBrowser parent;

    /**
     * The name of the popup menu.
     */
    public String name;

    /**
     * The rectangle of the popup window.
     */
    protected Rectangle rect;

    /**
     * The fg color of the popup window.
     */
    public Color fgColor;

    /**
     * The bg color of the popup window.
     */
    public Color bgColor;

    /**
     * The frame color of the popup window.
     */
    public Color frColor;

    /**
     * The shadow flag of the popup window.
     */
    public boolean shadow;

    /**
     * The font of the popup window.
     */
    public Font font;

    /**
     * The text lines in the popup window.
     */
    public String line[];

    /**
     * The number of text lines in the popup window.
     */
    public int lines;

    /**
     * The text pixel offset - X
     */
    public int hspace=3;

    /**
     * The text pixel offset - Y and spacing between lines
     */
    public int vspace=2;

    /**
     * The visibility flag of the popup window.
     */
    public boolean visible = false;

    /**
     * The font height
     */
    protected int fh;

    /**
     * The top text line in the popup menu (after PgUp).
     */
    protected int topLine = 0;

    /**
     * The number of visible text lines in the popup menu (save PgUp/Dn).
     */
    protected int visLines;

    /**
     * The text input mode flag of the popup menu.
     */
    protected boolean addStuff = false;

    /**
     * The pixel flag of the input width.
     */
    protected boolean stuffPixels = false;

    /**
     * The text input width of the popup menu.
     */
    protected int stuffWidth = 0;

    /**
     * The text input width of the popup menu.
     */
    protected int stuffFrom = 0;

    /**
     * The page mode flag of the popup menu.
     */
    protected boolean paged = false;

    /**
     * PgUp/Dn text.
     */
    protected String page[] = {"PgUp", "PgDn"};

    /**
     * PgUp/Dn button coords.
     */
    protected int hBut = 0, wBut = 0, yButTop = 0, yButBot = 0, xBut = 0, xTextBut = 0;

    /**
     * Acknowledge button coords.
     */
    private int wAck = 0, yAck = 0, xAck = 0;

    /**
     * Acknowledge button text.
     */
    private String Ack = null;

    /**
     * Acknowledge button flag.
     */
    private boolean hasAck = false;

    /**
     * Constructs a new popup window.
     */
    public aPopup() {
    }

    /**
     * Constructs and initializes a popup window with the specified parameters.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the popup window
     * @param height the height of the popup window
     * @param width the width of the popup window
     * @param height the height of the popup window
     */
    public aPopup(int x, int y, int width, int height, String line[], int lines, Color fgColor, Color bgColor, Color frColor, boolean shadow, Font font, aBrowser parent, String name) {
	rect = new Rectangle(x, y, width, height);
	this.parent = parent;
	this.name = name;
	this.lines = lines;
	this.fgColor = fgColor;
	this.bgColor = bgColor;
	this.frColor = frColor;
	this.font = font;
	this.shadow = shadow;
	this.line = new String[lines];
	for(int i=0;i<lines;i++) this.line[i] = line[i];
	visLines=lines;
    }

    /**
     * Constructs and initializes a popup window with the specified parameters.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the popup window
     * @param height the height of the popup window
     * @param width the width of the popup window
     * @param height the height of the popup window
     */
    public aPopup(String line[], int lines, Color fgColor, Color bgColor, Color frColor, boolean shadow, Font font, aBrowser parent, String name) {
	rect = new Rectangle();
	this.parent = parent;
	this.name = name;
	this.lines = lines;
	this.fgColor = fgColor;
	this.bgColor = bgColor;
	this.frColor = frColor;
	this.font = font;
	this.shadow = shadow;
	this.line = new String[lines];
	for(int i=0;i<lines;i++)
	   this.line[i] = line[i];
	visLines=lines;
    }

    /**
     * Places the popup window in the center of the rect area
     * after calculating its own size. Makes pages & buttons
     * PgUp/PgDn if necessary (height < window height)
     * @param g graphics
     * @param x left
     * @param y top
     * @param width the width of the area
     * @param height the height of the area
     */
    public void centerPopup(Graphics g, int x, int y, int width, int height){
      FontMetrics fm = g.getFontMetrics(font);
      fh = fm.getHeight();
      if(lines*(fh+vspace)+2*vspace > height-2*vspace){ // get page size
	paged = true;
	hBut = fh;
	visLines=(height-2*vspace-(hBut+vspace)*2)/(fh+vspace);
      }
      Dimension d = fitToText(fm);
      resize(d.width,d.height);
      move(x+(width-d.width)/2,y+(height-d.height)/2);
      if(paged){
	wBut = rect.width-2*hspace;
	yButTop = rect.y+vspace;
	yButBot = rect.y+rect.height-vspace-hBut;
	xBut = rect.x+hspace;
	xTextBut = xBut+(wBut-fm.stringWidth(page[0]))/2;
      }
//System.out.println("rec "+this);
    }

    /**
     * Places the popup window in the center of the rect area
     * after calculating its own size. Makes pages & buttons
     * PgUp/PgDn if necessary (height < window height). Adds
     * Acknowledge button
     * @param g graphics
     * @param x left
     * @param y top
     * @param width width of the area
     * @param height height of the area
     * @param text Acknowledge button text
     */
    public void centerPopup(Graphics g, int x, int y, int width, int height, String text){
      FontMetrics fm = g.getFontMetrics(font);
      fh = fm.getHeight();
      hasAck = true;
      Ack = text;
      hBut = fh;
      if(lines*(fh+vspace)+2*vspace > height-2*vspace-(hBut+vspace)*2){ // get page size
	paged = true;
	visLines=(height-2*vspace-(hBut+vspace)*3)/(fh+vspace);
      }
      Dimension d = fitToText(fm);
      d.height += hBut+vspace;
      resize(d.width,d.height);
      move(x+(width-d.width)/2,y+(height-d.height)/2);
      if(paged){
	wBut = rect.width-2*hspace;
	yButTop = rect.y+vspace;
	yButBot = rect.y+rect.height-vspace-hBut;
	xBut = rect.x+hspace;
	xTextBut = xBut+(wBut-fm.stringWidth(page[0]))/2;
      }
      wAck = fm.stringWidth(text)+4;
      xAck = rect.x + (rect.width - wAck)/2;
      yAck = rect.y+rect.height-vspace-hBut -(paged ? vspace+hBut : 0);
    }

    /**
     * Calculates & sets the width and height of the popup window
     */
    private Dimension fitToText(FontMetrics fm){
      int w, wmax = 0;
      for(int i=0;i<lines;i++){
	w = fm.stringWidth(line[i]);
	if(w > wmax) wmax = w;
      }
      if(addStuff){
	stuffFrom = wmax+2*hspace;
	if(!stuffPixels){
	  int tmpwidth = stuffWidth;
	  stuffWidth = 0;
	  for(int i=0;i<tmpwidth;i++)
	    stuffWidth += fm.stringWidth("W");
        }
	wmax += hspace + stuffWidth;
      } 
      Dimension d = new Dimension(wmax+2*hspace,visLines*(fh+vspace)+2*vspace+ (paged ? 2*(hBut+vspace) : 0));
      return d;
    }

    /**
     * Moves the popup window.
     */
    public void move(int x, int y) {
	rect.move(x, y);
    }	

    /**
     * Sets spacing for the popup window text.
     */
    public void spacing(int hspace, int vspace) {
	this.hspace = hspace;
	this.vspace = vspace;
    }	

    /**
     * Resizes the popup window.
     */
    public void resize(int width, int height) {
	rect.resize(width, height);
    }	

    /**
     * Checks if the specified point lies inside a popup window.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public boolean inside(int x, int y) {
	return rect.inside(x, y);
    }

    /**
     * Returns the String representation of this popup window's values.
     */
    public String toString() {
	return getClass().getName() + rect.toString();
    }

    /**
     * Draws the popup window.
     */
    public void draw(Graphics g) {
	drawFrame(g);
	g.setColor(fgColor);
	for(int i=0;i<lines;i++)
	  g.drawString(line[i],rect.x+hspace,rect.y+(i+1)*(fh+vspace));
	if(hasAck){
	   g.setColor(bgColor);
           g.fill3DRect(xAck,yAck, wAck, hBut, true);
	   g.setColor(fgColor);
	   g.drawString(Ack,xAck+2,yAck+fh-4);	
	}	
    }	

    /**
     * Draws the popup window frame/buttons.
     */
    public void drawFrame(Graphics g) {
	if(shadow){
	  g.setColor(Color.black);
	  g.fillRect(rect.x+3,rect.y+3,rect.width,rect.height);
	}
	g.setColor(frColor);
	g.drawRect(rect.x,rect.y,rect.width,rect.height);
	g.setColor(bgColor);
	g.fillRect(rect.x,rect.y,rect.width,rect.height);
	g.setFont(font);
	if(paged){
	   g.setColor(bgColor);
           g.fill3DRect(xBut,yButTop, wBut, hBut, true);
           g.fill3DRect(xBut,yButBot, wBut, hBut, true);
	   g.setColor(fgColor);
	   g.drawString(page[0],xTextBut,yButTop+fh-4);	
	   g.drawString(page[1],xTextBut,yButBot+fh-4);
	}	
   }

    /**
     * Scrolls the popup window text up
     */
    protected boolean scrollUp(){
	topLine -= visLines;
	if(topLine < 0) topLine=0;
	parent.repaint();
	return true;
    }

    /**
     * Scrolls the popup window text down
     */
    protected boolean scrollDn(){
	topLine += visLines;
	if(topLine+visLines > lines) topLine=lines-visLines;
	parent.repaint();
	return true;
    }

    /**
     * Handles mouse click inside the popup window.
     */
    public boolean mouseDown(int x, int y) {
      if(!inside(x,y)) return true;
      if(hitAck(x,y)){
	visible = false;
	parent.repaint();
	return true;
      }
      if(paged){
	if(y < hBut) return scrollUp();
	else if(y > rect.height-hBut-vspace) return scrollDn();
      }
      return true;
    }

    protected boolean hitAck(int x, int y) {
//System.out.println("x "+x+" y "+y+" xA "+xAck+" yA "+yAck);
      return (y > yAck && y < yAck+hBut && x > xAck && x < xAck+wAck);
    }
}

