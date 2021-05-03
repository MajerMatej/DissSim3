package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="44"
public class ExaTransitionManager extends Manager
{
	public ExaTransitionManager(int id, Simulation mySim, Agent myAgent)
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
	public void processExaminationRRExaminationAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.exaVaccTransitionProcess));
		startContinualAssistant(message);
	}

	//meta! sender="VaccinationCenterAgent", id="47", type="Request"
	public void processExaminationRRVaccinationCenterAgent(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.regExaTransitionProcess));
		startContinualAssistant(message);
	}

	//meta! sender="ExaminationAgent", id="63", type="Request"
	public void processLunchRRExaminationAgent(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));
		request(message);
	}

	//meta! sender="VaccinationCenterAgent", id="64", type="Response"
	public void processLunchRRVaccinationCenterAgent(MessageForm message)
	{
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="RegExaTransitionProcess", id="72", type="Finish"
	public void processFinishRegExaTransitionProcess(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.examinationAgent));
		message.setCode(Mc.examinationRR);

		request(message);
	}

	//meta! sender="ExaVaccTransitionProcess", id="74", type="Finish"
	public void processFinishExaVaccTransitionProcess(MessageForm message)
	{
		//message.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));
		message.setCode(Mc.examinationRR);

		response(message);
	}

	//meta! sender="VaccinationCenterAgent", id="121", type="Notice"
	public void processStartNotice(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.examinationAgent));
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
		case Mc.examinationRR:
			switch (message.sender().id())
			{
			case Id.vaccinationCenterAgent:
				processExaminationRRVaccinationCenterAgent(message);
			break;

			case Id.examinationAgent:
				processExaminationRRExaminationAgent(message);
			break;
			}
		break;

		case Mc.lunchRR:
			switch (message.sender().id())
			{
			case Id.vaccinationCenterAgent:
				processLunchRRVaccinationCenterAgent(message);
			break;

			case Id.examinationAgent:
				processLunchRRExaminationAgent(message);
			break;
			}
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.regExaTransitionProcess:
				processFinishRegExaTransitionProcess(message);
			break;

			case Id.exaVaccTransitionProcess:
				processFinishExaVaccTransitionProcess(message);
			break;
			}
		break;

		case Mc.startNotice:
			processStartNotice(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ExaTransitionAgent myAgent()
	{
		return (ExaTransitionAgent)super.myAgent();
	}

}