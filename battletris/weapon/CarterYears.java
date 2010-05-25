package battletris.weapon;

import battletris.Bazaar;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.BazaarModWeaponType;

public class CarterYears extends Weapon implements BazaarModWeaponType
{
	protected boolean m_usedOnce;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CarterYears(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
		
		m_usedOnce = false;
	}

	public void decrementDuration(int lines)
	{
		if (m_usedOnce)
		{
			super.decrementDuration(lines);
		}
	}

	public Class getWeaponType()
	{
		return BazaarModWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
		Bazaar l_bazaar = (Bazaar)wt.getTarget();
		
		l_bazaar.addWeapon(this);
	}
	
	public Weapon modifyWeapon(Weapon w)
	{
		m_usedOnce = true;
		
		Weapon l_new = (Weapon)w.clone();
		l_new.setCost(2*w.getCost());
		
		return l_new;
	}
	
	public Object clone()
	{
		CarterYears l_other = new CarterYears(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
