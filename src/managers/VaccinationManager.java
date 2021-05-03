package managers;

import Employee.Doctor;
import Employee.Nurse;
import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;

import java.util.LinkedList;

//meta! id="5"
public class VaccinationManager extends Manager
{
	private LinkedList<UniformContinuousRNG> m_nursesGens;
	private boolean m_lunchTime;
	private int m_maxLunchingEmp;
	private int m_currentLunchingEmp;

	public VaccinationManager(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}

		m_nursesGens = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfNurses() - 1; i++) {
			m_nursesGens.add(new UniformContinuousRNG(0.0, 1.0));
		}
		m_lunchTime = false;
		m_maxLunchingEmp = ((MySimulation)mySim()).getNumOfNurses() / 2;
		if(m_maxLunchingEmp == 0) m_maxLunchingEmp++;
		m_currentLunchingEmp = 0;
	}

	//meta! sender="VaccinationProcess", id="37", type="Finish"
	public void processFinishVaccinationProcess(MessageForm message)
	{

		finishWork(((MyMessage)message).getNurse());
		myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingVacc());

		if(m_currentLunchingEmp + 1 <= m_maxLunchingEmp && m_lunchTime
				&& !((MyMessage)message).getNurse().hadLunchBreak())
		{
			MessageForm copy = message.createCopy();

			sendNurseToLunch((MyMessage)copy, ((MyMessage) message).getNurse());
		}

		Nurse nurse = getAvailableNurse();
		if(myAgent().getCustomersQueue().size() > 0 && nurse != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingVacc(mySim().currentTime() - nextMessage.getStartWaitingVacc());

			startWork(nextMessage, nurse);
		}

		message.setCode(Mc.vaccinationRR);

		response(message);
	}

	//meta! sender="VaccinationCenterAgent", id="21", type="Request"
	public void processVaccinationRR(MessageForm message)
	{
		Nurse nurse = getAvailableNurse();
		if(nurse == null || myAgent().getCustomersQueue().size() > 0) {
			((MyMessage)message).setStartWaitingVacc(mySim().currentTime());
			myAgent().getCustomersQueue().enqueue((MyMessage) message);
			return;
		}
		startWork(message, nurse);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="VaccinationCenterAgent", id="65", type="Response"
	public void processLunchRR(MessageForm message)
	{
		((MyMessage)message).getLunchEmployee().backFromLunch(mySim().currentTime());
		m_currentLunchingEmp--;

		for (Nurse emp : myAgent().getAvailableNurses()) {
			if (!emp.hadLunchBreak())
			{
				sendNurseToLunch((MyMessage) message, emp);
				return;
			}
		}
		Nurse nurse = getAvailableNurse();
		if(myAgent().getCustomersQueue().size() > 0 && nurse != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingReg(mySim().currentTime() - nextMessage.getStartWaitingReg());

			startWork(nextMessage, nurse);
		}
	}

	//meta! sender="VaccRefillTransitionAgent", id="107", type="Response"
	public void processRefillRR(MessageForm message)
	{
		((MyMessage)message).getRefillNurse().setAvailable(mySim().currentTime());
		Nurse nurse = getAvailableNurse();
		if(myAgent().getCustomersQueue().size() > 0 && nurse != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingVacc(mySim().currentTime() - nextMessage.getStartWaitingVacc());

			startWork(nextMessage, nurse);
		}

	}

	//meta! sender="NurseLunchScheduler", id="116", type="Finish"
	public void processFinishNurseLunchScheduler(MessageForm message)
	{
		m_lunchTime = true;
		int i = 0;
		LinkedList<Nurse> availableNurses = myAgent().getAvailableNurses();
		for (Nurse nurse: availableNurses) {
			if(i < ((myAgent().getAvailableNurses().size()) / 2.0))
			{
				MyMessage newMessage = new MyMessage(mySim());
				sendNurseToLunch(newMessage, nurse);
			}
			i++;
		}
	}

	//meta! sender="VaccinationCenterAgent", id="123", type="Notice"
	public void processStartNotice(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.nurseLunchScheduler));
		startContinualAssistant(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.lunchRR:
			processLunchRR(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.vaccinationProcess:
				processFinishVaccinationProcess(message);
			break;

			case Id.nurseLunchScheduler:
				processFinishNurseLunchScheduler(message);
			break;
			}
		break;

		case Mc.refillRR:
			processRefillRR(message);
		break;

		case Mc.startNotice:
			processStartNotice(message);
		break;

		case Mc.vaccinationRR:
			processVaccinationRR(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public VaccinationAgent myAgent()
	{
		return (VaccinationAgent)super.myAgent();
	}

	private void startWork(MessageForm message, Nurse nurse)
	{
		nurse.setOccupied(mySim().currentTime());
		((MyMessage)message).setNurse(nurse);
		message.setAddressee(myAgent().findAssistant(Id.vaccinationProcess));
		startContinualAssistant(message);
	}

	private Nurse getAvailableNurse() {
		LinkedList<Nurse> availableNurses = myAgent().getAvailableNurses();
		if(availableNurses.isEmpty()) {
			return null;
		}
		if(availableNurses.size() == 1) {
			return availableNurses.remove(0);
		}

		double randValue = m_nursesGens.get(availableNurses.size() - 2 ).sample();
		for(int i = 0; i < availableNurses.size(); i++) {
			if(randValue < ((i + 1) / (double)availableNurses.size())) {
				return availableNurses.remove(i);
			}
		}
		return null;
	}

	private void finishWork(Nurse nurse) {
		if(nurse.getInjections() > 0)
		{
			nurse.setAvailable(mySim().currentTime());
			return;
		}
		MyMessage message = new MyMessage(mySim());
		message.setRefillNurse(nurse);
		message.setCode(Mc.refillRR);
		message.setAddressee(mySim().findAgent(Id.vaccRefillTransitionAgent));

		request(message);
	}

	private void sendNurseToLunch(MyMessage message, Nurse nurse)
	{
		message.setLunchEmployee(nurse);
		message.getLunchEmployee().goToLunch(mySim().currentTime());
		message.setCode(Mc.lunchRR);
		message.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));
		m_currentLunchingEmp++;
		request(message);
	}
}