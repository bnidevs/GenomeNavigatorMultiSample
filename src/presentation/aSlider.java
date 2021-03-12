package presentation;

/*
 * aSlider - proportional slider class
 *
 * Copyright (c) 1996 A Grigoriev
 *
 */

import java.awt.*;

public class aSlider
{
	private int xPos, yPos, Width, Height, Length, Resolution, incr, currentPosition;
	private int capSize, eleSize, loVal, hiVal, midRange;
	private boolean horizontal;
	private String name;

	private aBrowser parent = null;
	private Color bgColor, brColor, dkColor;

	aSlider(int x, int y, int h, int w, int res, double incr, int cap, int ele, boolean hori, int curpos, Color bg, aBrowser a, String name)
	{
		this.xPos = x;
		this.yPos = y;
		this.Height = h;
		this.Width = w;
		this.Resolution = res;
		this.incr = (int) (Resolution * incr);
		this.horizontal = hori;
		this.capSize = cap;
		this.parent = a;
		this.name = name;

		bgColor = bg;
		brColor = bgColor.brighter();
		dkColor = bgColor.darker();

		// changeEle(ele);
		changeEle(ele, 0.);
		/*
		 * this.currentPosition = curpos; if(curpos != 0)
		 * parent.SliderValue(currentPosition, name);
		 */
	}

	public void changeEle(int ele, double curpos)
	{
		// public void changeEle(int ele){
		this.eleSize = ele;

		// zero is when the midpoint of the elevator is against the bottom
		loVal = capSize + 1 + (eleSize / 2); // lowest value
		hiVal = (horizontal ? Width : Height) - capSize + 1 - (eleSize / 2);
		midRange = hiVal - loVal;
		if (midRange == 0)
			midRange = 1;

		int sample = (int) (curpos * Width);
		sample = Math.max(sample, loVal);
		sample = Math.min(sample, hiVal);
		currentPosition = Math.max(0, ((sample - loVal) * Resolution) / midRange);
		// System.out.println("s-l "+(sample -
		// loVal)+" mr "+midRange+" cp "+(((sample - loVal) *
		// Resolution)/midRange)+" curpos "+currentPosition+" ele "+eleSize);
	}

	private void rect3D(Graphics g, int x, int y, int w, int h, int thick, boolean b)
	{
		h--;
		w--;
		g.setColor(b ? dkColor : brColor);
		for (int i = 0; i < thick; i++)
		{
			g.drawLine(x + i, y + h - i, x + w - i, y + h - i);
			g.drawLine(x + w - i, y + h - i, x + w - i, y + i);
		}

		g.setColor(b ? brColor : dkColor);
		for (int i = 0; i < thick; i++)
		{
			g.drawLine(i + x, y + h - i, x + i, y + i);
			g.drawLine(i + x, y + i, x + w - i, y + i);
		}

	}

	/** Draw a little box (used for the elevator and end caps */
	void drawSquare(Graphics g, int x, int y, int size)
	{
		g.setColor(bgColor);
		g.fillRect(x + 2, y + 2, size, size);
		rect3D(g, x + 3, y + 3, size - 2, size - 2, 2, true);
		g.setColor(Constants.SLIDER_ARROW_COLOR);
	}

	/**
	 * Draw the left end cap on a horizontal slider.
	 */
	void leftCap(Graphics g, int x, int y)
	{
		drawSquare(g, x, y, capSize);
		for (int i = 0; i < 5; i++)
		{
			g.drawLine(x + 8 + i, y + 10 - i, x + 8 + i, y + 10 + i);
		}
	}

	void rightCap(Graphics g, int x, int y)
	{
		drawSquare(g, x, y, capSize);
		for (int i = 0; i < 5; i++)
		{
			g.drawLine(x + 9 + i, y + 10 - (4 - i), x + 9 + i, y + 10 + (4 - i));
		}
	}

	void topCap(Graphics g, int x, int y)
	{
		drawSquare(g, x, y, capSize);
		for (int i = 0; i < 5; i++)
		{
			g.drawLine(x + 10 - i, y + 8 + i, x + 10 + i, y + 8 + i);
		}
	}

	void bottomCap(Graphics g, int x, int y)
	{
		drawSquare(g, x, y, capSize);
		for (int i = 0; i < 5; i++)
		{
			g.drawLine(x + 10 - (4 - i), y + 8 + i, x + 10 + (4 - i), y + 8 + i);
		}
	}

	/**
	 * Paint the elevator on to the slider. Note that the computation for the
	 * minimum and maximum positions overlap the end caps by one pixel. this is
	 * done to leave a single pixel line between the cap and the elevator when
	 * fully extended.
	 */
	void elevator(Graphics g)
	{
		int minPos = capSize - 1;
		int maxPos = (horizontal ? Width : Height) - capSize - 2 - eleSize;
		int pos = (((maxPos - minPos) * currentPosition) / Resolution) + minPos;

		if (horizontal)
		{
			g.setColor(bgColor);
			g.fillRect(xPos + pos + 2, yPos + 2, eleSize, capSize);
			rect3D(g, xPos + pos + 3, yPos + 3, eleSize - 2, capSize - 2, 2, true);
		}
		else
		{
			g.setColor(bgColor);
			g.fillRect(xPos + 2, yPos + pos + 2, capSize, eleSize);
			rect3D(g, xPos + 3, yPos + pos + 3, capSize - 2, eleSize - 2, 2, true);
		}
	}

	/**
	 * Draw the slider on/off the screen.
	 */
	public void drawSlider(Graphics g)
	{
		g.setColor(bgColor);
		g.fillRect(xPos, yPos, Width, Height);
		rect3D(g, xPos, yPos, Width, Height, 2, false);

		if (horizontal)
		{
			leftCap(g, xPos, yPos);
			rightCap(g, xPos + Width - 21, yPos);
		}
		else
		{
			topCap(g, xPos, yPos);
			bottomCap(g, xPos, yPos + Height - 21);
		}
		elevator(g);
	}

	/* Calculate a new result value given the coordinates of the mouse */
	void newPosition(int x, int y)
	{
		int sample;

		sample = Math.max((horizontal ? x : y), loVal);
		sample = Math.min(sample, hiVal);
		currentPosition = Math.max(0, ((sample - loVal) * Resolution) / midRange);
		currentPosition = Math.min(Resolution, currentPosition);
		// System.out.println("s-l "+(sample -
		// loVal)+" mr "+midRange+" cp "+(((sample - loVal) *
		// Resolution)/midRange)+" curpos "+currentPosition+" ele "+eleSize);
		parent.SliderValue(currentPosition, name);
	}

	public boolean mouseDown(int x, int y)
	{
		x -= xPos;
		y -= yPos;

		if ((horizontal && ((x <= capSize + 3) && (x >= 0))) || ((!horizontal && (y <= capSize + 3) && (y >= 0))))
		{
			// System.out.println("curpos "+(currentPosition-incr)+" was "+currentPosition+" ele "+eleSize);
			currentPosition -= horizontal ? incr : 10 * incr;
			if (currentPosition < 0)
				currentPosition = 0;
			parent.SliderValue(currentPosition, name);

			return true;
		}

		if ((horizontal && ((x >= (Width - capSize + 3)) && (x <= Width))) || ((!horizontal && (y >= (Height - capSize + 3)) && (y <= Height))))
		{
			// System.out.println("curpos "+(currentPosition+incr)+" was "+currentPosition+" ele "+eleSize);
			currentPosition += horizontal ? incr : 10 * incr;
			if (currentPosition > Resolution)
				currentPosition = Resolution;
			parent.SliderValue(currentPosition, name);

			return true;
		}

		return true;
	}

	public boolean mouseDrag(int x, int y)
	{
		x -= xPos;
		y -= yPos;

		// ignore if it is inside the end caps
		if (horizontal && ((y <= Height) && (y >= 0)) && (((x <= (capSize + 2)) && (x >= 0)) || ((x >= (Width - capSize - 2)) && (x <= Width))))
		{
			return true;
		}

		if (!horizontal && ((x <= Height) && (x >= 0)) && (((y <= (capSize + 2)) && (y >= 0)) || ((y >= (Height - capSize - 2)) && (y <= Height))))
		{
			return true;
		}

		newPosition(x, y);
		return true;
	}

	public boolean inside(int x, int y)
	{
		x -= xPos;
		y -= yPos;
		if (x > 0 && x <= Width && y > 0 && y <= Height)
			return true;
		else
			return false;
	}
}
