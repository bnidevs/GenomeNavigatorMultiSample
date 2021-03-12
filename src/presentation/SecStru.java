package presentation;

import java.awt.*;

/**
 * A stripe, whose elements are named rectangles. Elements are selectable,
 * rearranged before display. Element format is: fromX|toX|name|ID|color|type
 * (sorted by fromX/toX). RNA structure stripe data contains two parts of data:
 * data for structure information and data for shades.
 * 
 * @see Stripe
 */
public class SecStru extends Stripe
{
	/* single strand */
	boolean ss = true;

	/* double strands */
	boolean ds = true;

	/* skipped region (gap) */
	boolean dash = true;

	/* structure info not available */
	boolean na = true;

	/* length of an element */
	private int seqlen = 0;

	/* scale of an element */
	private double sequnit = 1.;

	/* offset of the top of shade */
	private int tip = 5;

	/* the top of shade */
	int stripetop = 0;

	/**
	 * Constructs an empty SecStru stripe.
	 */
	SecStru()
	{
		objHeight = 15;
		objPad = 3;

	}

	/**
	 * Constructs an SecStru stripe.
	 * 
	 * @param pieces
	 *            number of elements
	 * @param name
	 *            stripe name
	 * @param shadepieces
	 *            number of shades
	 * @param shadename
	 *            shade name
	 */
	SecStru(int pieces, int shadepieces, String name, String shadename)
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
	 * calculate the scale of an element
	 */
	private void convert()
	{
		seqlen = 1;
		sequnit = 1. / seqlen;
	}

	/**
	 * Arranges elements to avoid overlaps, assigning Y positions relative to
	 * top given the current Y position of the stripe. Returns new current Y
	 * position
	 * 
	 * @param curY
	 *            current Y position
	 */
	public int arrangeObjects(int curY)
	{
		convert();

		/* current object index among all elements */
		int curObj = 0;
		setTop(curY);
		String str1 = "ss";
		String str2 = "ds";
		String str3 = "dash";
		String str4 = "na";

		/*
		 * maximum height of current stripe (all four structure types are
		 * presented)
		 */
		int maxY = 4 * objHeight + 3 * objPad;

		/* minimum height of current stripe (no structure data is available) */
		int minY = 3 * objPad;

		/**
		 * arrange the position of four structure types)
		 */
		for (int i = curObj; i < numObj; i++)
		{

			if (piece[i].type.equalsIgnoreCase(str1))
			{
				piece[i].y = 3 * (objHeight + objPad);
				piece[i].placed = true;
				curY = 3 * (objHeight + objPad);
			}

			else if (piece[i].type.equalsIgnoreCase(str2))
			{
				piece[i].y = 2 * (objHeight + objPad);
				piece[i].placed = true;
				curY = 2 * (objHeight + objPad);
			}

			else if (piece[i].type.equalsIgnoreCase(str3))
			{
				piece[i].y = objHeight + 2 * objPad;
				piece[i].placed = true;
				curY = objHeight + 2 * objPad;
			}
			else if (piece[i].type.equalsIgnoreCase(str4))
			{
				piece[i].y = 2 * objPad;
				piece[i].placed = true;
				curY = 2 * objPad;
			}
			else
				continue;
		}

		curY = maxY + minY;
		height = curY;
		stripetop = top;
		return height + top;
	}

	/**
	 * Arranges shades, assigning Y positions relative to top given the current
	 * Y position of the stripe. Returns new current Y position
	 * 
	 * @param curY
	 *            current Y position
	 */
	public int arrangeShades(int curY)
	{
		convert();
		int curObj = 0;
		setTop(curY);
		curObj = 0;
		int maxY = 4 * objHeight + 3 * objPad;
		int minY = 3 * objPad;

		for (int i = curObj; i < numShade; i++)
		{
			shade[i].y = 2 * objPad;
			shade[i].placed = true;
		}
		curY = maxY + minY;
		height = curY;
		return curY + top;
	}

	/**
	 * Draws pre-arranged shades
	 * 
	 * @see Stripe for parameters
	 */
	public void drawShade(Graphics g, aWindow win, Font font, One selected)
	{
		/* offset of the y-coordinate of stripe */
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return;

		for (int j = 0; j < numShade; j++)
		{
			/* convert from- and to-coordinate of shades */
			int from = (int) (win.scale * shade[j].from) - win.offX, to = (int) (win.scale * shade[j].to) - win.offX;
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
				from = 0;
			}

			/* sets colors and draws shade elements */
			g.setColor(shade[j].color);
			g.fillRect(from, shade[j].y + offY - tip, to - from + (int) win.scale, height - 3);
		}

	}

	/**
	 * Draws pre-arranged elements, showing their names if space permits
	 * 
	 * @see Stripe for parameters
	 */
	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* length of gap region in RNA structure */
		int dashlen = 0;

		/* length of 'na' structure in RNA structure */
		int nalen = 0;

		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return;

		/* sequence information display tag */
		boolean seqShow = false;

		/* base pair offset in sequence element */
		int charOff = 0;

		/* base pair offset in gap element */
		int dashcharOff = 0;
		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);

		/* font height */
		int fh = 11;

		/* length of one element */
		int len;

		/* length of total sequence */
		int letterslen = 0;

		/**
		 * Draws shades
		 */
		for (int j = 0; j < numShade; j++)
		{
			int from = (int) (win.scale * shade[j].from) - win.offX, to = (int) (win.scale * shade[j].to) - win.offX;
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
				from = 0;
				;
			}

			g.setColor(shade[j].color);
			g.fillRect(from, top, to - from, height);
		}

		/**
		 * Draws RNA structure
		 */
		for (int i = 0; i < numObj; i++)
		{
			if (piece[i].type.equals("ss"))
				ss = true;
			else
				ss = false;

			if (piece[i].type.equals("ds"))
				ds = true;
			else
				ds = false;

			if (piece[i].type.equals("dash"))
			{
				dash = true;
				dashlen = piece[i].to - piece[i].from;
			}
			else
				dash = false;

			if (piece[i].type.equals("na"))
			{
				na = true;
				nalen = piece[i].to - piece[i].from + 1;
			}
			else
				na = false;

			/* sequence of corresponding element */
			String letters = piece[i].name;

			/* converted coordinates */
			int from = (int) (win.scale * piece[i].from) - win.offX, to = (int) (win.scale * piece[i].to) - win.offX;

			/* initial converted coordinates */
			int realfrom = from, realto = to;

			if (from >= win.width)
			{
				continue;
			}

			double seqstep = sequnit * win.scale;

			to = from + (int) seqstep;
			len = (int) seqstep;

			/**
			 * Draws in stacked style, with no sequence details
			 */
			if (seqstep < 2.)
			{
				g.setColor(Color.lightGray);
				g.fillRect(from, piece[i].y + offY, to - from, objHeight);
				int tlen = fmetrics.stringWidth(piece[i].name);

				if (tlen < len)
				{
					g.setColor(Color.black);
					g.drawString(piece[i].name, from + (len - tlen) / 2, piece[i].y + offY + fh);
				}

				if (border)
				{
					g.setColor(Color.black);
					g.drawRect(from, piece[i].y + offY, to - from - 1, objHeight - 1);
				}
			}

			/**
			 * Draws with sequence details
			 */
			else
			{
				letterslen = letters.length();

				if (dash)
				{
					letterslen = 1;
				}

				if (fmetrics.stringWidth("A") * letterslen <= len)
				{
					seqShow = true;
					charOff = ((int) seqstep - fmetrics.stringWidth("A")) / 2;
					dashcharOff = ((int) seqstep * dashlen - fmetrics.stringWidth("A")) / 2;
				}

				for (int j = 0; j < letterslen; j++)
				{
					if (((int) (seqstep * j) + from + (int) seqstep) != ((int) (seqstep * (j + 1) + from)))
					{
						if (j + 1 < letterslen)
						{
							// System.out.println("222");
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep + 1, objHeight);
						}
						else if (j + 1 == letterslen)
						{
							// System.out.println("33333");
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
					}
					else
					{
						if (dash)
						{
							g.setColor(piece[i].color);
							g.fillRect((int) (seqstep / 2 * (j + 1)) + from, piece[i].y + offY, (int) seqstep * dashlen, objHeight);
						}
						else if (piece[i].type.equalsIgnoreCase("ss"))
						{
							g.setColor(piece[i].color);
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
						else if (piece[i].type.equalsIgnoreCase(" "))
						{
							g.setColor(Color.white);
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
						else if (piece[i].type.equalsIgnoreCase("na"))
						{
							g.setColor(Color.darkGray);
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
						else
						{
							g.setColor(piece[i].color);
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
					}

					/**
					 * Draws sequence
					 */
					if (seqShow)
					{
						g.setColor(Color.black);
						if (dash)
						{
							g.drawChars(letters.toCharArray(), j, letters.length(), (int) (seqstep / 2 * (j + 1) + dashcharOff) + from, piece[i].y + offY + fh);
						}
						else if (na)
						{
							g.setColor(Color.white);
							g.drawChars(letters.toCharArray(), j, 1, (int) (seqstep * j + charOff) + from, piece[i].y + offY + fh);
						}
						else
							g.drawChars(letters.toCharArray(), j, 1, (int) (seqstep * j + charOff) + from, piece[i].y + offY + fh);
					}
				}
			}

			/**
			 * Draws rectangle on selected objects
			 */
			if (na)
			{
				if (selected.equals(piece[i]))
				{
					g.setColor(Color.red);
					g.drawRect(from - 2, piece[i].y + offY - 2, (int) seqstep * nalen + 3, objHeight + 3);
				}
			}
			else
			{
				if ((selected.from == piece[i].from) && (selected.y == piece[i].y) && (selected.name.equalsIgnoreCase(piece[i].name)) && (selected.id.equalsIgnoreCase(piece[i].id))
						&& (selected.type.equalsIgnoreCase(piece[i].type)))
				{
					g.setColor(Color.magenta);
					if (dash)
					{
						g.drawRect((int) (seqstep / 2) + from - 2, piece[i].y + offY - 2, (int) seqstep * dashlen + 3, objHeight + 3);
					}
					else if (ss)
					{
						g.drawRect(from - 2, piece[i].y + offY - 2, (int) seqstep + 3, objHeight + 3);
					}
					else if (ds)
					{
						if (realto == realfrom + (int) seqstep)
						{
							g.drawRect(realfrom - 2, piece[i].y + offY - 2, (int) seqstep + 3, objHeight + 3);
						}
						else
						{
							g.drawRect(realfrom - 2, piece[i].y + offY - 2, (int) seqstep + 3, objHeight + 3);
							g.drawRect(realto - 2, piece[i].y + offY - 2, (int) seqstep + 3, objHeight + 3);
							g.drawLine(realfrom + (int) seqstep / 2, piece[i].y + objHeight + offY, realfrom + (int) seqstep / 2, piece[i].y + offY + 2 * objHeight + tip);
							g.drawLine(realto + (int) seqstep / 2, piece[i].y + objHeight + offY, realto + (int) seqstep / 2, piece[i].y + offY + 2 * objHeight + tip);
							g.drawLine(realfrom + (int) seqstep / 2, piece[i].y + offY + 2 * objHeight + tip, realto + (int) seqstep / 2, piece[i].y + offY + 2 * objHeight + tip);
						}
					}
				}
			}
		}

	}

	/**
	 * Finds a name of an object, containing given point (x,y) if present
	 * Returns name or <b>null</b>
	 * 
	 * @param win
	 *            current window
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public String objName(aWindow win, int x, int y)
	{
		y += win.offY - top;
		x = (int) ((x + win.offX) / win.scale);

		for (int i = 0; i < numObj; i++)
		{
			int dashlen = 0;
			if (piece[i].type.equals("dash"))
			{
				dash = true;
				dashlen = piece[i].to - piece[i].from;
			}
			else
				dash = false;

			if (dash)
			{
				if (y >= piece[i].y && y <= piece[i].y + objHeight && x >= piece[i].from && x < piece[i].from + 0.5 + dashlen)
					return (piece[i].name + ", SECSTRU");
			}
			else
			{
				if (y >= piece[i].y && y <= piece[i].y + objHeight && x >= piece[i].from && x < piece[i].from + 1)
					return (piece[i].name + ", SECSTRU");
			}
		}

		return null;
	}
}
