package presentation;

/* a primitive window */

public class aWindow
{
	int width, height, fullheight;
	int offX, offY, endY, from, to;
	double scale;

	One areaObj = null;
	int areaFrom, areaTo;
	boolean areaVisible = false;
	boolean print = true;
	
	String name = "";

	/* initialize window at 0,0 */

	aWindow(int width, int height, int from, int to, double zoom, boolean print)
	{
		this.width = width;
		this.height = height;
		this.fullheight = height;
		this.from = from;
		this.to = to;
		this.scale = this.width / (zoom * (this.to - this.from +1));
		this.print = print;
		if (this.scale >= 2.0)
			this.scale = (int) this.scale;
		//System.out.println((this.width / (zoom * (this.to - this.from))) + ":" + this.scale);
		if (print) {
			System.out.println("Scale:" + this.scale);
		}
		this.offY = 0;
		this.offX = (int) (this.scale * this.from);
		this.endY = (int) (this.scale * this.to);
		//System.out.println("scale "+this.scale + " offX "+this.offX+ " Wfrom "+this.from + " Wto " + this.to + " Wwidth "  + this.width);
	}

	public void setArea(One obj)
	{
		areaObj = obj;
		defineArea();
	}

	public void unsetArea()
	{
		areaObj = null;
		areaVisible = false;
	}

	protected void defineArea()
	{
		//System.out.println("**********************************1111111*********************************");
		areaFrom = (int) (scale * areaObj.areaFrom) - offX;
		areaTo = (int) (scale * areaObj.areaTo) - offX;
		if (areaTo <= 0 || areaFrom >= width)
		{
			areaVisible = false;
			return;
		}
		if (areaTo > width)
			areaTo = width;
		if (areaFrom < 0)
			areaFrom = 0;
		if (areaTo == areaFrom)
			areaTo = areaFrom + 1;
		areaVisible = true;
	}

	public int getX(int off)
	{
		return (int) ((offX + off) / scale);
	}
	
	public double getXPos(int off) {
		return (offX + off) / scale;
	}

	public void setX(int off)
	{
		offX = (int) (this.scale * this.from) + off;
		if (areaObj != null)
			defineArea();
	}

	public void setY(int off)
	{
		offY = off;
	}

	public double centerAt(int center)
	{
		offX = (int) (scale * center) - width / 2;

		if (offX / scale < from)
			offX = (int) (scale * from);
		else if ((offX + width) / scale > to)
			offX = (int) (scale * to) - width;

		// System.out.println("center "+center +
		// " and "+(1.*center-from)/(to-from));
		if (areaObj != null)
			defineArea();
		return (1. * center - from) / (to - from);
	}

	public double changeScale(double zoom)
	{
		double tmp_center = (offX + width / 2) / scale;

		scale = (width / (zoom * (to - from +1)));
		if (this.scale >= 2.0)
			this.scale = (int) this.scale;
		offX = (int) (scale * tmp_center) - width / 2;

		if (offX / scale < from)
			offX = (int) (scale * from);
		else if ((offX + width) / scale > to)
			offX = (int) (scale * to) - width;

		// System.out.println("tmp_center "+tmp_center +
		// " and "+(tmp_center-from)/(to-from));
		if (areaObj != null)
			defineArea();
		return (tmp_center - from) / (to - from);
	}

	public String toString()
	{
		return ("Window: w " + width + " h " + height + " offX " + offX + " offY " + offY + " from " + from + " to " + to + " scale " + scale);
	}
}
