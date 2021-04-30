package agents;

import Employee.AdminWorker;
import Employee.Nurse;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="5"
public class VaccinationAgent extends Agent
{
	private SimQueue<MyMessage> m_customersQueue;
	private Stat m_waitingTimeStat;

	private LinkedList<Nurse> m_nurses;

	public VaccinationAgent(int id, Simulation mySim, Agent parent)
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
		m_nurses = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfNurses(); i++) {
			m_nurses.add(new Nurse());
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new VaccinationManager(Id.vaccinationManager, mySim(), this);
		new VaccinationProcess(Id.vaccinationProcess, mySim(), this);
		addOwnMessage(Mc.lunchRR);
		addOwnMessage(Mc.vaccinationRR);
	}
	//meta! tag="end"

	public SimQueue<MyMessage> getCustomersQueue() {
		return m_customersQueue;
	}

	public Stat getWaitingTimeStat() {
		return m_waitingTimeStat;
	}

	public LinkedList<Nurse> getAvailableNurses() {
		LinkedList<Nurse> result = new LinkedList<>();
		for(Nurse nurse : m_nurses) {
			if (nurse.isAvailable())
			{
				result.add(nurse);
			}
		}
		return result;
	}

	public LinkedList<Nurse> getNurses() {
		return m_nurses;
	}

	public double getNursesUtilization() {
		double sum = 0;
		for (Nurse nurse: m_nurses) {
			sum += nurse.getTotalTimeOccupied() / mySim().currentTime();
		}

		return sum / m_nurses.size();
	}
}