package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="39"
public class WaitingProcess extends Process
{
	private static UniformContinuousRNG m_gen = new UniformContinuousRNG(0.0, 1.0);

	public WaitingProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="WaitingRoomAgent", id="40", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.finish);
		double waitingTime = ((m_gen.sample() < 0.95) ? 15 * 60 : 30 * 60);
		hold(waitingTime, message);
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
	public WaitingRoomAgent myAgent()
	{
		return (WaitingRoomAgent)super.myAgent();
	}

}
