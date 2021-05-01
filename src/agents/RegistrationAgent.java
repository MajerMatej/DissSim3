package agents;

import Employee.AdminWorker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="3"
public class RegistrationAgent extends Agent
{
	private SimQueue<MyMessage> m_customersQueue;
	private Stat m_waitingTimeStat;

	private LinkedList<AdminWorker> m_adminWorkers;

	public RegistrationAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		m_customersQueue = new SimQueue<>(new WStat(mySim()));
		m_waitingTimeStat = new Stat();
		m_adminWorkers = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfAdminWorkers(); i++) {
			m_adminWorkers.add(new AdminWorker());
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new RegistrationManager(Id.registrationManager, mySim(), this);
		new RegistrationProcess(Id.registrationProcess, mySim(), this);
		addOwnMessage(Mc.lunchRR);
		addOwnMessage(Mc.registrationRR);
	}
	//meta! tag="end"


	public SimQueue<MyMessage> getCustomersQueue() {
		return m_customersQueue;
	}

	public Stat getWaitingTimeStat() {
		return m_waitingTimeStat;
	}

	public LinkedList<AdminWorker> getAvailableAdminWorkers() {
		LinkedList<AdminWorker> result = new LinkedList<>();
		for(AdminWorker worker : m_adminWorkers) {
			if (worker.isAvailable())
			{
				result.add(worker);
			}
		}
		return result;
	}

	//todo remove
	public LinkedList<AdminWorker> getAdminWorkers() {
		return m_adminWorkers;
	}

	public double getAdminWorkersUtilization() {
		double sum = 0;
		for (AdminWorker worker: m_adminWorkers) {
			sum += worker.getTotalTimeOccupied() / mySim().currentTime();
		}

		return sum / m_adminWorkers.size();
	}
}