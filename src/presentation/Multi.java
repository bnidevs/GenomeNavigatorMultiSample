package presentation;

/*
 * @(#)Multi.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

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
 */

public class Multi extends Stripe
{

	/**
	 * Constructs an empty Multi stripe.
	 */
	private int tip = 5;
	
	Multi()
	{
		objHeight = 15;
		objPad = 3;
	}

	/**
	 * Constructs a Multi stripe.
	 * 
	 * @param pieces
	 *            number of elements
	 * @param name
	 *            stripe name
	 */

	Multi(int pieces, int shadepieces, String name, String shadename)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		numShade = shadepieces;
		shade = new One[numShade];
		this.name = name;
		this.shadename = shadename;
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
		setTop(curY);
		curY = objPad;
		for (int i = 0; i < numObj; i++)
		{
			piece[i].y = curY;
		}
		curY += objHeight + objPad;
		height = curY;
		return curY + top;
	}

	public int arrangeShades(int curY)
	{
		int curObj = 0;

		setTop(curY);
		// System.out.println("************* > " + curY);
		curObj = 0;

		int maxY = objHeight + objPad;

		for (int i = curObj; i < numShade; i++)
		{
			shade[i].y = 2 * objPad;
			shade[i].placed = true;
		}

		curY = maxY + objPad;
		height = curY;
		return curY + top;
	}

	/**
	 * Draws rectangles, showing their names if space permits
	 * 
	 * @see Stripe for parameters
	 */
	public void drawShade(Graphics g, aWindow win, Font font, One selected)
	{
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return;

		for (int j = 0; j < numShade; j++)
		{
			int from = (int) (win.scale * shade[j].from) - win.offX, to = (int) (win.scale * shade[j].to) - win.offX;
			//System.out.println("*****from "+shade[j].from + " ++++++++++to "+ shade[j].to);
			if (from >= win.width)
			{
				continue;
			}

			if ((from >= 0) && (from <= win.width) && (to >= win.width))
			{
				to = win.width;
			}

			if ((from <= 0) && (to <= win.width) && (to >= 0))
			{
				from = 1;
			}

			g.setColor(shade[j].color);
			g.fillRect(from, shade[j].y + offY - tip, to - from + (int) win.scale, height);
		}

	}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* only draw inside visible window */
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return; // add sticky
		font = new Font("Courier", Font.BOLD, 10);
		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = 10; // fmetrics.getHeight()/2 + (shift ? 3 : 0);
		// int fh = fmetrics.getAscent();

		// System.out.println("ws "+win.scale + " wx "+win.offX+ " wy "+offY);
		for (int i = 0; i < numObj; i++)
		{
			int from = (int) (win.scale * piece[i].from) - win.offX +1, to = (int) (win.scale * piece[i].to) - win.offX +1;
			 //System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
			if (to <= 0 || from >= win.width)
				continue;
			if (to > win.width)
				to = win.width;
			if (from < 0)
				from = 1;
			if (to == from)
				to = from + 1;
			int len = to - from + (int) win.scale;

			if (piece[i].color.equals(Color.white))
			{// draw black
				g.setColor(Color.black);
				g.drawRect(from, piece[i].y + offY, len - 1, objHeight - 1);
			}
			else
			{ // fill own
				g.setColor(piece[i].color);
				g.fillRect(from, piece[i].y + offY, len, objHeight);

				if (border)
				{ // draw border
					g.setColor(Color.black);
					g.drawRect(from, piece[i].y + offY, len - 1, objHeight - 1);
				}
			}

			int tlen = fmetrics.stringWidth(piece[i].name);
			if (tlen < len)
			{
				g.setColor(piece[i].color.equals(Color.black) ? Color.white : Color.black);
				g.drawString(piece[i].name, from + (len - tlen) / 2, piece[i].y + offY + fh);
			}

			if (selected.equals(piece[i]))
			{
				g.setColor(Color.red);
				g.drawRect(from - 2, piece[i].y + offY - 2, len + 3, objHeight + 3);
			}
		}
	}

}
