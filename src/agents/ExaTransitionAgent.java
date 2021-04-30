package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="44"
public class ExaTransitionAgent extends Agent
{
	public ExaTransitionAgent(int id, Simulation mySim, Agent parent)
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
		new ExaTransitionManager(Id.exaTransitionManager, mySim(), this);
		new RegExaTransitionProcess(Id.regExaTransitionProcess, mySim(), this);
		new ExaVaccTransitionProcess(Id.exaVaccTransitionProcess, mySim(), this);
		addOwnMessage(Mc.examinationRR);
		addOwnMessage(Mc.lunchRR);
	}
	//meta! tag="end"
}