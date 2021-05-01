package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="73"
public class ExaVaccTransitionProcess extends Process
{

	public ExaVaccTransitionProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="ExaTransitionAgent", id="74", type="Start"
	public void processStart(MessageForm message)
	{
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
	public ExaTransitionAgent myAgent()
	{
		return (ExaTransitionAgent)super.myAgent();
	}

}