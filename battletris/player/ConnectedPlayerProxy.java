package battletris.player;

import battletris.BoardImageData;
import battletris.weapon.Weapon;
import starwarp.net.NetworkException;
import starwarp.net.PacketHandler;

public interface ConnectedPlayerProxy 
{
	public PacketHandler getPacketHandler();
	
	public PlayerInfo getPlayerInfo();
	public void requestForNewGame(PlayerInfo requestor) throws NetworkException;
	public void newGameAccepted(PlayerInfo requestee) throws NetworkException;
	public void newGameCanceled() throws NetworkException;
	public void newGameRejected() throws NetworkException;

	// Let player know that game has ended for opponent
	public void gameEnded() throws NetworkException;

	// Cause weapon to be applied to player
	public void applyWeapon(Weapon weapon) throws NetworkException;

	// Update player on opponents stats
	public void receivedPlayerUpdate(int lines, int points, int funds, PlayerInfo playerInfo) throws NetworkException;
	
	public void receivedBoardUpdate(BoardImageData data) throws NetworkException;

	// Decrement player's lines-til-next-bazaar
	public void updateBazaarLines(int lines) throws NetworkException;
}
