package battletris.piece;

import java.awt.Color;
import java.util.ArrayList;

import battletris.Block;
import battletris.Board;
import battletris.BlockSetInfo;

abstract class PieceImpl implements Runnable, Piece
{
	protected static final long DEFAULT_INITIAL_FALL_DELAY = 500;
	protected static final long DEFAULT_DROP_FALL_DELAY = 10;
	protected static final long DEAD_WAIT = 500;
	protected Color m_color;
	protected Block[] m_blocks;
	protected Board m_boardRef;
	protected int m_squareSize;
	protected int m_squareMargin;
	protected long m_fallDelayTime;
	protected int m_centerX;
	protected int m_centerY;
	protected boolean m_allowSlide;
	protected boolean m_isDropping;
	protected boolean m_isRotatable;
	protected boolean m_isPaused;
	protected boolean m_isStopped;
	
	protected ArrayList m_pieceMovementModifierList;

	protected void draw()
	{
		if (m_blocks != null)
		{
			BlockSetInfo l_setInfo = new BlockSetInfo(m_blocks.length);
			//boolean finalSquare = false;
			for(int i = 0; i < m_blocks.length; i++)
			{
				/*
				if (i == (m_blocks.length-1)) {
					finalSquare = true;
				}
				*/
				m_blocks[i].draw(m_boardRef, l_setInfo);
			}
		}
	}

	protected void erase()
	{
		if (m_blocks != null)
		{
			//boolean finalSquare = false;
			BlockSetInfo l_setInfo = new BlockSetInfo(m_blocks.length);
			for(int i = 0; i < m_blocks.length; i++)
			{
				/*
				if (i == (m_blocks.length-1)) {
					finalSquare = true;
				}
				*/
				m_blocks[i].erase(m_boardRef, l_setInfo);
			}
		}
	}

	protected void fallDelay()
	{
		try
		{
			Thread.sleep(m_fallDelayTime);
		}
		catch(InterruptedException p_ex)
		{
			System.out.println("PieceImpl::fallDelay: Interrupted!");
		}
	}

	protected boolean fallOneLevel()
	{
		return this.moveSquares(0, 1, 10);
	}

	protected Block makeBlockAt(int type, int relativeX, int relativeY, Color color)
	{
		Block l_block = new Block(type, m_boardRef.getPixelXForBoardX(m_centerX+relativeX),
					  m_boardRef.getPixelYForBoardY(m_centerY+relativeY),
					  m_squareSize,
					  color);
		return l_block;
	}
	
	protected void addBlockAt(int relativeX, int relativeY, Color color)
	{
		Block[] l_oldBlockArr = m_blocks;
		m_blocks = new Block[l_oldBlockArr.length+1];
	}
	
	protected void addBlocks(Block[] newBlocks)
	{
		Block[] l_oldBlockArr = m_blocks;
		
		m_blocks = new Block[l_oldBlockArr.length+newBlocks.length];
	
		System.arraycopy(l_oldBlockArr, 0, m_blocks, 0, m_blocks.length);
		System.arraycopy(newBlocks, 0, m_blocks, l_oldBlockArr.length, newBlocks.length);
	}

	protected synchronized boolean moveSquares(int inX, int inY, long sleepTime)
	{
		return m_boardRef.moveSquares(m_blocks, inX, inY, sleepTime);
	}

	PieceImpl(Board b, int squareSize, int squareMargin, int startBlockX, int startBlockY, Color color, boolean isRotatable)
	{
		m_boardRef = b;
		m_color = color;
		m_squareSize = squareSize;
		m_squareMargin = squareMargin;
		m_centerX = startBlockX;
		m_centerY = startBlockY;
		m_fallDelayTime = DEFAULT_INITIAL_FALL_DELAY;
		m_isDropping = false;
		m_allowSlide = true;
		m_isRotatable = isRotatable;
		m_isPaused = false;
		m_isStopped = false;
		
		m_pieceMovementModifierList = new ArrayList();
	}
	
	public void addPieceMovementModifier(PieceMovementModifier pmm)
	{
		pmm.init(this);
		
		m_pieceMovementModifierList.add(pmm);
	}

	public void run()
	{
		boolean l_done = false;
		while(!l_done)
		{
			if (m_isStopped)
			{
				l_done = true;
			}
			else
			{
				while(m_isPaused && !m_isStopped)
				{
					try
					{
						Thread.sleep(2000);
					}
					catch(InterruptedException ie)
					{
						break;
					}
				}
				if (m_isStopped)
				{
					l_done = true;
				}				
				else 
				{
					boolean l_canFall = fallOneLevel();
					if (!l_canFall)
					{
						l_canFall = m_boardRef.lockMe(m_blocks, m_allowSlide);
					}
					
					if (l_canFall)
					{
						this.fallDelay();
					}
					else
					{
						l_done = true;
					}
				}
				/*	
				else if (!fallOneLevel())
				{
					m_boardRef.lockMe(m_blocks);
					l_done = true;
				}
				else
				{
					this.fallDelay();
				}
				*/
			}
		}
		
		// Clean up any modifiers
		for(int i = 0; i < m_pieceMovementModifierList.size(); i++)
		{
			PieceMovementModifier l_pmm = (PieceMovementModifier)m_pieceMovementModifierList.get(i);
			l_pmm.finish();
		}
	}
	
	public void setPause(boolean pause)
	{
		m_isPaused = pause;
	}

	public void togglePause()
	{
		if (m_isPaused)
		{
			m_isPaused = false;
		}
		else
		{
			m_isPaused = true;
		}
	}

	public void stop()
	{
		m_isStopped = true;
	}

	public void create()
	{
		this.draw();
	}

	public void delete()
	{
		this.erase();
	}

	public void moveLeft()
	{
		if (!m_isPaused && !m_isStopped)
		{
			if (m_allowSlide || !m_isDropping)
			{
				this.moveSquares(-1,0, 0);
			}
		}
	}

	public void moveRight()
	{
		if (!m_isPaused && !m_isStopped)
		{
			if (m_allowSlide || !m_isDropping)
			{
				this.moveSquares(1,0, 0);
			}
		}
	}
	
	public void drop()
	{
		drop(DEFAULT_DROP_FALL_DELAY);
	}

	public void drop(long dropDelay)
	{
		if (!m_isPaused && !m_isStopped)
		{
			m_fallDelayTime = dropDelay;
			m_isDropping = true;
		}
	}

	protected void rotate(boolean cw)
	{
		if (m_isRotatable && !m_isPaused && !m_isStopped)
		{
			if (m_allowSlide || !m_isDropping)
			{
				if ((null != m_blocks) && (m_blocks.length > 1))
				{
					Block l_center = m_blocks[0];
	
					int l_centerX1 = l_center.getX();
					int l_centerY1 = l_center.getY();
	
					boolean l_canMove = true;
	
					int[] l_newX = new int[m_blocks.length-1];
					int[] l_newY = new int[m_blocks.length-1];
					for(int i = 0; i < m_blocks.length-1; i++)
					{
						int l_x = m_blocks[i+1].getX();
						int l_y = m_blocks[i+1].getY();
	
						int l_newPixelX = 0;
						int l_newPixelY = 0;
						if (cw)
						{
							l_newPixelX = l_centerX1 + l_centerY1 - l_y;
							l_newPixelY = l_centerY1 - l_centerX1 + l_x;
						}
						else
						{
							l_newPixelX = l_centerX1 - l_centerY1 + l_y;
							l_newPixelY = l_centerY1 + l_centerX1 - l_x;
						}
	
						if (m_boardRef.canMoveInto(l_newPixelX, l_newPixelY))
						{
							int l_boardX = m_boardRef.getBoardXForPixelX(l_x);
							int l_boardY = m_boardRef.getBoardYForPixelY(l_y);
	
							int l_newBoardX = m_boardRef.getBoardXForPixelX(l_newPixelX);
							int l_newBoardY = m_boardRef.getBoardYForPixelY(l_newPixelY);
	
							int l_deltaBoardX = l_newBoardX - l_boardX;
							int l_deltaBoardY = l_newBoardY - l_boardY;
	
							l_newX[i] = l_deltaBoardX;
							l_newY[i] = l_deltaBoardY;
						}
						else
						{
							l_canMove = false;
						}
					}
	
					if (l_canMove)
					{
						Block[] l_toMove = new Block[1];
						for(int i = 0; i < m_blocks.length-1; i++)
						{
							l_toMove[0] = m_blocks[i+1];
	
							m_boardRef.moveSquares(l_toMove, l_newX[i], l_newY[i], 0);
						}
					}
				}
			}
		}
	}

	public void rotateCW()
	{
		this.rotate(true);
	}

	public void rotateCCW()
	{
		this.rotate(false);
	}
	
	public void setAllowSlide(boolean allowSlide)
	{
		m_allowSlide = allowSlide;
	}
}
