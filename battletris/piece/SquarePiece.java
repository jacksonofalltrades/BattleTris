package battletris.piece;

import java.awt.Color;

import battletris.Board;
import battletris.Block;

public class SquarePiece extends PieceImpl
{
	protected static final Color DEFAULT_COLOR = Color.YELLOW;

	public SquarePiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, false);

		m_blocks = new Block[4];

		// top left
		m_blocks[0] = makeBlockAt(Board.BLOCK_TYPE_SQ, 0, 0, DEFAULT_COLOR);

		// top right
		m_blocks[1] = makeBlockAt(Board.BLOCK_TYPE_SQ, 1, 0, DEFAULT_COLOR);

		// bottom left
		m_blocks[2] = makeBlockAt(Board.BLOCK_TYPE_SQ, 0, 1, DEFAULT_COLOR);

		// bottom right
		m_blocks[3] = makeBlockAt(Board.BLOCK_TYPE_SQ, 1, 1, DEFAULT_COLOR);
	}
}
