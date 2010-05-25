package battletris.player.test;

import java.util.ArrayList;
import java.util.HashSet;

import battletris.BoardImageData;
import battletris.net.ServerPlayerInfo;
import battletris.player.ConnectedPlayer;
import battletris.player.Player;
import battletris.player.PlayerInfo;
import battletris.player.PlayerInfoImpl;
import battletris.weapon.Weapon;

public class ConnectedPlayerStub implements ConnectedPlayer 
{
	ServerPlayerInfo m_spi_a;
	ServerPlayerInfo m_spi_b;
	ServerPlayerInfo m_spi_c;
	ServerPlayerInfo m_spi_d;
	ServerPlayerInfo m_spi_e;
	ServerPlayerInfo m_spi_f;
	ServerPlayerInfo m_spi_g;
	ServerPlayerInfo m_spi_h;
	ServerPlayerInfo m_spi_i;
	ServerPlayerInfo m_spi_j;
	ServerPlayerInfo m_spi_k;
	
	Player m_player;
	PlayerInfo m_a;
	PlayerInfo m_b;
	PlayerInfo m_c;
	PlayerInfo m_d;
	PlayerInfo m_e;
	PlayerInfo m_f;
	PlayerInfo m_g;
	PlayerInfo m_h;
	PlayerInfo m_i;
	PlayerInfo m_j;
	PlayerInfo m_k;
	
	TestOpponentThread m_opponent;
	
	boolean m_lastReturnFromBazaarResponse = true;
	
	boolean m_requestNewGameCanceled;
	
	RequestNewGameThread m_rt;

	// Needs access to player so it can mess with it :)
	public ConnectedPlayerStub(Player player)
	{
		m_requestNewGameCanceled = false;
		
		m_player = player;

		m_spi_a = new ServerPlayerInfo("abe", "Amazon", "");
		m_spi_b = new ServerPlayerInfo("betty", "Boisterous", "");
		m_spi_c = new ServerPlayerInfo("carl", "Catastrophic", "");
		m_spi_d = new ServerPlayerInfo("darla", "Daring", "");
		m_spi_e = new ServerPlayerInfo("elaine", "Eminent", "");
		m_spi_f = new ServerPlayerInfo("fred", "Firecracker", "");
		m_spi_g = new ServerPlayerInfo("greg", "Gorgeous", "");
		m_spi_h = new ServerPlayerInfo("helga", "Hardy", "");
		m_spi_i = new ServerPlayerInfo("irving", "Illustrious", "");
		m_spi_j = new ServerPlayerInfo("jack", "Jocular", "");
		m_spi_k = new ServerPlayerInfo("kelly", "Kirlian", "");
		
		m_a = new PlayerInfoImpl("abe", "Amazon");
		m_b = new PlayerInfoImpl("betty", "Boisterous");
		m_c = new PlayerInfoImpl("carl", "Catastrophic");
		m_d = new PlayerInfoImpl("darla", "Daring");
		m_e = new PlayerInfoImpl("elaine", "Eminent");
		m_f = new PlayerInfoImpl("fred", "Firecracker");
		m_g = new PlayerInfoImpl("greg", "Gorgeous");
		m_h = new PlayerInfoImpl("helga", "Hardy");
		m_i = new PlayerInfoImpl("irving", "Illustrious");
		m_j = new PlayerInfoImpl("jack", "Jocular");
		m_k = new PlayerInfoImpl("kelly", "Kirlian");
		
		System.err.println("ConnectedPlayerStub started with player username="+m_player.getUsername());
		
		if (m_player.getUsername().equals("jack")) {
			System.err.println("ConnectedPlayerStub starting thread");

			// Start greg opponent thread
			m_opponent = new TestOpponentThread(m_spi_g, m_g, m_player);
			
			m_opponent.start();
		}
	}

	public int connectToServer() 
	{
		// TODO Auto-generated method stub  
		return 0;
	}

	public void disconnectFromServer() {
		// TODO Auto-generated method stub

	}

	public void requestNewGame(String opponentUsername) 
	{
		m_requestNewGameCanceled = false;

		m_rt = new RequestNewGameThread(opponentUsername, this);

		m_rt.start();		
	}
	
	public void sendBoardUpdate(BoardImageData data)
	{
		
	}

	public void acceptNewGameRequest(String opponentUsernameRequestee) 
	{
		// TODO Auto-generated method stub
	}

	public void cancelNewGameRequest(String opponentUsernameRequestee) 
	{
		m_requestNewGameCanceled = true;
	}

	public void rejectNewGameRequest(String opponentUsernameRequestee) 
	{
		// TODO Auto-generated method stub
	}

	public void gameEnded(String playerUsername) 
	{
		// For ConnectedPlayerProxy testing
	}

	public HashSet getPlayerList() {
		HashSet l_set = new HashSet();
		
		m_spi_b.setOpponent(m_spi_d);
		m_spi_d.setOpponent(m_spi_b);

		m_spi_b.setAvailable(false);
		m_spi_d.setAvailable(false);

		l_set.add(m_spi_a);
		l_set.add(m_spi_b);
		l_set.add(m_spi_c);
		l_set.add(m_spi_d);
		l_set.add(m_spi_e);
		l_set.add(m_spi_f);
		l_set.add(m_spi_g);
		l_set.add(m_spi_h);
		l_set.add(m_spi_i);
		l_set.add(m_spi_j);
		l_set.add(m_spi_k);

		return l_set;
	}

	public ArrayList getPlayerRankings() {
		// TODO Auto-generated method stub
		return null;
	}

	public void useWeapon(String playerUsername, Weapon weapon) {
		// TODO Auto-generated method stub

	}

	public boolean requestReturnFromBazaar(String playerUsername,
			boolean firstRequest) 
	{
		boolean l_response = !m_lastReturnFromBazaarResponse;
		if (firstRequest){
			m_lastReturnFromBazaarResponse = !l_response;
			return l_response;
		}
		else {
			return true;
		}
	}

	public void updateLines(String playerUsername, int lines) {
		// TODO Auto-generated method stub

	}

	public void playerUpdate(int lines, int points,
			int funds, PlayerInfo playerInfo) {
		// TODO Auto-generated method stub

	}

	// Let opponent know that game has ended for player
	public void sendGameEnded(String playerUsername)
	{
		if (null != m_opponent) {
			m_opponent.shutdown();
		}
	}
	
	public PlayerInfo getPlayerInfo()
	{
		// TODO Fill this in
		return null;
	}
}
