package battletris;

import java.util.ArrayList;

import battletris.weapon.Weapon;
import battletris.weapon.WeaponListParser;
import battletris.weapon.type.BazaarModWeaponType;

public class Bazaar implements BazaarInfo
{
	protected static final int DEFAULT_NUM_LINES_TO_NEXT_BAZAAR = 20;

	protected int m_numLinesToNextBazaar;

	protected ArrayList m_weaponList;
	
	protected ArrayList m_bazaarActiveWeaponList;

	public Bazaar()
	{
		this(null);
	}

	public Bazaar(String configPath)
	{
		if (BattleTrisApp.isTestMode())
		{
			m_numLinesToNextBazaar = 10;
		}
		else
		{
			m_numLinesToNextBazaar = DEFAULT_NUM_LINES_TO_NEXT_BAZAAR;
		}

		m_weaponList = new ArrayList();

		if (configPath != null)
		{
			WeaponListParser l_wlp = new WeaponListParser();
			m_weaponList = l_wlp.getWeaponList(configPath);
		}
		
		m_bazaarActiveWeaponList = new ArrayList();
	}
	
	public void addWeapon(BazaarModWeaponType w)
	{
		m_bazaarActiveWeaponList.add(w);
	}

	public ArrayList getWeaponList()
	{
		ArrayList l_retList = new ArrayList(m_weaponList.size());
		
		for(int i = 0; i < m_weaponList.size(); i++)
		{
			Weapon l_weapon = (Weapon)m_weaponList.get(i);
			
			ArrayList l_listCopy = new ArrayList(m_bazaarActiveWeaponList);
			for(int j = 0; j < l_listCopy.size(); j++)
			{
				BazaarModWeaponType l_aw = (BazaarModWeaponType)l_listCopy.get(j);
				if (l_aw.isActive())
				{
					l_weapon = l_aw.modifyWeapon(l_weapon);
				}
				else
				{
					m_bazaarActiveWeaponList.remove(l_weapon);
				}
			}
			l_retList.add(l_weapon);
		}
		
		return l_retList;
	}
		
	public void resetLines()
	{
		if (BattleTrisApp.isTestMode())
		{
			m_numLinesToNextBazaar = 10;
		}
		else
		{
			m_numLinesToNextBazaar = DEFAULT_NUM_LINES_TO_NEXT_BAZAAR;
		}
	}

	public boolean updateLinesToNextBazaar(int lines)
	{
		m_numLinesToNextBazaar -= lines;

		if (m_numLinesToNextBazaar <= 0)
		{
			m_numLinesToNextBazaar = 0;
			return true;
		}

		return false;
	}

	public int getNumLinesToNextBazaar()
	{
		return m_numLinesToNextBazaar;
	}
}
