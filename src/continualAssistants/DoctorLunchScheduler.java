package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="113"
public class DoctorLunchScheduler extends Scheduler
{
	public DoctorLunchScheduler(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="ExaminationAgent", id="114", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.finish);
		hold(3 * 60 * 60.0 + 45 * 60, message);
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
	public ExaminationAgent myAgent()
	{
		return (ExaminationAgent)super.myAgent();
	}

}