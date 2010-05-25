package battletris;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyListener;

public interface Board extends ModifiableBoard
{
	public static final int SLIDE_WAIT = 200;
	
	public static final int BLOCK_TYPE_BLACK = 0;
	public static final int BLOCK_TYPE_I = 10;
	public static final int BLOCK_TYPE_J = 20;
	public static final int BLOCK_TYPE_L = 30;
	public static final int BLOCK_TYPE_S = 40;
	public static final int BLOCK_TYPE_T = 50;
	public static final int BLOCK_TYPE_Z = 60;
	public static final int BLOCK_TYPE_SQ = 70;
	
	public boolean canMoveInto(Block[] blocks, int inX, int inY);

	public boolean moveSquares(Block[] blocks, int inX, int inY, long sleepTime);

	public void clearAll();

	public boolean canMoveInto(int pixelX, int pixelY);

	public int getSquareSize();

	public int getSquareMargin();

	public int getStartBlockCenterX();

	public int getBoardXForPixelX(int pixelX);
	
	public int getBoardYForPixelY(int pixelY);

	public int getPixelXForBoardX(int boardX);

	public int getPixelYForBoardY(int boardY);

	public boolean lockMe(Block[] blocks);
	
	public boolean lockMe(Block[] blocks, boolean allowSlide);
	
	public void plotPoint(int x, int y, Color c);

	public void drawRegImage(int x, int y, int id, BlockSetInfo setInfo);
	   
	public void repaint();
	
	public void addKeyListener(KeyListener kl);
	
	public Component getComponent();
}
