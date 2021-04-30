package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="42"
public class LunchAgent extends Agent
{
	public LunchAgent(int id, Simulation mySim, Agent parent)
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
		new LunchManager(Id.lunchManager, mySim(), this);
		new LunchProcess(Id.lunchProcess, mySim(), this);
		addOwnMessage(Mc.lunchRR);
	}
	//meta! tag="end"
}