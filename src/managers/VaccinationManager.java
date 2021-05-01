package managers;

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
	}

	//meta! sender="VaccinationProcess", id="37", type="Finish"
	public void processFinish(MessageForm message)
	{
		finishWork(((MyMessage)message).getNurse());
		//((MyMessage)message).getNurse().setAvailable(mySim().currentTime());
		myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingVacc());

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
	}

	//meta! userInfo="Removed from model"
	public void processVaccinationRRVaccinationFillAgent(MessageForm message)
	{
	}

	//meta! sender="VaccRefillTransitionAgent", id="95", type="Response"
	public void processRefillRR(MessageForm message)
	{
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
		case Mc.vaccinationRR:
			processVaccinationRR(message);
		break;

		case Mc.lunchRR:
			processLunchRR(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.refillRR:
			processRefillRR(message);
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
		message.setCode(Mc.refillRR);
		message.setAddressee(mySim().findAgent(Id.vaccRefillTransitionAgent));

		request(message);
	}
}