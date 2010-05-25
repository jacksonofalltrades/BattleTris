package battletris.player;

import java.io.PrintStream;

import starwarp.net.ClientProxy;
import starwarp.net.DataPacket;
import starwarp.net.NetworkException;
import starwarp.net.PacketHandler;
import battletris.BoardImageData;
import battletris.net.BattleTrisServer;
import battletris.net.common.ApplyWeaponCall;
import battletris.net.common.BattleTrisServerRefMethodCall;
import battletris.net.common.GameEndedCall;
import battletris.net.common.NewGameAcceptedCall;
import battletris.net.common.NewGameCanceledCall;
import battletris.net.common.NewGameRejectedCall;
import battletris.net.common.PlayerUpdateCall;
import battletris.net.common.ReceivedBoardUpdateCall;
import battletris.net.common.RequestForNewGameCall;
import battletris.net.common.ReturnValueHandler;
import battletris.net.common.ReturnValueHandlerImpl;
import battletris.net.common.UpdateBazaarLinesCall;
import battletris.weapon.Weapon;

public class ConnectedPlayerProxyImpl implements ConnectedPlayerProxy, PacketHandler
{
	protected PrintStream m_outLogWriter;
	protected PrintStream m_errLogWriter;
	
	protected BattleTrisServer m_btServerRef;
	protected ClientProxy m_clientProxy;
	
	protected PlayerInfo m_playerInfo;
	
	protected ReturnValueHandler m_returnValueHandler;
	
	public ConnectedPlayerProxyImpl(BattleTrisServer btServerRef, ClientProxy cp, PlayerInfo playerInfo, PrintStream outLogWriter, PrintStream errLogWriter)
	{
		m_outLogWriter = outLogWriter;
		m_errLogWriter = errLogWriter;
		m_btServerRef = btServerRef;
		m_clientProxy = cp;
		
		m_playerInfo = playerInfo;
		
		m_clientProxy.setPacketHandler(this);
		
		m_returnValueHandler = new ReturnValueHandlerImpl(m_clientProxy);
	}
	
	public PacketHandler getPacketHandler()
	{
		return this;
	}
	
	public PlayerInfo getPlayerInfo()
	{
		return m_playerInfo;
	}
	
	public ReturnValueHandler getReturnValueHandler()
	{
		return m_returnValueHandler;
	}
	
	public void handleDataPacket(DataPacket dp)
	{		
		if (dp instanceof BattleTrisServerRefMethodCall) {
			BattleTrisServerRefMethodCall l_call = (BattleTrisServerRefMethodCall)dp;
			
			l_call.invoke(m_btServerRef, m_returnValueHandler);
		}
	}

	public void requestForNewGame(PlayerInfo requestor) throws NetworkException 
	{
		RequestForNewGameCall l_call = new RequestForNewGameCall(this.getPlayerInfo(), requestor);
		
		m_clientProxy.send(l_call);
	}

	public void newGameAccepted(PlayerInfo requestee) throws NetworkException {
		NewGameAcceptedCall l_call = new NewGameAcceptedCall(m_playerInfo, requestee);
		
		m_clientProxy.send(l_call);
	}

	public void newGameCanceled() throws NetworkException {
		NewGameCanceledCall l_call = new NewGameCanceledCall(m_playerInfo);
		
		m_clientProxy.send(l_call);
	}

	public void newGameRejected() throws NetworkException {
		NewGameRejectedCall l_call = new NewGameRejectedCall(m_playerInfo);
		
		m_clientProxy.send(l_call);
	}

	public void gameEnded() throws NetworkException {
		GameEndedCall l_call = new GameEndedCall(m_playerInfo);
		
		m_clientProxy.send(l_call);
	}

	public void applyWeapon(Weapon weapon) throws NetworkException {
		ApplyWeaponCall l_call = new ApplyWeaponCall(m_playerInfo, weapon);
		
		m_clientProxy.send(l_call);
	}

	public void receivedPlayerUpdate(int lines, int points, int funds,
			PlayerInfo playerInfo) throws NetworkException {
		PlayerUpdateCall l_call = new PlayerUpdateCall(m_playerInfo, lines, points, funds, playerInfo);
		m_clientProxy.send(l_call);
	}
	
	public void receivedBoardUpdate(BoardImageData data)
	throws NetworkException
	{
		ReceivedBoardUpdateCall l_call = new ReceivedBoardUpdateCall(m_playerInfo, data);
		m_clientProxy.send(l_call);
	}

	public void updateBazaarLines(int lines) throws NetworkException {
		UpdateBazaarLinesCall l_call = new UpdateBazaarLinesCall(m_playerInfo, lines);
		
		m_clientProxy.send(l_call);		
	}

}
