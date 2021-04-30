package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class ModelManager extends Manager
{
	public ModelManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="VaccinationCenterAgent", id="23", type="Notice"
	public void processCustomerLeftNotice(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.enviroAgent));
		notice(message);
	}

	//meta! sender="EnviroAgent", id="17", type="Notice"
	public void processCustomerArrivalNotice(MessageForm message)
	{
		message.createCopy();
		message.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));

		notice(message);
	}

	public void processStartGeneratingNotice(MessageForm message) {
		message.setAddressee(mySim().findAgent(Id.enviroAgent));
		notice(message);
	}


		//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.startGeneratingNotice:
				processStartGeneratingNotice(message);
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
		case Mc.customerArrivalNotice:
			processCustomerArrivalNotice(message);
		break;

		case Mc.customerLeftNotice:
			processCustomerLeftNotice(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ModelAgent myAgent()
	{
		return (ModelAgent)super.myAgent();
	}

}
