package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="52"
public class VaccinationFillAgent extends Agent
{
	public VaccinationFillAgent(int id, Simulation mySim, Agent parent)
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
		new VaccinationFillManager(Id.vaccinationFillManager, mySim(), this);
		new VaccinationFillProcess(Id.vaccinationFillProcess, mySim(), this);
		addOwnMessage(Mc.vaccinationRR);
	}
	//meta! tag="end"
}