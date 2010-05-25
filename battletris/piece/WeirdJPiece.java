package battletris.piece;

import java.awt.Color;

import battletris.Block;
import battletris.Board;

public class WeirdJPiece extends PieceImpl 
{
	protected static final Color DEFAULT_COLOR = JPiece.DEFAULT_COLOR;

	public WeirdJPiece(Board b)
	{	
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, true);
	
		m_blocks = new Block[4];

		// center
		m_blocks[0] = makeBlockAt(Board.BLOCK_TYPE_J, 0, 0, DEFAULT_COLOR);

		// top
		m_blocks[1] = makeBlockAt(Board.BLOCK_TYPE_J, 0, -1, DEFAULT_COLOR);

		// bottom
		m_blocks[2] = makeBlockAt(Board.BLOCK_TYPE_J, 0, 2, DEFAULT_COLOR);

		// bottom left
		m_blocks[3] = makeBlockAt(Board.BLOCK_TYPE_J, -1, 1, DEFAULT_COLOR);
	
	}

}
