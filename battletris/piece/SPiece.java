package battletris.piece;

import java.awt.Color;

import battletris.Board;
import battletris.Block;

public class SPiece extends PieceImpl
{
	protected static final Color DEFAULT_COLOR = Color.MAGENTA;

	public SPiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, true);

		m_blocks = new Block[4];

		// center
		m_blocks[0] = makeBlockAt(Board.BLOCK_TYPE_S, 0, 0, DEFAULT_COLOR);

		// right
		m_blocks[3] = makeBlockAt(Board.BLOCK_TYPE_S, 1, 0, DEFAULT_COLOR);

		// bottom
		m_blocks[2] = makeBlockAt(Board.BLOCK_TYPE_S, 0, 1, DEFAULT_COLOR);

		// bottom left
		m_blocks[1] = makeBlockAt(Board.BLOCK_TYPE_S, -1, 1, DEFAULT_COLOR);
	}
}
