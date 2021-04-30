package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="48"
public class WaitTransitionAgent extends Agent
{
	public WaitTransitionAgent(int id, Simulation mySim, Agent parent)
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
		new WaitTransitionManager(Id.waitTransitionManager, mySim(), this);
		new VaccWaitTransitionProcess(Id.vaccWaitTransitionProcess, mySim(), this);
		addOwnMessage(Mc.waitingRR);
		addOwnMessage(Mc.lunchRR);
	}
	//meta! tag="end"
}