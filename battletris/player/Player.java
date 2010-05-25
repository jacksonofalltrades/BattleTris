package battletris.player;

import java.awt.event.*;

import starwarp.net.NetworkException;

import battletris.BazaarBuyer;
import battletris.weapon.Weapon;

public interface Player extends KeyListener, BazaarBuyer, ConnectedPlayer
{
	// Network methods
	public void update(int lines, int points, int funds) throws NetworkException;
	public void applyWeapon(Weapon weapon);
	public void gameEnded();
	public void newGameAccepted(PlayerInfo playerInfo);
	public void newGameCanceled();
	public void newGameRejected();
	public void updateBazaarLines(int lines);
	public void receivedPlayerUpdate(int lines, int points, int funds, PlayerInfo playerInfo) throws NetworkException;
	public void requestForNewGame(PlayerInfo pi);
	
	// Non-network methods
	public PlayerInfo getPlayerInfo();
	public int getMoney();
	public void reset(boolean isFinal);
	public String getUsername();
}
