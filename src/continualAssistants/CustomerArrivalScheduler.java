package continualAssistants;

import OSPABA.*;
import OSPRNG.*;
import simulation.*;
import agents.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

//meta! id="27"
public class CustomerArrivalScheduler extends Scheduler
{
	private static UniformDiscreteRNG m_missedCustomersGenerator = new UniformDiscreteRNG(5, 25);
	private static UniformContinuousRNG m_missedDecision = new UniformContinuousRNG(0.0, 1.0);

	private static UniformContinuousRNG m_earlyArrivalsGen = new UniformContinuousRNG(0.0, 1.0);
	private static EmpiricRNG m_earlyArrivalMinutesGen = new EmpiricRNG(
			new EmpiricPair(new UniformContinuousRNG(1.0, 20.0 ), 0.3),
			new EmpiricPair(new UniformContinuousRNG(20.0, 60.0 ), 0.4),
			new EmpiricPair(new UniformContinuousRNG(60.0, 80.0 ), 0.2),
			new EmpiricPair(new UniformContinuousRNG(80.0, 240.0 ), 0.1)
	);

	private double m_numOfMissedCustomersGenerated;
	private int m_numOfMissedCustomers;
	private final int m_defaultNumOfCustomers = 540;
	private int m_numOfCustomers;
	private final double m_defaultIntensity = 60.0;
	private double m_arrivalIntensity;
	private ArrayList<Double> m_timesOfArrivals;

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
		m_timesOfArrivals = new ArrayList<>();
	}

	//meta! sender="EnviroAgent", id="28", type="Start"
	public void processStart(MessageForm message)
	{
		// plan all customers arrivals
		double time = 0.0;
		for (int i = 0; i < m_numOfCustomers - m_numOfMissedCustomersGenerated; i++)
		{
			time += m_arrivalIntensity;
			while(!customerArrived()) {
				time += m_arrivalIntensity;
			}

			if(((MyMessage)message).getExperiment3())
			{
				double expTime = time;
				if(m_earlyArrivalsGen.sample() < 0.9)
				{
					expTime -= (double)m_earlyArrivalMinutesGen.sample() * 60;
					if (expTime < 0.0)
					{
						expTime = 0.0;
					}
				}
				m_timesOfArrivals.add(expTime);
			} else
			{
				m_timesOfArrivals.add(time);
			}
		}

		m_timesOfArrivals.sort((time1, time2) -> {
			return time1 - time2 > 0 ? 1 : time1 - time2 < 0 ? -1 : 0;
		});
		message.setCode(Mc.customerArrivalNotice);
		hold(m_timesOfArrivals.remove(0), message);

	}

	public void processCustomerArrival(MessageForm message)
	{

		MessageForm copy = message.createCopy();
		if(m_timesOfArrivals.size() > 0)
		{
			double time = m_timesOfArrivals.remove(0) - _mySim.currentTime();
			if(mySim().currentTime() + time < ((MySimulation)mySim()).getEndSimTime())
			{
				hold(time, copy);
				assistantFinished(message);
			}
		}
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