package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class ModelAgent extends Agent
{
	public ModelAgent(int id, Simulation mySim, Agent parent)
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
		new ModelManager(Id.modelManager, mySim(), this);
		addOwnMessage(Mc.customerLeftNotice);
		addOwnMessage(Mc.customerArrivalNotice);
	}

	public void runSim()
	{
		MyMessage message = new MyMessage(mySim());
		message.setCode(Mc.startGeneratingNotice);
		message.setAddressee(this);
		manager().notice(message);
	}
	//meta! tag="end"
}
