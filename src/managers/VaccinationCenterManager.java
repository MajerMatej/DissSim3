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

	//meta! sender="ExaTransitionAgent", id="47", type="Response"
	public void processExaminationRR(MessageForm message)
	{
		message.setCode(Mc.vaccinationRR);
		message.setAddressee(mySim().findAgent(Id.vaccinationAgent));

		request(message);
	}

	//meta! sender="WaitTransitionAgent", id="50", type="Response"
	public void processWaitingRR(MessageForm message)
	{
		message.setCode(Mc.customerLeftNotice);
		message.setAddressee(mySim().findAgent(Id.modelAgent));

		notice(message);
	}

	//meta! sender="RegistrationAgent", id="61", type="Request"
	public void processLunchRRRegistrationAgent(MessageForm message)
	{
	}

	//meta! sender="WaitTransitionAgent", id="69", type="Request"
	public void processLunchRRWaitTransitionAgent(MessageForm message)
	{
	}

	//meta! sender="VaccinationAgent", id="65", type="Request"
	public void processLunchRRVaccinationAgent(MessageForm message)
	{
	}

	//meta! sender="ExaTransitionAgent", id="64", type="Request"
	public void processLunchRRExaTransitionAgent(MessageForm message)
	{
	}

	//meta! userInfo="Removed from model"
	public void processLunchRRLunchAgent(MessageForm message)
	{
	}

	//meta! sender="LunchTransitionAgent", id="84", type="Response"
	public void processRequestResponse(MessageForm message)
	{
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
		case Mc.lunchRR:
			switch (message.sender().id())
			{
			case Id.registrationAgent:
				processLunchRRRegistrationAgent(message);
			break;

			case Id.waitTransitionAgent:
				processLunchRRWaitTransitionAgent(message);
			break;

			case Id.vaccinationAgent:
				processLunchRRVaccinationAgent(message);
			break;

			case Id.exaTransitionAgent:
				processLunchRRExaTransitionAgent(message);
			break;
			}
		break;

		case Mc.customerArrivalNotice:
			processCustomerArrivalNotice(message);
		break;

		case Mc.requestResponse:
			processRequestResponse(message);
		break;

		case Mc.waitingRR:
			processWaitingRR(message);
		break;

		case Mc.vaccinationRR:
			processVaccinationRR(message);
		break;

		case Mc.examinationRR:
			processExaminationRR(message);
		break;

		case Mc.registrationRR:
			processRegistrationRR(message);
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