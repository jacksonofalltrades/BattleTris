package battletris;

import java.awt.Color;

public class DiceBlock extends Block
{
	public static final int TYPE_ONE = 1;
	public static final int TYPE_TWO = 2;
	public static final int TYPE_THREE = 3;
	public static final int TYPE_FOUR = 4;
	public static final int TYPE_FIVE = 5;
	public static final int TYPE_SIX = 6;
	
	protected int m_value;

	/*
	protected void drawDot(Board b, int centerX, int centerY)
	{
		b.plotPoint(centerX, centerY, Color.BLACK);
		b.plotPoint(centerX+1, centerY+1, Color.BLACK);
		b.plotPoint(centerX, centerY+1, Color.BLACK);
		b.plotPoint(centerX+1, centerY, Color.BLACK);
	}

	protected synchronized void drawOne(Board b)
	{
		// Find the center
		int l_centerX = x+Math.round((float)size/2f);
		int l_centerY = y+Math.round((float)size/2f);

		this.drawDot(b, l_centerX, l_centerY);
	}

	protected synchronized void drawTwo(Board b)
	{
		// Find point halfway between edge and center
		int l_leftCenterX = x+Math.round((float)size/4f);
		int l_topCenterY = y+Math.round((float)size/4f);

		int l_rightCenterX = x+3*Math.round((float)size/4f);
		int l_btmCenterY = y+3*Math.round((float)size/4f);

		this.drawDot(b, l_leftCenterX, l_topCenterY);
		this.drawDot(b, l_rightCenterX, l_btmCenterY);
	}

	protected void drawThree(Board b)
	{
		this.drawOne(b);
		this.drawTwo(b);
	}

	protected void drawFour(Board b)
	{
		// Find point halfway between edge and center
		int l_leftCenterX = x+Math.round((float)size/4f);
		int l_topCenterY = y+Math.round((float)size/4f);

		int l_rightCenterX = x+3*Math.round((float)size/4f);
		int l_btmCenterY = y+3*Math.round((float)size/4f);

		this.drawDot(b, l_leftCenterX, l_topCenterY);
		this.drawDot(b, l_rightCenterX, l_btmCenterY);
		this.drawDot(b, l_leftCenterX, l_btmCenterY);
		this.drawDot(b, l_rightCenterX, l_topCenterY);
	}

	protected void drawFive(Board b)
	{
		this.drawFour(b);
		this.drawOne(b);
	}

	protected void drawSix(Board b)
	{
		this.drawFour(b);

		// Find the center
		int l_leftCenterX = x+Math.round((float)size/4f);
		int l_rightCenterX = x+3*Math.round((float)size/4f);
		int l_centerY = y+Math.round((float)size/2f);

		this.drawDot(b, l_leftCenterX, l_centerY);
		this.drawDot(b, l_rightCenterX, l_centerY);
	}
	*/

	public DiceBlock(int x, int y, int size, int value)
	{
		super(value, x, y, size, Color.WHITE);

		m_value = value;

		if (value < 1)
		{
			m_value = 1;
		}

		if (value > 6)
		{
			m_value = 6;
		}
	}

	public int getValue()
	{
		return m_value;
	}

	public synchronized void draw(Board b, BlockSetInfo setInfo)
	{
		super.draw(b, setInfo);
	}
}