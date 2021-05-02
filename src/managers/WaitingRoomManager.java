package managers;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="7"
public class WaitingRoomManager extends Manager
{
	public WaitingRoomManager(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="WaitTransitionAgent", id="22", type="Request"
	public void processWaitingRR(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.waitingProcess));
		((MyMessage)message).setStartWaitingRoom(mySim().currentTime());
		myAgent().setCustomersWaiting(myAgent().getCustomersWaiting() + 1);
		startContinualAssistant(message);
	}

	//meta! sender="WaitingProcess", id="40", type="Finish"
	public void processFinish(MessageForm message)
	{
		((MyMessage)message).setTotalWaitingRoom(mySim().currentTime() - ((MyMessage)message).getStartWaitingRoom());
		myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingRoom());
		myAgent().setCustomersWaiting(myAgent().getCustomersWaiting() - 1);
		message.setCode(Mc.waitingRR);
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="WaitTransitionAgent", id="68", type="Response"
	public void processLunchRR(MessageForm message)
	{
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.waitingRR:
			processWaitingRR(message);
		break;

		case Mc.lunchRR:
			processLunchRR(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public WaitingRoomAgent myAgent()
	{
		return (WaitingRoomAgent)super.myAgent();
	}

}