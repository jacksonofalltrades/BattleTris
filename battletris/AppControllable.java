package battletris;

import java.util.HashSet;

import battletris.player.PlayerListener;
import battletris.player.PlayerInfo;
import battletris.weapon.Weapon;

public interface AppControllable extends PlayerListener
{
	int REQUEST_WAIT_INTERVAL = 2000;
	int REQUEST_WAIT_TIMEOUT = 60000;

	int NO_REQUEST = 0;
	int REQUEST_REJECTED = 1;
	int REQUEST_ACCEPTED = 2;
	int REQUEST_TIMEOUT = 3;
	int REQUEST_CANCELED = 4;
	int REQUEST_SERVER_ERROR = 5;
	int SEND_GAME_ENDED_SERVER_ERROR = 6;

	public boolean isGameInProgress();

	public void startGame();

	public void pauseGame();

	public void customizeInputControls();

	public void useWeapon(Weapon weapon);

	public HashSet getPlayerList();

	public void updateBazaarLines(int lines);

	public void showNewGameRequestDialog(PlayerInfo requestor);

	public void receivedBoardUpdate(BoardImageData data);
	
	/**
	 * This method blocks on a response from the Server.
	 * After some specified timeout, it will stop waiting for a
	 * response and automatically cancel the request.
	 * @return true if opponent accepts, false otherwise
 	 **/
	public int requestNewGame(String opponentUsername);

	public void endGame();

	/**
	 * This method stops the loop that is waiting on a response
	 * from the opponent and tells the server that the request is
	 * canceled.
 	 **/
	public void cancelNewGameRequest();

	public void newGameCanceled();

	public void newGameAccepted(PlayerInfo opponent);

	public void newGameRejected();

	public void weaponUsed(Weapon weapon);

	public void gameEnded();
	
	/*************** NEW EVENTS (as of 2008-Jun-27) **********************/
	
	public void setPauseEnabled(boolean pe);
	public void setShowNextPiece(boolean snp);
	public void setShowPlayerIP(boolean pip);
	public void setShowServerIP(boolean sip);
}
