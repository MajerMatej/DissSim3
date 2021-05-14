package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="87"
public class LunchTransitionProcess extends Process
{
	private static UniformContinuousRNG m_gen = new UniformContinuousRNG(70.0, 200.0);
	public LunchTransitionProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="LunchTransitionAgent", id="88", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.finish);
		hold(m_gen.sample(),message);
//		hold(0d, message);
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
	public LunchTransitionAgent myAgent()
	{
		return (LunchTransitionAgent)super.myAgent();
	}

}