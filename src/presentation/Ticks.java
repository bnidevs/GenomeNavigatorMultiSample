package presentation;

/*
 * @(#)Ticks.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A stripe, whose elements are named sized ticks/flags.<p>
 *
 * Elements are selectable, name positions rearranged before display.
 * Datafile contains fromX|toX|name|ID|color
 * for each element (sorted by fromX/toX).<p>
 *
 * Call makeTicks() before arrangeObjects().
 *
 * @see Stripe
 * @version 	03 Dec 1996
 * @author 	Andrei Grigoriev
 */

public class Ticks extends Stripe{
    /**
     * Array with the basepair to values for each object.
     */
  private int bp_to[] = null;
    /**
     * Array with the name widths in basepair pixels (?).
     */
  private int bp_width[] = null;
    /**
     * The bottom pad value (below baseline).
     */
  private int bottomPad = 5;


    /**
     * Constructs an empty Ticks stripe.
     */

  Ticks(){
    visibleName = true;
  }

    /**
     * Calculates tick X positions/extent. Use after every scale 
     * change followed by arrangeObjects() to minimize space used
     * @see Stripe for parameters
     */

  public void makeTicks(Graphics g, aWindow win, Font font){
    if(bp_to == null) bp_to = new int[numObj];
    if(bp_width == null) bp_width = new int[numObj];

    g.setFont(font);
    FontMetrics fmetrics=g.getFontMetrics(font);
    objHeight = fmetrics.getHeight();
    objPad = 1;

    for(int i=0;i<numObj;i++){
      bp_width[i] = fmetrics.stringWidth(piece[i].name);
      int to = (int)(piece[i].from + bp_width[i]/win.scale);
      /* hack for arrangement, bp_to holds basepair value */
      bp_to[i] = to; 
//System.out.println("wid " + fmetrics.stringWidth(piece[i].name) + "t "+to+" p.to "+bp_to[i]+" tmp " + piece[i].to);
//      if(to > bp_to[i]) bp_to[i] = to;
    }
  }

    /**
     * Arranges names to avoid overlaps, assigning Y positions 
     * relative to top given the current Y position of the stripe. 
     * Returns new current Y position    
     * @param curY current Y position
     */

  public int arrangeObjects(int curY){
    int nextRowIndex, moreObj = numObj, curObj = 0;
    
    setTop(curY);
    curY = objPad;
    if(numObj > 0){
      piece[0].y = curY;
      piece[0].placed = true;
      moreObj--;
    }
    
    while(moreObj > 0){
      nextRowIndex = -1;
      for(int i=curObj;i<numObj;i++){
//System.out.println("p "+bp_to[i]String());
	if(piece[i].placed) continue;
	if(piece[i].from > bp_to[curObj]){
	  piece[i].y = curY;
	  piece[i].placed = true;
	  curObj = i;
	  moreObj--;
	}
	else if(nextRowIndex == -1)
	  nextRowIndex = i;
      }
      if(nextRowIndex > -1){
	curObj = nextRowIndex;
	curY += objHeight + objPad;
	piece[curObj].y = curY;
	piece[curObj].placed = true;
	moreObj--;
//System.out.println("p "+bp_to[curObj]String());
      }
    }
    curY += objHeight + objPad;
    curY += bottomPad;
    height += curY;
/*
    for(int i=0;i<numObj;i++)
      bp_to[i] = piece[i].to;  reverse to original */

    return curY+top;
  }
 
    /**
     * Draws black baseline & ticks, always showing their names 
     * @see Stripe for parameters
     */

  public void draw(Graphics g, aWindow win, Font font, One selected){
    /* only draw inside visible window */
    int offY = top-win.offY;
    if(offY > win.height-1 || offY + height < 1) return; // add sticky

    int baseY = height+objPad+offY-bottomPad;

    g.setColor(Color.black);
    g.drawLine(0,baseY, win.width,baseY);
    g.setFont(font);
    FontMetrics fmetrics=g.getFontMetrics(font);
    int fh = 8; //fmetrics.getHeight()/2 + (shift ? 3 : 0);
//    int fh = fmetrics.getAscent();
// System.out.println("fh "+fh+" asc "+ fmetrics.getAscent() +" masc "+ fmetrics.getMaxAscent() + " desc "+ fmetrics.getDescent() + " mdesc "+ fmetrics.getMaxDescent() + " lead "+fmetrics.getLeading());
    /* only draw inside visible window */

//System.out.println("ws "+win.scale + " wx "+win.offX+ " wy "+offY);
    for(int i=0;i<numObj;i++){
      int from = (int)(win.scale*piece[i].from)-win.offX, to = (int)(win.scale*piece[i].to)-win.offX;
//System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
      if(to < 0 || from >= win.width) continue;
      if(to > win.width) to = win.width;
      if(from < 0) from = 0;
      if(to == from) to = from+1;

//System.out.println("2f "+from + " t "+to+" y "+piece[i].y+" h" + height);

      g.setColor(piece[i].color);
      g.drawLine(from,piece[i].y+offY, from, baseY);
      g.drawLine(from,piece[i].y+offY, to,piece[i].y+offY);
//      g.setColor(Color.black);
      g.drawString(piece[i].name,from+1,piece[i].y+offY+fh);

      if(selected.equals(piece[i])){
	g.setColor(Color.red);
	g.drawRect(from-2,piece[i].y+offY-2,to-from+4,objHeight);
//	g.drawRect(from-2, piece[i].y+offY-2,(int)(win.scale*(bp_to[i]-piece[i].from))+4,objHeight);
      }
    }
  }

    /**
     * Finds a name of an object, containing given point (x,y) 
     * Here - point covered by the visible name 
     * Returns name or <b>null</b>    
     * @param win current window
     * @param x X coordinate
     * @param y Y coordinate
     */

  public String objName(aWindow win, int x, int y) {
    y += win.offY-top;
    x = (int)((x+win.offX)/win.scale);
    for(int i=0;i<numObj;i++){
//System.out.println("x "+x + " y "+y+" from "+piece[i].from+" to "+bp_to[i]);
	if(y > piece[i].y && y < piece[i].y+objHeight && x >= piece[i].from && x  <= piece[i].from+(int)(bp_width[i]/win.scale))
          return (piece[i].name+", "+name);
   }
   return null;
  }

    /**
     * Finds an object, containing given point (x,y) 
     * Here - point covered by the visible name 
     * Returns object or null    
     * @param win current window
     * @param x X coordinate
     * @param y Y coordinate
     */

  public One selectObject(aWindow win, int x, int y) {
    y += win.offY-top;
    x = (int)((x+win.offX)/win.scale);
    for(int i=0;i<numObj;i++){
//System.out.println("x "+x + " bpw "+(int)(bp_to[i]*win.scale)+" from "+piece[i].from+" to "+bp_to[i]);
	if(y > piece[i].y && y < piece[i].y+objHeight && x >= piece[i].from && x  <= piece[i].from+(int)(bp_width[i]/win.scale))
          return (piece[i]);
   }
   return null;
  }

}

