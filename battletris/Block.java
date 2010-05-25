package battletris;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block
{	
	protected int t;
	protected int x;
	protected int y;
	protected int size;
	protected Color acolor;
	
	public static void setTypeBlockImage(int type, Color color, String filePath, int size)
	{
		BufferedImage l_img = null;
		if (null == filePath) {
			l_img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					l_img.setRGB(i, j, color.getRGB());
				}
			}
		}
		else {
			File l_file = new File(BattleTrisApp.getRootPath()+"/images/"+filePath);
			try {
				l_img = ImageIO.read(l_file);
			}
			catch(IOException ioe)
			{
			}
		}
		
		BoardImpl.registerImage(type, l_img);
	}
	
	protected void draw(Board b, Color drawNewColor, BlockSetInfo setInfo)
	{
		int type = t;
		if (null == drawNewColor) {
			type = Board.BLOCK_TYPE_BLACK;
		}
		b.drawRegImage(x, y, type, setInfo);
		/*
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				b.plotPoint(x+i, y+j, drawNewColor);
			}
		}
		long e = System.nanoTime();
		long t = (e-s);
		//System.out.println("Block.draw: render time="+String.valueOf(t));
		BattleTrisApp.sm_totalRenderTime += t;
		BattleTrisApp.sm_drawCount++;
		*/
	}

	public Block(int t, int x, int y, int size, Color acolor)
	{
		this.t = t;
		this.x = x;
		this.y = y;
		this.size = size;
		this.acolor = acolor;

	}

	public int getValue()
	{
		return 0;
	}

	public int getSize()
	{
		return this.size;
	}

	public Color getColor()
	{
		return this.acolor;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public synchronized void draw(Board b, BlockSetInfo setInfo)
	{
		this.draw (b, acolor, setInfo);
	}

	public synchronized void erase (Board b, BlockSetInfo setInfo)
	{
		this.draw (b, null, setInfo);
	}


	public void setNewLocation (int x, int y)
	{
		this.x = x;
		this.y = y;

	}
}
