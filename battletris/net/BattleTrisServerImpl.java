package battletris.net;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import starwarp.net.ClientProxy;
import starwarp.net.ClientProxyCreationListener;
import starwarp.net.ClientShutdownObserver;
import starwarp.net.DataPacket;
import starwarp.net.NetworkException;
import starwarp.net.PacketHandler;
import starwarp.net.Server;
import starwarp.net.bdcs.ServerImpl;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.ConnectedPlayerProxyImpl;
import battletris.player.PlayerInfo;
import battletris.player.PlayerInfoImpl;
import battletris.weapon.Weapon;
import battletris.BattleTrisExitCodes;
import battletris.BoardImageData;
import battletris.PlayerStats;
import battletris.DefaultPlayerStats;
import battletris.BattleTrisApp;

import battletris.display.BattleTrisServerDisplay;

public class BattleTrisServerImpl implements BattleTrisServer, PacketHandler, ClientProxyCreationListener, ClientShutdownObserver
{
	private static final long serialVersionUID = 1L;
	
	public static boolean DEBUG = true;

	static {
		String debug = System.getProperty("DEBUG");
		if (debug != null) {
			DEBUG = true;
		}
		else {
			DEBUG = false;
		}
	}	
	
	protected static String sm_rootPath;
	
	protected static PrintStream sm_outLogWriter;
	protected static PrintStream sm_errLogWriter;
	
	static {
		BattleTrisServerImpl.setOutLogWriter(System.out);
		BattleTrisServerImpl.setErrLogWriter(System.err);
	}
	
	protected static void setRootPath(String rp)
	{
		sm_rootPath = rp;
	}
	
	protected static String getRootPath()
	{
		return sm_rootPath;
	}

	protected Object m_serverLock;
	
	protected HashMap m_clientLockMap;
	
	//protected ServerProxy m_serverProxy;
	
	protected ServerConfig m_serverConfig;
	
	protected Server m_server;

	protected HashSet m_serverPlayerInfoList;
	
	protected HashMap m_usernameServerPlayerInfoMap;
	
	protected HashMap m_usernameClientMap;

	// Storing this AND the ServerPlayerInfoMap is NOT optimal
	protected HashMap m_usernamePlayerRefMap;

	protected HashMap m_gamesInProgressMap;

	protected HashMap m_pendingNewGameRequestMap;

	protected PlayerStats m_playerStats;
	
	protected ArrayList m_packetQueue;

	protected boolean isPlayerInGame(String playerUsername)
	{
		ServerPlayerInfo l_spi = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(playerUsername);
		if (null != l_spi)
		{
			if (false == l_spi.isAvailable())
			{
				return true;
			}
		}

		return false;
	}

	protected String getGameKey(String playerUsername)
	{
		ServerPlayerInfo l_spi = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(playerUsername);
		if (null != l_spi)
		{
			String l_p1;
			String l_p2;
			if (l_spi.isRequestor())
			{
				l_p1 = playerUsername;
				l_p2 = l_spi.getOpponentUsername();
			}
			else
			{
				l_p1 = l_spi.getOpponentUsername();
				l_p2 = playerUsername;
			}

			String l_key = getGameKey(l_p1,l_p2);

			return l_key;
		}

		return null;
	}

	protected String getGameKey(String requestor, String opponent)
	{
		return requestor+":"+opponent;
	}
	
	public static void setOutLogWriter(PrintStream olw)
	{
		if (null != olw) {
			sm_outLogWriter = olw;
		}
	}
	
	public static void setErrLogWriter(PrintStream elw)
	{
		if (null != elw) {
			sm_errLogWriter = elw;
		}
	}

	public static String getServerUri()
	{
		return getServerUri(null);
	}

	public static String getServerUri(String ipAddress)
	{
		String l_hostIp = null;
		if (null == ipAddress)
		{
			// Get ip address
			InetAddress l_ipAddr = null;
			try
			{
				l_ipAddr = InetAddress.getLocalHost();
				l_hostIp = l_ipAddr.getHostAddress();
			}
			catch(java.net.UnknownHostException uhe)
			{
				l_hostIp = "localhost";
				//String l_ipAddr = //"192.168.1.101";
			}
		}
		else
		{
			l_hostIp = ipAddress;
		}

		String l_uriPrefix = "rmi://"+l_hostIp+":"+DEFAULT_PORT+"/";
		String l_serverUri = l_uriPrefix+SERVER_NAME;

		if (DEBUG) {
			sm_outLogWriter.println("Getting server URI = "+l_serverUri);
		}

		return l_serverUri;
	}

	public BattleTrisServerImpl(String configPath)
	{
		// Initialize player proxy factory
		//vAbstractPlayerProxyFactory.make(System.getProperty(AbstractPlayerProxyFactory.FACTORY_KEY,AbstractPlayerProxyFactory.DEFAULT_FACTORY));
		
		m_serverLock = new Object();
		
		m_clientLockMap = new HashMap();
		
		m_serverConfig = new ServerConfig(BattleTrisServerImpl.getRootPath()+ServerConfig.DEFAULT_CONFIG_PATH);

		// TODO: Load rankings from config
		m_playerStats = new DefaultPlayerStats(configPath);

		m_serverPlayerInfoList = new HashSet();

		m_usernameClientMap = new HashMap();
		
		// String<username>:ServerPlayerInfo
		m_usernameServerPlayerInfoMap = new HashMap();
		
		// String<username>:ConnectedPlayerProxy
		m_usernamePlayerRefMap = new HashMap();

		// String<requestorUsername:opponentUsername>:Game
		m_gamesInProgressMap = new HashMap();

		// String<requestorUsername:opponentUsername>:Game
		m_pendingNewGameRequestMap = new HashMap();
		
		m_packetQueue = new ArrayList();

		int l_maxClients = 20;
		String host = "localhost";
		int port = 3333;
		m_server = new ServerImpl(l_maxClients, host, port);
		m_server.setPacketHandler(this);
		m_server.setClientProxyCreationListener(this);
		m_server.setClientShutdownObserver(this);
	}
	
	public void notifyNewClientProxy(ClientProxy cp)
	{
		if (DEBUG) {
			System.err.println("notifyNewClientProxy called!");
		}
		synchronized(m_packetQueue)
		{
			if (DEBUG) {
				System.err.println("notifyNewClientProxy called (inside sync)");
			}
			
			ArrayList l_toRemove = new ArrayList();
			
			String l_dispName = cp.getExtraDataValue(PlayerInfo.KEY_DISPLAY_NAME);
			PlayerInfo l_pi = new PlayerInfoImpl(cp.getClientId(), l_dispName);
			ConnectedPlayerProxy l_playerRef = new ConnectedPlayerProxyImpl(this, cp, l_pi, sm_outLogWriter, sm_errLogWriter);
			
			m_usernameClientMap.put(cp.getClientId(), l_playerRef);
			m_clientLockMap.put(cp.getClientId(), new Object());
			
			if (m_packetQueue.size() > 0) {
				for(int i = m_packetQueue.size(); i >= 0; --i)
				{
					DataPacket dp = (DataPacket)m_packetQueue.get(i);
					if (l_pi.getUsername().equals(dp.getSenderId())) {
						l_toRemove.add(dp);
						
						PacketHandler l_handler = l_playerRef.getPacketHandler();
						l_handler.handleDataPacket(dp);
					}
				}

				m_packetQueue.removeAll(l_toRemove);
			}
		}
	}
	
	protected void removeClientPackets(String clientId)
	{
		synchronized(m_packetQueue)
		{
			ArrayList l_toRemove = new ArrayList();
			if (m_packetQueue.size() > 0) {
				for(int i = m_packetQueue.size(); i >= 0; --i)
				{
					DataPacket dp = (DataPacket)m_packetQueue.get(i);
					if (clientId.equals(dp.getSenderId())) {
						l_toRemove.add(dp);						
					}
				}
	
				m_packetQueue.removeAll(l_toRemove);
			}		
		}
	}
	
	protected Object clientLock(String clientId)
	{
		return m_clientLockMap.get(clientId);
	}
	
	protected void cleanupGameMap(String clientId, HashMap gameMap)
	{
		synchronized(gameMap)
		{
			ArrayList l_toRemove = new ArrayList();
			Set l_keys = gameMap.keySet();
			Iterator l_iter = l_keys.iterator();
			while(l_iter.hasNext()) {
				String l_key = (String)l_iter.next();
				Game l_game = (Game)gameMap.get(l_key);
				String requestorId = l_game.getRequestor().getPlayerInfo().getUsername();
				String oppId = l_game.getOpponent().getPlayerInfo().getUsername();
				if (requestorId.equals(clientId) || oppId.equals(clientId))
				{
					l_toRemove.add(l_key);
				}
			}
			
			for(int i = 0; i < l_toRemove.size(); i++)
			{
				String l_key = (String)l_toRemove.get(i);
				gameMap.remove(l_key);
			}
		}
	}
	
	public void notifyClientShutdown(String clientId)
	{
		// Create lock object for each clientId 
		
		synchronized(clientLock(clientId))
		{
			try {
				this.disconnectFromServer(clientId);
			}
			catch(NetworkException ne)
			{
				ne.printStackTrace(sm_errLogWriter);
			}			
		}
	}
	
	public void handleDataPacket(DataPacket dp)
	{
		// Queue up packets until they can be properly handled
		// by the owning ConnectedPlayerProxy objects
		m_packetQueue.add(0, dp);		
	}

	public void run()
	{
		m_server.start();
		
		try {
			m_server.join(0);
		}
		catch(InterruptedException ie)
		{
		}
	}
	
	/**
	 * @return List of ServerPlayerInfo indicating players playing and waiting for a game.
	 **/
	public HashSet getPlayerList()
	{
		return m_serverPlayerInfoList;
	}

	/**
	 * @return A list of player rankings recorded on server.
	 **/
	public ArrayList getPlayerRankings()
	throws NetworkException
	{
		return m_playerStats.toArrayList();
	}

	/**
	 * Give the specified player info to the server to store in it's ServerPlayerInfo list.
 	 **/
	public int connectToServer(PlayerInfo player)
	throws NetworkException
	{
		if (null == player)
		{
			throw new NetworkException("PlayerInfo not passed!");
		}
		
		if (m_usernameServerPlayerInfoMap.size() >= m_serverConfig.getMaxClients())
		{
			return BattleTrisExitCodes.SERVER_FULL;
		}

		// First make sure this player doesn't already exist on the server
		String l_username = player.getUsername();
		
		if (null == m_usernameServerPlayerInfoMap.get(l_username))
		{
			ServerPlayerInfo l_spi = new ServerPlayerInfo(player.getUsername(), player.getDisplayName(), player.getUri());
			m_usernameServerPlayerInfoMap.put(l_username, l_spi);

			// Add to list as well
			m_serverPlayerInfoList.add(l_spi);

			// Add player ref now too
			ConnectedPlayerProxy l_playerRef = (ConnectedPlayerProxy)m_usernameClientMap.get(l_username);
			
			m_usernamePlayerRefMap.put(l_username, l_playerRef);
			
			if (DEBUG)
			{
				sm_outLogWriter.println("Successfully connected player to BattleTris Server: "+l_spi);
			}
			
			return BattleTrisExitCodes.OK;
		}
		else
		{
			return BattleTrisExitCodes.USER_ALREADY_EXISTS;
		}
	}

	/**
	 * Forward the request to the player being requested. Add an entry to the PendingNewGameRequests list.
	 * Requestor client should
	 * disallow further input until a response arrives (via newGameAccepted) or until the Requestor clicks Cancel.
	 **/
	public void requestNewGame(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException
	{
		// Verify that both usernames passed are registered in the ServerPlayerInfo map
		ServerPlayerInfo l_spiPlayer = null;
		ServerPlayerInfo l_spiOpponent = null;

		synchronized(m_serverLock)
		{
			l_spiPlayer = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(playerUsernameRequestor);
			l_spiOpponent = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(opponentUsernameRequestee);

			if (null == l_spiPlayer)
			{
				throw new NetworkException("Player with username "+playerUsernameRequestor+" is not connected to the BattleTris Server.");
			}

			if (null == l_spiOpponent)
			{
				throw new NetworkException("Player with username "+opponentUsernameRequestee+" is not connected to the BattleTris Server.");
			}
		}

		l_spiPlayer.setOpponent(l_spiOpponent);
		l_spiOpponent.setOpponent(l_spiPlayer);
		l_spiPlayer.setRequestor(true);
		l_spiOpponent.setRequestor(false);

		String l_key = getGameKey(playerUsernameRequestor, opponentUsernameRequestee);

		// Create a Game and place it in the pending map
		ConnectedPlayerProxy l_playerRef = (ConnectedPlayerProxy)this.m_usernamePlayerRefMap.get(l_spiPlayer.getUsername());
		ConnectedPlayerProxy l_opponentRef = (ConnectedPlayerProxy)this.m_usernamePlayerRefMap.get(l_spiOpponent.getUsername());;
		
		Game l_newGame = new Game(l_playerRef, l_opponentRef);

		m_pendingNewGameRequestMap.put(l_key, l_newGame);

		// Forward request to requestee
		final ConnectedPlayerProxy l_requestor = l_newGame.getRequestor();
		final ConnectedPlayerProxy l_opponent = l_newGame.getOpponent();
		if (null == l_opponent)
		{
			throw new NetworkException("There was a problem getting a remote reference to player with username "+opponentUsernameRequestee);
		}
		else
		{
			if (null == l_requestor)
			{
				throw new NetworkException("There was a problem getting a remote reference to player with username "+playerUsernameRequestor);
			}
			else
			{
				new Thread()
				{
					public void run()
					{
						try
						{
							if (DEBUG) {
								System.err.println("BattleTrisServerImpl::requestNewGame called! WHO GOES HERE!!!");
							}
							l_opponent.requestForNewGame(l_requestor.getPlayerInfo());
						}
						catch(NetworkException ne)
						{
							ne.getCause().printStackTrace(sm_errLogWriter);
						}
					}
				}.start();
			}
		}

		if (DEBUG)
		{
			sm_outLogWriter.println("DEBUG: Returning from BattleTrisServerImpl::requestNewGame");
		}
	}

	/**
	 * This means requestor cancelled. Remove the entry from the PendingNewGameRequests list and send
	 * a message to the Requestee client that the request to play has been cancelled
	 **/
	public void cancelNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsernameRequestor, opponentUsernameRequestee);

		Game l_game = (Game)m_pendingNewGameRequestMap.get(l_key);
		if (null != l_game)
		{
			m_pendingNewGameRequestMap.remove(l_key);

			ConnectedPlayerProxy l_opponent = l_game.getOpponent();
			
			l_opponent.newGameCanceled();
		}
	}

    /**
     * Remove the entry from the PendingNewGameRequests list and place it in the GamesInProgress list.
     * Update the appropriate ServerPlayerInfo objects to indicate who is playing who now.
     * Create a PlayerRef to the original Requestor and call newGameAccepted() on it passing the PlayerRef
     * of the Requestee.
     * @return the opponent
     **/
	public void acceptNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException
	{				
		if (DEBUG)
		{
			sm_outLogWriter.println("DEBUG: BattleTrisServerImpl::acceptNewGameRequest: request from ["+playerUsernameRequestor+"] to ["+opponentUsernameRequestee+"]");
		}
		
		// Verify that both usernames passed are registered in the ServerPlayerInfo map
		ServerPlayerInfo l_spiPlayer = null;
		ServerPlayerInfo l_spiOpponent = null;

		synchronized(m_serverLock)
		{
			l_spiPlayer = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(playerUsernameRequestor);
			l_spiOpponent = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(opponentUsernameRequestee);

			if (null == l_spiPlayer)
			{
				throw new NetworkException("Player with username "+playerUsernameRequestor+" is not connected to the BattleTris Server.");
			}

			if (null == l_spiOpponent)
			{
				throw new NetworkException("Player with username "+opponentUsernameRequestee+" is not connected to the BattleTris Server.");
			}
		}

		l_spiPlayer.setOpponent(l_spiOpponent);
		l_spiOpponent.setOpponent(l_spiPlayer);
	
		l_spiPlayer.setAvailable(false);
		l_spiOpponent.setAvailable(false);

		l_spiPlayer.setRequestor(true);
		l_spiOpponent.setRequestor(false);

		String l_key = getGameKey(playerUsernameRequestor, opponentUsernameRequestee);
		Game l_game = (Game)m_pendingNewGameRequestMap.remove(l_key);

		m_gamesInProgressMap.put(l_key, l_game);
		
		if (DEBUG) {
			sm_errLogWriter.println("BattleTrisServerImpl::acceptNewGame: l_key="+l_key);
		}

		ConnectedPlayerProxy l_requestor = l_game.getRequestor();
		ConnectedPlayerProxy l_opponent = l_game.getOpponent();

		if (DEBUG)
		{
			sm_outLogWriter.println("DEBUG: BattleTrisServerImpl::acceptNewGameRequest: about to call newGameAccepted");
		}
		
		l_requestor.newGameAccepted(l_opponent.getPlayerInfo());

		//l_requestor.newGameAccepted(l_opponent.getPlayerInfo());

		if (DEBUG)
		{
			sm_outLogWriter.println("DEBUG: Returning from BattleTrisServerImpl::acceptNewGameRequest");
		}
	}

	public void rejectNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsernameRequestor, opponentUsernameRequestee);

		Game l_game = (Game)m_pendingNewGameRequestMap.get(l_key);
		if (null != l_game)
		{
			m_pendingNewGameRequestMap.remove(l_key);

			ConnectedPlayerProxy l_requestor = l_game.getRequestor();

			if (DEBUG)
			{
				sm_outLogWriter.println("BattleTrisServerImpl::rejectNewGameRequest: requestor="+l_requestor.getPlayerInfo());
			}
			
			l_requestor.newGameRejected();
		}
	}
	
	public void sendBoardUpdate(String playerUsername, BoardImageData data)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsername);

		Game l_game = (Game)m_gamesInProgressMap.get(l_key);
		
		if (null != l_game) {
			ConnectedPlayerProxy l_requestor = l_game.getRequestor();
			ConnectedPlayerProxy l_opponent = l_game.getOpponent();
			
			if (playerUsername.equals(l_requestor.getPlayerInfo().getUsername())) {
				l_opponent.receivedBoardUpdate(data);
			}
			else {
				l_requestor.receivedBoardUpdate(data);
			}
		}
	}

	/**
	 * Increment lines for the game that the specified player is playing
	 * and return the new total number of lines.
 	 **/
	public void updateLines(String playerUsername, int lines)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsername);

		Game l_game = (Game)m_gamesInProgressMap.get(l_key);
		if (null != l_game)
		{
			ConnectedPlayerProxy l_requestor = l_game.getRequestor();
			ConnectedPlayerProxy l_opponent = l_game.getOpponent();
			
			
			class BazaarThread extends Thread 
			{
				protected ConnectedPlayerProxy m_playerRef;
				protected int m_lines;

				public BazaarThread(ConnectedPlayerProxy pref, int lines)
				{
					m_playerRef = pref;
					m_lines = lines;
				}

				public void run()
				{
					try
					{
						m_playerRef.updateBazaarLines(m_lines);
					}
					catch(NetworkException ne)
					{
						ne.printStackTrace(sm_errLogWriter);
					}
				}
			};

			BazaarThread l_rt = new BazaarThread(l_requestor, lines);
			BazaarThread l_op = new BazaarThread(l_opponent, lines);

			l_rt.start();
			l_op.start();
			
			try
			{
				l_rt.join();
				l_op.join();
			}
			catch(InterruptedException ie)
			{
				ie.printStackTrace(sm_errLogWriter);
			}
		}
		else
		{
			sm_errLogWriter.println("BattleTrisServerImpl::updateLines: Could not find game for player ["+playerUsername+"]");
		}
	}
	public boolean requestReturnFromBazaar(String playerUsername, boolean firstRequest)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsername);
		
		Game l_game = (Game)m_gamesInProgressMap.get(l_key);
		if (null != l_game)
		{
			ConnectedPlayerProxy l_requestor = l_game.getRequestor();
			PlayerInfo l_reqPi = l_requestor.getPlayerInfo();

			ConnectedPlayerProxy l_opponent = l_game.getOpponent();
			PlayerInfo l_oppPi = l_opponent.getPlayerInfo();

			if (firstRequest)
			{
				if (l_reqPi.getUsername().equals(playerUsername))
				{
					l_game.setWaiting(l_requestor);
				}
				else if (l_oppPi.getUsername().equals(playerUsername))
				{
					l_game.setWaiting(l_opponent);
				}
			}

			boolean l_retVal = l_game.isBazaarDone();

			if (l_retVal)
			{
				l_game.resetBazaar();
			}

			return l_retVal;
		}
		else
		{
			sm_errLogWriter.println("BattleTrisServerImpl::requestReturnFromBazaar: Could not find game for player ["+playerUsername+"]");
			return true;
		}
	}

	public void useWeapon(String playerUsername, Weapon weapon)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsername);

		Game l_game = (Game)m_gamesInProgressMap.get(l_key);
		if (null != l_game)
		{
			ConnectedPlayerProxy l_requestor = l_game.getRequestor();
			PlayerInfo l_reqPi = l_requestor.getPlayerInfo();

			ConnectedPlayerProxy l_opponent = l_game.getOpponent();

			if (l_reqPi.getUsername().equals(playerUsername))
			{
				l_opponent.applyWeapon(weapon);
			}
			else
			{
				l_requestor.applyWeapon(weapon);
			}
		}
	}
	
	public void receivedPlayerUpdate(String playerUsername, int lines, int points, int funds,
			PlayerInfo playerInfo)
	throws NetworkException
	{
		String l_key = getGameKey(playerUsername);

		Game l_game = (Game)m_gamesInProgressMap.get(l_key);
		if (null != l_game)
		{
			ConnectedPlayerProxy l_requestor = l_game.getRequestor();
			PlayerInfo l_reqPi = l_requestor.getPlayerInfo();

			ConnectedPlayerProxy l_opponent = l_game.getOpponent();

			if (l_reqPi.getUsername().equals(playerUsername))
			{
				l_opponent.receivedPlayerUpdate(lines, points, funds, playerInfo);
			}
			else
			{
				l_requestor.receivedPlayerUpdate(lines, points, funds, playerInfo);
			}
		}
	}

	/**
	 * Game has ended for specified player. Find opponent from GamesInProgress list and call gameOver() on
	 * opponent PlayerRef. Remove game from GamesInProgress list and update ServerPlayerInfo for both players
	 * to "available".
	 * Record scores, wins, losses, streaks, etc. in rankings database.
	 **/
	public void sendGameEnded(String playerUsername) throws NetworkException
	{
		String l_key = getGameKey(playerUsername);

		Game l_game = (Game)m_gamesInProgressMap.get(l_key);

		// 1. Save stats for both players
		m_playerStats.save(l_game);

		// 2. Remove Game from games in progress map
		m_gamesInProgressMap.remove(l_key);

		ConnectedPlayerProxy l_requestor = l_game.getRequestor();
		PlayerInfo l_reqInfo = l_requestor.getPlayerInfo();

		ConnectedPlayerProxy l_opponent = l_game.getOpponent();
		PlayerInfo l_oppInfo = l_opponent.getPlayerInfo();

		class GameOverThread extends Thread
		{
			protected ConnectedPlayerProxy m_player;

			public GameOverThread(ConnectedPlayerProxy player)
			{
				m_player = player;
			}

			public void run()
			{
				try
				{
					m_player.gameEnded();
				}
				catch(NetworkException ne)
				{
					ne.printStackTrace(sm_errLogWriter);
					ne.getCause().printStackTrace(sm_errLogWriter);
				}
			}
		}

		if (l_reqInfo.getUsername().equals(playerUsername))
		{
			GameOverThread l_got = new GameOverThread(l_opponent);
			l_got.start();
		}
		else if (l_oppInfo.getUsername().equals(playerUsername))
		{
			GameOverThread l_got = new GameOverThread(l_requestor);
			l_got.start();
		}

		// Verify that both usernames passed are registered in the ServerPlayerInfo map
		ServerPlayerInfo l_spiPlayer = null;
		ServerPlayerInfo l_spiOpponent = null;

		synchronized(m_serverLock)
		{
			l_spiPlayer = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(l_reqInfo.getUsername());
			l_spiOpponent = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(l_oppInfo.getUsername());

			if (null == l_spiPlayer)
			{
				throw new NetworkException("Player with username "+l_reqInfo.getUsername()+" is not connected to the BattleTris Server.");
			}

			if (null == l_spiOpponent)
			{
				throw new NetworkException("Player with username "+l_oppInfo.getUsername()+" is not connected to the BattleTris Server.");
			}

			l_spiPlayer.setOpponent(null);
			l_spiPlayer.setAvailable(true);
			l_spiPlayer.setRequestor(false);

			l_spiOpponent.setOpponent(null);
			l_spiOpponent.setAvailable(true);
			l_spiOpponent.setRequestor(false);
		}
	}

	/**
	 * This means the player is probably shutting down their client. Remove them from the ServerPlayerInfo list
	 **/
	public void disconnectFromServer(String playerUsernameRequestor)
	throws NetworkException
	{
		// Make sure to clean up player before disconnecting:
		// 1. Remove player from in-progress game
		ServerPlayerInfo l_spi = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(playerUsernameRequestor);
		if (null == l_spi)
		{
			throw new NetworkException("Player with username "+playerUsernameRequestor+" is not connected to the BattleTris Server");
		}

		// If this player happens to be in the middle of a Game,
		if (isPlayerInGame(playerUsernameRequestor))
		{
			sendGameEnded(playerUsernameRequestor);
		}
		else
		{
			boolean l_isRequestor = l_spi.isRequestor();

			// 2. Remove player from pending list if necessary
			String l_key = getGameKey(playerUsernameRequestor);

			Game l_game = (Game)m_pendingNewGameRequestMap.get(l_key);
			if (null != l_game)
			{
				PlayerInfo l_player = new PlayerInfoImpl(playerUsernameRequestor, l_spi.getDisplayName());
				ServerPlayerInfo l_oppSpi = (ServerPlayerInfo)m_usernameServerPlayerInfoMap.get(l_spi.getOpponentUsername());
				PlayerInfo l_opponent = new PlayerInfoImpl(l_oppSpi.getUsername(), l_oppSpi.getDisplayName());
				
				if (l_isRequestor)
				{
					cancelNewGameRequest(l_player.getUsername(), l_opponent.getUsername());
				}
				else
				{
					rejectNewGameRequest(l_player.getUsername(), l_opponent.getUsername());
				}
			}
		}

		m_usernameServerPlayerInfoMap.remove(playerUsernameRequestor);
		m_serverPlayerInfoList.remove(l_spi);
		
		m_usernamePlayerRefMap.remove(playerUsernameRequestor);
		
		m_usernameClientMap.remove(playerUsernameRequestor);
		
		this.removeClientPackets(playerUsernameRequestor);
					
		if (DEBUG) {
			sm_outLogWriter.println("Removed player with name "+playerUsernameRequestor);
		}
	}

	public static void main(String[] args)
	{
		BattleTrisServerDisplay l_serverDisplay = new BattleTrisServerDisplay();
		
		if (null == l_serverDisplay);

		String l_rootPath = null;
		
		Boolean l_isDirectEnvMode = Boolean.valueOf(System.getProperty(BattleTrisApp.DIRECT_ENV_MODE_KEY, BattleTrisApp.DEFAULT_DIRECT_ENV_MODE));
		if (Boolean.FALSE == l_isDirectEnvMode)
		{
			l_rootPath = System.getProperty(BattleTrisApp.ROOT_PATH_KEY, BattleTrisApp.DEFAULT_ROOT_PATH);
		}
		else
		{
			l_rootPath = System.getenv(BattleTrisApp.ROOT_PATH_ENV_KEY);
		}

		sm_outLogWriter.println("BattleTrisServerImpl: bt.rootpath="+l_rootPath);
		BattleTrisServerImpl.setRootPath(l_rootPath);
				
		String l_serverUri = BattleTrisServerImpl.getServerUri();
		sm_outLogWriter.println("BattleTrisServerImpl@"+l_serverUri+" starting...");

		BattleTrisServerImpl l_bts = null;
		try
		{
			l_bts = new BattleTrisServerImpl(null);
			
			l_bts.run();
			//Naming.rebind(l_serverUri, l_bts);
		}
		catch(Exception ex)
		{
			ex.printStackTrace(sm_errLogWriter);
			System.exit(1);
		}
	}
}
