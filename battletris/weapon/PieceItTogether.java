package battletris.weapon;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import battletris.Board;
import battletris.ModifiableBoard;
import battletris.weapon.target.WeaponTarget;

public class PieceItTogether extends RandomBlockWeapon
{
	protected static final int[] TYPES = new int[]{
		Board.BLOCK_TYPE_I,
		Board.BLOCK_TYPE_J,
		Board.BLOCK_TYPE_L,
		Board.BLOCK_TYPE_S,
		Board.BLOCK_TYPE_SQ,
		Board.BLOCK_TYPE_T,
		Board.BLOCK_TYPE_Z};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int getRandomType()
	{
		Random l_rand = new Random();
		
		return TYPES[l_rand.nextInt(TYPES.length)];
	}

	public PieceItTogether(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public void visit(WeaponTarget wt)
	{
		ModifiableBoard l_board = (ModifiableBoard)wt.getTarget();

		// Stay below top 25% of board
		int l_minX = l_board.getMinX();
		int l_minY = (int)((float)(l_board.getMaxY() - l_board.getMinY())*0.25);
		int l_maxX = l_board.getMaxX();
		int l_maxY = l_board.getMaxY();
		
		Dimension l_blockToModify = this.getRandomBlockDimension(l_board, true, l_minX, l_minY, l_maxX, l_maxY);
		
		if (null != l_blockToModify)
		{		
			int type = getRandomType();
			Color l_color = Color.WHITE;
						
			l_board.drawBlock(type, (int)l_blockToModify.getWidth(), (int)l_blockToModify.getHeight(), l_color);
		}		
		this.deactivate();
	}

	public Object clone()
	{
		PieceItTogether l_other = new PieceItTogether(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
