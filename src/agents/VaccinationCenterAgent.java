package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class VaccinationCenterAgent extends Agent
{
	public VaccinationCenterAgent(int id, Simulation mySim, Agent parent)
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
		new VaccinationCenterManager(Id.vaccinationCenterManager, mySim(), this);
		addOwnMessage(Mc.examinationRR);
		addOwnMessage(Mc.waitingRR);
		addOwnMessage(Mc.lunchRR);
		addOwnMessage(Mc.registrationRR);
		addOwnMessage(Mc.requestResponse);
		addOwnMessage(Mc.vaccinationRR);
		addOwnMessage(Mc.customerArrivalNotice);
	}
	//meta! tag="end"
}