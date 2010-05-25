package battletris.player.test;

import java.util.Random;

import starwarp.net.NetworkException;

import battletris.net.ServerPlayerInfo;
import battletris.player.Player;
import battletris.player.PlayerInfo;
import battletris.player.PlayerInfoImpl;
import battletris.weapon.MissingPieces;
import battletris.weapon.Weapon;

public class TestOpponentThread extends Thread 
{
	/*
	 * Incoming method calls to Player
	 */
	protected static final int METHOD_MAX = 7;
	protected static final int[] SF_PAUSE_TIMES = new int[]{1000,2000,3000,4000,5000};

	protected boolean m_isRunning;
	protected ServerPlayerInfo m_spi;
	protected PlayerInfo m_pi;
	protected Player m_player;
	
	protected Weapon m_testWeapon;
	
	public TestOpponentThread(ServerPlayerInfo spi, PlayerInfo pi, Player p)
	{
		m_isRunning = true;
		m_spi = spi;
		m_pi = pi;
		m_player = p;
				
		m_testWeapon = new MissingPieces("Missing Pieces", "Randomly removes your<br>&nbsp;opponent's pieces.", Integer.valueOf(30), Integer.valueOf(0));
	}
	
	public void shutdown()
	{
		m_isRunning = false;
	}
	
	/*
	 * receivedPlayerUpdate
	   applyWeapon
	   gameEnded
	   updateBazaarLines

	   requestForNewGame
	   newGameCanceled
	 */
	
	public void run()
	{
		Random l_rand = new Random();
		
		while(m_isRunning)
		{
			System.err.println("TestOpponentThread is running");
			
			int l_pauseIndex = l_rand.nextInt(SF_PAUSE_TIMES.length);
			int l_methodIndex = l_rand.nextInt(METHOD_MAX);
			
			try {
				Thread.sleep(SF_PAUSE_TIMES[l_pauseIndex]);
			}
			catch(InterruptedException ie)
			{				
			}
			
			if (m_pi.getUsername() == "greg")
			{
				try {
					Thread.sleep(2000);
				}
				catch(InterruptedException ie)
				{				
				}
				
				System.err.println("TestOpponentThread: opponent is greg");
				
				m_player.requestForNewGame(m_pi);
								
				// To test this, need to wrap requestForNewGame in
				// a separate thread since it blocks.
				m_player.newGameCanceled();
			}
			else {
				System.err.println("TestOpponentThread: opponent is NOT greg");

				switch(l_methodIndex) 
				{
					case 0:
					case 2:
					case 4:
					{
						// updateBazaarLines
						// receivedPlayerUpdate
						try {
							PlayerInfoImpl pii = (PlayerInfoImpl)m_pi;
							pii.m_lines += 2;
							pii.m_money += 3;
							pii.m_score += 20;
							m_player.receivedPlayerUpdate(0, 0, 0, m_pi);
							m_player.updateBazaarLines(2);
						}
						catch(NetworkException ne) {						
						}
					}
					break;
					case 1:
					case 3:
					case 5:
					{
						// applyWeapon
						m_player.applyWeapon(m_testWeapon);					
					}
					break;
					case 6:
					{
						// gameEnded
						m_player.gameEnded();
						m_isRunning = false;
					}
					break;
					default:
					{
						System.err.println("INVALID METHOD...continuing");
					}
				}
			}
		}
	}
}
