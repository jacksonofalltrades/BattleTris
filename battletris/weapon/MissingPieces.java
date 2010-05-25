package battletris.weapon;

import java.awt.Dimension;
import battletris.ModifiableBoard;
import battletris.weapon.target.WeaponTarget;

public class MissingPieces extends RandomBlockWeapon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MissingPieces(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public void visit(WeaponTarget wt)
	{
		ModifiableBoard l_board = (ModifiableBoard)wt.getTarget();
		
		Dimension l_blockToModify = this.getRandomBlockDimension(l_board, false);
		
		if (null != l_blockToModify)
		{				
			l_board.eraseBlock((int)l_blockToModify.getWidth(), (int)l_blockToModify.getHeight());
		}
		this.deactivate();
	}

	public Object clone()
	{
		MissingPieces l_other = new MissingPieces(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
