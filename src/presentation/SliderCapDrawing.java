package presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * Draws images used for slider arrows
 * Used to make jpg files in /etc folder - maybe save for legacy purposes
 */
public class SliderCapDrawing {

	final static int SLIDER_W = 21, SLIDER_CAP = 17;
	Color bgColor;
	int xPos = 0;
	int yPos = 0;
	int Width = SLIDER_W;
	int Height = Width;
	int capSize = SLIDER_CAP;
	
	public void drawImages() {
		BufferedImage bufferedImageLeftCap = new BufferedImage(Width, Height,
				BufferedImage.TYPE_INT_RGB);
		drawLeftArrow(bufferedImageLeftCap.getGraphics());
		File f = new File("left_arrow21.jpg");
		try {
			ImageIO.write(bufferedImageLeftCap, "JPG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage bufferedImageRightCap = new BufferedImage(Width, Height,
				BufferedImage.TYPE_INT_RGB);
		drawRightArrow(bufferedImageRightCap.getGraphics());
		File f2 = new File("right_arrow21.jpg");
		try {
			ImageIO.write(bufferedImageRightCap, "JPG", f2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage bufferedImageTopCap = new BufferedImage(Width, Height,
				BufferedImage.TYPE_INT_RGB);
		drawTopArrow(bufferedImageTopCap.getGraphics());
		File f3 = new File("top_arrow21.jpg");
		try {
			ImageIO.write(bufferedImageTopCap, "JPG", f3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage bufferedImageBottomCap = new BufferedImage(Width, Height,
				BufferedImage.TYPE_INT_RGB);
		drawBottomArrow(bufferedImageBottomCap.getGraphics());
		File f4 = new File("bottom_arrow21.jpg");
		try {
			ImageIO.write(bufferedImageBottomCap, "JPG", f4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawLeftArrow(Graphics g)
	{
		g.setColor(UtilityMethods.hexColor("666666"));
		g.fillRect(xPos, yPos, Width, Height);
		rect3D(g, xPos, yPos, Width, Height, 2, false);
		
		leftCap(g, xPos, yPos);
	}
	
	public void drawRightArrow(Graphics g)
	{
		g.setColor(UtilityMethods.hexColor("666666"));
		g.fillRect(xPos, yPos, Width, Height);
		rect3D(g, xPos, yPos, Width, Height, 2, false);
		
		rightCap(g, xPos, yPos);
	}
	
	public void drawTopArrow(Graphics g)
	{
		g.setColor(UtilityMethods.hexColor("666666"));
		g.fillRect(xPos, yPos, Width, Height);
		rect3D(g, xPos, yPos, Width, Height, 2, false);
		
		topCap(g, xPos, yPos);
	}
	
	public void drawBottomArrow(Graphics g)
	{
		g.setColor(UtilityMethods.hexColor("666666"));
		g.fillRect(xPos, yPos, Width, Height);
		rect3D(g, xPos, yPos, Width, Height, 2, false);
		
		bottomCap(g, xPos, yPos);
	}
	
	private void rect3D(Graphics g, int x, int y, int w, int h, int thick, boolean b)
	{
		h--;
		w--;
		bgColor = UtilityMethods.hexColor("666666");
		Color brColor = bgColor.brighter();
		Color dkColor = bgColor.darker();
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
		g.setColor(Color.red);
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
	
	public static void main(String args[]) {
		SliderCapDrawing d = new SliderCapDrawing();
		d.drawImages();
	}
	
}
