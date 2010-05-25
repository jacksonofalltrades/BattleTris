package battletris.launch;

public class LauncherCleanupThread extends Thread
{
	protected LauncherRef m_launcherRef;
	protected Process m_server;
	protected Process m_client;

	protected LauncherLogThread m_st;
	protected LauncherLogThread m_ct;

	public LauncherCleanupThread(LauncherRef launcher, Process server, Process client)
	{
		m_launcherRef = launcher;
		m_server = server;
		m_client = client;

		// Create a logging thread for each process
		if (null != m_server)
		{
			m_st = new LauncherLogThread(launcher.getRootPath(), String.valueOf(launcher.hashCode()), LauncherLogThread.PROC_TYPE_SERVER, m_server);
			m_st.start();
		}

		m_ct = new LauncherLogThread(launcher.getRootPath(), String.valueOf(launcher.hashCode()), LauncherLogThread.PROC_TYPE_CLIENT, m_client);
		m_ct.start();
	}

	public void run()
	{
		int l_serverExit = -1;
		int l_clientExit = -1;
		try
		{
			l_clientExit = m_client.waitFor();

			if (l_clientExit != 0)
			{
				if (null != m_server)
				{
					m_server.destroy();
					m_server.waitFor();
				}

				System.err.println("BattleTrisLauncher ["+m_client.toString()+"]: client exited with code "+String.valueOf(l_clientExit));
				m_launcherRef.notifyFailure(l_clientExit);
				return;
			}

			if (null != m_server)
			{
				l_serverExit = m_server.waitFor();
				if (l_serverExit != 0)
				{
					System.err.println("BattleTrisLauncher ["+m_server.toString()+"]: server exited with code "+String.valueOf(l_clientExit));
				}
			}

			// Now exit the launcher
			System.exit(0);
		}
		catch(InterruptedException ie)
		{
		}
	}
}
