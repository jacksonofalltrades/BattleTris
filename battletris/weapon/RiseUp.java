package battletris.weapon;

import battletris.ModifiableBoard;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.StaticBoardModWeaponType;

public class RiseUp extends Weapon implements StaticBoardModWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiseUp(String name, String desc, Integer duration, Integer cost)
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
		// TODO: Fill this in
	}

	public Object clone()
	{
		RiseUp l_other = new RiseUp(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
