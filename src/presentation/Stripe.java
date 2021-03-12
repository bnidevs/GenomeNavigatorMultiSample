package presentation;

/*
 * @(#)Stripe.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;
import java.util.Vector;

/**
 * A basic stripe class - common variable/method placeholder.<p>
 *
 * Consists of numObj elements, normally read form a datafile but sometimes
 * created dynamically (e.g., Scale). Allows to calculate element positions -
 * makeTicks(), (re)arrange elements - arrangeObjects(), draw them - draw(),
 * find out if a point is covered by an element - objName(), select an element
 * - selectObject(), find a list of elements with matching name - objFind().
 *
 * @see Clone
 * @see Ticks
 * @see Scale
 * @see Multi
 * @see Plot
 * @see Compare
 * @see Connect
 * @version 	03 Dec 1996
 * @author 	Andrei Grigoriev
 */

public class Stripe{
    /**
     * Number of elements.
     */
  int numObj;
    /**
     * Array of elements.
     */
  One piece[];
  
 int numShade;
  
  One shade[];
  
  
    /**
     * Stripe name.
     */
  String name;
  
  String shadename;
  
  
    /**
     * Stripe ID.
     */
  String id;
  
  String shadeid;
    
  String type; 
  
  String shadetype;
  
  /**
   * Stripe top Y coordinate.
   */ 
  int top;
    /**
     * Stripe height.
     */
  int height;
    /**
     * Element height.
     */
  int objHeight;
    /**
     * Vertical pad between elements.
     */
  int objPad;
    /**
     * Element name visibility flag (true for Ticks).
     */
  boolean visibleName = false;
    /**
     * Stripe visibility flag (if false cannot be seen in any window).
     */
  boolean visible = true;
    /**
     * Stripe always visible if true.
     */
  boolean sticky = false;
    /**
     * Stripe background highlight flag.
     */
  boolean hilite = false;
    /**
     * Element border flag.
     */
  boolean border = true;
    /**
     * Text shift flag.
     */
  boolean shift = false;

    /**
     * Arranges names to avoid overlaps (if necessary), assigning Y 
     * positions relative to top given the current Y position of the stripe. 
     * <i>Does nothing in the base Stripe class!</i>    
     * Returns new current Y position    
     * @param curY current Y position
     */
  public int arrangeObjects(int curY){
	return curY;
  }
  
  public int arrangeShades(int curY){
		return curY;
	  }
  
    /**
     * Calculates element X positions/extent. To be called after scale 
     * change followed by arrangeObjects() to minimize space used
     * <i>Does nothing in the base Stripe class!</i>    
     * @param g Graphics
     * @param win current window
     * @param font current font
     * @see aWindow
     */

  public void makeTicks(Graphics g, aWindow win, Font font){
	return;
  }

    /**
     * Draws elements and their names (if possible) plus extras (baseline,
     * etc). Highlights selected element (if one exists).
     * <i>Does nothing in the base Stripe class!</i>    
     * @param g Graphics
     * @param win current window
     * @param font current font
     * @param selected currently selected element
     * @see One
     * @see aWindow
     */

  public void draw(Graphics g, aWindow win, Font font, One selected)
  {
	return;
  }
  
  public void drawShade(Graphics g, aWindow win, Font font, One selected)
  {
	return;
  }


    /**
     * Changes element border flag.
     * @param yes border
     */

  public void setBorder(boolean yes){
	border = yes;
  }

    /**
     * Changes text shift flag.
     * @param yes shift
     */

  public void setShift(boolean yes){
	shift = yes;
  }

    /**
     * Sets stripe absolute top.
     * @param y top
     */

  public void setTop(int y){
	top = y;
  }
  
  public void setShadeTop(int y){
	top = y;
  }
    /**
     * Checks if Y coordinate belongs to the stripe.
     * @param win current window
     * @param y coordinate
     * @see aWindow
     */

  public boolean inside(aWindow win, int y) {
	y += win.offY-top;
	if(y > 0 && y < height)
	   return true;
	else
	   return false;
  }

    /**
     * Finds elements with matching partial name.
     * Returns filled list    
     * @param namepart search string
     * @param list list to fill
     */

  public Vector objFind(String namepart, Vector list) {
    for(int i=0;i<numObj;i++){
	if(piece[i].name.indexOf(namepart) != -1)
			list.addElement(piece[i]);
   }
   return list;
  }
  
  public Vector findFirstObj(Vector list) {
	  list.addElement(piece[0]);
	  return list;
  }

//  public Vector objFindExactMatch(String namepart, Vector list) {
//	  for(int i=0;i<numObj;i++){
//		  if (piece[i].name.equals(namepart)) {
//			  list.addElement(piece[i]);
//		  }
//	  }
//
//	  return list;
//  }

    /**
     * Finds a name of an object, containing given point (x,y) 
     * Returns name or <b>null</b>    
     * @param win current window
     * @param x X coordinate
     * @param y Y coordinate
     */

  public String objName(aWindow win, int x, int y) {
	  if (y > win.height - 25) {
		  return null;
	  } else {
		  y += win.offY-top;
		  x = (int)((x+win.offX+Constants.OVER_OBJ_CORR)/win.scale);
		  for(int i=0;i<numObj;i++){
			  if(y > piece[i].y && y < piece[i].y+objHeight && x > piece[i].from && x  <= piece[i].to)
				  //return (piece[i].name+", "+name);
				  return (piece[i].name);
		  }
	  }
	  return null;
  }
  
  public String objID(aWindow win, int x, int y) {
	  if (y > win.height - 25) {
		  return null;
	  } else {
		  y += win.offY-top;
		  x = (int)((x+win.offX+Constants.OVER_OBJ_CORR)/win.scale);
		  for(int i=0;i<numObj;i++){
			  if(y > piece[i].y && y < piece[i].y+objHeight && x > piece[i].from && x  <= piece[i].to) {
				  return (piece[i].id);
			  } 
		  }
	  }
	  return null;
  }

    /**
     * Finds an object, containing given point (x,y) 
     * Returns object or null    
     * @param win current window
     * @param x X coordinate
     * @param y Y coordinate
     */

  public One selectObject(aWindow win, int x, int y) {
    y += win.offY-top;
    x = (int)((x+win.offX+Constants.OVER_OBJ_CORR)/win.scale);
    for(int i=0;i<numObj;i++)
    {
    	//System.out.println("++++++++++++++x:" + x + "++++++++++++++y:" + y + "++++++++++++++piece[i].x:" + piece[i].x +"++++++++++++++piece[i].y:" + piece[i].y + "++++++++++++++piece[i].from:" + piece[i].from + "++++++++++++++piece[i].to:" + piece[i].to);
		if(y >= piece[i].y && y <= piece[i].y+objHeight && x >= piece[i].from && x  <= piece[i].to)
		{
			One tmp = piece[i];
			if(win.scale >= 2.0)
			{
				tmp.areaFrom = x;
				tmp.areaTo = x + 1;
			}
			else
			{
				tmp.areaFrom = tmp.from;
				tmp.areaTo = tmp.to;
			}
			//System.out.println("**********************************" + tmp.areaFrom + "--------------*******" + tmp.areaTo);
	        return (tmp);
		}
   }
   return null;
  }
  
}
