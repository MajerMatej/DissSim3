package agents;

import OSPABA.*;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class WaitingRoomAgent extends Agent
{
	private Stat m_waitingTimeStat;
	private int m_customersWaiting;

	public WaitingRoomAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		m_waitingTimeStat = new Stat();
		m_customersWaiting = 0;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new WaitingRoomManager(Id.waitingRoomManager, mySim(), this);
		new WaitingProcess(Id.waitingProcess, mySim(), this);
		addOwnMessage(Mc.waitingRR);
	}
	//meta! tag="end"


	public Stat getWaitingTimeStat() {
		return m_waitingTimeStat;
	}

	public int getCustomersWaiting() {
		return m_customersWaiting;
	}

	public void setCustomersWaiting(int customersWaiting) {
		this.m_customersWaiting = customersWaiting;
	}
}