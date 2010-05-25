package battletris.player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

import starwarp.net.Client;
import starwarp.net.ClosedForSendingException;
import starwarp.net.DataPacket;
import starwarp.net.NetworkException;
import starwarp.net.PacketHandler;
import starwarp.net.ShutdownPacket;
import starwarp.net.bdcs.ClientImpl;
import battletris.BattleTrisExitCodes;
import battletris.BoardImageData;
import battletris.net.common.AcceptNewGameRequestCall;
import battletris.net.common.CancelNewGameRequestCall;
import battletris.net.common.ConnectToServerCall;
import battletris.net.common.GetPlayerListCall;
import battletris.net.common.PlayerRefMethodCall;
import battletris.net.common.PlayerRefReturnValue;
import battletris.net.common.ReceivedPlayerUpdateCall;
import battletris.net.common.RejectNewGameRequestCall;
import battletris.net.common.RequestNewGameCall;
import battletris.net.common.RequestReturnFromBazaarCall;
import battletris.net.common.ReturnValueHandlerImpl;
import battletris.net.common.SendBoardUpdateCall;
import battletris.net.common.SendGameEndedCall;
import battletris.net.common.UpdateLinesCall;
import battletris.net.common.UseWeaponCall;
import battletris.weapon.Weapon;

public class ConnectedPlayerImpl implements ConnectedPlayer, PacketHandler
{
	protected PlayerImpl m_playerRef;
	protected Client m_client;
	
	protected ReturnValueHandlerImpl m_returnValueHandler;
	
	protected PrintStream m_outLogWriter;
	protected PrintStream m_errLogWriter;

	public ConnectedPlayerImpl(PlayerImpl playerRef, String username, String serverHost, int port, PrintStream outLogWriter, PrintStream errLogWriter)
	{
		m_playerRef = playerRef;
		
		m_outLogWriter = outLogWriter;
		m_errLogWriter = errLogWriter;

		ClientImpl l_client = new ClientImpl(username, serverHost, port, outLogWriter, errLogWriter);
		l_client.addIdentityData(PlayerInfo.KEY_DISPLAY_NAME, m_playerRef.getDisplayName());
		m_client = l_client;
		
		m_client.setPacketHandler(this);

		m_returnValueHandler = new ReturnValueHandlerImpl(m_client);		
	}
	
	public void shutdown()
	{
		try {
			m_client.send(new ShutdownPacket(m_client.getClientId(), ShutdownPacket.TYPE_SDR));
		}
		catch(ClosedForSendingException cfse) {
			System.err.println("Client ["+m_client.getClientId()+"] is already shutdown.");
		}
	}
	
	public void handleDataPacket(DataPacket dp)
	{
		if (dp instanceof PlayerRefReturnValue) {
			m_returnValueHandler.addReturnValue((PlayerRefReturnValue)dp);
		}
		else {
			PlayerRefMethodCall l_call = (PlayerRefMethodCall)dp;
			
			class HandlerThread extends Thread
			{
				protected PlayerRefMethodCall m_call;
				
				public HandlerThread(PlayerRefMethodCall call)
				{
					m_call = call;
				}

				public void run()
				{
					try {
						m_call.invoke(m_playerRef, m_returnValueHandler);
					}
					catch(NetworkException ne)
					{
						ne.printStackTrace(m_errLogWriter);
					}					
				}
			}
			
			HandlerThread l_ht = new HandlerThread(l_call);
			
			l_ht.start();
		}
	}

	public int connectToServer() throws NetworkException 
	{
		m_client.connect();
		
		if (m_client.isConnected()) {
			m_client.start();
			
			while(!m_client.isReadyForSending()) 
			{
				try {
					Thread.sleep(500);
				}
				catch(InterruptedException ie)
				{
				}
			}
					
			ConnectToServerCall l_call = new ConnectToServerCall(m_playerRef.getPlayerInfo());
			m_client.send(l_call);
			
			PlayerRefReturnValue l_rv = m_returnValueHandler.getReturnValue(l_call.getPacketId());
			
			return l_rv.getInteger().intValue();
		}
		else {
			return BattleTrisExitCodes.NO_SERVER;
		}
	}

	public void disconnectFromServer() throws NetworkException 
	{
		if (m_client.isReadyForSending())
		{				
			// Clean up client
			try {
				m_client.send(new ShutdownPacket(m_playerRef.getUsername(), ShutdownPacket.TYPE_SDR));
			}
			catch(ClosedForSendingException cfse) {
				m_outLogWriter.println("Client ["+m_playerRef.getUsername()+"] is already shutdown.");
			}
		}
	}

	public void requestNewGame(String opponent) throws NetworkException 
	{
		RequestNewGameCall l_call = new RequestNewGameCall(m_playerRef.getPlayerInfo(), opponent);
		m_client.send(l_call);
	}

	public void acceptNewGameRequest(String opponentUsernameRequestor)
	throws NetworkException 
	{
		AcceptNewGameRequestCall l_call = new AcceptNewGameRequestCall(m_playerRef.getPlayerInfo(), opponentUsernameRequestor);
		m_client.send(l_call);
	}

	public void cancelNewGameRequest(String opponentUsernameRequestee)
	throws NetworkException
	{
		CancelNewGameRequestCall l_call = new CancelNewGameRequestCall(m_playerRef.getPlayerInfo(), opponentUsernameRequestee);
		m_client.send(l_call);
	}

	public void rejectNewGameRequest(String opponentUsernameRequestor)
	throws NetworkException 
	{
		RejectNewGameRequestCall l_call = new RejectNewGameRequestCall(m_playerRef.getPlayerInfo(), opponentUsernameRequestor);
		m_client.send(l_call);
	}

	public void sendGameEnded(String playerUsername) throws NetworkException 
	{
		SendGameEndedCall l_call = new SendGameEndedCall(m_playerRef.getPlayerInfo());
		m_client.send(l_call);
	}

	public HashSet getPlayerList() throws NetworkException 
	{
		if (m_client.isReadyForSending())
		{
			GetPlayerListCall l_call = new GetPlayerListCall(m_playerRef.getPlayerInfo());
			
			m_client.send(l_call);
			
			PlayerRefReturnValue l_rv = m_returnValueHandler.getReturnValue(l_call.getPacketId());
			
			return l_rv.getHashSet();
		}
		else {
			return new HashSet();
		}
	}

	public ArrayList getPlayerRankings() throws NetworkException {
		// TODO Auto-generated method stub
		return null;
	}

	public void useWeapon(String playerUsername, Weapon weapon)
	throws NetworkException 
	{
		UseWeaponCall l_call = new UseWeaponCall(m_playerRef.getPlayerInfo(), weapon);
		m_client.send(l_call);
	}

	public boolean requestReturnFromBazaar(String playerUsername,
			boolean firstRequest) 
	throws NetworkException 
	{
		RequestReturnFromBazaarCall l_call = new RequestReturnFromBazaarCall(m_playerRef.getPlayerInfo(), firstRequest);
		m_client.send(l_call);

		PlayerRefReturnValue l_rv = m_returnValueHandler.getReturnValue(l_call.getPacketId());
		
		return l_rv.getBoolean().booleanValue();
	}

	public void updateLines(String playerUsername, int lines)
	throws NetworkException 
	{
		UpdateLinesCall l_call = new UpdateLinesCall(m_playerRef.getPlayerInfo(), lines);
		m_client.send(l_call);
	}

	public void sendBoardUpdate(BoardImageData data) 
	throws NetworkException
	{
		SendBoardUpdateCall l_call = new SendBoardUpdateCall(m_playerRef.getPlayerInfo(), data);
		m_client.send(l_call);
	}
	
	public void playerUpdate(int lines, int points, int funds,
			PlayerInfo playerInfo) throws NetworkException 
	{
		ReceivedPlayerUpdateCall l_call = new ReceivedPlayerUpdateCall(m_playerRef.getPlayerInfo(), lines, points, funds, playerInfo);
		m_client.send(l_call);
	}

}
