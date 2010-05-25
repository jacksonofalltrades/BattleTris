package battletris.piece;

import java.util.Random;
import java.awt.Color;

import battletris.Board;
import battletris.Block;
import battletris.DiceBlock;

public class MoneyPiece extends PieceImpl
{
	protected static final Color DEFAULT_COLOR = Color.WHITE;

	public MoneyPiece(Board b)
	{
		super(b, b.getSquareSize(), b.getSquareMargin(), b.getStartBlockCenterX(), 2, DEFAULT_COLOR, false);

		m_blocks = new Block[1];

		Random l_rand = new Random();
		int l_val = l_rand.nextInt(6)+1;

		// one piece
		m_blocks[0] = new DiceBlock(b.getPixelXForBoardX(m_centerX),
							  		b.getPixelYForBoardY(m_centerY),
									m_squareSize,
									l_val);
	}
}
