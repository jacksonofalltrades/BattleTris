package battletris.piece;

import java.awt.Color;

import battletris.Block;
import battletris.Board;

public class WeirdLPiece extends PieceImpl 
{
	protected static final Color DEFAULT_COLOR = LPiece.DEFAULT_COLOR;

	public WeirdLPiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, true);
		
		m_blocks = new Block[4];

		// center
		m_blocks[0] = makeBlockAt(Board.BLOCK_TYPE_L, 0, 0, DEFAULT_COLOR);

		// top
		m_blocks[1] = makeBlockAt(Board.BLOCK_TYPE_L, 0, -1, DEFAULT_COLOR);

		// bottom
		m_blocks[2] = makeBlockAt(Board.BLOCK_TYPE_L, 2, 0, DEFAULT_COLOR);

		// bottom right
		m_blocks[3] = makeBlockAt(Board.BLOCK_TYPE_L, 2, -1, DEFAULT_COLOR);		
	}

}
