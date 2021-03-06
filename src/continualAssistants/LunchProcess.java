package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="80"
public class LunchProcess extends Process
{
	private static TriangularRNG m_gen = new TriangularRNG(5.0 * 60,15.0 * 60,30.0 * 60);	public LunchProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="LunchAgent", id="81", type="Start"
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
	public LunchAgent myAgent()
	{
		return (LunchAgent)super.myAgent();
	}

}