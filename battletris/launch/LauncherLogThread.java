package battletris.launch;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

public class LauncherLogThread extends Thread
{
	static final String LOG_PATH_PREFIX = "/log/";
	static final String LOG_PATH_SUFFIX = ".log";
	static final String PROC_TYPE_SERVER = "Server";
	static final String PROC_TYPE_CLIENT = "Client";

	protected String m_sessionId;
	protected String m_procTypePrefix;
	protected Process m_proc;
	
	protected String m_logPath;
	
	protected FileWriter m_logWriter;

	protected void logOutputLine(String line)
	{
		// For now log to standard out
		System.out.println("BattleTrisLauncher ["+m_proc.toString()+"]: "+line);
	}

	protected void logErrorLine(String line)
	{
		// For now log to standard err
		System.err.println("BattleTrisLauncher ["+m_proc.toString()+"]: "+line);

		Date l_now = new Date();
		String l_nowStr = l_now.toString();
		
		// Log to file as well
		if (null == m_logWriter)
		{
			try
			{
				m_logWriter = new FileWriter(m_logPath, true);
								
				// Header
				m_logWriter.append(m_sessionId);
				m_logWriter.append("***");
				m_logWriter.append(l_nowStr);
				m_logWriter.append("***");
				m_logWriter.append("\n");
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace(System.err);
			}
		}
		try
		{
			m_logWriter.append(l_nowStr);
			m_logWriter.append("******");
			m_logWriter.append(line);
			m_logWriter.append("\n");
		}
		catch(IOException ioe)
		{
		}			
	}

	public LauncherLogThread(String rootPath, String sessionId, String procTypePrefix, Process proc)
	{
		m_procTypePrefix = procTypePrefix;
		m_proc = proc;
		
		m_sessionId = sessionId;
		m_logPath = rootPath+LOG_PATH_PREFIX+m_procTypePrefix+LOG_PATH_SUFFIX;
		m_logWriter = null;
	}

	public void run()
	{
		InputStream l_is = m_proc.getInputStream();
		InputStream l_es = m_proc.getErrorStream();

		InputStreamReader l_isr = new InputStreamReader(l_is);
		InputStreamReader l_esr = new InputStreamReader(l_es);
		
		BufferedReader l_isbr = new BufferedReader(l_isr);
		BufferedReader l_esbr = new BufferedReader(l_esr);

		String eline = null;
		String iline = null;

		try
		{
			while( (null != (eline = l_esbr.readLine())) ||
			       (null != (iline = l_isbr.readLine()))
			     )
			{
				if (null != eline)
				{
					logErrorLine(eline);
				}
		
				if (null != iline)
				{
					logOutputLine(iline);
				}
			}
		}
		catch(IOException ioe)
		{
		}
		
		if (null != m_logWriter)
		{
			try
			{
				m_logWriter.close();
			}
			catch(IOException ioe)
			{
			}
		}
	}
}
