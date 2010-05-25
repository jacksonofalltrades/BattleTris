package battletris.net;

import java.util.Vector;

import utils.io.LinearParser;
import utils.io.ParsedBlock;

public class ServerConfig extends LinearParser 
{
	public static final String DEFAULT_CONFIG_PATH = "/conf/server.cfg";
	protected static final String MAX_CLIENTS_TAG = "MAX_CLIENTS";
	
	protected int m_maxClients;

	public ServerConfig(String configPath)
	{
		super("UTF8", '#', false, true/*parse numbers*/, true/*quote char*/, "ServerConfig");		

		if (null != configPath)
		{
			setVerbose(false);

			setHeader("BTServerConfigFormat");

			// <name> <value>
	
			boolean ok = true;
			Vector vals;

			vals = new Vector();
			vals.addElement(Integer.class); // value
	
			ok = addVectorValueTag(MAX_CLIENTS_TAG, vals);

			if (!ok)
			{
				System.err.println("ServerConfig::ServerConfig: failed to add parse params");
			}

			parseFile(configPath);		
		}
	}

	protected ParsedBlock nextParsedBlock() 
	{
		// TODO: Fill this in when we want to provide
		// the ADVANCED tab for specifying max-clients
		return null;
	}

	protected void handleParsedBlock(ParsedBlock pb) 
	{
		String l_tag = pb.m_tag;

		if (l_tag.equals(MAX_CLIENTS_TAG))
		{
			Integer l_maxClients = (Integer)pb.m_values.elementAt(0);
			
			m_maxClients = l_maxClients.intValue();
		}
	}
	
	public int getMaxClients()
	{
		return m_maxClients;
	}
}
