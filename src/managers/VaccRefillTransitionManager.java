package managers;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="93"
public class VaccRefillTransitionManager extends Manager
{
	public VaccRefillTransitionManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="VaccinationFillAgent", id="55", type="Response"
	public void processRefillRRVaccinationFillAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.refillTransitionProcess));
		startContinualAssistant(message);
	}

	//meta! sender="VaccinationAgent", id="95", type="Request"
	public void processRefillRRVaccinationAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.refillTransitionProcess));
		startContinualAssistant(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="RefillTransitionProcess", id="98", type="Finish"
	public void processFinish(MessageForm message)
	{

		message.setAddressee(mySim().findAgent(Id.vaccinationFillAgent));
		message.setAddressee(Mc.refillRR);

		switch (message.sender().id())
		{
			case Id.vaccinationFillAgent:
				response(message);
				break;

			case Id.vaccinationAgent:
				request(message);
				break;
		}

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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.refillRR:
			switch (message.sender().id())
			{
			case Id.vaccinationFillAgent:
				processRefillRRVaccinationFillAgent(message);
			break;

			case Id.vaccinationAgent:
				processRefillRRVaccinationAgent(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public VaccRefillTransitionAgent myAgent()
	{
		return (VaccRefillTransitionAgent)super.myAgent();
	}

}