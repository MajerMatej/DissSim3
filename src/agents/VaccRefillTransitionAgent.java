package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="93"
public class VaccRefillTransitionAgent extends Agent
{
	public VaccRefillTransitionAgent(int id, Simulation mySim, Agent parent)
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
		new VaccRefillTransitionManager(Id.vaccRefillTransitionManager, mySim(), this);
		new VaccRefillTransitionProcess(Id.vaccRefillTransitionProcess, mySim(), this);
		new RefillVaccTransitionProcess(Id.refillVaccTransitionProcess, mySim(), this);
		addOwnMessage(Mc.refillRR);
	}
	//meta! tag="end"
}