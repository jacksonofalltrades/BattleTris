package battletris;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import utils.graphics.PointPlotter;

import battletris.weapon.type.StaticBoardModWeaponType;

public class OpponentBoardImpl extends PointPlotter implements Board 
{
	private static final long serialVersionUID = 1L;
	
	protected static final int NO_SEQ_IND = -1;
	
	protected OpponentRenderThread m_renderThread;
		
	public OpponentBoardImpl(int width, int height)
	{
		super(width, height);
		this.setBuffer(width, height);
		m_renderThread = new OpponentRenderThread(this);
		m_renderThread.start();
	}

	public boolean canMoveInto(Block[] blocks, int inX, int inY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean moveSquares(Block[] blocks, int inX, int inY, long sleepTime) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clearAll() 
	{
		super.clear();
		/*
		BufferedImage l_img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		super.drawImage(0, 0, l_img);
		*/
	}
	
	public void drawRegImage(int x, int y, int id, BlockSetInfo setInfo)
	{
	}
	public synchronized void drawBoardImageData(int[] x, int[] y, int[] id)
	{
		int n = x.length;
		for(int i = 0; i < n; i++)
		{
			if (BoardImageData.EMPTY == x[i] || (BoardImageData.EMPTY == y[i]) || BoardImageData.EMPTY == id[i]) {
				break;
			}
			else {
				
				/*
				if (Board.BLOCK_TYPE_BLACK == id[i]) {
					System.out.println("OpponentBoardImpl::drawBoardImageData: ["+x[i]+"]["+y[i]+"]");
				}
				*/
				BufferedImage img = getRegImage(id[i]);
				drawImage(x[i], y[i], img);
			}
		}
		repaint();					
	}
	
	public synchronized void drawBoardImageData(int seqIndex, int[] x, int[] y, int[] id)
	{
		m_renderThread.addData(seqIndex, x, y, id);
		//System.out.println("OpponentBoardImpl::drawBoardImageData: block set seq index="+seqIndex);			
	}
	
	public boolean canMoveInto(int pixelX, int pixelY) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getSquareSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSquareMargin() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getStartBlockCenterX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBoardXForPixelX(int pixelX) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBoardYForPixelY(int pixelY) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPixelXForBoardX(int boardX) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPixelYForBoardY(int boardY) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean lockMe(Block[] blocks) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean lockMe(Block[] blocks, boolean allowSlide) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addKeyListener(KeyListener kl) {
		// TODO Auto-generated method stub

	}

	public Component getComponent() {
		return this;
	}

	public Block[][] getBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMinX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void drawBlock(int t, int x, int y, Color c) {
		// TODO Auto-generated method stub

	}

	public void eraseBlock(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void setScale(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void addWeapon(StaticBoardModWeaponType wt) {
		// TODO Auto-generated method stub

	}

	public void removeWeapon(StaticBoardModWeaponType wt) {
		// TODO Auto-generated method stub

	}

}
