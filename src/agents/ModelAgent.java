package agents;

import OSPABA.*;
import simulation.*;
import managers.*;

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
	//meta! tag="end"
	public void runSim()
	{
		MyMessage message = new MyMessage(mySim());
		message.setExperiment3(((MySimulation)mySim()).isExperiment3());
		message.setCode(Mc.startGeneratingNotice);
		message.setAddressee(this);
		manager().notice(message);

		MyMessage newMessage = new MyMessage(mySim());
		newMessage.setCode(Mc.startNotice);
		newMessage.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));
		manager().notice(newMessage);

	}
}