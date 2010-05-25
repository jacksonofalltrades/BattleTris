package battletris.weapon;

import java.io.Serializable;

import battletris.InventoryItem;
import battletris.weapon.type.WeaponType;

abstract public class Weapon implements Cloneable, Serializable, InventoryItem, WeaponType
{
	protected String m_name;
	protected String m_description;
	protected Integer m_duration;
	protected Integer m_cost;

	protected boolean m_isActive;
	protected int m_remainingLines;

	public Weapon(String name, String desc, Integer duration, Integer cost)
	{
		m_name = name;
		m_description = desc;
		m_duration = duration;
		m_cost = cost;

		m_isActive = true;

		if (getDuration() > 0)
		{
			m_remainingLines = getDuration();
		}
		else
		{
			m_remainingLines = Integer.MAX_VALUE;
		}
	}

	abstract public Class getWeaponType();

	public void decrementDuration(int lines)
	{
		if (m_remainingLines < Integer.MAX_VALUE)
		{
			if (m_remainingLines > 0)
			{
				m_remainingLines -= lines;
				if (m_remainingLines <= 0)
				{
					m_remainingLines = 0;
					m_isActive = false;
				}
			}
		}
	}

	public boolean isActive()
	{
		return m_isActive;
	}

	public void deactivate()
	{
		m_isActive = false;
	}

	public String getName()
	{
		return m_name;
	}

	public String getDesc()
	{
		return m_description;
	}

	public int getDuration()
	{
		return m_duration.intValue();
	}

	public int getCost()
	{
		return m_cost.intValue();
	}
	
	void setCost(int cost)
	{
		m_cost = new Integer(cost);
	}

	public Object clone()
	{
		throw new RuntimeException("Must be called on a subclass of Weapon!!!");
	}
}
