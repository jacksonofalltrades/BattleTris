package battletris.weapon;

import starwarp.net.NetworkException;

import battletris.player.Player;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.PlayerModWeaponType;

public class ReaganEra extends Weapon implements PlayerModWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaganEra(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public Class getWeaponType()
	{
		return PlayerModWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
		Player l_player = (Player)wt.getTarget();
		
		try
		{
			int l_funds = l_player.getMoney();
			l_player.update(0, 0, -l_funds);
			
			// NEED TO FIX THIS
			//l_player.playerUpdate()
		}
		catch(NetworkException ne)
		{
			// Weird
		}
		
		this.deactivate();
	}

	public Object clone()
	{
		ReaganEra l_other = new ReaganEra(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
