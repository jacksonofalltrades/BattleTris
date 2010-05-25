package battletris.launch;

import java.io.File;
import java.util.Vector;

import utils.io.LinearParser;
import utils.io.ParsedBlock;

public class LaunchConfig extends LinearParser
{
	protected static final String DEFAULT_CONFIG_PATH = "/conf/launch.cfg";
	protected static final String USERNAME_TAG = "USERNAME";
	protected static final String HANDLE_TAG = "HANDLE";
	protected static final String SERVER_TAG = "SERVER";
	protected static final String IS_SERVER_TAG = "IS_SERVER";
	protected static final String NET_PLAYER_PROXY_FACTORY_TAG = "NET_PLAYER_PROXY_FACTORY";

	protected String m_username;
	protected String m_handle;
	protected String m_server;
	protected boolean m_isServer;
	//protected String m_netPlayerProxyFactory;

	protected int m_saveIndex = 0;

	protected void resetSaveIndex()
	{
		m_saveIndex = 0;
	}

	protected ParsedBlock nextParsedBlock()
	{
		ParsedBlock l_pb = new ParsedBlock();

		switch(m_saveIndex)
		{
			case 0:
				l_pb.m_tag = USERNAME_TAG;
				l_pb.m_values = new Vector();
				l_pb.m_values.addElement("\""+m_username+"\"");
				break;
			case 1:
				l_pb.m_tag = HANDLE_TAG;
				l_pb.m_values = new Vector();
				l_pb.m_values.addElement("\""+m_handle+"\"");
				break;
			case 2:
				l_pb.m_tag = SERVER_TAG;
				l_pb.m_values = new Vector();
				l_pb.m_values.addElement("\""+m_server+"\"");
				break;
			case 3:
				l_pb.m_tag = IS_SERVER_TAG;
				l_pb.m_values = new Vector();
				l_pb.m_values.addElement(new Boolean(m_isServer));
				break;
				/*
			case 4:
				l_pb.m_tag = NET_PLAYER_PROXY_FACTORY_TAG;
				l_pb.m_values = new Vector();
				l_pb.m_values.addElement("\""+m_netPlayerProxyFactory+"\"");
				break;
				*/
			default:
				return null;
		}

		m_saveIndex++;

		return l_pb;
	}

	protected void handleParsedBlock(ParsedBlock pb)
	{
		String l_tag = pb.m_tag;

		if (l_tag.equals(USERNAME_TAG))
		{
			m_username = (String)pb.m_values.elementAt(0);
		}
		else if (l_tag.equals(HANDLE_TAG))
		{
			m_handle = (String)pb.m_values.elementAt(0);
		}
		else if (l_tag.equals(SERVER_TAG))
		{
			m_server = (String)pb.m_values.elementAt(0);
		}
		else if (l_tag.equals(IS_SERVER_TAG))
		{
			m_isServer = ((Boolean)pb.m_values.elementAt(0)).booleanValue();
		}
		/*
		else if (l_tag.equals(NET_PLAYER_PROXY_FACTORY_TAG))
		{
			m_netPlayerProxyFactory = (String)pb.m_values.elementAt(0);
		}
		*/
	}

	public LaunchConfig()
	{
		super("UTF8", '#', false, false/*parse numbers*/, true/*quote char*/, "LaunchConfig");

		m_username = "";
		m_handle = "";
		m_server = "";
		m_isServer = true;
		//m_netPlayerProxyFactory = AbstractPlayerProxyFactory.DEFAULT_FACTORY;

		setVerbose(false);

		setHeader("BTLaunchConfigFormat");

		// <name> <value>
	
		boolean ok = true;
		Vector vals;

		vals = new Vector();
		vals.addElement(String.class); // value
	
		ok = addVectorValueTag(USERNAME_TAG, vals);
		ok = addVectorValueTag(HANDLE_TAG, vals);
		ok = addVectorValueTag(SERVER_TAG, vals);

		vals = new Vector();
		vals.addElement(Boolean.class); // value
		ok = addVectorValueTag(IS_SERVER_TAG, vals);

		vals = new Vector();
		vals.addElement(String.class); // value
		ok = addVectorValueTag(NET_PLAYER_PROXY_FACTORY_TAG, vals);
		
		if (!ok)
		{
			System.err.println("LaunchConfig::LaunchConfig: failed to add parse params");
		}
	}

	public boolean load(String configPath)
	{
		if (null != configPath)
		{
			// First make sure file exists for reading
			File l_testFile = new File(configPath);
			if (l_testFile.exists())
			{
				parseFile(configPath);

				return true;
			}
		}

		return false;
	}

	public boolean save(String configPath)
	{
		if (null != configPath)
		{
			resetSaveIndex();
			reverseParse(configPath);

			return true;
		}

		return false;
	}

	public String getUsername()
	{
		return m_username;
	}

	public String getHandle()
	{
		return m_handle;
	}

	public String getServer()
	{
		return m_server;
	}

	public boolean getIsServer()
	{
		return m_isServer;
	}

	/*
	public String getNetPlayerProxyFactory()
	{
		return m_netPlayerProxyFactory;
	}
	*/

	public void setUsername(String username)
	{
		m_username = username;
	}

	public void setHandle(String handle)
	{
		m_handle = handle;
	}

	public void setServer(String server)
	{
		m_server = server;
	}

	public void setIsServer(boolean isServer)
	{
		m_isServer = isServer;
	}

	/*
	public void setNetPlayerProxyFactory(String name)
	{
		m_netPlayerProxyFactory = name;
	}
	*/
}
