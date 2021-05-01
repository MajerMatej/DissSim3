package managers;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="2"
public class EnviroManager extends Manager
{
	private int m_customersArrived;
	private int m_customersLeft;

	public EnviroManager(int id, Simulation mySim, Agent myAgent)
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
		m_customersArrived = 0;
		m_customersLeft = 0;
	}

	//meta! sender="ModelAgent", id="16", type="Notice"
	public void processStartGeneratingNotice(MessageForm message)
	{
		MyMessage m = new MyMessage(mySim());
		m.setAddressee(myAgent().findAssistant(Id.customerArrivalScheduler));
		startContinualAssistant(m);
	}

	//meta! sender="ModelAgent", id="25", type="Notice"
	public void processCustomerLeftNotice(MessageForm message)
	{
		m_customersLeft++;

		if(m_customersArrived == m_customersLeft && mySim().currentTime() > ((MySimulation)mySim()).getEndSimTime()) {
			((MySimulation) mySim()).setFinishTime(mySim().currentTime());
			mySim().stopReplication();
		}
	}

	//meta! sender="CustomerArrivalScheduler", id="28", type="Finish"
	public void processFinish(MessageForm message)
	{
		m_customersArrived++;
		message.setCode(Mc.customerArrivalNotice);
		message.setAddressee(mySim().findAgent(Id.modelAgent));

		notice(message);
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
		case Mc.customerLeftNotice:
			processCustomerLeftNotice(message);
		break;

		case Mc.startGeneratingNotice:
			processStartGeneratingNotice(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public EnviroAgent myAgent()
	{
		return (EnviroAgent)super.myAgent();
	}

}