package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="52"
public class VaccinationFillManager extends Manager
{
	private final int m_maxNursesFilling = 2;
	private int m_nursesFilling;
	public VaccinationFillManager(int id, Simulation mySim, Agent myAgent)
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
		m_nursesFilling = 0;
	}

	//meta! sender="VaccRefillTransitionAgent", id="55", type="Request"
	public void processRefillRR(MessageForm message)
	{
		if (m_nursesFilling < m_maxNursesFilling)
		{
			m_nursesFilling++;
			((MyMessage)message).getRefillNurse().refillInjections();
			message.setAddressee(myAgent().findAssistant(Id.vaccinationFillProcess));
			startContinualAssistant(message);
			return;
		}
		((MyMessage)message).getRefillNurse().waitForRefill();
		((MyMessage)message).setStartWaitingRefill(mySim().currentTime());
		myAgent().getNursesQueue().enqueue((MyMessage) message);

	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="VaccinationFillProcess", id="78", type="Finish"
	public void processFinish(MessageForm message)
	{
		m_nursesFilling--;
		//myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingRefill());
		if (myAgent().getNursesQueue().size() > 0)
		{
			m_nursesFilling++;
			MyMessage nextMessage = (MyMessage)myAgent().getNursesQueue().dequeue();
			nextMessage.getRefillNurse().refillInjections();
			nextMessage.setTotalWaitingRefill(mySim().currentTime() - (nextMessage).getStartWaitingRefill());
			myAgent().getWaitingTimeStat().addSample(((MyMessage)nextMessage).getTotalWaitingRefill());
			nextMessage.setAddressee(myAgent().findAssistant(Id.vaccinationFillProcess));
			startContinualAssistant(nextMessage);
		}

		message.setCode(Mc.refillRR);
		response(message);
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
		case Mc.refillRR:
			processRefillRR(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public VaccinationFillAgent myAgent()
	{
		return (VaccinationFillAgent)super.myAgent();
	}

}