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
	public void processLunchRRLunchAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.lunchTransitionProcess));
		((MyMessage)message).getLunchEmployee().goBackFromLunch();
		startContinualAssistant(message);
	}

	//meta! sender="VaccinationCenterAgent", id="84", type="Request"
	public void processLunchRRVaccinationCenterAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.lunchTransitionProcess));
		//((MyMessage)message).getLunchEmployee().goToLunch(mySim().currentTime());
		startContinualAssistant(message);
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
		if(((MyMessage)message).getLunchEmployee().hadLunchBreak())
		{
			message.setCode(Mc.lunchRR);
			response(message);
			return;
		}
		message.setAddressee(mySim().findAgent(Id.lunchAgent));
		message.setCode(Mc.lunchRR);
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
		case Mc.lunchRR:
			switch (message.sender().id())
			{
			case Id.vaccinationCenterAgent:
				processLunchRRVaccinationCenterAgent(message);
			break;

			case Id.lunchAgent:
				processLunchRRLunchAgent(message);
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
	public LunchTransitionAgent myAgent()
	{
		return (LunchTransitionAgent)super.myAgent();
	}

}