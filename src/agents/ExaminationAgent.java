package agents;

import Employee.AdminWorker;
import Employee.Doctor;
import Employee.Nurse;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.LinkedList;

//meta! id="4"
public class ExaminationAgent extends Agent
{
	private SimQueue<MyMessage> m_customersQueue;
	private Stat m_waitingTimeStat;

	private LinkedList<Doctor> m_doctors;

	public ExaminationAgent(int id, Simulation mySim, Agent parent)
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
		m_doctors = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfDoctors(); i++) {
			m_doctors.add(new Doctor());
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ExaminationManager(Id.examinationManager, mySim(), this);
		new ExaminationProces(Id.examinationProces, mySim(), this);
		addOwnMessage(Mc.examinationRR);
		addOwnMessage(Mc.lunchRR);
	}
	//meta! tag="end"

	public SimQueue<MyMessage> getCustomersQueue() {
		return m_customersQueue;
	}

	public Stat getWaitingTimeStat() {
		return m_waitingTimeStat;
	}

	public LinkedList<Doctor> getAvailableDoctors() {
		LinkedList<Doctor> result = new LinkedList<>();
		for(Doctor doctor : m_doctors) {
			if (doctor.isAvailable())
			{
				result.add(doctor);
			}
		}
		return result;
	}

	public LinkedList<Doctor> getDoctors() {
		return m_doctors;
	}

	public double getDoctorsUtilization() {
		double sum = 0;
		for (Doctor doctor: m_doctors) {
			sum += doctor.getTotalTimeOccupied() / mySim().currentTime();
		}

		return sum / m_doctors.size();
	}
}