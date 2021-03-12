package presentation;

/*
 * @(#)aPopupMenu.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A multiple-choice popup menu defined by x, y, width, height and text lines.
 *
 * @version 	14 Oct 1996
 * @author 	Andrei Grigoriev
 */
public class aPopupMenu extends aPopup {

    /**
     * The selection tick color of the popup menu.
     */
    public Color tickColor;

    /**
     * The selected lines in the popup menu.
     */
    public boolean selectedLine[];

    /**
     * The originally selected lines in the popup menu.
     */
    private boolean selectedLineOrig[];

    /**
     * The special action lines in the popup menu.
     */
    public boolean specialLine[];

    /**
     * The currently selected line in the popup menu.
     */
    public int currentLine;

    /**
     * The tick pixel offset - X
     */
    public int tickspace=3;

    /**
     * Constructs a new popup menu.
     */
    public aPopupMenu() {
	super();
    }

    /**
     * Constructs and initializes a popup menu with the specified parameters.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the popup window
     * @param height the height of the popup window
     * @param width the width of the popup window
     * @param height the height of the popup window
     */
    public aPopupMenu(int x, int y, int width, int height, String line[], int lines, boolean selectedLine[], boolean specialLine[], Color fgColor, Color bgColor, Color frColor, Color tickColor, boolean shadow, Font font, aBrowser parent, String name) {
      super(x, y, width, height, line, lines, fgColor, bgColor, frColor, shadow, font, parent, name);
      this.tickColor = tickColor;
      this.selectedLine = new boolean[lines];
      System.arraycopy(selectedLine, 0, this.selectedLine, 0, lines);
      this.selectedLineOrig = new boolean[lines];
      System.arraycopy(selectedLine, 0, selectedLineOrig, 0, lines);
      this.specialLine = new boolean[lines];
      System.arraycopy(specialLine, 0, this.specialLine, 0, lines);
      spacing(hspace*3+tickspace,vspace);
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
    public aPopupMenu(String line[], int lines, boolean selectedLine[], boolean specialLine[], Color fgColor, Color bgColor, Color frColor, Color tickColor, boolean shadow, Font font, aBrowser parent, String name) {
      super(line, lines, fgColor, bgColor, frColor, shadow, font, parent, name);
      this.tickColor = tickColor;
      this.selectedLine = new boolean[lines];
      System.arraycopy(selectedLine, 0, this.selectedLine, 0, lines);
      this.selectedLineOrig = new boolean[lines];
      System.arraycopy(selectedLine, 0, selectedLineOrig, 0, lines);
      this.specialLine = new boolean[lines];
      System.arraycopy(specialLine, 0, this.specialLine, 0, lines);
      spacing(hspace*3+tickspace,vspace);
    }

    /**
     * Updates menu selections.
     */
    public void updateSelections() {
	System.arraycopy(selectedLine, 0, selectedLineOrig, 0, lines);
	return;
    }

    /**
     * Restores menu selections.
     */
    public void restoreSelections() {
	System.arraycopy(selectedLineOrig, 0, selectedLine, 0, lines);
	return;
    }

    /**
     * Draws the popup menu.
     */
    public void draw(Graphics g) {
        super.drawFrame(g);
	int y = rect.y + (paged ? hBut+vspace : 0);
	for(int i=0;i<visLines;i++){
	  y += fh+vspace; //?
	  if(i == currentLine-topLine){
	    g.setColor(fgColor);
	    g.fillRect(rect.x,y-fh+vspace,rect.width,fh+vspace);
	  }
	  if(selectedLine[i+topLine] && !specialLine[i+topLine]){
	    g.setColor(tickColor);
	    g.drawLine(rect.x+2*tickspace,y-2,rect.x+3*tickspace,y-8);
	    g.drawLine(rect.x+2*tickspace,y-2,rect.x+tickspace,y-5);
	    g.drawLine(rect.x+2*tickspace,y-3,rect.x+3*tickspace,y-9);
	    g.drawLine(rect.x+2*tickspace,y-3,rect.x+tickspace,y-6);
	  }
	  if(i == currentLine-topLine)
	    g.setColor(bgColor);
	  else
	    g.setColor(fgColor);
	  g.drawString(line[i+topLine],rect.x+5*tickspace,y);
	}
    }	

    /**
     * Handles mouse click inside the popup menu.
     */
    public boolean mouseDown(int x, int y) {
      if(!inside(x,y)) return true;
      y -= rect.y+vspace;
      if(paged){
	if(y < hBut) return scrollUp();
	else if(y > rect.height-hBut-vspace) return scrollDn();
	else y -= hBut+vspace;
      }
      y /= fh+vspace;
      if(y < 0 || y > visLines-1) return true;
      currentLine = y+topLine;
      if(specialLine[y+topLine]){
	parent.MenuAction(y+topLine,name);
	return true;
      }
      selectedLine[y+topLine] = !selectedLine[y+topLine];
      parent.repaint();
      return true;
    }

    /**
     * Handles mouse move inside the popup menu.
     */
    public boolean mouseMove(int x, int y) {
      if(!inside(x,y)) return true;
      y -= rect.y+vspace;
      if(paged){
	if(y < hBut || y > rect.height-hBut-vspace){
	   return true;
	}
	else y -= hBut+vspace;
      }
      y /= fh+vspace;
      if(y < 0 || y > visLines-1) return true;
      currentLine = y+topLine;
      parent.repaint();
      return true;
    }

    /**
     * Handles mouse move inside the popup menu, notifying the parent.
     */
    public boolean mouseMoveSynchro(int x, int y) {
      if(!inside(x,y)) return true;
      y -= rect.y+vspace;
      if(paged){
	if(y < hBut || y > rect.height-hBut-vspace){
	   return true;
	}
	else y -= hBut+vspace;
      }
      y /= fh+vspace;
      if(y < 0 || y > visLines-1) return true;
      currentLine = y+topLine;
      parent.MenuPosition(y+topLine, name);
      return true;
    }
}

