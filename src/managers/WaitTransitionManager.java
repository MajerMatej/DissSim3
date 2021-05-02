package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="48"
public class WaitTransitionManager extends Manager
{
	public WaitTransitionManager(int id, Simulation mySim, Agent myAgent)
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
	}

	//meta! sender="WaitingRoomAgent", id="22", type="Response"
	public void processWaitingRRWaitingRoomAgent(MessageForm message)
	{

		response(message);
	}

	//meta! sender="VaccinationCenterAgent", id="50", type="Request"
	public void processWaitingRRVaccinationCenterAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.vaccWaitTransitionProcess));
		startContinualAssistant(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="VaccWaitTransitionProcess", id="76", type="Finish"
	public void processFinish(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.waitingRoomAgent));
		message.setCode(Mc.waitingRR);

		request(message);
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
		case Mc.waitingRR:
			switch (message.sender().id())
			{
			case Id.vaccinationCenterAgent:
				processWaitingRRVaccinationCenterAgent(message);
			break;

			case Id.waitingRoomAgent:
				processWaitingRRWaitingRoomAgent(message);
			break;
			}
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
	public WaitTransitionAgent myAgent()
	{
		return (WaitTransitionAgent)super.myAgent();
	}

}