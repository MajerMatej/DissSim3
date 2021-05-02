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
		message.setAddressee(mySim().findAgent(Id.exaTransitionAgent));

		request(message);
	}

	//meta! sender="VaccinationAgent", id="21", type="Response"
	public void processVaccinationRR(MessageForm message)
	{
		message.setCode(Mc.waitingRR);
		message.setAddressee(mySim().findAgent(Id.waitTransitionAgent));

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
		message.setAddressee(mySim().findAgent(Id.lunchTransitionAgent));
		request(message);
	}

	//meta! sender="VaccinationAgent", id="65", type="Request"
	public void processLunchRRVaccinationAgent(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.lunchTransitionAgent));
		request(message);
	}

	//meta! sender="ExaTransitionAgent", id="64", type="Request"
	public void processLunchRRExaTransitionAgent(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.lunchTransitionAgent));
		request(message);
	}

	//meta! sender="LunchTransitionAgent", id="84", type="Response"
	public void processRequestResponse(MessageForm message)
	{
	}

	//meta! sender="ModelAgent", id="119", type="Notice"
	public void processStartNotice(MessageForm message)
	{
			MessageForm copyReg = message.createCopy();
		copyReg.setAddressee(mySim().findAgent(Id.registrationAgent));
		notice(copyReg);

		MessageForm copyExa = message.createCopy();
		copyExa.setAddressee(mySim().findAgent(Id.exaTransitionAgent));
		notice(copyExa);

		message.setAddressee(mySim().findAgent(Id.vaccinationAgent));
		notice(message);
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
			case Id.vaccinationAgent:
				processLunchRRVaccinationAgent(message);
			break;

			case Id.exaTransitionAgent:
				processLunchRRExaTransitionAgent(message);
			break;

			case Id.registrationAgent:
				processLunchRRRegistrationAgent(message);
			break;
			}
		break;

		case Mc.requestResponse:
			processRequestResponse(message);
		break;

		case Mc.waitingRR:
			processWaitingRR(message);
		break;

		case Mc.registrationRR:
			processRegistrationRR(message);
		break;

		case Mc.examinationRR:
			processExaminationRR(message);
		break;

		case Mc.customerArrivalNotice:
			processCustomerArrivalNotice(message);
		break;

		case Mc.startNotice:
			processStartNotice(message);
		break;

		case Mc.vaccinationRR:
			processVaccinationRR(message);
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