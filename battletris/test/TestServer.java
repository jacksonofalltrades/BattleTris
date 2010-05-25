package battletris.test;

import java.util.HashSet;
import java.util.Random;

import battletris.BattleTrisExitCodes;
import battletris.net.ServerPlayerInfo;
import battletris.net.common.ConnectToServerCall;
import battletris.net.common.GameEndedCall;
import battletris.net.common.GetPlayerListCall;
import battletris.net.common.NewGameAcceptedCall;
import battletris.net.common.PlayerRefReturnValue;
import battletris.net.common.RequestForNewGameCall;
import battletris.player.PlayerInfo;
import battletris.player.PlayerInfoImpl;
import starwarp.net.ClientProxy;
import starwarp.net.ClosedForSendingException;
import starwarp.net.DataPacket;
import starwarp.net.PacketHandler;
import starwarp.net.Server;
import starwarp.net.bdcs.ServerImpl;

public class TestServer implements PacketHandler
{
	protected Server m_server;
	
	protected ServerPlayerInfo m_spi_a, m_spi_b, m_spi_c, m_spi_d, m_spi_e, m_spi_f, m_spi_g, m_spi_h, m_spi_i, m_spi_j, m_spi_k;
	
	public TestServer(int maxClients, String host, int port)
	{
		m_server = new ServerImpl(maxClients, host, port);
		m_server.setPacketHandler(this);
		
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
	}
	
	public void handleDataPacket(DataPacket dp)
	{
		// Test each packet type
		/*
		 * connectToServer
		 * requestNewGame
		 * getPlayerList
		 * sendGameEnded
		 * cancelNewGameRequest
		 * acceptNewGameRequest
		 * rejectNewGameRequest
		 * disconnectFromServer
		 * useWeapon
		 * requestReturnFromBazaar
		 * updateLines
		 */
		if (dp instanceof ConnectToServerCall)
		{
			ConnectToServerCall l_call = (ConnectToServerCall)dp;
			
			String l_senderId = dp.getSenderId();
			ClientProxy l_client = m_server.getClientProxy(l_senderId);
			
			Random l_rand = new Random();
			int l_retcode = l_rand.nextInt(3);
			int l_realRetcode = -1;
			switch(l_retcode)
			{
				case 0:
				{
					l_realRetcode = BattleTrisExitCodes.SERVER_FULL;
				}
				break;
				case 1:
				{
					l_realRetcode = BattleTrisExitCodes.USER_ALREADY_EXISTS;
				}
				break;
				case 2:
				{
					l_realRetcode = BattleTrisExitCodes.OK;
				}
				break;
			}
			
			PlayerRefReturnValue l_retValPacket = new PlayerRefReturnValue(l_call, new Integer(l_realRetcode));
			try {
				l_client.send(l_retValPacket);
			}
			catch(ClosedForSendingException cfse)
			{
			}
		}
		else if (dp instanceof GetPlayerListCall)
		{
			GetPlayerListCall l_call = (GetPlayerListCall)dp;

			String l_senderId = dp.getSenderId();
			ClientProxy l_client = m_server.getClientProxy(l_senderId);
			
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
			
			PlayerRefReturnValue l_playerListPacket = new PlayerRefReturnValue(l_call, l_set);

			try {
				l_client.send(l_playerListPacket);
			}
			catch(ClosedForSendingException cfse)
			{
			}		
		}
		else if (dp instanceof RequestForNewGameCall)
		{
			RequestForNewGameCall l_call = (RequestForNewGameCall)dp;
			
			String l_senderId = dp.getSenderId();
			ClientProxy l_client = m_server.getClientProxy(l_senderId);
			
			PlayerInfo l_requestor = new PlayerInfoImpl(l_call.getSenderId(), null);
			PlayerInfo l_requestee = new PlayerInfoImpl("carl", "Catastrophic");
			NewGameAcceptedCall l_retCall = new NewGameAcceptedCall(l_requestee, l_requestor);
			
			try {
				l_client.send(l_retCall);
			}
			catch(ClosedForSendingException cfse)
			{
			}		
		}
		else if (dp instanceof GameEndedCall)
		{
			GameEndedCall l_call = (GameEndedCall)dp;
			
			System.err.println(l_call);
		}
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
	
	public static void main(String[] args)
	{
		TestServer l_server = new TestServer(20, "localhost", 3333);
		
		l_server.run();
		
		System.exit(0);		
	}
}
