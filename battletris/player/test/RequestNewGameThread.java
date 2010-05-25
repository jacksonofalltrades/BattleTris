package battletris.player.test;

import battletris.AppControllable;

public class RequestNewGameThread extends Thread 
{
	protected String opponentUsername;
	protected ConnectedPlayerStub cps;
		
	public RequestNewGameThread(String opponentUsername, ConnectedPlayerStub cps)
	{
		this.opponentUsername = opponentUsername;
		this.cps = cps;
	}

	public void run()
	{
		// Brief pause
		try {
			Thread.sleep(2500);
		}
		catch(InterruptedException ie)
		{
		}
		
		// Test each scenario:
		// 1. Request accepted (carl)
		if (opponentUsername == "carl") {
			try {
				Thread.sleep(2500);
			}
			catch(InterruptedException ie)
			{
			}

			if (!cps.m_requestNewGameCanceled) 
			{
				cps.m_player.newGameAccepted(cps.m_c);
				cps.m_opponent = new TestOpponentThread(cps.m_spi_c, cps.m_c, cps.m_player);
				cps.m_opponent.start();
			}
		}
			
		// 2. Request rejected (elaine)
		if (opponentUsername == "elaine") {
			cps.m_player.newGameRejected();
		}
			
		// 3. Request timed out (fred)
		if (opponentUsername == "fred") {
			try {
				Thread.sleep(AppControllable.REQUEST_WAIT_TIMEOUT);
			}
			catch(InterruptedException ie)
			{
			}
		}
		
		// Not allowed for anyone else		
	}
}
