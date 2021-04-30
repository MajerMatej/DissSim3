package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="82"
public class LunchTransitionAgent extends Agent
{
	public LunchTransitionAgent(int id, Simulation mySim, Agent parent)
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
		new LunchTransitionManager(Id.lunchTransitionManager, mySim(), this);
		new LunchTransitionProcess(Id.lunchTransitionProcess, mySim(), this);
		addOwnMessage(Mc.lunchRR);
		addOwnMessage(Mc.requestResponse);
	}
	//meta! tag="end"
}