package presentation;

import java.awt.*;

/* first element for multi-exon elements has color null */
/* it gives the gene direction */
/* format: from|to(is to<from, dir -1)|?|number of elements|DER BROWSER */

/* ADD DIR TO CODE */

public class ExonOld extends Stripe
{
	/**
	 * Array with the number of elements in each gene, 0 for each element.
	 */
	private int elem[];

	/**
	 * Array with the directions.
	 */
	private boolean left[];

	/**
	 * Constructs an empty Exon stripe.
	 */
	
	private boolean lineBack = Constants.DRAW_EXON_LINE_BACK;

	ExonOld()
	{
		objHeight = 15;
		objPad = 3;
	}

	/**
	 * Constructs an Exon stripe.
	 * 
	 * @param pieces
	 *            number of elements
	 * @param name
	 *            stripe name
	 */

	ExonOld(int pieces, String name)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		this.name = name;
	}

	/**
	 * Arranges genes to avoid overlaps, assigning Y positions relative to top
	 * given the current Y position of the stripe. Returns new current Y
	 * position
	 * 
	 * @param curY
	 *            current Y position
	 */

	public int arrangeObjects(int curY)
	{
		int nextRowIndex, moreObj = numObj, curObj = 0;

		if (elem == null)
		{
			elem = new int[numObj];
			left = new boolean[numObj];
			for (int i = 0; i < numObj; i++)
			{
				if (piece[i].color == null)
				{
					try
					{
						elem[i] = Integer.parseInt(piece[i].id);
						//System.out.println("Exon-Intron numbers:" + elem[i]);
					}
					catch (Exception e)
					{
						elem[i] = 1;
						continue;
					}
					
					for (int j = 1; j <= elem[i]; j++)
						elem[i + j] = 0;
						piece[i].id = piece[i + 1].id;
						i += elem[i];
					
				}
				else
					elem[i] = 1;
			}
		}

		/* reverse from/to */

		for (int i = 0; i < numObj; i++)
		{
			left[i] = (piece[i].from > piece[i].to) ? true : false;
			if (left[i])
			{
				int t = piece[i].from;
				piece[i].from = piece[i].to;
				piece[i].to = t;
			}
		}

		setTop(curY);
		curY = objPad;
		if (numObj > 0)
		{
			piece[0].y = curY;
			piece[0].placed = true;
			if (elem[0] > 0)
			{
				for (int j = 1; j <= elem[0]; j++)
				{
					piece[j].y = curY;
					piece[j].placed = true;
				}
				moreObj -= elem[0];
			}
			moreObj--;
		}

		while (moreObj > 0)
		{
			nextRowIndex = -1;
			for (int i = curObj; i < numObj; i++)
			{
				// System.out.println("p "+piece[i].toString());
				if (elem[i] == 0 || piece[i].placed)
					continue;
				if (piece[i].from > piece[curObj].to)
				{
					piece[i].y = curY;
					piece[i].placed = true;
					if (elem[i] > 1)
					{
						for (int j = 1; j <= elem[i]; j++)
						{
							piece[i + j].y = curY;
							piece[i + j].placed = true;
						}
						moreObj -= elem[i];
					}
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
				if (elem[curObj] > 1)
				{
					for (int j = 1; j <= elem[curObj]; j++)
					{
						piece[curObj + j].y = curY;
						piece[curObj + j].placed = true;
					}
					moreObj -= elem[curObj];
				}
				moreObj--;
				// System.out.println("p "+piece[curObj].toString());
			}
		}
		curY += objHeight + objPad;
		height = curY;
		return curY + top;
	}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* only draw inside visible window */
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return; // add sticky

		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = 8; // fmetrics.getHeight()/2 + (shift ? 3 : 0);
		// int fh = fmetrics.getAscent();

		// System.out.println("ws "+win.scale + " wx "+win.offX+ " wy "+offY);
		for (int i = 0; i < numObj; i++)
		{
			int from = (int) (win.scale * piece[i].from) - win.offX, to = (int) (win.scale * piece[i].to) - win.offX;
//			if (elem[i] == 1)
//			{
//				 //System.out.println("1f "+from +
//				//" t "+to+" c "+piece[i].color);
//				if (to <= 0 || from >= win.width)
//					continue;
//				if (to > win.width)
//					to = win.width;
//				if (from < 0)
//					from = 0;
//				if (to == from)
//					to = from + 1;
//				int len = to - from;
//	
//				if (piece[i].color.equals(Color.white))
//				{ // draw black
//					g.setColor(Color.black);
//					drawArrow(g, from, piece[i].y + offY, len - 1, objHeight - 1, left[i]);
//				}
//				else
//				{ // fill own
//					g.setColor(piece[i].color);
//					fillArrow(g, from, piece[i].y + offY, len, objHeight, left[i]);
//					if (border)
//					{ // draw border
//						g.setColor(Color.black);
//						drawArrow(g, from, piece[i].y + offY, len - 1, objHeight - 1, left[i]);
//					}
//				}
//				int tlen = fmetrics.stringWidth(piece[i].name);
//				if (tlen < len)
//				{
//					g.setColor(piece[i].color.equals(Color.black) ? Color.white : Color.black);
//					g.drawString(piece[i].name, from + (len - tlen) / 2, piece[i].y + offY + fh);
//				}
//
//				if (selected.equals(piece[i]))
//				{
//					g.setColor(Color.red);
//					g.drawRect(from - 2, piece[i].y + offY - 2, len + 3, objHeight + 3);
//				}
//			}
//			else
//			{
				if (to <= 0 || from >= win.width)
					continue;
				int oldto = 0;
				for (int j = 1; j <= elem[i]; j++)
				{
					/* again */
					from = (int) (win.scale * piece[i + j].from) - win.offX;
					to = (int) (win.scale * piece[i + j].to) - win.offX;
					 //System.out.println("1f "+from +
					 //" t "+to+" c "+piece[i+j].color);					
					
					
					if (to <= 0 || from >= win.width)
					{
						if (j != 1)
						{ /* line back */
							 //System.out.println(j + " " + " 1f "+from +
							 //" 1o "+oldto+" 1x "+(from+oldto)/2);
							 if (from > win.width)
								 from = win.width;
							 if (to < 0)
								 to = 0;
							 if (to == from)
								 to = from + 1;							 
							 
							g.setColor(border ? Color.black : piece[i + j].color);
							g.drawLine(from, piece[i + j].y + offY + objHeight / 2, (from + oldto) / 2, piece[i + j].y + offY + (left[i + j] ? objHeight : 0));
							g.drawLine(oldto, piece[i + j].y + offY + objHeight / 2, (from + oldto) / 2, piece[i + j].y + offY + (left[i + j] ? objHeight : 0));
						}
						continue;
					}
					
					if (to > win.width)
						to = win.width;
					if (from < 0)
						from = 0;
					if (to == from)
						to = from + 1;
					int len = to - from + (int) win.scale;
					

					if (piece[i + j].color.equals(Color.white))
					{ // draw black
						g.setColor(Color.black);
						if (j != (left[i] ? 1 : elem[i]))
							g.drawRect(from, piece[i + j].y + offY, len - 1, objHeight - 1);
						else
							drawArrow(g, from, piece[i + j].y + offY, len - 1, objHeight - 1, left[i + j]);
					}
					else
					{ // fill own
						g.setColor(piece[i + j].color);
						if (j != (left[i] ? 1 : elem[i]))
							g.fillRect(from, piece[i + j].y + offY, len, objHeight);
						else
							fillArrow(g, from, piece[i + j].y + offY, len, objHeight, left[i + j]);
						if (border)
						{ // draw border
							g.setColor(Color.black);
							if (j != (left[i] ? 1 : elem[i]))
								g.drawRect(from, piece[i + j].y + offY, len - 1, objHeight - 1);
							else
								drawArrow(g, from, piece[i + j].y + offY, len - 1, objHeight - 1, left[i + j]);
						}
					}
					if (j != 1)
					{ /* line back */
						// System.out.println(j + " " + " piece.from " + piece[i+j].from + " FROM "+from + " TO " + to+
						 //" oldto "+oldto+" x "+(from+oldto)/2);
						if (lineBack) {
							g.drawLine(from, piece[i + j].y + offY + objHeight / 2, (from + oldto) / 2, piece[i + j].y + offY + (left[i + j] ? objHeight : 0));
							g.drawLine(oldto, piece[i + j].y + offY + objHeight / 2, (from + oldto) / 2, piece[i + j].y + offY + (left[i + j] ? objHeight : 0));
						}
					}
					int tlen = fmetrics.stringWidth(piece[i + j].name);
					if (tlen < len)
					{
						g.setColor(piece[i + j].color.equals(Color.black) ? Color.white : Color.black);
						g.drawString(piece[i + j].name, from + (len - tlen) / 2, piece[i + j].y + offY + fh);
					}

					if (selected.equals(piece[i + j]))
					{
						g.setColor(Color.red);
						g.drawRect(from - 2, piece[i + j].y + offY - 2, len + 3, objHeight + 3);
					}
					oldto = to;
				}
//			}
		}
	}

	private void drawArrow(Graphics g, int x, int y, int w, int h, boolean left)
	{
		if (w < h)
		{
			g.drawRect(x, y, w, h);
		}
		else
		{
			int haf = h / 2;
			if (left)
			{
				g.drawLine(x + haf, y, x + w, y);
				g.drawLine(x + haf, y + h, x + w, y + h);
				g.drawLine(x + w, y, x + w, y + h);
				g.drawLine(x, y + haf, x + haf, y + h);
				g.drawLine(x, y + haf, x + haf, y);
			}
			else
			{
				g.drawLine(x, y, x + w - haf, y);
				g.drawLine(x, y + h, x + w - haf, y + h);
				g.drawLine(x, y, x, y + h);
				g.drawLine(x + w, y + haf, x + w - haf, y + h);
				g.drawLine(x + w, y + haf, x + w - haf, y);
			}
		}
		return;

	}

	private void fillArrow(Graphics g, int x, int y, int w, int h, boolean left)
	{
		if (w < h)
		{
			g.fillRect(x, y, w, h);
		}
		else
		{
			int haf = h / 2;
			g.fillRect(x + (left ? haf : 0), y, w - haf, h);
			if (left)
			{
				for (int i = 1; i < haf; i++)
				{
					g.drawLine(x + i, y + haf - i, x + i, y + haf + i);
				}
			}
			else
			{
				for (int i = 1; i < haf; i++)
				{
					g.drawLine(x + w - i - 1, y + haf - i, x + w - i - 1, y + haf + i);
				}
			}
		}
		return;
	}

	/**
	 * Finds a name of an object, containing given point (x,y) Here - avoid
	 * whole genes, only exons, if present Returns name or <b>null</b>
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
			if (y > piece[i].y && y < piece[i].y + objHeight && x > piece[i].from && x <= piece[i].to)
				if (elem[i] < 2)
					return (piece[i].name + ", EXON");
		}
		return null;
	}

	/**
	 * Finds an object, containing given point (x,y) Here - avoid whole genes,
	 * only exons, if present Returns object or null
	 * 
	 * @param win
	 *            current window
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */

	public One selectObject(aWindow win, int x, int y)
	{
		y += win.offY - top;
		x = (int) ((x + win.offX) / win.scale);
		for (int i = 0; i < numObj; i++)
		{
			//System.out.println("++++++++++++++x:" + x + "++++++++++++++y:" + y + "++++++++++++++piece[i].x:" + piece[i].x +"++++++++++++++piece[i].y:" + piece[i].y + "++++++++++++++piece[i].from:" + piece[i].from + "++++++++++++++piece[i].to:" + piece[i].to);
			if (y > piece[i].y && y < piece[i].y + objHeight && x > piece[i].from && x <= piece[i].to)
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
				if (elem[i] < 2)
					return (tmp);
			}
		}
		return null;
	}
}
