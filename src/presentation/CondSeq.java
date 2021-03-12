package presentation;

import java.awt.*;

public class CondSeq extends Stripe
{
	boolean dna = true;
	boolean protein = true;
	boolean plus = true;
	private int seqlen = 0;
	private double sequnit = 1.;

	CondSeq()
	{
		objHeight = 5;
		objPad = 3;

	}

	CondSeq(int pieces, String name, String type)
	{
		this();
		numObj = pieces;
		piece = new One[numObj];
		this.name = name;
		this.type = type;
	}

	private void convert()
	{
		seqlen = piece[0].name.length();
		sequnit = (1. * piece[0].to - piece[0].from + 1) / seqlen;
	}

	public int arrangeObjects(int curY)
	{
		convert();

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
				curY += objHeight + 3*objPad +10;
				piece[curObj].y = curY;
				piece[curObj].placed = true;
				moreObj--;
			}

		}

		curY += objHeight + 3*objPad + 10;
		height = curY;
		return curY + top;
	}

	public void draw(Graphics g, aWindow win, Font font, One selected)
	{
		int offY = top - win.offY;
		if (offY > win.height - 1 || offY + height < 1)
			return;
		//boolean seqShow = false;
		int len;
		int charOff = 0;
		g.setFont(font);
		FontMetrics fmetrics = g.getFontMetrics(font);
		int fh = 15;

		for (int i = 0; i < numObj; i++)
		{
			boolean seqShow = false;
			if (piece[i].type.equals("dna"))
				dna = true;
			else
				dna = false;

			if (piece[i].type.equals("protein"))
				protein = true;
			else
				protein = false;

			if (piece[i].type.equals("+"))
				plus = true;
			else
				plus = false;
			
			String letters = piece[i].name;
			int from = (int) (win.scale * piece[i].from) - win.offX, to = (int) (win.scale * piece[i].to) - win.offX;

			if (to < 0 || from >= win.width)
			{
				// System.out.println("ERROR ERROR--------------------------");
				continue;
			}

			// if (to > win.width)
			// to = win.width;
			
			double seqstep = sequnit * win.scale;
			
			if (to == from)
			{
				if (from == 0)
				{
					from =1;
					to = from + (int)seqstep;
					len = (int)seqstep;
				}
				else
				{
					to = from + (int)seqstep;
					len = (int)seqstep;
				}			
			}
			else
				len = to -from+1;
			
			int pluspacewid = 0;
			
			for (int k = 0; k < letters.length(); k++)
			{
				if (letters.length() == 1)
				{
					pluspacewid = 0;
					break;
				}
				else
				{
					if (k != (letters.length()-1))
					{
						char a = letters.charAt(k);
						char b = letters.charAt(k+1);
						String s1 = Character.toString(a);
						String s2 = Character.toString(b);
						if (!(s1.equals(s2)))
						{
							pluspacewid = (int)((to - from)* k/letters.length());
							break;
						}
					}
					else
					{
						char a = letters.charAt(k-1);
						char b = letters.charAt(k);
						String s1 = Character.toString(a);
						String s2 = Character.toString(b);
						if (!(s1.equals(s2)))
						{
							pluspacewid = (int)((to - from)* k/letters.length());
							break;
						}
					}
				}
			}
			
			/* hybrid sequence display */
			
			String idrdcntstring = piece[i].id.toString();
			if (idrdcntstring != null)
			{								
				charOff = ((int) seqstep - fmetrics.stringWidth("A")) / 2;
				for (int j = 0; j < letters.length(); j++)
				{
					if (dna)
					{
						switch (letters.charAt(j))
						{
						case 'n':
							g.setColor(Color.white);
							break;
						case 'N':
							g.setColor(Color.white);
							break;
						case 'a':
							g.setColor(Color.green);
							break;
						case 't':
							g.setColor(Color.red);
							break;
						case 'c':
							g.setColor(Color.yellow);
							break;
						case 'g':
							g.setColor(Color.blue);
							break;
						case '+':
							g.setColor(Color.magenta);
							break;
						case '-':
							g.setColor(Color.white);
							break;
						case ' ':
							g.setColor(Color.black);
							break;
						default:
							g.setColor(Color.lightGray);
							break;
						}
					}

					else if (protein)
					{
						switch (Character.toUpperCase(letters.charAt(j)))
						{
						case 'A': // non-polar aliphatic
						case 'V':
						case 'L':
						case 'I':
						case 'M':
							g.setColor(Color.lightGray);
							break;
						case 'S': // polar neutral
						case 'T':
						case 'N':
						case 'Q':
							g.setColor(Color.green);
							break;
						case 'D': // polar negative
						case 'E':
							g.setColor(Color.yellow);
							break;
						case 'H': // polar positive
						case 'K':
						case 'R':
							g.setColor(Color.cyan);
							break;
						case 'C': // non-polar aromatic
							g.setColor(Color.yellow);
							break;
						case 'F': // non-polar aromatic
						case 'Y':
						case 'W':
							g.setColor(Color.magenta);
							break;
						case 'P': // non-polar aromatic
						case 'G':
							g.setColor(Color.pink);
							break;
						case 'B': // special char
						case 'Z':
						case 'X':
						case '-':
							g.setColor(Color.white);
							break;
						default:
							g.setColor(Color.blue);
							break;
						}
					}

					if (((int) (seqstep * j) + from + (int) seqstep) != ((int) (seqstep * (j + 1) + from)))
					{
						if (j + 1 < letters.length())
						{
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep + 1, objHeight);
						}
						
						else if (j + 1 == letters.length())
						{
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
						
					}
					else
					{
						g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
					}
					if (j ==  letters.length()-1)
					{
						g.setColor(Color.black);
						g.drawString(idrdcntstring, (int) (seqstep * j + charOff)/2 + from, piece[i].y + offY + fh);
						g.setColor(Color.gray);
						g.drawRect(from, piece[i].y + offY, to - from + (int) seqstep, objHeight + 13);
					}
				}
			}
			
			else
			{
				if (seqstep < 2.)
				{
					if (plus)
					{
						g.setColor(Color.white);
						g.fillRect(from, piece[i].y + offY, pluspacewid, objHeight);		
						
						g.setColor(Color.lightGray);
						g.fillRect(from + pluspacewid, piece[i].y + offY, to - from - pluspacewid, objHeight);
	
					}
					else
					{
						g.setColor(Color.lightGray);
						g.fillRect(from, piece[i].y + offY, to - from, objHeight);
					}
				}
	
				else
				{
					for (int j = 0; j < letters.length(); j++)
					{
						if (plus)
						{
							switch (letters.charAt(j))
							{
							case ' ':
								g.setColor(Color.white);
								break;
							default:
								g.setColor(Color.magenta);
								break;
							}
						}
						
						else if (dna)
						{
							switch (letters.charAt(j))
							{
							case 'n':
								g.setColor(Color.white);
								break;
							case 'N':
								g.setColor(Color.white);
								break;
							case 'a':
								g.setColor(Color.green);
								break;
							case 't':
								g.setColor(Color.red);
								break;
							case 'c':
								g.setColor(Color.yellow);
								break;
							case 'g':
								g.setColor(Color.blue);
								break;
							case '+':
								g.setColor(Color.magenta);
								break;
							case '-':
								g.setColor(Color.white);
								break;
							case ' ':
								g.setColor(Color.black);
								break;
							default:
								g.setColor(Color.lightGray);
								break;
							}
						}
	
						else if (protein)
						{
							switch (Character.toUpperCase(letters.charAt(j)))
							{
							case 'A': // non-polar aliphatic
							case 'V':
							case 'L':
							case 'I':
							case 'M':
								g.setColor(Color.lightGray);
								break;
							case 'S': // polar neutral
							case 'T':
							case 'N':
							case 'Q':
								g.setColor(Color.green);
								break;
							case 'D': // polar negative
							case 'E':
								g.setColor(Color.yellow);
								break;
							case 'H': // polar positive
							case 'K':
							case 'R':
								g.setColor(Color.cyan);
								break;
							case 'C': // non-polar aromatic
								g.setColor(Color.yellow);
								break;
							case 'F': // non-polar aromatic
							case 'Y':
							case 'W':
								g.setColor(Color.magenta);
								break;
							case 'P': // non-polar aromatic
							case 'G':
								g.setColor(Color.pink);
								break;
							case 'B': // special char
							case 'Z':
							case 'X':
							case '-':
								g.setColor(Color.white);
								break;
							default:
								g.setColor(Color.blue);
								break;
							}
						}
	
						if (((int) (seqstep * j) + from + (int) seqstep) != ((int) (seqstep * (j + 1) + from)))
						{
							if (j + 1 < letters.length())
								g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep + 1, objHeight);
							else if (j + 1 == letters.length())
								g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
						}
						else
							g.fillRect((int) (seqstep * j) + from, piece[i].y + offY, (int) seqstep, objHeight);
	
					}
				}
	
				if (selected.equals(piece[i]))
				{
					g.setColor(Color.red);
					if (to == from + (int)seqstep)
					{
						g.drawRect(from - 2, piece[i].y + offY - 2, (int) seqstep + 3, objHeight + 15);
					}
					else
					{
						g.drawRect(from - 2, piece[i].y + offY - 2, to + (int) seqstep - from + 3, objHeight + 15);
					}
				}
			}
		}
	}

	public String objName(aWindow win, int x, int y)
	{
		y += win.offY - top;
		x = (int) ((x + win.offX) / win.scale);

		for (int i = 0; i < numObj; i++)
		{
			if (y > piece[i].y && y < piece[i].y + objHeight && x > piece[i].from && x <= piece[i].to)
				return (piece[i].name + ", CONDSEQ");
		}

		return null;
	}
}
