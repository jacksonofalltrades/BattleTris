package battletris.piece;

import java.awt.Color;

import battletris.Board;
import battletris.Block;

public class WeirdTPiece extends PieceImpl
{
	protected static final Color DEFAULT_COLOR = TPiece.DEFAULT_COLOR;

	public WeirdTPiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, true);

		m_blocks = new Block[4];

		// center
		m_blocks[1] = makeBlockAt(Board.BLOCK_TYPE_T, 1, 2, DEFAULT_COLOR);

		// top
		m_blocks[0] = makeBlockAt(Board.BLOCK_TYPE_T, 0, 0, DEFAULT_COLOR);

		// left
		m_blocks[2] = makeBlockAt(Board.BLOCK_TYPE_T, -1, 1, DEFAULT_COLOR);

		// right
		m_blocks[3] = makeBlockAt(Board.BLOCK_TYPE_T, 1, 1, DEFAULT_COLOR);
	}
}
