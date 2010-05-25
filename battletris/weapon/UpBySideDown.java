package battletris.weapon;

import battletris.ModifiableBoard;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.StaticBoardModWeaponType;

public class UpBySideDown extends Weapon implements StaticBoardModWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpBySideDown(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public Class getWeaponType()
	{
		return StaticBoardModWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
	}

	public void modifyBoard(ModifiableBoard board)
	{
		// If i am active, make sure the scale is set upside down
		// If i am inactive reset scale and remove myself from the board 
	}
	
	public Object clone()
	{
		UpBySideDown l_other = new UpBySideDown(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
