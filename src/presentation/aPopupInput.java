package presentation;

/*
 * @(#)aPopupInput.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A multiple-line popup input defined by x, y, width, height and text lines.
 *
 * @version 	14 Oct 1996
 * @author 	Andrei Grigoriev
 */
public class aPopupInput extends aPopup {

    /**
     * The active input field bg color of the popup input.
     */
    public Color fieldColor;

    /**
     * The input field text color of the popup input.
     */
    public Color fieldfgColor;

    /**
     * The input lines in the popup input.
     */
    public StringBuffer inputLine[];

    /**
     * The currently selected line in the popup input.
     */
    public int currentLine;

    /**
     * The currently selected line in the popup input.
     */
    public int inputWidth;

    /**
     * Constructs a new popup input.
     */
    public aPopupInput() {
	super();
    }

    /**
     * Constructs and initializes a popup input with the specified parameters.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the popup window
     * @param height the height of the popup window
     * @param width the width of the popup window
     * @param height the height of the popup window
     */
    public aPopupInput(int x, int y, int width, int height, String line[], int lines, int inputWidth, Color fgColor, Color bgColor, Color frColor, Color fieldColor, Color fieldfgColor, boolean shadow, Font font, aBrowser parent, String name) {
      super(x, y, width, height, line, lines, fgColor, bgColor, frColor, shadow, font, parent, name);
      this.inputWidth = inputWidth;
      this.fieldColor = fieldColor;
      this.fieldfgColor = fieldfgColor;
      inputLine = new StringBuffer[lines];
      for(int i=0;i<lines;i++)
	inputLine[i] = new StringBuffer(inputWidth+1);
      addStuff = true;
      stuffWidth = inputWidth;
      stuffPixels = false;
      currentLine = 0;
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
    public aPopupInput(String line[], int lines, int inputWidth, Color fgColor, Color bgColor, Color frColor, Color fieldColor, Color fieldfgColor, boolean shadow, Font font, aBrowser parent, String name) {
      super(line, lines, fgColor, bgColor, frColor, shadow, font, parent, name);
      this.fieldColor = fieldColor;
      this.fieldfgColor = fieldfgColor;
      this.inputWidth = inputWidth;
      inputLine = new StringBuffer[lines];
      for(int i=0;i<lines;i++)
	inputLine[i] = new StringBuffer(inputWidth+1);
      addStuff = true;
      stuffWidth = inputWidth;
      stuffPixels = false;
      currentLine = 0;
    }

    /**
     * Draws the popup input.
     */
    public void draw(Graphics g) {
        super.draw(g);
	int y = rect.y + (paged ? hBut+vspace : 0);
	for(int i=0;i<visLines;i++){
	  y += fh+vspace; //?
	  if(i == currentLine-topLine)
	    g.setColor(Color.white);
	  else
	    g.setColor(fieldColor);
	  g.fill3DRect(rect.x+stuffFrom, y-fh+vspace-1,stuffWidth,fh+vspace-1,false);
	  g.setColor(fieldfgColor);
	  g.drawString(inputLine[i+topLine].toString()+"*",rect.x+stuffFrom,y);
	}
    }	

    /**
     * Handles mouse click inside the popup input.
     */
    public boolean mouseDown(int x, int y) {
      if(!inside(x,y)) return true;
//System.out.println("x "+x+" y "+y);
      if(super.hitAck(x,y)){
	visible = false;
	parent.textInput(inputLine, name);
	return true;
      }
      y -= rect.y+vspace;
/*
      if(paged){
	if(y < hBut) return scrollUp();
	else if(y > rect.height-hBut-vspace) return scrollDn();
	else y -= hBut+vspace;
      }
*/
      y /= fh+vspace;
      if(y < 0 || y > visLines-1) return true;
      currentLine = y+topLine;
      parent.repaint();
      return true;
    }

    public boolean keyDown(int key) {
	switch ((char)key) {
	  case 0x08: // Backsp
	    if(inputLine[currentLine].length() == 0) return true;
	    inputLine[currentLine].setLength(inputLine[currentLine].length()-1);
	    parent.repaint();
	    break;

	  case 0x0A: // Enter
	    visible = false;
	    parent.textInput(inputLine, name);
	    break;

	  default:
		if (inputLine[currentLine].length() < inputWidth-1) {
	  	  inputLine[currentLine].append((char)key); 
//System.out.println("line "+currentLine+":"+inputLine[currentLine]);
	  	  parent.repaint();
	  	  return true;
		}
	    break;
	}
//System.out.println("line "+currentLine+":"+inputLine[currentLine]);
	return true;
    }

    public String getValue(int whatLine) {
	if(inputLine[whatLine].length() < 1) return null;
	return inputLine[whatLine].toString();
    }

    public void setValue(String value, int whatLine) {
	inputLine[whatLine].setLength(0);
	inputLine[whatLine].append(value);
    }
}

