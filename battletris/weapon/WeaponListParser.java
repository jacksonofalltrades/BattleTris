package battletris.weapon;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Vector;
import java.util.ArrayList;

import utils.io.LinearParser;
import utils.io.ParsedBlock;

public class WeaponListParser extends LinearParser
{
	protected static final String WEAPON = "Weapon";

	protected ArrayList m_weapons;

	public WeaponListParser()
	{
		super("UTF8", '#', false, true/*parse numbers*/, true/*quote char*/, "WeaponListParser");

		setVerbose(false);

		setHeader("BTWeaponListFormat");

		// Weapon <weaponClassname> "<name>" "<desc>" <cost> <duration>

		boolean ok = true;
		Vector vals;

		vals = new Vector();
		vals.addElement(String.class); // classname
		vals.addElement(String.class); // name
		vals.addElement(String.class); // description
		vals.addElement(Integer.class); // cost
		vals.addElement(Integer.class); // duration

		ok = addVectorValueTag("Weapon", vals);
		
		if (!ok)
		{
			System.err.println("WeaponListParser::WeaponListParser: failed to add parse params");
		}
	}

	protected ParsedBlock nextParsedBlock()
	{
		return  null;
	}

	protected void handleParsedBlock(ParsedBlock pb)
	{
		String l_tag = pb.m_tag;

		if (l_tag.equals(WEAPON))
		{
			String l_className = (String)pb.m_values.elementAt(0);
			String l_name = (String)pb.m_values.elementAt(1);
			String l_desc = (String)pb.m_values.elementAt(2);
			Integer l_cost = (Integer)pb.m_values.elementAt(3);
			Integer l_dur = (Integer)pb.m_values.elementAt(4);

			// Find weapon class
			Class l_wclass = null;
			try
			{
				l_wclass = Class.forName(l_className);
			}
			catch(ClassNotFoundException cnfe)
			{
				System.err.println("No class found for name ["+l_className+"]");
				return;
			}

			Class[] l_paramTypes = new Class[4];
			l_paramTypes[0] = String.class;
			l_paramTypes[1] = String.class;
			l_paramTypes[2] = Integer.class;
			l_paramTypes[3] = Integer.class;

			Object[] l_params = new Object[4];
			l_params[0] = l_name;
			l_params[1] = l_desc;
			l_params[2] = l_dur;
			l_params[3] = l_cost;

			Constructor l_con = null;
			try
			{
				l_con = l_wclass.getConstructor(l_paramTypes);
			}
			catch(NoSuchMethodException nsme)
			{
				nsme.printStackTrace(System.err);
				return;
			}
			catch(SecurityException se)
			{
				se.printStackTrace(System.err);
				return;
			}

			Weapon l_weapon = null;
			try
			{
				l_weapon = (Weapon)l_con.newInstance(l_params);
			}
			catch(InstantiationException ie)
			{
				ie.printStackTrace(System.err);
				return;
			}
			catch(IllegalAccessException iae)
			{
				iae.printStackTrace(System.err);
				return;
			}
			catch(IllegalArgumentException iae)
			{
				iae.printStackTrace(System.err);
				return;
			}
			catch(InvocationTargetException ite)
			{
				ite.printStackTrace(System.err);
				return;
			}

			if (null != l_weapon)
			{
				m_weapons.add(l_weapon);
			}
		}
	}

	public ArrayList getWeaponList(String path)
	{
		m_weapons = new ArrayList();

		parseFile(path);

		return m_weapons;
	}
}
