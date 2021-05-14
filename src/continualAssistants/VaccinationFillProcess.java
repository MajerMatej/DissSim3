package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="77"
public class VaccinationFillProcess extends Process
{
	private static TriangularRNG m_gen = new TriangularRNG(6.0, 10.0, 40.0);
	public VaccinationFillProcess(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="VaccinationFillAgent", id="78", type="Start"
	public void processStart(MessageForm message)
	{
		double timeToFill = 0.0;
		for(int i = 0; i < 20; i++) {
			timeToFill += m_gen.sample();
		}
		message.setCode(Mc.finish);
		hold(timeToFill, message);
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
	public VaccinationFillAgent myAgent()
	{
		return (VaccinationFillAgent)super.myAgent();
	}

}