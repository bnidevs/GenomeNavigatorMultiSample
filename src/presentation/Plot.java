package presentation;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;

/* first element gives y range bottom|top */
/* others give x|y pairs */
/* colors: 1st - axis, 2nd - fg */

public class Plot extends Stripe
{
	private boolean hist = false;
	private double scale = 0.;
	private int zeroY;
	private Color axcol, fgcol;

	Plot()
	{
		objHeight = 15;
		objPad = 3;
	}

	Plot(int pieces, int shadepieces, String name, String shadename)
	{
		numObj = pieces;
		piece = new One[numObj];
		numShade = shadepieces;
		shade = new One[numShade];
		this.name = name;
		this.shadename = shadename;
		objHeight = 15;
		objPad = 3;
	}

	/* nothing to arrange */

	public int arrangeObjects(int curY)
	{
		setTop(curY);
		curY = objPad;
		try
		{
			if (piece[0].name != null)
				objHeight = Integer.parseInt(piece[0].name);
		}
		catch (NumberFormatException e)
		{
			objHeight = 15;
		}

		scale = (1. * piece[0].to - piece[0].from) / objHeight;
		zeroY = (int) (piece[0].to / scale);

		for (int i = 1; i < numObj; i++)
		{
			piece[i].y = (int) ((piece[0].to - piece[i].to) / scale);
		}
		// System.out.println("1st "+piece[1].y+ " ZERO " + zeroY);
		curY += objHeight + objPad;
		height = curY;
		axcol = piece[0].color;
		fgcol = piece[1].color;
		if (piece[0].id.equals("hist"))
			hist = true;
		return curY + top;
	}
	
	//public int arrangeShades(int curY)
	//{
	//	setTop(curY);
	//	curY = objPad;
	//	curY += objHeight + objPad;
	//	height = curY;
	//	return curY + top;
	//}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		/* only draw inside visible window */
//		int offY = top - win.offY;
//		if (offY > win.height - 1 || offY + height < 1)
//			return; // add sticky
		int offY = 0;
		
		int baseY = height - 2 * objPad + offY;
		double seqstep = 1. * win.scale;
		for (int i = 2; i < numObj; i++)
		{
			int from = (int) (win.scale * piece[i - 1].from) - win.offX, to = (int) (win.scale * piece[i].from) - win.offX;
			// System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
			if (to <= 0 || from >= win.width)
				continue;
			g.setColor(piece[i - 1].color);
			if (hist)
			{
				if (piece[i - 1].y == piece[i].y)
				{
					int startY = piece[i].to < 0 ? (int) Math.ceil(piece[0].to / scale) : piece[i - 1].y;
					int realHeight = piece[i].to < 0 ? (int) Math.ceil(-1 * piece[i].to / scale) : (int) Math.ceil(piece[i].to / scale);
					g.fillRect(from, startY + offY, to - from, realHeight);
				}
				/*
				 * else if (piece[i - 1].x == piece[i].x) { continue; }
				 */

				else
				{
					int startY = piece[i].to < 0 ? (int) Math.ceil(piece[0].to / scale) : piece[i - 1].y;
					int realHeight = piece[i].to < 0 ? (int) Math.ceil(-1 * piece[i].to / scale) : (int) Math.ceil(piece[i - 1].to / scale);
					g.fillRect(from, startY + offY, to - from, realHeight);
				}
			}
			else
			{
				g.drawLine(from + (int) seqstep/2, piece[i - 1].y + offY, to + (int) seqstep/2, piece[i].y + offY);
			}
		}
		g.setColor(piece[numObj - 1].color);
		g.fillRect((int) (win.scale * piece[numObj - 1].from) - win.offX, (piece[numObj - 1].to < 0 ? (int) Math.ceil(piece[0].to / scale) : piece[numObj - 1].y) + offY, 1 * (int) win.scale,
				(piece[numObj - 1].to < 0 ? (int) Math.ceil(-1 * piece[numObj].to / scale) : (int) Math.ceil(piece[numObj - 1].to / scale)));
		g.setColor(axcol);
		g.drawLine(0, zeroY + offY, win.width, zeroY + offY);
		g.drawLine(0, offY, 0, baseY);
		Font fontaxl = new Font("Courier", Font.BOLD,12);
		g.setFont(fontaxl);
		FontMetrics fmetrics = g.getFontMetrics(fontaxl);
		int fh = 8; 
		// fmetrics.getHeight()/2 + (shift ? 3 : 0);
		// int fh = fmetrics.getAscent();
		//g.setColor(fgcol);
		g.setColor(axcol);
		g.drawString("" + piece[0].to, 2, offY + fh + 2);
		g.drawString("" + piece[0].from, 2, baseY);
	}
	
	/*public void drawShade(Graphics g, aWindow win, Font font, One selected)
	{
		for (int j = 0; j < numShade; j++)
		{
			int from = (int) (win.scale * shade[j].from) - win.offX, to = (int) (win.scale * shade[j].to) - win.offX;
			if (from >= win.width)
			{
				continue;
			}
			
			if ( (from >= 0) && (from <= win.width) && (to >= win.width))
			{
				to = win.width;
			}
			
			if ( (from <= 0) && (to <= win.width) && (to >= 0))
			{
				from = 0;;
			}			
			
			g.setColor(shade[j].color);
			g.fillRect(from, top , to - from, height);
		}
		
	}*/
	
	/**
	 * Prevent any items from being found in plot
	 */
	public Vector objFind(String namepart, Vector list) {
		return list;
	}

//	public boolean inside(aWindow win, int y)
//	{
//		return false;
//	}
}
