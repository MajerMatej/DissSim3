package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class VaccinationCenterManager extends Manager
{
	public VaccinationCenterManager(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="ExaminationAgent", id="20", type="Response"
	public void processExaminationRR(MessageForm message)
	{
		message.setCode(Mc.vaccinationRR);
		message.setAddressee(mySim().findAgent(Id.vaccinationAgent));

		request(message);
	}

	//meta! sender="WaitingRoomAgent", id="22", type="Response"
	public void processWaitingRR(MessageForm message)
	{
		message.setCode(Mc.customerLeftNotice);
		message.setAddressee(mySim().findAgent(Id.modelAgent));

		notice(message);
	}

	//meta! sender="RegistrationAgent", id="19", type="Response"
	public void processRegistrationRR(MessageForm message)
	{
		message.setCode(Mc.examinationRR);
		message.setAddressee(mySim().findAgent(Id.examinationAgent));

		request(message);
	}

	//meta! sender="VaccinationAgent", id="21", type="Response"
	public void processVaccinationRR(MessageForm message)
	{
		message.setCode(Mc.waitingRR);
		message.setAddressee(mySim().findAgent(Id.waitingRoomAgent));

		request(message);

	}

	//meta! sender="ModelAgent", id="18", type="Notice"
	public void processCustomerArrivalNotice(MessageForm message)
	{
		message.setCode(Mc.registrationRR);
		message.setAddressee(mySim().findAgent(Id.registrationAgent));

		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
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
		case Mc.registrationRR:
			processRegistrationRR(message);
		break;

		case Mc.examinationRR:
			processExaminationRR(message);
		break;

		case Mc.vaccinationRR:
			processVaccinationRR(message);
		break;

		case Mc.waitingRR:
			processWaitingRR(message);
		break;

		case Mc.customerArrivalNotice:
			processCustomerArrivalNotice(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public VaccinationCenterAgent myAgent()
	{
		return (VaccinationCenterAgent)super.myAgent();
	}

}
