package battletris;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import starwarp.net.NetworkException;

import battletris.display.AppFrame;
import battletris.display.AppDisplay;
import battletris.display.AppMenuBar;
import battletris.display.BazaarDisplay;
import battletris.display.BoardDisplay;
import battletris.display.InventoryDisplay;
import battletris.display.PlayerDisplay;
import battletris.display.CommunicationDisplay;
import battletris.display.ServerPlayerDisplay;

import battletris.piece.Piece;

import battletris.player.Player;
import battletris.player.PlayerInfo;
import battletris.player.PlayerFactory;
import battletris.player.PlayerListener;

import battletris.weapon.Weapon;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.target.WeaponTargetFactory;

import battletris.weapon.type.PieceMovementWeaponType;
import battletris.weapon.type.PieceGenerationWeaponType;
import battletris.weapon.type.SpecialPieceWeaponType;
import battletris.weapon.type.StaticBoardModWeaponType;
import battletris.weapon.type.BazaarModWeaponType;
import battletris.weapon.type.PlayerModWeaponType;

public class BattleTrisApp extends JFrame implements AppFrame, AppControllable, PlayerListener, CurrentPieceContainer, WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String USERNAME_TITLE = "Enter Username";
	protected static final String DISPLAYNAME_TITLE = "Enter Display Name";

	protected static final String GAME_REQUEST_MESSAGE_SUFFIX = " is challenging you to a game. Do you accept?";
	protected static final String GAME_REQUEST_TITLE = "New Game Request";

	protected static final String ENTER_USERNAME_MESSAGE = "Please enter your BattleTris username:";
	protected static final String ENTER_DISPLAYNAME_MESSAGE = "Please enter your BattleTris display name:";

	protected static final String YOU_LOSE_MESSAGE = "You lose!";

	protected static final String YOU_WIN_MESSAGE = "You win!";
	
	public static final String ROOT_PATH_ENV_KEY = "BATTLETRIS_ROOT";

	public static final String ROOT_PATH_KEY = "bt.rootpath";
	
	public static final String TEST_MODE_KEY = "bt.istest";
	
	public static final String DIRECT_ENV_MODE_KEY = "bt.directenv";

	protected static final String DEFAULT_WEAPON_CONFIG_PATH = "/conf/weapons.cfg";

	public static final String DEFAULT_ROOT_PATH = "";
	
	public static final String DEFAULT_TEST_MODE = "false";
	
	public static final String DEFAULT_DIRECT_ENV_MODE = "false";

	protected static final int BAZAAR_WAIT_INTERVAL = 500;

	protected static final int JOIN_TIMEOUT = 50;
		
	protected static final int SERVER_TRY_SLEEP = 1000;
	
	protected static boolean sm_directEnvMode = false;

	protected static String sm_rootPath = "";
	
	protected static boolean sm_isTestMode = false;

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

	public static long sm_drawCount;
	public static long sm_totalRenderTime;
	
	public static long getDrawCount()
	{
		return sm_drawCount;
	}
	
	public static long getTotalRenderTime()
	{
		return sm_totalRenderTime;
	}
	
	public static double getAverageRenderTime()
	{
		return ((double)sm_totalRenderTime/(double)sm_drawCount);
	}
	
	public static final int REQUEST_PAUSE_TIME = 2000;

	protected Object m_requestLock;

	protected String m_serverIpAddress;
	
	protected AppMenuBar m_menuBar;

	protected boolean m_connectedToServer;
	
	protected Board m_board;
	protected OpponentBoardImpl m_oppBoard;
	protected Piece m_lastPiece;
	protected Piece m_currentPiece;
	protected Thread m_lastPieceThread;
	protected Thread m_currentPieceThread;
	protected PerformanceCalculator m_perfCalc;
	protected Player m_player;
	protected PlayerInfo m_opponentInfo;
	protected boolean m_isGameInProgress;
	protected PieceGenerator m_generator;

	//protected AbstractPlayerProxyFactory m_playerProxyFactory;

	protected PieceControllableAdapter m_currentPieceController;

	protected HashMap m_weaponTargetMap;

	protected ArrayList m_activeWeaponList;

	protected Bazaar m_bazaar;

	protected PlayerStats m_playerStats;

	protected ArrayList m_playerListenerList;

	protected AppDisplay m_appDisplay;
	protected BoardDisplay m_boardDisplay;
	protected BoardDisplay m_oppBoardDisplay;
	protected InventoryDisplay m_invDisplay;
	protected PlayerDisplay m_playerDisplay;
	protected CommunicationDisplay m_commDisplay;
	protected BazaarDisplay m_bazaarDisplay;
	protected ServerPlayerDisplay m_serverPlayerDisplay;

	protected int m_newGameRequestStatus = NO_REQUEST;
	protected int m_opponentGameRequestStatus = NO_REQUEST;
	protected String m_lastRequestedOpponentUsername;
	protected Thread m_waitingRequestThread;
	
	protected boolean m_isCurrentPiecePaused;

	protected static void setRootPath(String rootPath)
	{
		sm_rootPath = rootPath;
	}
	
	protected static void setIsTestMode(boolean tm)
	{
		sm_isTestMode = tm;
	}
	
	protected static void setDirectEnvMode(boolean dem)
	{
		sm_directEnvMode = dem;
	}

	public static String getRootPath()
	{
		return sm_rootPath;
	}
	
	public static boolean isTestMode()
	{
		return sm_isTestMode;
	}
	
	public static boolean isDirectEnvMode()
	{
		return sm_directEnvMode;
	}
	
	protected void initializeWeaponTargets()
	{
		m_weaponTargetMap.put(PieceMovementWeaponType.class, WeaponTargetFactory.makeGeneratorWeaponTarget(m_generator));
		m_weaponTargetMap.put(PieceGenerationWeaponType.class, WeaponTargetFactory.makeGeneratorWeaponTarget(m_generator));
		m_weaponTargetMap.put(SpecialPieceWeaponType.class, WeaponTargetFactory.makeGeneratorWeaponTarget(m_generator));
		m_weaponTargetMap.put(StaticBoardModWeaponType.class, WeaponTargetFactory.makeBoardWeaponTarget(m_board));
		m_weaponTargetMap.put(BazaarModWeaponType.class, WeaponTargetFactory.makeBazaarWeaponTarget(m_bazaar));
		m_weaponTargetMap.put(PlayerModWeaponType.class, WeaponTargetFactory.makePlayerWeaponTarget(m_player));
	}

	protected WeaponTarget getWeaponTarget(Weapon w)
	{
		return (WeaponTarget)m_weaponTargetMap.get(w.getWeaponType());
	}

	protected void updateActiveWeaponList(int lines)
	{
		Iterator l_iter = m_activeWeaponList.iterator();
		while(l_iter.hasNext())
		{
			Weapon l_weapon = (Weapon)l_iter.next();
			l_weapon.decrementDuration(lines);

			if (false == l_weapon.isActive())
			{
				l_iter.remove();
			}
		}
	}

	protected void deactivateWeapons()
	{
		Iterator l_iter = m_activeWeaponList.iterator();
		while(l_iter.hasNext())
		{
			Weapon l_weapon = (Weapon)l_iter.next();

			l_weapon.deactivate();
		}
	}
	
	protected void killPiece(Piece p, Thread pt)
	{
		if (null != p)
		{
			if (DEBUG)
			{
				System.err.println("DEBUG: About to tell current piece to STOP for ["+m_player.getUsername()+"]");
			}

			p.stop();

			p.delete();

			m_board.repaint();

			if (DEBUG)
			{
				System.err.println("DEBUG: About to tell current piece thread to JOIN for ["+m_player.getUsername()+"]");
			}

			try
			{
				if (pt.isAlive())
				{
					pt.join(JOIN_TIMEOUT);
				}
			}
			catch(InterruptedException ie)
			{
			}
		}
	}

	protected void commonEnd(String gameOverMessage)
	{
		m_isGameInProgress = false;
		
		m_menuBar.setChallengeEnabled(true);
		m_oppBoardDisplay.setPlayerHandle(true, " ");

		if (DEBUG)
		{
			System.err.println("DEBUG: Entered commonEnd for ["+m_player.getUsername()+"]");
		}

		if (DEBUG)
		{
			System.err.println("DEBUG: Game ended for ["+m_player.getUsername()+"] with message ["+gameOverMessage+"]");
		}

		// 1. Stop current piece
		killPiece(m_lastPiece, m_lastPieceThread);
		killPiece(m_currentPiece, m_currentPieceThread);

		// 2. Show game over dialog indicating other player lost
		JOptionPane.showMessageDialog(this, gameOverMessage);

		// 3. Clear client data
		m_bazaar.resetLines();
		m_board.clearAll();
		m_oppBoard.clearAll();
		m_player.reset(false);

		// 4. Deactivate any active weapons
		deactivateWeapons();

		// 5. Clear client UI
		m_playerDisplay.playerUpdate(0, 0, 0, m_player.getPlayerInfo());

		// Clear opponents stats
		m_playerDisplay.playerUpdate(0, 0, 0, m_opponentInfo);
		
		m_invDisplay.inventoryUpdate(m_player.getInventory());
	}
	
	protected void setPause(boolean pause)
	{
		m_isCurrentPiecePaused = pause;
		
		m_currentPiece.setPause(pause);
	}

	public BattleTrisApp(String serverIpAddress)
	{
		this(serverIpAddress, 240, 480, null, null);
	}

	public BattleTrisApp(String serverIpAddress, int w, int h)
	{
		this(serverIpAddress, w, h, null, null);
	}

	public BattleTrisApp(String serverIpAddress, int w, int h, String username, String displayName)
	{
		m_serverIpAddress = serverIpAddress;
		
		m_connectedToServer = false;

		m_requestLock = new Object();

		m_perfCalc = new DefaultPerformanceCalculator();
		m_generator = new PieceGenerator();

		m_weaponTargetMap = new HashMap();

		m_activeWeaponList = new ArrayList();

		m_playerListenerList = new ArrayList();
	
		// Not until we connect to an opponent
		m_isGameInProgress = false;
		
		this.setTitle("BattleTris");

		try
		{
			m_board = new BoardImpl(this, w, h);
		}
		catch(Exception p_ex)
		{
			System.out.println(p_ex);

			System.exit(1);
		}

		m_bazaar = new Bazaar(getRootPath()+DEFAULT_WEAPON_CONFIG_PATH);

		String l_username = username;
		String l_displayName = displayName;

		if (null == username || null == displayName)
		{
			while(l_username == null || l_username.equals(""))
			{
				l_username = JOptionPane.showInputDialog(this, ENTER_USERNAME_MESSAGE, USERNAME_TITLE, JOptionPane.QUESTION_MESSAGE);
			}

			while(l_displayName == null || l_displayName.equals(""))
			{
				l_displayName = JOptionPane.showInputDialog(this, ENTER_DISPLAYNAME_MESSAGE, DISPLAYNAME_TITLE, JOptionPane.QUESTION_MESSAGE);
			}
		}

		m_currentPieceController = new PieceControllableAdapter();
		
		m_player = PlayerFactory.makePlayer(this, m_currentPieceController, m_serverIpAddress, l_username, l_displayName);

		
		m_board.addKeyListener(m_player);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addKeyListener(m_player);

		m_boardDisplay = new BoardDisplay(false, m_board);
		m_boardDisplay.addKeyListener(m_player);
		
		m_boardDisplay.setPlayerHandle(false, m_player.getPlayerInfo().getDisplayName());
		
		// TODO: Fill this in
		m_oppBoard = new OpponentBoardImpl(w, h);
		m_oppBoardDisplay = new BoardDisplay(true, m_oppBoard);
		
		m_invDisplay = new InventoryDisplay(m_player.getInventory());
		m_invDisplay.addKeyListener(m_player);

		m_playerDisplay = new PlayerDisplay(m_bazaar, m_player.getPlayerInfo());
		m_playerDisplay.addKeyListener(m_player);

		m_bazaarDisplay = new BazaarDisplay(m_bazaar);
		m_bazaarDisplay.addInventoryListener(m_invDisplay);
		
		//m_bazaarDisplay
		
		addWindowListener(this);

		m_serverPlayerDisplay = new ServerPlayerDisplay(this, this, m_player.getPlayerInfo());

		addPlayerListener(m_playerDisplay);

		m_commDisplay = new CommunicationDisplay();
				
		m_appDisplay = new AppDisplay(this, m_boardDisplay, m_invDisplay, m_playerDisplay, m_oppBoardDisplay, m_commDisplay);
		m_appDisplay.addKeyListener(m_player);

		m_appDisplay.addPanel(AppDisplay.BAZAAR_DISPLAY, m_bazaarDisplay);
		m_appDisplay.addPanel(AppDisplay.SERVER_PLAYER_DISPLAY, m_serverPlayerDisplay);

		m_menuBar = new AppMenuBar(this, this);
		
		this.setMenuBar(m_menuBar);

		getContentPane().add(m_appDisplay);

		pack();
		
		initializeWeaponTargets();
				
		// Initial attempt, ok if it fails
		this.connectPlayer();
		
		this.showBazaar();		
	}
	
	public void showBazaar()
	{
		if (BattleTrisApp.isTestMode())
		{
			m_bazaarDisplay.showBazaar(m_player);
		}
	}

	public Frame getVisualFrame()
	{
		return this;
   	}

	public Piece getCurrentPiece()
	{
		return m_currentPiece;
	}
	
	public void sendBoardUpdate(BoardImageData bid)
	{
		try {
			m_player.sendBoardUpdate(bid);
		}
		catch(NetworkException ne)
		{
			// TODO: Do something here
		}
	}
	
	public void receivedBoardUpdate(BoardImageData data)
	{
		data.render(m_oppBoard);
	}

	public void setPauseEnabled(boolean pe)
	{
		// TODO: Fill this in!
		System.out.println("Pause enabled: "+pe);
	}

	public void setShowNextPiece(boolean snp)
	{
		// TODO: Fill this in!		
		System.out.println("Show next piece: "+snp);
	}
	
	public void setShowPlayerIP(boolean pip)
	{
		String l_addrStr = null;
		if (pip) {
			l_addrStr = "unknown";
			InetAddress l_addr = null;
			try
			{
				Enumeration l_niEnum = NetworkInterface.getNetworkInterfaces();
				while(l_niEnum.hasMoreElements())
				{
					NetworkInterface l_ni = (NetworkInterface)l_niEnum.nextElement();
	
					System.err.println("AppDisplay::actionPerformed: network interface=["+l_ni.getName()+"]");
					
					Enumeration l_addrEnum = l_ni.getInetAddresses();
					while(l_addrEnum.hasMoreElements())
					{
						l_addr = (InetAddress)l_addrEnum.nextElement();
						if (l_addr.isSiteLocalAddress())
						{
							l_addrStr = l_addr.getHostAddress();
							System.err.println("AppDisplay::actionPerformed: addr props=["+l_addrStr+"]");
						}
					}
				}
			}
			catch(SocketException se)
			{
				
			}
		}
		
		m_commDisplay.setClientIPMessage(l_addrStr);
					
		//JOptionPane.showMessageDialog(this, AppDisplay.WEB_ADDRESS_MESSAGE_PREFIX+l_addrStr, AppDisplay.WEB_ADDRESS_TITLE, JOptionPane.INFORMATION_MESSAGE);						
	}
	
	public void setShowServerIP(boolean sip)
	{
		if (sip) {
			m_commDisplay.setServerIPMessage(m_serverIpAddress);
		}
		else {
			m_commDisplay.setServerIPMessage(null);			
		}
	}
	
	/**
	 * This method blocks on a response from the Server.
	 * After some specified timeout, it will stop waiting for a
	 * response and automatically cancel the request.
	 * @return true if opponent accepts, false otherwise
 	 **/
	public int requestNewGame(String opponentUsername)
	{
		m_lastRequestedOpponentUsername = opponentUsername;
		m_waitingRequestThread = Thread.currentThread();
		
		try {
			m_player.requestNewGame(opponentUsername);			
		}
		catch(NetworkException ne) {
			return REQUEST_SERVER_ERROR;
		}

		synchronized(m_requestLock)
		{
			try
			{
				m_requestLock.wait(REQUEST_WAIT_TIMEOUT);
				System.out.println("BattleTrisApp::requestNewGame done waiting for response");
			}
			catch(InterruptedException ie)
			{
				if (DEBUG) {
					System.out.println("BattleTrisApp::requestNewGame wait interrupted");
				}

				int l_retVal = m_newGameRequestStatus;
				m_newGameRequestStatus = NO_REQUEST;

				return l_retVal;
			}
		}

		if (DEBUG) {
			System.out.println("BattleTrisApp::requestNewGame wait timed out");
		}

		// Timed out
		int l_retVal = REQUEST_TIMEOUT;
		m_newGameRequestStatus = NO_REQUEST;

		return l_retVal;
	}

	public void endGame()
	{
		if (DEBUG)
		{
			System.err.println("DEBUG: About to tell opponent that game is over");
		}
		
		try {
			m_player.sendGameEnded(m_player.getUsername());
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}

		if (DEBUG)
		{
			System.err.println("DEBUG: Told opponent game is over");
		}

		this.commonEnd(YOU_LOSE_MESSAGE);
	}

	public HashSet getPlayerList()
	{
		HashSet l_ret = null;
		try
		{
			l_ret = m_player.getPlayerList();
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}

		return l_ret;
	}

	/**
	 * This method stops the loop that is waiting on a response
	 * from the opponent and tells the server that the request is
	 * canceled.
 	 **/
	public void cancelNewGameRequest()
	{
		m_newGameRequestStatus = REQUEST_CANCELED;

		if (DEBUG)
		{
			System.out.println("DEBUG: Attempting to cancel new game request");
		}
		if (Thread.currentThread() == m_waitingRequestThread)
		{
			System.err.println("ERROR: Tried to interrupt myself!!!");
		}
		m_waitingRequestThread.interrupt();
		try
		{
			m_player.cancelNewGameRequest(m_lastRequestedOpponentUsername);

			if (DEBUG)
			{
				System.out.println("DEBUG: Called cancelNewGameRequest");
			}

			/*
			synchronized(m_requestLock)
			{
				m_requestLock.notify();
			}
			*/
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}

	public void newGameAccepted(PlayerInfo opponent)
	{		
		if (DEBUG)
		{
			System.out.println("DEBUG: BattleTrisApp::newGameAccepted called!");
		}
		m_newGameRequestStatus = REQUEST_ACCEPTED;

		/*
		synchronized(m_requestLock)
		{
			m_requestLock.notify();
		}
		*/
		m_waitingRequestThread.interrupt();

		if (DEBUG) {
			System.out.println("DEBUG: new opponent="+opponent.toString());
		}
		
		m_opponentInfo = opponent;
		m_isGameInProgress = true;

		this.startNextPiece();
	}

	public void newGameCanceled()
	{
		m_opponentGameRequestStatus = REQUEST_CANCELED;
		
		if (DEBUG) {
			System.err.println("newGameCanceled called for ["+m_player.getUsername()+"]!");
		}
	}

	public void newGameRejected()
	{
		if (DEBUG)
		{
			System.out.println("DEBUG: BattleTrisApp: player is "+m_player.getUsername());
			System.out.println("DEBUG: BattleTrisApp::newGameRejected called!");
		}
		m_newGameRequestStatus = REQUEST_REJECTED;

		/*
		synchronized(m_requestLock)
		{
			System.out.println("DEBUG: About to notify ChallengeDialog of rejected game");
			m_requestLock.notify();
		}
		*/
		m_waitingRequestThread.interrupt();
	}


	public void gameEnded()
	{
		this.commonEnd(YOU_WIN_MESSAGE);
	}

	public void showNewGameRequestDialog(PlayerInfo opponent)
	{
		m_opponentGameRequestStatus = NO_REQUEST;
		if (DEBUG) {
			System.err.println("BattleTrisApp::showNewGameRequestDialog (About to show) for ["+m_player.getUsername()+"] opponentReqStatus="+m_opponentGameRequestStatus);
		}
		int l_response = JOptionPane.showConfirmDialog(this, opponent.getDisplayName()+GAME_REQUEST_MESSAGE_SUFFIX, GAME_REQUEST_TITLE, JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == l_response)
		{
			if (DEBUG) {
				System.err.println("BattleTrisApp::showNewGameRequestDialog for ["+m_player.getUsername()+"] opponentReqStatus="+m_opponentGameRequestStatus);
			}
			if (REQUEST_CANCELED != m_opponentGameRequestStatus)
			{
				try
				{
					if (DEBUG)
					{
						System.out.println("Accepting request from ["+opponent.getUsername()+"] to ["+m_player.getUsername()+"]");
					}
					m_player.acceptNewGameRequest(opponent.getUsername());

					if (DEBUG)
					{
						System.out.println("DEBUG: new opponent="+opponent.toString());
					}
					
					m_opponentInfo = opponent;
					m_isGameInProgress = true;

					if (DEBUG)
					{
						System.out.println("Starting piece!");
					}
					this.startNextPiece();
				}
				catch(NetworkException ne)
				{
					ne.getCause().printStackTrace(System.err);
				}
			}
		}
		else
		{
			if (REQUEST_CANCELED != m_opponentGameRequestStatus)
			{
				try
				{
					if (DEBUG) {
						System.out.println("Rejecting request from ["+opponent.getUsername()+"] to ["+m_player.getUsername()+"]");
					}
					m_player.rejectNewGameRequest(opponent.getUsername());
				}
				catch(NetworkException ne)
				{
					ne.getCause().printStackTrace(System.err);
				}
			}
		}
	}
	
	protected void exitGame()
	{
		if (null != m_currentPiece)
		{
			m_currentPiece.stop();

			try
			{
				if (m_currentPieceThread.isAlive())
				{
					m_currentPieceThread.join();
				}
			}
			catch(InterruptedException ie)
			{
			}
		}

		m_serverPlayerDisplay.shutdown();
		
		try
		{
			m_player.reset(true);
			m_player.disconnectFromServer();
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}

		System.exit(0);
	}
	
	// WindowListener methods
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) 
	{
	}

	public void windowClosing(WindowEvent e) 
	{
		exitGame();
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected void connectPlayer()
	{
		int l_connStat = 0;
		try {
			l_connStat = m_player.connectToServer();
			
			if (DEBUG) {
				System.err.println("Player.connectToServer: "+l_connStat);
			}
			
			if (BattleTrisExitCodes.OK != l_connStat)
			{
				System.err.println("BattleTrisApp: Failed to connect to server");
				m_connectedToServer = false;
				m_menuBar.setChallengeEnabled(false);
				m_menuBar.setConnectEnabled(true);
				m_commDisplay.addMessage("Failed to connect to server ["+m_serverIpAddress+"]");
			}
			else {
				m_connectedToServer = true;
				m_menuBar.setChallengeEnabled(true);
				m_menuBar.setConnectEnabled(false);
				m_commDisplay.addMessage("Connected to server ["+m_serverIpAddress+"]");
			}
			addPlayerListener(m_player);
		}
		catch(NetworkException ne)
		{
			ne.printStackTrace(System.err);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if (src instanceof JButton)
		{
		}
		else if (src instanceof MenuItem)
		{
			MenuItem l_mi = (MenuItem)src;
			String l_cmd = l_mi.getActionCommand();
			
			if (l_cmd.equals(AppMenuBar.CMD_CONNECT)) {
				this.connectPlayer();
			}
			else if (l_cmd.equals(AppMenuBar.CMD_CHALLENGE)) {
				this.startGame();
			}
			else if (l_cmd.equals(AppMenuBar.CMD_QUIT)) {				
				double rt = BattleTrisApp.getAverageRenderTime();
				long total_rt = BattleTrisApp.getTotalRenderTime();
				long count = BattleTrisApp.getDrawCount();
				if (DEBUG)
				{
					System.out.println("Total blocks rendered: "+String.valueOf(count));
					System.out.println("Total render time: "+String.valueOf(total_rt));		
					System.out.println("Average block render time (ms): "+String.valueOf(rt));
				}

				exitGame();				
			}
			else if (l_cmd.equals(AppMenuBar.CMD_ABOUT)) {
				m_appDisplay.showPanel(AppDisplay.ABOUT_DISPLAY);
			}
		}
	}

	public void addPlayerListener(PlayerListener pl)
	{
		m_playerListenerList.add(pl);
	}

	public void playerUpdate(int lines, int points, int funds, PlayerInfo playerInfo)
	throws NetworkException
	{
		// For now, this will only consist of the following:
		// 1. displays
		// 2. This client's player 
		//    (which will send the request to the server and pass it
		//    to the opponent)
		for(int i = 0; i < m_playerListenerList.size(); i++)
		{
			PlayerListener l_pl = (PlayerListener)m_playerListenerList.get(i);
			l_pl.playerUpdate(lines, points, funds, playerInfo);
		}
	}

	/*
    public void startClient(Player player)
    {
	}
    */
	
	public void startGame()
	{
		if (!m_isGameInProgress)
		{
			m_serverPlayerDisplay.showServerPlayerInfo();
		}
	}

	public boolean isGameInProgress()
	{
		return m_isGameInProgress;
	}

    public void startNextPiece()
    {
    	if (m_isGameInProgress)
    	{
    		m_lastPiece = m_currentPiece;
    		m_lastPieceThread = m_currentPieceThread;
    		
    		m_currentPiece = m_generator.getNextPiece(m_board);
    		m_currentPiece.create();

    		m_currentPieceThread = new Thread(m_currentPiece);

    		m_currentPieceThread.start();

    		m_currentPieceController.updatePiece(m_currentPiece);
		
    		m_currentPiece.setPause(m_isCurrentPiecePaused);
    		
    		m_menuBar.setChallengeEnabled(false);
    		
    		m_oppBoardDisplay.setPlayerHandle(true, m_opponentInfo.getDisplayName());
    		
    		m_serverPlayerDisplay.hideServerPlayerInfo();
    	}
	}

	public void pauseGame()
	{
		// TODO: Eventually, tell opponent game to pause as well
		// TODO: Put this back: m_currentPiece.togglePause();
		setPause(!m_isCurrentPiecePaused);
	}

	public void customizeInputControls()
	{
		// TODO: Show controls dialog
	}

	public void useWeapon(Weapon weapon)
	{
		try
		{
			m_commDisplay.addMessage("You use **"+weapon.getName()+"** on "+m_opponentInfo.getDisplayName());
			m_player.useWeapon(m_player.getUsername(), weapon);
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}

		// Update inventory display
		m_invDisplay.inventoryUpdate(m_player.getInventory());
	}

	public void weaponUsed(Weapon weapon)
	{
		if (DEBUG) {
			System.err.println("Interfaces implemented by weapon ["+weapon.getClass()+"]: ");
			Class[] l_interfaces = weapon.getClass().getInterfaces();
			for(int i = 0; i < l_interfaces.length; i++)
			{
				System.err.println(l_interfaces[i]);
			}
		}

		WeaponTarget l_wt = getWeaponTarget(weapon);

		if (null != l_wt)
		{
			weapon.visit(l_wt);

			m_activeWeaponList.add(weapon);
			
			m_commDisplay.addMessage(m_opponentInfo.getDisplayName()+" used **"+weapon.getName()+"** on you!");
		}
		else
		{
			System.err.println("No target mapping for class ["+weapon.getClass()+"]");
		}
	}

	public void updateBazaarLines(int lines)
	{
		boolean l_showBazaar = m_bazaar.updateLinesToNextBazaar(lines);

		if (l_showBazaar)
		{
			setPause(true);
			
			m_bazaarDisplay.showBazaar(m_player);
			while(m_bazaarDisplay.isOpen())
			{
				try {
					Thread.sleep(50);
				}
				catch(InterruptedException ie)
				{				
				}
			}

			m_bazaar.resetLines();

			//WaitingForOpponent l_wfoDlog = null;
			try
			{
				//l_wfoDlog = new WaitingForOpponent(this, m_opponentInfo.getDisplayName());
				boolean l_firstRequest = true;
				while(!m_player.requestReturnFromBazaar(m_player.getUsername(), l_firstRequest))
				{
					if (l_firstRequest)
					{
						l_firstRequest = false;
						//l_wfoDlog.show();
						m_commDisplay.addMessage("Waiting for "+m_opponentInfo.getDisplayName());
						//l_wfoDlog.setVisible(true);
						
					}
					try
					{
						Thread.sleep(BAZAAR_WAIT_INTERVAL);
						// DEBUG:
						if (DEBUG)
						{
							System.out.println();
							System.out.print(m_player.getUsername()+" is waiting for other player...");
						}
					}
					catch(InterruptedException ie)
					{
					}
				}
			}
			catch(NetworkException ne)
			{
				ne.printStackTrace(System.err);
			}
			finally
			{
				// Hide dialog
				//l_wfoDlog.hide();
				//l_wfoDlog.setVisible(false);
				m_commDisplay.addMessage(m_opponentInfo.getDisplayName()+" has returned");
			}

			if (DEBUG) {
				System.out.println("done");
			}

			// Unpause
			setPause(false);
		}

		try
		{
			PlayerInfo pi = m_player.getPlayerInfo();
			this.playerUpdate(0, 0, pi.getMoney(), pi);
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}

	public void reportPlayerLinesAndValues(int lines, int totalValue)
	{
		if (null != m_player)
		{
			// Calculate points and money
			int l_points = m_perfCalc.getPoints(lines, totalValue);
			int l_funds = m_perfCalc.getFunds(lines, totalValue);

			try
			{
				m_player.update(lines, l_points, l_funds);
				m_player.updateLines(m_player.getUsername(), lines);
				
			}
			catch(NetworkException ne)
			{
				ne.getCause().printStackTrace(System.err);
			}

			/*
			try
			{
				this.playerUpdate(false, lines, l_points, l_funds, m_player.getPlayerInfo());
			}
			catch(RemoteException re)
			{
				re.getCause().printStackTrace(System.err);
			}
*/
			// Update weapons
			updateActiveWeaponList(lines);
		}
	}

	public static void main(String[] args)
	{
		if (args.length < 3) {
			System.err.println("usage: BattleTrisApp <server ip> <width> <height> [<username> <displayName>] -Dbt.rootpath=<rootpath>");
			System.exit(1);
		}

		Boolean l_isDirectEnvMode = Boolean.valueOf(System.getProperty(DIRECT_ENV_MODE_KEY, DEFAULT_DIRECT_ENV_MODE));
		BattleTrisApp.setDirectEnvMode(l_isDirectEnvMode.booleanValue());

		String l_rootPath = null;
		if (Boolean.FALSE == l_isDirectEnvMode)
		{
			l_rootPath = System.getProperty(ROOT_PATH_KEY, DEFAULT_ROOT_PATH);
		}
		else
		{
			l_rootPath = System.getenv(ROOT_PATH_ENV_KEY);
		}
		BattleTrisApp.setRootPath(l_rootPath);
		
		String l_isTest = System.getProperty(TEST_MODE_KEY, DEFAULT_TEST_MODE);

		System.out.println("BattleTrisApp: bt.rootpath="+l_rootPath);
		System.out.println("BattleTrisApp: bt.istest="+l_isTest);
	
		Boolean l_isTestMode = Boolean.valueOf(l_isTest);
		BattleTrisApp.setIsTestMode(l_isTestMode.booleanValue());
		
		String ipAddress = null;
		ipAddress = args[0];

		String l_username = null;
		String l_displayName = null;

		if (args.length > 3)
		{
			l_username = args[3];
			l_displayName = args[4];
		}

		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());			
		} catch(Exception xcp) {
		}

		int w = 200, h = 100;

		try {
			w = Integer.parseInt(args[1]);
		} catch(NumberFormatException xcp) {
		}

		try {
			h = Integer.parseInt(args[2]);
		} catch(NumberFormatException xcp) {
		}

		BattleTrisApp app = null;
		if (l_username != null && l_displayName != null)
		{
			app = new BattleTrisApp(ipAddress, w, h, l_username, l_displayName);
		}
		else
		{
			app = new BattleTrisApp(ipAddress, w, h);
		}
		app.setVisible(true);
	}
}
