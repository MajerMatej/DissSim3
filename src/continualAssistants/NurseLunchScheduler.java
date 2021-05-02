package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="115"
public class NurseLunchScheduler extends Scheduler
{
	public NurseLunchScheduler(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="VaccinationAgent", id="116", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.finish);
		hold(5 * 60 * 60.0 + 30 * 60, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.finish:
				assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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

}