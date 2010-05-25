package battletris.player;

import java.util.ArrayList;
import java.util.HashSet;

import starwarp.net.NetworkException;
import battletris.BoardImageData;
import battletris.weapon.Weapon;

public interface ConnectedPlayer extends PlayerListener
{
	public static final int DEFAULT_PORT = 3333;
	
	public int connectToServer() throws NetworkException;
	public void disconnectFromServer() throws NetworkException;
	
	public void requestNewGame(String opponent) throws NetworkException;
	public void acceptNewGameRequest(String opponentUsernameRequestor) throws NetworkException;
	public void cancelNewGameRequest(String opponentUsernameRequestor) throws NetworkException;
	public void rejectNewGameRequest(String opponentUsernameRequestor) throws NetworkException;

	// Let opponent know that game has ended for player
	public void sendGameEnded(String playerUsername) throws NetworkException;

	public HashSet getPlayerList() throws NetworkException;
	public ArrayList getPlayerRankings() throws NetworkException;

	// Use player's weapon on opponent
	public void useWeapon(String playerUsername, Weapon weapon) throws NetworkException;

	public boolean requestReturnFromBazaar(String playerUsername, boolean firstRequest) throws NetworkException;

	// Send my most recent new line count to server
	public void updateLines(String playerUsername, int lines) throws NetworkException;
	
	public void sendBoardUpdate(BoardImageData data) throws NetworkException;
	
    // Update opponent on my changed stats
	public void playerUpdate(int lines, int points, int funds, PlayerInfo playerInfo) throws NetworkException;
}
