package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;

//meta! id="27"
public class CustomerArrivalScheduler extends Scheduler
{
	private static UniformDiscreteRNG m_missedCustomersGenerator = new UniformDiscreteRNG(5, 25);
	private static UniformContinuousRNG m_missedDecision = new UniformContinuousRNG(0.0, 1.0);

	private double m_numOfMissedCustomersGenerated;
	private int m_numOfMissedCustomers;
	private final int m_defaultNumOfCustomers = 540;
	private int m_numOfCustomers;
	private final double m_defaultIntensity = 60.0;
	private double m_arrivalIntensity;

	public CustomerArrivalScheduler(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		m_numOfCustomers = ((MySimulation)mySim()).getNumOfCustomers();
		m_arrivalIntensity = (((MySimulation)mySim()).getEndSimTime() / m_defaultNumOfCustomers) * (((double)m_defaultNumOfCustomers) / m_numOfCustomers);
		m_numOfMissedCustomersGenerated = (m_missedCustomersGenerator.sample() / ((double)m_defaultNumOfCustomers)) * m_numOfCustomers;

	}

	//meta! sender="EnviroAgent", id="28", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.customerArrivalNotice);
		hold(m_arrivalIntensity, message);

	}

	public void processCustomerArrival(MessageForm message)
	{

		MessageForm copy = message.createCopy();
		double time = m_arrivalIntensity;

		while(!customerArrived()) {
			time += m_arrivalIntensity;
		}

		if(mySim().currentTime() + time < ((MySimulation)mySim()).getEndSimTime())
		{
			hold(time, copy);
			assistantFinished(message);
		}

		//assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.customerArrivalNotice:
				processCustomerArrival(message);
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
	public EnviroAgent myAgent()
	{
		return (EnviroAgent)super.myAgent();
	}

	private boolean customerArrived() {
		if(m_missedDecision.sample() < m_numOfMissedCustomersGenerated / m_numOfCustomers) {
			m_numOfMissedCustomers++;
			return false;
		}
		return true;
	}


}