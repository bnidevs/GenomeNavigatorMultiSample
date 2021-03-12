package presentation;

/*
 * @(#)Scale.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A stripe, whose elements are named scale ticks.
 * 
 * Elements are non-selectable, calculated/arranged before display. No datafile,
 * produces numObj ticks per window. for each element (sorted by fromX/toX).
 * <p>
 * 
 * Call makeTicks() before arrangeObjects().
 * 
 * @see Stripe
 * @version 03 Dec 1996
 * @author Andrei Grigoriev
 * @author James Kelley
 */

public class ScaleFixed extends Stripe
{
	/**
	 * Scale tick factor.
	 */
	public int factor = 1000;
	private boolean off = false;
	public boolean silent = false;

	/**
	 * Constructs a Scale stripe with 6 empty elements.
	 */
	ScaleFixed()
	{
		numObj = 6;
		sticky = true;
		piece = new One[numObj];
		for (int i = 0; i < numObj; i++)
			piece[i] = new One();
	}

	/**
	 * Calculates tick X positions and assigns tick names/values
	 * 
	 * @see Stripe for parameters
	 */

	public void makeTicks(Graphics g, aWindow win, Font font)
	{
		if (factor == 0)
		{
			off = true;
			return;
		}
		
		int between = (int) Math.ceil((win.width / ((numObj - 1) * win.scale))), r;
		
		for (r = 1000000; r > 0; r /= 10)
			if (between / r > 0)
				break;

		//System.out.println(" windows width " + win.width + " win.scale " + win.scale + " r "+r+" b "+between);
		if (r > 1)
			r /= 10;

		between = r * (between / r);
		piece[0].from = (int) (win.offX / (win.scale * between)) * between;
		if (!silent) {
			System.out.println(" **************r= " + r);
		}
		
		if (piece[0].from < (int) (win.offX / win.scale))
			piece[0].from += between;

		piece[0].name = "" + (piece[0].from / factor);
		for (int i = 1; i < numObj; i++)
		{
			piece[i].from = piece[i - 1].from + between;
			piece[i].name = "" + (piece[i].from / factor);
			
		}
		//System.out.println(" **************win.from " +win.from);
		

		FontMetrics fmetrics = g.getFontMetrics(font);
		// objHeight = fmetrics.getAscent();
		objHeight = fmetrics.getHeight();
		objPad = 5;
	}

	/**
	 * Calculates tick X positions and assigns tick names/values
	 * 
	 * @see Stripe for parameters
	 */
	/*
	 * public void makeTicks(Graphics g, aWindow win, Font font){ int between =
	 * win.width/numObj; piece[0].from = (win.offX/between)*between;
	 * if(piece[0].from < win.offX) piece[0].from += between; piece[0].from =
	 * (int)(piece[0].from/win.scale); piece[0].name =
	 * ""+(piece[0].from/factor); for(int i=1;i<numObj;i++){ piece[i].from =
	 * piece[i-1].from + (int)(between/win.scale); piece[i].name =
	 * ""+(piece[i].from/factor); }
	 * 
	 * FontMetrics fmetrics=g.getFontMetrics(font); // objHeight =
	 * fmetrics.getAscent(); objHeight = fmetrics.getHeight(); objPad = 5; }
	 */
	/**
	 * Arranges ticks, assigning Y positions relative to top given the current Y
	 * position of the stripe. Returns new current Y position
	 * 
	 * @param curY
	 *            current Y position
	 */

	public int arrangeObjects(int curY)
	{
		setTop(curY);
		if (off)
			return curY;

		for (int i = 0; i < numObj; i++)
		{
			piece[i].y = 0;
			// System.out.println("p "+piece[i].toString());
		}
		height = objHeight + objPad;
		curY += height + objPad;
		return curY;
	}

	/**
	 * Draws black baseline & ticks, always showing their names
	 * 
	 * @see Stripe for parameters
	 */

	public void draw(Graphics g, aWindow win, Font font)
	{
		if (off)
			return;

		/* only draw inside visible window */
//		int offY = top - win.offY;
//		if (offY > win.height - 1 || offY + height < 1)
//			return; // add sticky
		
		int padding = height + 2;
		int offY = win.height - padding;
		int padding2 = padding + 3;
		
		g.setColor(new Color(253, 253, 253));
		g.fillRect(0, win.height - padding2, win.width, padding2);
		
		g.setColor(Color.black);
		g.drawLine(0, height + piece[0].y + offY, win.width, height + piece[0].y + offY);
		g.setFont(font);

		for (int i = 0; i < numObj; i++)
		{
			int from = (int) (win.scale * piece[i].from) - win.offX, to = (int)(win.scale * piece[i].to) - win.offX;
			g.drawLine(from, piece[i].y + offY, from, height + piece[i].y + offY);
			if (piece[i].name != null) {
				g.drawString(piece[i].name, from + 2, height / 2 + piece[i].y + offY);
			}
		}
		
		g.setColor(new Color(248, 248, 248));
//		Graphics2D g2 = (Graphics2D) g;
//        g2.setStroke(new BasicStroke(2));
		g.drawLine(0, win.height - padding2, win.width, win.height - padding2);
		g.drawLine(0, win.height - padding2 - 1, win.width, win.height - padding2 - 1);
		
//		g2.setStroke(new BasicStroke(1));
	}
}
