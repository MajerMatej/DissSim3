package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="82"
public class LunchTransitionManager extends Manager
{
	public LunchTransitionManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="LunchAgent", id="62", type="Response"
	public void processLunchRR(MessageForm message)
	{
	}

	//meta! sender="VaccinationCenterAgent", id="84", type="Request"
	public void processRequestResponse(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="LunchTransitionProcess", id="88", type="Finish"
	public void processFinish(MessageForm message)
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
		case Mc.requestResponse:
			processRequestResponse(message);
		break;

		case Mc.lunchRR:
			processLunchRR(message);
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
	public LunchTransitionAgent myAgent()
	{
		return (LunchTransitionAgent)super.myAgent();
	}

}