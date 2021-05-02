package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="111"
public class WorkerLunchScheduler extends Scheduler
{
	public WorkerLunchScheduler(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="RegistrationAgent", id="112", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.finish);
		hold(3 * 60 * 60.0, message);
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
	public RegistrationAgent myAgent()
	{
		return (RegistrationAgent)super.myAgent();
	}

}
