package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="52"
public class VaccinationFillAgent extends Agent
{
	private SimQueue<MyMessage> m_nursesQueue;
	private Stat m_waitingTimeStat;
	public VaccinationFillAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		m_nursesQueue = new SimQueue<>(new WStat(mySim()));
		m_waitingTimeStat = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new VaccinationFillManager(Id.vaccinationFillManager, mySim(), this);
		new VaccinationFillProcess(Id.vaccinationFillProcess, mySim(), this);
		addOwnMessage(Mc.refillRR);
	}
	//meta! tag="end"


	public SimQueue<MyMessage> getNursesQueue() {
		return m_nursesQueue;
	}

	public Stat getWaitingTimeStat() {
		return m_waitingTimeStat;
	}
}