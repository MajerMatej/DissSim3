package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="2"
public class EnviroAgent extends Agent
{
	public EnviroAgent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new EnviroManager(Id.enviroManager, mySim(), this);
		new CustomerArrivalScheduler(Id.customerArrivalScheduler, mySim(), this);
		addOwnMessage(Mc.startGeneratingNotice);
		addOwnMessage(Mc.customerLeftNotice);
		addOwnMessage(Mc.customerArrivalNotice);
	}
	//meta! tag="end"
}
