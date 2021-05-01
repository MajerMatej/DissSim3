package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="52"
public class VaccinationFillManager extends Manager
{
	public VaccinationFillManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="VaccRefillTransitionAgent", id="55", type="Request"
	public void processRefillRR(MessageForm message)
	{
		((MyMessage)message).getRefillNurse().refillInjections();
		message.setAddressee(myAgent().findAssistant(Id.vaccinationFillProcess));
		startContinualAssistant(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="VaccinationFillProcess", id="78", type="Finish"
	public void processFinish(MessageForm message)
	{
		message.setCode(Mc.refillRR);
		response(message);
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
			processRefillRR(message);
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