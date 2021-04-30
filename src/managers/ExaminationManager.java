package managers;

import Employee.AdminWorker;
import Employee.Doctor;
import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.LinkedList;

//meta! id="4"
public class ExaminationManager extends Manager
{
	private LinkedList<UniformContinuousRNG> m_doctorsGens;

	public ExaminationManager(int id, Simulation mySim, Agent myAgent)
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

		m_doctorsGens = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfDoctors() - 1; i++) {
			m_doctorsGens.add(new UniformContinuousRNG(0.0, 1.0));
		}
	}

	//meta! sender="ExaTransitionAgent", id="20", type="Request"
	public void processExaminationRR(MessageForm message)
	{
		Doctor doctor = getAvailableDoctor();
		if(doctor == null || myAgent().getCustomersQueue().size() > 0) {
			((MyMessage)message).setStartWaitingExa(mySim().currentTime());
			myAgent().getCustomersQueue().enqueue((MyMessage) message);
			return;
		}
		startWork(message, doctor);
	}

	//meta! sender="ExaminationProces", id="34", type="Finish"
	public void processFinish(MessageForm message)
	{
		((MyMessage)message).getDoctor().setAvailable(mySim().currentTime());
		myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingExa());

		Doctor doctor = getAvailableDoctor();
		if(myAgent().getCustomersQueue().size() > 0 && doctor != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingExa(mySim().currentTime() - nextMessage.getStartWaitingExa());

			startWork(nextMessage, doctor);
		}

		message.setCode(Mc.examinationRR);

		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="ExaTransitionAgent", id="63", type="Response"
	public void processLunchRR(MessageForm message)
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.lunchRR:
			processLunchRR(message);
		break;

		case Mc.examinationRR:
			processExaminationRR(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ExaminationAgent myAgent()
	{
		return (ExaminationAgent)super.myAgent();
	}

	private void startWork(MessageForm message, Doctor doctor)
	{
		doctor.setOccupied(mySim().currentTime());
		((MyMessage)message).setDoctor(doctor);
		message.setAddressee(myAgent().findAssistant(Id.examinationProces));
		startContinualAssistant(message);
	}

	private Doctor getAvailableDoctor() {
		LinkedList<Doctor> availableDoctors = myAgent().getAvailableDoctors();
		if(availableDoctors.isEmpty()) {
			return null;
		}
		if(availableDoctors.size() == 1) {
			return availableDoctors.remove(0);
		}

		double randValue = m_doctorsGens.get(availableDoctors.size() - 2 ).sample();
		for(int i = 0; i < availableDoctors.size(); i++) {
			if(randValue < ((i + 1) / (double)availableDoctors.size())) {
				return availableDoctors.remove(i);
			}
		}
		return null;
	}

}