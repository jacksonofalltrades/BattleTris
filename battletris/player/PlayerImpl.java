package battletris.player;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

import starwarp.net.NetworkException;
import starwarp.net.PacketHandler;

import battletris.BoardImageData;
import battletris.InputControls;
import battletris.AppControllable;
import battletris.PieceControllable;
import battletris.Inventory;
import battletris.InventoryView;
import battletris.BattleTrisKeyEvents;
import battletris.weapon.Weapon;
import battletris.BattleTrisApp;

class PlayerImpl implements Player, ConnectedPlayerProxy
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputControls m_inputControls;
	protected AppControllable m_ac;
	protected PieceControllable m_pc;
	
	protected PlayerInfoImpl m_info;
	protected Inventory m_inventory;
	
	protected ConnectedPlayer m_netClient;

	protected void deductMoney(int amount)
	{
		m_info.m_money -= amount;
	}

	public PlayerImpl(AppControllable ac, PieceControllable pc, String serverHost, String username, String displayName)
	{
		// TODO: For now, use default, eventually pass in config file path
		m_inputControls = new InputControls(BattleTrisApp.getRootPath()+InputControls.DEFAULT_CONFIG_PATH);

		m_ac = ac;
		m_pc = pc;

		m_info = new PlayerInfoImpl();

		m_info.m_username = username;
		m_info.m_displayName = displayName;
		m_info.m_score = 0;
		
		if (BattleTrisApp.isTestMode())
		{
			m_info.m_money = 10000;			
		}
		else
		{
			m_info.m_money = 0;
		}
		m_info.m_lines = 0;

		/*
		// Get the localhost
		// Get ip address
		InetAddress l_ipAddr = null;
		String l_hostIp = null;
		try
		{
			l_ipAddr = InetAddress.getLocalHost();

			l_hostIp = l_ipAddr.getHostName();
		}
		catch(java.net.UnknownHostException uhe)
		{
			l_hostIp = "localhost";
			//String l_ipAddr = //"192.168.1.101";

			uhe.printStackTrace(System.err);
		}
		*/

		//String l_uriPrefix = "rmi://"+l_hostIp+":"+DEFAULT_PORT+"/";

		//m_info.m_uri = l_uriPrefix+m_info.m_username;

		m_netClient = new ConnectedPlayerImpl(this, username, serverHost, 3333, System.out, System.err);
		
		/*
		try {
			m_info.m_uri = m_netClient.getPlayerInfo().getUri();
		}
		catch(NetworkException ne)
		{
			ne.printStackTrace(System.err);
		}
		*/

		m_inventory = new Inventory();
	}

	public void reset(boolean isFinal)
	{
		m_info.m_score = 0;
		m_info.m_lines = 0;

		
		if (false == BattleTrisApp.isTestMode())
		{
			m_info.m_money = 0;
			m_inventory.removeAll();
		}
	}

	public PlayerInfo getPlayerInfo()
	{
		return m_info;
	}
	
	public PacketHandler getPacketHandler()
	{
		return null;
	}

	public int connectToServer()
	throws NetworkException
	{
		return m_netClient.connectToServer();
	}
	
	public void disconnectFromServer()
	throws NetworkException
	{
		m_netClient.disconnectFromServer();
	}

	/**
	 * Called to alert this client that requestor is requesting a new game.
	 * This method should tell the UI to show the user that a new game request has come in.
	 **/	
	public void requestForNewGame(PlayerInfo requestor)
	{
		m_ac.showNewGameRequestDialog(requestor);
	}
	
	public void requestNewGame(String requestor)
	throws NetworkException
	{
		m_netClient.requestNewGame(requestor);
	}
	
	public void acceptNewGameRequest(String opponentUsernameRequestor)
	throws NetworkException
	{
		m_netClient.acceptNewGameRequest(opponentUsernameRequestor);
	}
	
	public void cancelNewGameRequest(String opponentUsernameRequestee)
	throws NetworkException
	{
		m_netClient.cancelNewGameRequest(opponentUsernameRequestee);
	}
	
	public void rejectNewGameRequest(String opponentUsernameRequestor)
	throws NetworkException
	{
		m_netClient.rejectNewGameRequest(opponentUsernameRequestor);
	}

	// Let opponent know that game has ended for player
	public void sendGameEnded(String playerUsername)
	throws NetworkException
	{
		m_netClient.sendGameEnded(playerUsername);
	}

	public HashSet getPlayerList()
	throws NetworkException
	{
		return m_netClient.getPlayerList();
	}
	
	public ArrayList getPlayerRankings()
	throws NetworkException
	{
		return m_netClient.getPlayerRankings();
	}

	// Use player's weapon on opponent
	public void useWeapon(String playerUsername, Weapon weapon)
	throws NetworkException
	{
		m_netClient.useWeapon(playerUsername, weapon);
	}

	public boolean requestReturnFromBazaar(String playerUsername, boolean firstRequest)
	throws NetworkException
	{
		return m_netClient.requestReturnFromBazaar(playerUsername, firstRequest);
	}
	
	public void sendBoardUpdate(BoardImageData data)
	throws NetworkException
	{
		m_netClient.sendBoardUpdate(data);
	}

	// Send my most recent new line count to server
	public void updateLines(String playerUsername, int lines)
	throws NetworkException
	{
		m_netClient.updateLines(playerUsername, lines);
	}
	
    // Update opponent on my changed stats
	public void playerUpdate(int lines, int points, int funds, PlayerInfo playerInfo)
	throws NetworkException
	{
		// If PlayerInfo is not me, don't send- why would we update
		// our opponent about herself?
		if (playerInfo.getUsername() == this.m_info.getUsername())
		{
			// Only send here
			// First param is ignored
			m_netClient.playerUpdate(lines, points, funds, playerInfo);
		}
	}
		
	/**
	 * Called to alert this client that requestee has accepted new game request.
	 * This method should tell the UI to start a new game with the opponent specified.
	 **/
	public void newGameAccepted(PlayerInfo opponent)
	{
		m_ac.newGameAccepted(opponent);
	}

	public void newGameRejected()
	{
		m_ac.newGameRejected();
	}


	public void newGameCanceled()
	{
		m_ac.newGameCanceled();
	}

	public void gameEnded()
	{
		m_ac.gameEnded();
	}

	public void updateBazaarLines(int lines)
	{
		m_ac.updateBazaarLines(lines);
	}

	public String getDisplayName()
	{
		return m_info.m_displayName;
	}

	public int getMoney()
	{
		return m_info.m_money;
	}

	public String getUsername()
	{
		return m_info.m_username;
	}

	/**
	 * A weapon is being applied to this player, who, in turn, will pass it on
	 * to the AppControllable to delegate to appropriate objects
 	 **/
	public void applyWeapon(Weapon weapon)
	{
		m_ac.weaponUsed(weapon);
	}

	public InventoryView getInventory()
	{
		return m_inventory;
	}

	public boolean purchaseWeapon(Weapon weapon)
	{
		if (m_inventory.addItem((Weapon)weapon.clone()))
		{
			deductMoney(weapon.getCost());

			return true;
		}
		else
		{
			return false;
		}
	}

	public void receivedPlayerUpdate(int lines, int points, int funds, PlayerInfo playerInfo)
	throws NetworkException
	{		
		// Do not do anything if somehow we got called with ourself
		if (!playerInfo.equals(this))
		{
			m_ac.playerUpdate(lines, points, funds, playerInfo);
		}
	}
	
	public void receivedBoardUpdate(BoardImageData data)
	{
		m_ac.receivedBoardUpdate(data);
	}

	public void keyPressed(KeyEvent e)
	{
		int l_keyCode = e.getKeyCode();
		char l_keyChar = e.getKeyChar();

		//System.out.println(e.paramString());

		if (m_ac.isGameInProgress() && m_inputControls.isWeaponEvent(l_keyChar))
		{
			Weapon l_weapon = null;

			int l_weaponSlot = Character.digit(l_keyChar, 10);
			l_weapon = (Weapon)m_inventory.getAndRemoveItemAt(l_weaponSlot);

			if (null != l_weapon)
			{
				m_ac.useWeapon(l_weapon);
			}
		}
		else
		{
			Integer l_btEvent = m_inputControls.getEventForKey(l_keyCode);

			//System.out.println("BTEvent="+l_btEvent);

			if (null != l_btEvent)
			{
				int l_btEventInt = l_btEvent.intValue();

				switch(l_btEventInt)
				{
					case BattleTrisKeyEvents.MOVE_LEFT:
						m_pc.moveLeft();
						break;
					case BattleTrisKeyEvents.MOVE_RIGHT:
						m_pc.moveRight();
						break;
					case BattleTrisKeyEvents.DROP:
						m_pc.drop();
						break;
					case BattleTrisKeyEvents.ROTATE_CCW:
						// Disallow this until we can fix rotation
						//m_pc.rotateCCW();
						break;
					case BattleTrisKeyEvents.ROTATE_CW:
						m_pc.rotateCW();
						break;
					case BattleTrisKeyEvents.PAUSE:
						// TODO: Put pause back as a synchronized event
						// only when both players have it enabled
						//m_ac.pauseGame();
						break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void update(int lines, int points, int funds)
	throws NetworkException
	{
		m_info.m_score += points;
		m_info.m_money += funds;
		m_info.m_lines += lines;

		m_ac.playerUpdate(lines, points, funds, this.getPlayerInfo());
	}
}
