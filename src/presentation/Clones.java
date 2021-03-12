package presentation;

/*
 * @(#)Clones.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

import java.awt.*;

/**
 * A stripe, whose elements are named rectangles.
 * 
 * Elements are selectable, rearranged before display. Datafile contains
 * fromX|toX|name|ID|color for each element (sorted by fromX/toX).
 * 
 * @see Stripe
 * @version 03 Dec 1996
 * @author Andrei Grigoriev
 */

public class Clones extends Stripe
{

	/**
	 * Constructs an empty Clones stripe.
	 */

	Clones()
	{
		objHeight = 15;
		objPad = 3;
	}

	/**
	 * Constructs a Clones stripe.
	 * 
	 * @param pieces
	 *            number of elements
	 * @param name
	 *            stripe name
	 */

	Clones(int pieces, String name)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		this.name = name;
	}

	/**
	 * Arranges clones to avoid overlaps, assigning Y positions relative to top
	 * given the current Y position of the stripe. Returns new current Y
	 * position
	 * 
	 * @param curY
	 *            current Y position
	 */

	public int arrangeObjects(int curY)
	{
		// initialize all pieces as not placed since vector is copied when OK button
		// clicked multiple times and placed may have been set to true in copied vector
		// resulting in infinite loop
		for (int i = 0; i < numObj; i++) {
			piece[i].placed = false;
		}
		int nextRowIndex, moreObj = numObj, curObj = 0;

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
				// System.out.println("p "+piece[i].toString());
				if (piece[i].placed)
					continue;
				if (piece[i].from > piece[curObj].to)
				{
					piece[i].y = curY;
					piece[i].placed = true;
					curObj = i;
					moreObj--;
				}
				else if (nextRowIndex == -1)
					nextRowIndex = i;
			}
			
			if (nextRowIndex > -1)
			{
				curObj = nextRowIndex;
				curY += objHeight + objPad;
				piece[curObj].y = curY;
				piece[curObj].placed = true;
				moreObj--;
				// System.out.println("p "+piece[curObj].toString());
			}
		}
		curY += objHeight + objPad;
		height = curY;
		return curY + top;
	}

	/**
	 * Draws pre-arranged clones, showing their names if space permits
	 * 
	 * @see Stripe for parameters
	 */

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* only draw inside visible window */
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return; // add sticky

		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = Constants.CLONES_FH; // fmetrics.getHeight()/2 + (shift ? 3 : 0);
		// int fh = fmetrics.getAscent();

		// System.out.println("ws "+win.scale + " wx "+win.offX+ " wy "+offY);
		//System.out.println("clones " + numObj);
		for (int i = 0; i < numObj; i++)
		{
			int from = (int) (win.scale * (piece[i].from - 1)) - win.offX, to = (int) (win.scale * (piece[i].to - 1)) - win.offX;
			// System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
			if (to <= 0 || from >= win.width)
				continue;
			if (to > win.width)
				to = win.width;
			if (from < 0)
				from = 0;
			if (to == from)
				to = from + 1;
			int len = to - from;

			if (piece[i].color.equals(Color.white))
			{ // draw black
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

			if (selected != null && selected.equals(piece[i]))
			{
				g.setColor(Color.red);
				g.drawRect(from - 2, piece[i].y + offY - 2, len + 3, objHeight + 3);
			}
		}
	}

}
