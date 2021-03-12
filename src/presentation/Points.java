package presentation;

/*
 * @(#)Points.java	
 *
 * Based on Multi.java
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A stripe, whose elements are named rectangles (many in one line).
 * <p>
 * 
 * Elements are selectable, NOT rearranged before display. Datafile contains
 * fromX|toX|name|ID|color for each element.
 * 
 * @see Stripe
 * @version 03 Dec 1996
 * @author Andrei Grigoriev
 * @author James Kelley
 */

public class Points extends Stripe
{
	public ArrayList<String> samplesList = new ArrayList<String>();
	//private ArrayList<Integer> zeroPValueFromList = new ArrayList<Integer>();
	int pointHeight = Constants.POINT_SIZE;
	
	Points()
	{
		objHeight = 10;
		objPad = Constants.POINTS_OBJ_PAD;
	}

	/**
	 * Constructs a Multi stripe.
	 * 
	 * @param pieces
	 *            number of elements
	 * @param name
	 *            stripe name
	 */

	Points(int pieces, String name)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		this.name = name;
	}
	
	/**
	 * Arranges rectangles, assigning Y positions relative to top given the
	 * current Y position of the stripe. Returns new current Y position
	 * 
	 * @param curY
	 *            current Y position
	 */

	public int arrangeObjects(int curY)
	{
		int nextRowIndex, moreObj = numObj, curObj = 0;
		int max = 37;
//		HashMap<String, Integer> collisionPosMap = new HashMap<String, Integer>();
//		int numCollisions = 0;
		
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
				//System.out.println("p "+piece[i].toString());
				if (piece[i].placed) {
					//continue;
				} else if (nextRowIndex == -1) {
					nextRowIndex = i;
				}
			}
			
			if (nextRowIndex > -1)
			{
				curObj = nextRowIndex;
				double offset = Double.parseDouble(piece[curObj].id);
				// add points below line
				if (offset == max) {
					samplesList.add(piece[curObj].name);
				}
//				boolean collision = false;
//				if (offset == 1) {
//					for (int i = 0; i < zeroPValueFromList.size(); i++) {
//						if (piece[curObj].from > zeroPValueFromList.get(i) - 10 && 
//								piece[curObj].from < zeroPValueFromList.get(i) + 10) {
//							//System.out.println(piece[curObj].from);
//						//if (piece[curObj].from == zeroPValueFromList.get(i)) {
//							collision = true;
//							String fromPos = Integer.toString(piece[curObj].from%10);
//							if (collisionPosMap.containsKey(fromPos)) {
//								numCollisions = collisionPosMap.get(fromPos);
//								numCollisions += 1;
//							} else {
//								numCollisions = 1;
//							}
//							collisionPosMap.put(fromPos, numCollisions);
//						} 
//					}
//					if (!collision) {
//						zeroPValueFromList.add(piece[curObj].from);
//					} 
//				}
				if (offset > 1) {
					curY = (int) (offset*(objHeight + objPad));
				} else {
					curY = objPad + Constants.ZERO_P_VALUE_Y_OFFSET;
					piece[curObj].color = Color.gray;
//					if (collision) {
//						//curY += (objHeight);
//						curY += (pointHeight + 2)*numCollisions;
//					}
				}
				
				piece[curObj].y = curY;
				piece[curObj].placed = true;
				moreObj--;
				// System.out.println("p "+piece[curObj].toString());
			}
		}
		curY = (max + 1)*(objHeight + objPad);
		height = curY;
		return curY + top;
	}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* only draw inside visible window */
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return; // add sticky

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint( RenderingHints.  KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint( RenderingHints.  KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
		
		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = 8; // fmetrics.getHeight()/2 + (shift ? 3 : 0);
		// int fh = fmetrics.getAscent();

		// System.out.println("ws "+win.scale + " wx "+win.offX+ " wy "+offY);
		for (int i = 0; i < numObj; i++)
		{
			piece[i].to = piece[i].from + (int) (10/win.scale);
			int from = (int) (win.scale * piece[i].from) - win.offX;
			int to = (int) (win.scale * piece[i].to) - win.offX;
			// System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
			
			if (selected.equals(piece[i]) && samplesList.contains(piece[i].name))
			{
				g2d.setColor(new Color(128, 0, 0));
				if (i%2 == 0) {	
					int from1 = (int) (win.scale * piece[i + 1].from) - win.offX;
					if (from1 > 0 && from1 < win.width)
					g2d.drawOval(from1 - 3, piece[i + 1].y + offY - 3, pointHeight + 5, pointHeight + 5);
					//g.drawOval(from1 - 2, piece[i + 1].y + offY - 2, objHeight + 3, objHeight + 3);
				} else if (i%2 == 1) {
					int from1 = (int) (win.scale * piece[i - 1].from) - win.offX;
					if (from1 > 0 && from1 < win.width)
					g2d.drawOval(from1 - 3, piece[i - 1].y + offY - 3, pointHeight + 5, pointHeight + 5);
					//g.drawOval(from1 - 2, piece[i - 1].y + offY - 2, objHeight + 3, objHeight + 3);
				}
			}
			
			if (to <= 0 || from >= win.width)
				continue;
//			if (to > win.width)
//				to = win.width;
//			if (from < 0)
//				from = 0;
//			if (to == from)
//				to = from + 1;
//			int len = to - from;

			if (samplesList.contains(piece[i].name)) {
				if (piece[i].color != null) {
					if (piece[i].color.equals(Color.white))
					{ // draw black
						g2d.setColor(Color.black);
						g2d.drawOval(from, piece[i].y + offY, pointHeight - 1, pointHeight - 1);
						//g.drawOval(from, piece[i].y + offY, objHeight - 1, objHeight - 1);
					}
					else
					{ // fill own
						g2d.setColor(piece[i].color);
						g2d.fillOval(from, piece[i].y + offY, pointHeight, pointHeight);
						//g.fillOval(from, piece[i].y + offY, objHeight, objHeight);
						if (border)
						{ // draw border
							g2d.setColor(Color.black);
							double offset = Double.parseDouble(piece[i].id);
							// draw oval with no border, actually ovals with no border look jagged
							// but drawing a border with the same color as the oval smooths this out
							if (offset == 1) {
								g2d.setColor(piece[i].color);
							}
							g2d.drawOval(from, piece[i].y + offY, pointHeight - 1, pointHeight - 1);
							//g.drawOval(from, piece[i].y + offY, objHeight - 1, objHeight - 1);
						}
					}
//					int tlen = fmetrics.stringWidth(piece[i].name);
//					if (tlen < len)
//					{
//						g.setColor(piece[i].color.equals(Color.black) ? Color.white : Color.black);
//						g.drawString(piece[i].name, from + (len - tlen) / 2, piece[i].y + offY + fh);
//					}

					if (selected.equals(piece[i]))
					{
						g2d.setColor(Color.red);
						g2d.drawOval(from - 3, piece[i].y + offY - 3, pointHeight + 5, pointHeight + 5);
						//g.drawOval(from - 2, piece[i].y + offY - 2, objHeight + 3, objHeight + 3);
					}
				}
			}
		}
	}
	
	public One selectObject(aWindow win, int x, int y) {
	    y += win.offY-top;
	    x = (int)((x+win.offX)/win.scale);
//	    System.out.println(x);
//	    System.out.println(win.scale);
	    int radius = pointHeight/2;
	    int range = (int) ((pointHeight + 1)/win.scale);
	    for(int i=0;i<numObj;i++)
	    {
	    	//System.out.println("++++++++++++++x:" + x + "++++++++++++++y:" + y + "++++++++++++++piece[i].x:" + piece[i].x +"++++++++++++++piece[i].y:" + piece[i].y + "++++++++++++++piece[i].from:" + piece[i].from + "++++++++++++++piece[i].to:" + piece[i].to);
			if (samplesList.contains(piece[i].name)) {
				if(y >= piece[i].y - radius && y <= piece[i].y+objHeight && x >= piece[i].from - range && x <= piece[i].from + range)
				//if(y >= piece[i].y && y <= piece[i].y+objHeight && x >= piece[i].from && x <= piece[i].to)
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
	   }
	   return null;
	  }
	
	/**
     * Finds a name of an object, containing given point (x,y) 
     * Returns name or <b>null</b>    
     * @param win current window
     * @param x X coordinate
     * @param y Y coordinate
     */

  public String objName(aWindow win, int x, int y) {
    y += win.offY-top;
    x = (int)((x+win.offX)/win.scale);
    
    int radius = pointHeight/2;
    int range = (int) ((pointHeight + 1)/win.scale);
    
    for(int i=0;i<numObj;i++){
    	if(y >= piece[i].y - radius && y <= piece[i].y+objHeight && x >= piece[i].from - range && x <= piece[i].from + range)
          return (piece[i].name);
   }
   return null;
  }

}

