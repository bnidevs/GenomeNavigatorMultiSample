package presentation;

/*
 * @(#)Connect.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A stripe, whose elements connect pairs of map positions.<p>
 *
 * Simplified version of Compare class. Elements unnamed and
 * non-selectable. Datafile contains topX|bottomX coordinate
 * pairs and color for each element.
 *
 * @see Stripe
 * @version 	03 Dec 1996
 * @author 	Andrei Grigoriev
 */

public class Connect extends Stripe{
  private int tip = 5;

    /**
     * Constructs an empty Connect stripe.
     */

  Connect(){
    objHeight = 25; objPad = 3;
  }

    /**
     * Constructs a Connect stripe.
     * @param pieces number of elements
     * @param name stripe name
     */

  Connect(int pieces, String name){
    this();
    numObj = pieces;
    piece = new One[numObj];
    this.name = name;
  }

    /**
     * Arranges connections, assigning Y positions 
     * relative to top given the current Y position of the stripe. 
     * Returns new current Y position    
     * @param curY current Y position
     */

  public int arrangeObjects(int curY){
    setTop(curY);
    curY = objPad;
    for(int i=0;i<numObj;i++)
    {
    	piece[i].y = curY;
    	//System.out.println("curY**********" + curY);
    	curY += tip;
    	//System.out.println("curY:" + curY);
    }
    curY += objHeight + objPad + numObj * tip;
    height = curY;
    //System.out.println("HEIGHT**********" + height);
    return curY+top;
  }

    /**
     * Draws connecting diagonals 
     * @see Stripe for parameters
     */

  public void draw(Graphics g, aWindow win, Font font, One selected){
    /* only draw inside visible window */
    int offY = top-win.offY;
    if(offY > win.height-1 || offY + height < 1) return; // add sticky

    for(int i=0;i<numObj;i++){
      int from = (int)(win.scale*piece[i].from)-win.offX, to = (int)(win.scale*piece[i].to)-win.offX;
//System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
//      if(to&from <= 0 || to&from >= win.width) continue; ?? 
//      int len = to-from;

      g.setColor(piece[i].color);
      g.drawLine(from,piece[0].y+offY, from,piece[i].y+offY + objHeight + i* tip);
      g.drawLine(to,piece[0].y+offY, to,piece[i].y+offY + objHeight + i* tip);
      g.drawLine(from,piece[i].y+offY +objHeight + i* tip,to,piece[i].y+offY +objHeight + i* tip);
/*
      if(selected.equals(piece[i])){
	g.setColor(Color.red);
	g.drawRect(from-2,piece[i].y+offY-2,len+3,objHeight+3);
      }
*/
    }
  }

    /**
     * Overrides inside() to avoid object selection 
     * @see Stripe
     */

  public boolean inside(aWindow win, int y) {
	   return false;
  }
}

