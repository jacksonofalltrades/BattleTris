package battletris;

import java.awt.event.KeyEvent;

import java.util.HashMap;
import java.util.Vector;

import utils.io.LinearParser;
import utils.io.ParsedBlock;

public class InputControls extends LinearParser
{
	public static final String DEFAULT_CONFIG_PATH = "/conf/input.cfg";
	protected static final String MOVE_LEFT_TAG = "MOVE_LEFT";
	protected static final String MOVE_RIGHT_TAG = "MOVE_RIGHT";
	protected static final String DROP_TAG = "DROP";
	protected static final String ROTATE_CCW_TAG = "ROTATE_CCW";
	protected static final String ROTATE_CW_TAG = "ROTATE_CW";
	protected static final String START_GAME_TAG = "START_GAME";
	protected static final String CONTROLS_TAG = "CONTROLS";
	protected static final String PAUSE_TAG = "PAUSE";

	protected HashMap m_eventMap;

	protected ParsedBlock nextParsedBlock()
	{
		return  null;
	}

	protected void handleParsedBlock(ParsedBlock pb)
	{
		//String l_tag = pb.m_tag;

		Integer l_keyCode = (Integer)pb.m_values.elementAt(0);
		Integer l_code = (Integer)pb.m_values.elementAt(1);
		
		m_eventMap.put(l_keyCode, l_code);
	}

	public InputControls()
	{
		this(null);
	}

	public InputControls(String configPath)
	{
		super("UTF8", '#', false, true/*parse numbers*/, true/*quote char*/, "InputControls");

		m_eventMap = new HashMap();

		if (null != configPath)
		{
			setVerbose(false);

			setHeader("BTInputControlsFormat");

			// <name> <key> <code>
	
			boolean ok = true;
			Vector vals;

			vals = new Vector();
			vals.addElement(Integer.class); // key
			vals.addElement(Integer.class); // code
	
			ok = addVectorValueTag(MOVE_LEFT_TAG, vals);
			ok = addVectorValueTag(MOVE_RIGHT_TAG, vals);
			ok = addVectorValueTag(DROP_TAG, vals);
			ok = addVectorValueTag(ROTATE_CCW_TAG, vals);
			ok = addVectorValueTag(ROTATE_CW_TAG, vals);
			ok = addVectorValueTag(START_GAME_TAG, vals);
			ok = addVectorValueTag(CONTROLS_TAG, vals);
			ok = addVectorValueTag(PAUSE_TAG, vals);
			
			if (!ok)
			{
				System.err.println("InputControls::InputControls: failed to add parse params");
			}

			parseFile(configPath);
		}

		if (BattleTrisApp.DEBUG)
		{
			System.out.println(m_eventMap);
		}

		if (m_eventMap.isEmpty())
		{
			m_eventMap.put(new Integer(KeyEvent.VK_LEFT), new Integer(BattleTrisKeyEvents.MOVE_LEFT));
			m_eventMap.put(new Integer(KeyEvent.VK_RIGHT), new Integer(BattleTrisKeyEvents.MOVE_RIGHT));
			m_eventMap.put(new Integer(KeyEvent.VK_SPACE), new Integer(BattleTrisKeyEvents.DROP));
			//m_eventMap.put(new Integer(KeyEvent.VK_DOWN), new Integer(BattleTrisKeyEvents.ROTATE_CCW));
			m_eventMap.put(new Integer(KeyEvent.VK_UP), new Integer(BattleTrisKeyEvents.ROTATE_CW));
			m_eventMap.put(new Integer(KeyEvent.VK_S), new Integer(BattleTrisKeyEvents.START_GAME));
			m_eventMap.put(new Integer(KeyEvent.VK_C), new Integer(BattleTrisKeyEvents.CONTROLS));
			m_eventMap.put(new Integer(KeyEvent.VK_P), new Integer(BattleTrisKeyEvents.PAUSE));
		}
	}


	public boolean isWeaponEvent(char ch)
	{
		if (Character.isDigit(ch))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Integer getEventForKey(int keyCode)
	{
		Integer l_kc = new Integer(keyCode);
		return (Integer)m_eventMap.get(l_kc);
	}
}
