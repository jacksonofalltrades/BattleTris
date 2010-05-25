package battletris.net;

import java.util.ArrayList;
import java.util.HashSet;

import starwarp.net.NetworkException;

import battletris.BoardImageData;
import battletris.player.PlayerInfo;
import battletris.weapon.Weapon;

public interface BattleTrisServer
{
	public static final int DEFAULT_INT_PORT = 3333;
	public static final String DEFAULT_PORT = "3333";
	public static final String SERVER_NAME = "BattleTrisServerImpl";

	/**
	 * @return List of ServerPlayerInfo indicating players playing and waiting for a game.
	 **/
	public HashSet getPlayerList() throws NetworkException;

	/**
	 * @return A list of player rankings recorded on server.
	 **/
	public ArrayList getPlayerRankings() throws NetworkException;

	/**
	 * Give the specified player info to the server to store in it's ServerPlayerInfo list.
	 * @return true if connect was successful, false if user already exists on the server
 	 **/
	public int connectToServer(PlayerInfo player) throws NetworkException;

	/**
	 * Forward the request to the player being requested. Add an entry to the PendingNewGameRequests list.
	 * Requestor client should
	 * disallow further input until a response arrives (via newGameAccepted) or until the Requestor clicks Cancel.
	 **/
	public void requestNewGame(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException;

	/**
	 * This means requestor cancelled. Remove the entry from the PendingNewGameRequests list and send
	 * a message to the Requestee client that the request to play has been cancelled
	 **/
	public void cancelNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException;

    /**
     * Remove the entry from the PendingNewGameRequests list and place it in the GamesInProgress list.
     * Update the appropriate ServerPlayerInfo objects to indicate who is playing who now.
     * Create a PlayerRef to the original Requestor and call newGameAccepted() on it passing the PlayerRef
     * of the Requestee.
     * @return the opponent
     **/
	public void acceptNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException;

	public void rejectNewGameRequest(String playerUsernameRequestor, String opponentUsernameRequestee)
	throws NetworkException;

	public void sendBoardUpdate(String playerUsername, BoardImageData bid)
	throws NetworkException;
	
	/**
	 * Increment lines for the game that the specified player is playing
	 * and return the new total number of lines.
 	 **/
	public void updateLines(String playerUsername, int lines)
	throws NetworkException;

	public boolean requestReturnFromBazaar(String playerUsername, boolean firstRequest)
	throws NetworkException;

	public void useWeapon(String playerUsername, Weapon weapon)
	throws NetworkException;
	
	public void receivedPlayerUpdate(String playerUsername, int lines, int points, int funds,
			PlayerInfo playerInfo)
	throws NetworkException;

	/**
	 * Game has ended for specified player. Find opponent from GamesInProgress list and call gameOver() on
	 * opponent PlayerRef. Remove game from GamesInProgress list and update ServerPlayerInfo for both players
	 * to "available".
	 * Record scores, wins, losses, streaks, etc. in rankings database.
	 **/
	public void sendGameEnded(String playerUsername) throws NetworkException;

	/**
	 * This means the player is probably shutting down their client. Remove them from the ServerPlayerInfo list
	 **/
	public void disconnectFromServer(String playerUsernameRequestor)
	throws NetworkException;
	
	public void run();
}