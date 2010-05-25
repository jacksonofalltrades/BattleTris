package battletris.piece;

import java.awt.Color;

import battletris.Board;
import battletris.Block;

public class FourByFourPiece extends PieceImpl
{
	protected static final Color DEFAULT_COLOR = Color.YELLOW;

	public FourByFourPiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, false);

		m_blocks = new Block[16];

		int i = 0;
		int centerIndex = 0;
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				m_blocks[i] = makeBlockAt(Board.BLOCK_TYPE_SQ, x, y, DEFAULT_COLOR);
				if ((x == 2) && (y == 2))
				{
					centerIndex = i;
				}
				i++;
			}
		}

		// Swap (2,2) with index zero
		Block l_tmp = m_blocks[0];
		m_blocks[0] = m_blocks[centerIndex];
		m_blocks[centerIndex] = l_tmp;
	}
}
