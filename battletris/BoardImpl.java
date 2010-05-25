package battletris;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import battletris.weapon.type.StaticBoardModWeaponType;

import utils.graphics.PointPlotter;


class BoardImpl extends PointPlotter implements Board
{
	private static final long serialVersionUID = 1L;
	protected static final int DEFAULT_SQUARE_SIZE = 20;
	protected static final int DEFAULT_SQUARE_MARGIN = 4;
	protected static final int UNDEF = -2;
	protected static final long BOARD_UPDATE_DELAY = 2000;
	public static final int SLIDE_WAIT = 200;
	

	protected BattleTrisApp m_appRef;

	protected int m_scaleX;
	protected int m_scaleY;
	protected int m_squareSize;
	protected int m_squareMargin;
	protected int m_minBoardX;
	protected int m_maxBoardX;
	protected int m_minBoardY;
	protected int m_maxBoardY;
	protected Block[][] m_filledBlocks;
	
	protected int[] m_pixelXToBoardXCache;
	protected int[] m_pixelYToBoardYCache;
	protected int[] m_boardXToPixelXCache;
	protected int[] m_boardYToPixelYCache;
	
	protected long m_lastBoardUpdate;
	
	protected ArrayList m_weaponList;
	
	protected HashMap m_boardImageDataStoreMap;
	
	//protected BoardImageDataStore m_boardImageDataStore;
	
	public void addWeapon(StaticBoardModWeaponType w)
	{
		m_weaponList.add(w);
	}
	
	public void removeWeapon(StaticBoardModWeaponType w)
	{
		m_weaponList.remove(w);
	}

	public int getMinX()
	{
		return m_minBoardX;
	}

	public int getMaxX()
	{
		return m_maxBoardX;
	}
	
	public int getMinY()
	{
		return m_minBoardY;
	}
	
	public int getMaxY()
	{
		return m_maxBoardY;
	}
	
	public Block[][] getBlocks()
	{
		return m_filledBlocks;
	}
	
	public synchronized void drawBlock(int t, int x, int y, Color c)
	{
		int l_pixelX = this.getPixelXForBoardX(x);
		int l_pixelY = this.getPixelYForBoardY(y);
		Block l_b = new Block(t, l_pixelX, l_pixelY, m_squareSize, c);
		
		l_b.draw(this, null);
		
		m_filledBlocks[x][y] = l_b;
	}
		
	public void drawRegImage(int x, int y, int id, BlockSetInfo setInfo)
	{
		super.drawRegImage(x, y, id);
		
		//if (id == Board.BLOCK_TYPE_BLACK) {
			//System.err.println("BoardImpl::drawRegImage: ["+x+"]["+y+"]");
		//}
		
		BoardImageDataStore l_imgStore = null;
		
		if (null == setInfo) {
			l_imgStore = new BoardImageDataStore(1);
			l_imgStore.addImage(x, y, id);
		}
		else {
			Integer key = new Integer(setInfo.getSeqIndex());
			if (m_boardImageDataStoreMap.containsKey(key)) {
				l_imgStore = (BoardImageDataStore)m_boardImageDataStoreMap.get(key);
			}
			else {
				l_imgStore = new BoardImageDataStore(setInfo.getSize());
				m_boardImageDataStoreMap.put(key, l_imgStore);
			}
			l_imgStore.addImage(x,y,id);
		}

		if (l_imgStore.isFull()) {
			int l_seqIndex;
			if (null == setInfo) {
				l_seqIndex = 600000;
			}
			else {
				l_seqIndex = setInfo.getSeqIndex();
			}
			
			//System.out.println("BoardImpl.drawRegImage: seq index sent="+l_seqIndex);
			
			BoardImageData l_bid = l_imgStore.flush(l_seqIndex);
			m_appRef.sendBoardUpdate(l_bid);
		}		
	}
	
	public void drawImage(int x, int y, BufferedImage img, BlockSetInfo setInfo)
	{
		super.drawImage(x, y, img);
		/*
		long l_newTime = System.currentTimeMillis();
		if (finalSquare && (l_newTime-m_lastBoardUpdate) >= BOARD_UPDATE_DELAY)
		{
			BufferedImage l_snap = this.snapshot();
			Raster l_rast = l_snap.getData();
			Object data = l_rast.getDataElements(0, 0, getWidth(), getHeight(), null);
			m_appRef.sendBoardUpdate(data);
			m_lastBoardUpdate = l_newTime;
		}
		*/
	}
	
	public synchronized void eraseBlock(int x, int y)
	{
		if (null != m_filledBlocks[x][y])
		{
			m_filledBlocks[x][y].erase(this, null);
			m_filledBlocks[x][y] = null;
		}
	}
		
	public synchronized void setScale(int x, int y)
	{
		// TODO: Flip the entire board contents in x and y
		// and set board scale values for future drawing
	}
	
	public boolean canMoveInto(Block[] blocks, int inX, int inY)
	{
		boolean l_canMove = true;
		
		for(int i = 0; i < blocks.length; i++)
		{
			if (null != blocks[i]) 
			{
				int l_oldPixelX = blocks[i].getX();
				int l_oldPixelY = blocks[i].getY();
				int l_oldBoardX = this.getBoardXForPixelX(l_oldPixelX);
				int l_oldBoardY = this.getBoardYForPixelY(l_oldPixelY);
				int l_newPixelX = this.getPixelXForBoardX(l_oldBoardX+inX);
				int l_newPixelY = this.getPixelYForBoardY(l_oldBoardY+inY);
	
				if (false == this.canMoveInto(l_newPixelX, l_newPixelY))
				{
					l_canMove = false;
					break;
				}
			}
		}
		
		return l_canMove;
	}

	public synchronized boolean moveSquares(Block[] blocks, int inX, int inY, long sleepTime)
	{
		if (blocks != null)
		{
			int[] oldX = new int[blocks.length];
			int[] oldY = new int[blocks.length];
			int[] newX = new int[blocks.length];
			int[] newY = new int[blocks.length];
			for(int i = 0; i < blocks.length; i++)
			{
				if (null != blocks[i]) {
					int l_oldPixelX = blocks[i].getX();
					int l_oldPixelY = blocks[i].getY();
					int l_oldBoardX = this.getBoardXForPixelX(l_oldPixelX);
					int l_oldBoardY = this.getBoardYForPixelY(l_oldPixelY);
					int l_newPixelX = this.getPixelXForBoardX(l_oldBoardX+inX);
					int l_newPixelY = this.getPixelYForBoardY(l_oldBoardY+inY);

					if (this.canMoveInto(l_newPixelX, l_newPixelY))
					{
						oldX[i] = l_oldPixelX;
						oldY[i] = l_oldPixelY;
						newX[i] = l_newPixelX;
						newY[i] = l_newPixelY;
					}
					else
					{
						return false;
					}
				}
			}


			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException p_ie)
			{
			}

			BlockSetInfo l_setInfo = new BlockSetInfo(2 * blocks.length);
			
			//boolean finalSquare = false;
			for(int i = 0; i < blocks.length; i++)
			{
				if (null != blocks[i])
				{
					blocks[i].erase(this, l_setInfo);
				}
			}

/*
			for(int i = 0; i < blocks.length; i++)
			{
				int ox = oldX[i];
				int oy = oldY[i];
				this.repaint(ox, oy, m_squareSize, m_squareSize);
			}
			*/

			for(int i = 0; i < blocks.length; i++)
			{
				if (null != blocks[i])
				{
					blocks[i].setNewLocation(newX[i], newY[i]);
					blocks[i].draw(this, l_setInfo);
				}
			}						
			
			this.repaint();

/*
			for(int i = 0; i < blocks.length; i++)
			{
				int ox = oldX[i];
				int oy = oldY[i];
				int nx = newX[i];
				int ny = newY[i];

				this.repaint(ox, oy, m_squareSize, m_squareSize);
				this.repaint(nx, ny, m_squareSize, m_squareSize);
			}
*/

			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isLineFull(int boardY)
	{
		boolean l_isLineFull = true;
		for(int i = m_minBoardX; i <= m_maxBoardX; i++)
		{
			if (m_filledBlocks[i][boardY] == null)
			{
				l_isLineFull = false;
				break;
			}
		}

		return l_isLineFull;
	}

	protected void clearLines()
	{
		int l_linesCleared = 0;
		int l_totalValue = 0;

		for(int y = m_maxBoardY; y >= m_minBoardY; y--)
		{
			if (isLineFull(y)) {

				do
				{
					l_linesCleared++;

					// Erase line
					BlockSetInfo l_setInfo = new BlockSetInfo((m_maxBoardX-m_minBoardX)+1);
					
					//boolean finalSquare = false;
					for(int x = m_minBoardX; x <= m_maxBoardX; x++)
					{
						if (null == m_filledBlocks[x][y])
						{
							throw new RuntimeException("Line should be full!!");
						}
						else
						{
							//if (m_maxBoardX == x) {
								//finalSquare = true;
							//}
							l_totalValue += m_filledBlocks[x][y].getValue();
							m_filledBlocks[x][y].erase(this, l_setInfo);
							m_filledBlocks[x][y] = null;
						}
					}

					this.repaint();

					// MIGHT NOT NEED THIS: this.repaint();

					// Move blocks down
					Block[] l_blockToMove = new Block[1];
					//Block[] l_lineToMove = new Block[m_maxBoardX];
					for(int j = (y-1); j >= m_minBoardY; j--)
					{
						//int l_arrIndex = 0;

						boolean l_isLineEmpty = true;

						for(int i = m_minBoardX; i <= m_maxBoardX; i++)
						{
							Block l_block = m_filledBlocks[i][j];
							if (l_block != null)
							{
								l_isLineEmpty = false;

								m_filledBlocks[i][j] = null;

								//l_lineToMove[l_arrIndex] = l_block;
								l_blockToMove[0] = l_block;

								this.moveSquares(l_blockToMove, 0, 1, 0);

								m_filledBlocks[i][j+1] = l_block;

							}
							//l_arrIndex++;

						}

						if (l_isLineEmpty)
						{
							break;
						}
					}

				} while(isLineFull(y));
			}
		}

		if ((l_linesCleared > 0) || (l_totalValue > 0))
		{
			m_appRef.reportPlayerLinesAndValues(l_linesCleared, l_totalValue);
		}
		
		// Call weapons
		for(int i = 0; i < m_weaponList.size(); i++)
		{
			StaticBoardModWeaponType l_weapon = (StaticBoardModWeaponType)m_weaponList.get(i);
			
			l_weapon.modifyBoard(this);
		}
	}

	public BoardImpl(BattleTrisApp app, int w, int h)
	throws Exception
	{
		this(app, w, h, DEFAULT_SQUARE_SIZE, DEFAULT_SQUARE_MARGIN);
	}

	public BoardImpl(BattleTrisApp app, int w, int h, int squareSize, int squareMargin)
	throws Exception
	{
		super(w, h);
		this.setBuffer(w, h);
		m_squareSize = squareSize;
		m_squareMargin = squareMargin;
		
		m_weaponList = new ArrayList();

		m_appRef = app;
		
		m_lastBoardUpdate = 0;
		
		m_boardImageDataStoreMap = new HashMap();
		//m_boardImageDataStore = new BoardImageDataStore();
		
		// Create block template images
		Block.setTypeBlockImage(Board.BLOCK_TYPE_I, Color.BLUE, "trisblock-blu.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_J, Color.RED, "trisblock-red.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_L, Color.GREEN, "trisblock-grn.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_S, Color.MAGENTA, "trisblock-purp.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_T, Color.CYAN, "trisblock-aqua.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_Z, Color.ORANGE, "trisblock-orange.jpg", m_squareSize);		
		Block.setTypeBlockImage(Board.BLOCK_TYPE_SQ, Color.YELLOW, "trisblock-orange.jpg", m_squareSize);
		Block.setTypeBlockImage(Board.BLOCK_TYPE_BLACK, Color.BLACK, null, m_squareSize);
		
		Block.setTypeBlockImage(DiceBlock.TYPE_ONE, Color.WHITE, "dice-1.jpg", m_squareSize);
		Block.setTypeBlockImage(DiceBlock.TYPE_TWO, Color.WHITE, "dice-2.jpg", m_squareSize);
		Block.setTypeBlockImage(DiceBlock.TYPE_THREE, Color.WHITE, "dice-3.jpg", m_squareSize);
		Block.setTypeBlockImage(DiceBlock.TYPE_FOUR, Color.WHITE, "dice-4.jpg", m_squareSize);
		Block.setTypeBlockImage(DiceBlock.TYPE_FIVE, Color.WHITE, "dice-5.jpg", m_squareSize);
		Block.setTypeBlockImage(DiceBlock.TYPE_SIX, Color.WHITE, "dice-6.jpg", m_squareSize);
		

		// Throw an exception if w and h are not both perfect
		// multiples of squareSize
		if (getWidth()%(m_squareSize+m_squareMargin) != 0)
		{
			throw new Exception("Width must be multiple of square size+margin");
		}

		if (getHeight()%(m_squareSize+m_squareMargin) != 0)
		{
			throw new Exception("Height must be multiple of square size+margin");
		}

		// Make empty filled blocks
		int l_boardW = this.getBoardXForPixelX(getWidth(), false) + 2;
		int l_boardH = this.getBoardYForPixelY(getHeight(), false) + 2;
		
		int l_pixelW = this.getPixelXForBoardX(l_boardW, false);
		int l_pixelH = this.getPixelYForBoardY(l_boardH, false);

		m_pixelXToBoardXCache = new int[l_pixelW];
		m_pixelYToBoardYCache = new int[l_pixelH];

		m_boardXToPixelXCache = new int[l_boardW];
		m_boardYToPixelYCache = new int[l_boardH];
		
		// Initialize cache to UNDEF
		Arrays.fill(m_pixelXToBoardXCache, UNDEF);
		Arrays.fill(m_pixelYToBoardYCache, UNDEF);
		Arrays.fill(m_boardXToPixelXCache, UNDEF);
		Arrays.fill(m_boardYToPixelYCache, UNDEF);

		m_minBoardX = 1;
		m_minBoardY = 1;
		m_maxBoardX = l_boardW-2;
		m_maxBoardY = l_boardH-2;

		try
		{
		for(int x = 0; x < l_boardW; x++)
		{
			int l_pixelX = this.getPixelXForBoardX(x, false);
			m_pixelXToBoardXCache[l_pixelX] = x;
			
			m_boardXToPixelXCache[x] = l_pixelX;
		}
		
		for(int y = 0; y < l_boardH; y++)
		{
			int l_pixelY = this.getPixelYForBoardY(y, false);
			m_pixelYToBoardYCache[l_pixelY] = y;
			
			m_boardYToPixelYCache[y] = l_pixelY;
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace(System.err);
		}
		
		System.out.println("Board Width="+String.valueOf(l_boardW));
		System.out.println("Board Height="+String.valueOf(l_boardH));

		m_filledBlocks = new Block[l_boardW][l_boardH];

		int l_pixelX = this.getPixelXForBoardX(0);

		// Initial filled blocks
		// (0,0) to (0,m_height)
		// (m_width,0) to (m_width,m_height)
		// (0,m_height) to (m_width,m_height)
		for(int i = 0; i < l_boardH; i++)
		{
			int l_pixelY = this.getPixelYForBoardY(i);
			m_filledBlocks[0][i] = new Block(Board.BLOCK_TYPE_BLACK, l_pixelX, l_pixelY, m_squareSize, Color.WHITE);
		}

		l_pixelX = this.getPixelXForBoardX(l_boardW-1);

		for(int i = 0; i < l_boardH; i++)
		{
			int l_pixelY = this.getPixelYForBoardY(i);
			m_filledBlocks[l_boardW-1][i] = new Block(Board.BLOCK_TYPE_BLACK, l_pixelX, l_pixelY, m_squareSize, Color.WHITE);
		}

		int l_pixelY = this.getPixelYForBoardY(l_boardH-1);

		for(int i = 0; i < l_boardW; i++)
		{
			l_pixelX = this.getPixelXForBoardX(i);

			m_filledBlocks[i][l_boardH-1] = new Block(Board.BLOCK_TYPE_BLACK, l_pixelX, l_pixelY, m_squareSize, Color.WHITE);
		}
	}

	public void clearAll()
	{
		/*
		m_minBoardX = 1;
		m_minBoardY = 1;
		m_maxBoardX = l_boardW-2;
		m_maxBoardY = l_boardH-2;
		*/
		//boolean finalSquare = false;
		for(int x = m_minBoardX; x <= m_maxBoardX; x++)
		{
			for(int y = m_minBoardY; y <= m_maxBoardY; y++)
			{
				if (null != m_filledBlocks[x][y])
				{
					/*
					if (m_maxBoardX == x && m_maxBoardY == y) {
						finalSquare = true;
					}
					*/
					
					m_filledBlocks[x][y].erase(this, null);
					m_filledBlocks[x][y] = null;
				}
			}
		}

		this.repaint();
	}

	public boolean canMoveInto(int pixelX, int pixelY)
	{
		int l_boardX = this.getBoardXForPixelX(pixelX);
		int l_boardY = this.getBoardYForPixelY(pixelY);

		return (m_filledBlocks[l_boardX][l_boardY] == null);
	}

	public int getSquareSize()
	{
		return m_squareSize;
	}

	public int getSquareMargin()
	{
		return m_squareMargin;
	}

	public int getStartBlockCenterX()
	{
		int l_numSquares = getWidth()/(m_squareSize+m_squareMargin);
		return Math.round((float)l_numSquares/2f);
	}

	public int getBoardXForPixelX(int pixelX)
	{
		return getBoardXForPixelX(pixelX, true);
	}
	
	public int getBoardXForPixelX(int pixelX, boolean useCache)
	{
		if (useCache)
		{
			return m_pixelXToBoardXCache[pixelX];
		}
		else
		{
			return ((pixelX + m_squareSize) / (m_squareSize + m_squareMargin));
		}
	}

	public int getBoardYForPixelY(int pixelY)
	{
		return getBoardYForPixelY(pixelY, true);
	}

	public int getBoardYForPixelY(int pixelY, boolean useCache)
	{
		if (useCache)
		{
			return m_pixelYToBoardYCache[pixelY];
		}
		else
		{
			return ((pixelY + m_squareSize) / (m_squareSize + m_squareMargin));
		}
	}

	public int getPixelXForBoardX(int boardX)
	{
		return getPixelXForBoardX(boardX, true);
	}

	public int getPixelXForBoardX(int boardX, boolean useCache)
	{
		if (useCache)
		{
			return m_boardXToPixelXCache[boardX];
		}
		else
		{
			if (boardX <= 0)
			{
				return 0;
			}
			else
			{
				return (boardX)*(m_squareSize+m_squareMargin) - m_squareSize;
			}
		}
	}

	public int getPixelYForBoardY(int boardY)
	{
		return getPixelYForBoardY(boardY, true);
	}
	
	public int getPixelYForBoardY(int boardY, boolean useCache)
	{
		if (useCache)
		{
			return m_boardYToPixelYCache[boardY];
		}
		else
		{
			if (boardY <= 0)
			{
				return 0;
			}
			else
			{
				return (boardY)*(m_squareSize+m_squareMargin) - m_squareSize;
			}
		}
	}

	public boolean lockMe(Block[] blocks)
	{
		return lockMe(blocks, true);
	}

	public boolean lockMe(Block[] blocks, boolean allowSlide)
	{
		if (blocks != null)
		{
			if (allowSlide)
			{
				try
				{
					Thread.sleep(Board.SLIDE_WAIT);
				}
				catch(InterruptedException ie)
				{
				}
			
				if (this.canMoveInto(blocks, 0, 1))
				{
					return true;
				}
			}
			
			boolean l_hitTop = false;

			for(int i = 0; i < blocks.length; i++)
			{
				int l_boardX = this.getBoardXForPixelX(blocks[i].getX());
				int l_boardY = this.getBoardYForPixelY(blocks[i].getY());

				if (l_boardY <= m_minBoardY)
				{
					l_hitTop = true;
				}

				m_filledBlocks[l_boardX][l_boardY] = blocks[i];
			}

			this.clearLines();

			if (l_hitTop)
			{
				m_appRef.endGame();
			}
			else
			{
				m_appRef.startNextPiece();
			}
		}
		
		return false;
	}
	
	public Component getComponent()
	{
		return this;
	}
}
