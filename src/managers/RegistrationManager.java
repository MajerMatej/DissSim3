package managers;

import Employee.AdminWorker;
import Employee.Employee;
import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;

import java.util.LinkedList;

//meta! id="3"
public class RegistrationManager extends Manager
{
	private LinkedList<UniformContinuousRNG> m_adminWorkersGen;
	private boolean m_lunchTime;
	private int m_maxLunchingEmp;
	private int m_currentLunchingEmp;

	public RegistrationManager(int id, Simulation mySim, Agent myAgent)
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
		m_adminWorkersGen = new LinkedList<>();
		for(int i = 0; i < ((MySimulation)mySim()).getNumOfAdminWorkers() - 1; i++) {
			m_adminWorkersGen.add(new UniformContinuousRNG(0.0, 1.0));
		}
		m_lunchTime = false;
		m_maxLunchingEmp = ((MySimulation)mySim()).getNumOfAdminWorkers() / 2;
		if(m_maxLunchingEmp == 0) m_maxLunchingEmp++;
		m_currentLunchingEmp = 0;
	}

	//meta! sender="VaccinationCenterAgent", id="19", type="Request"
	public void processRegistrationRR(MessageForm message)
	{
		AdminWorker worker = getAvailableWorker();
		if(worker == null || myAgent().getCustomersQueue().size() > 0) {
			((MyMessage)message).setStartWaitingReg(mySim().currentTime());
			myAgent().getCustomersQueue().enqueue((MyMessage) message);
			return;
		}
		startWork(message, worker);
	}

	//meta! sender="RegistrationProcess", id="31", type="Finish"
	public void processFinishRegistrationProcess(MessageForm message)
	{
		((MyMessage)message).getWorker().setAvailable(mySim().currentTime());
		myAgent().getWaitingTimeStat().addSample(((MyMessage)message).getTotalWaitingReg());
		if(m_currentLunchingEmp + 1 <= m_maxLunchingEmp && m_lunchTime
			&& !((MyMessage)message).getWorker().hadLunchBreak())
		{
			MessageForm copy = message.createCopy();

			sendWorkerToLunch((MyMessage)copy, ((MyMessage) message).getWorker());
		}
		AdminWorker worker = getAvailableWorker();
		if(myAgent().getCustomersQueue().size() > 0 && worker != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingReg(mySim().currentTime() - nextMessage.getStartWaitingReg());

			startWork(nextMessage, worker);
		}

		message.setCode(Mc.registrationRR);

		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="VaccinationCenterAgent", id="61", type="Response"
	public void processLunchRR(MessageForm message)
	{
		((MyMessage)message).getLunchEmployee().backFromLunch(mySim().currentTime());
		m_currentLunchingEmp--;

		for (AdminWorker emp : myAgent().getAvailableAdminWorkers()) {
			if (!emp.hadLunchBreak())
			{
				sendWorkerToLunch((MyMessage) message, emp);
				return;
			}
		}
		AdminWorker worker = getAvailableWorker();
		if(myAgent().getCustomersQueue().size() > 0 && worker != null)
		{
			MyMessage nextMessage = (MyMessage)myAgent().getCustomersQueue().dequeue();
			nextMessage.setTotalWaitingReg(mySim().currentTime() - nextMessage.getStartWaitingReg());

			startWork(nextMessage, worker);
		}
	}

	//meta! sender="WorkerLunchScheduler", id="112", type="Finish"
	public void processFinishWorkerLunchScheduler(MessageForm message)
	{
		m_lunchTime = true;
		int i = 0;
		LinkedList<AdminWorker> availableWorkers = myAgent().getAvailableAdminWorkers();
		for (AdminWorker worker: availableWorkers) {
			if(i < ((myAgent().getAvailableAdminWorkers().size()) / 2.0))
			{
				MyMessage newMessage = new MyMessage(mySim());
				sendWorkerToLunch(newMessage, worker);
			}
			i++;
		}
	}

	//meta! sender="VaccinationCenterAgent", id="120", type="Notice"
	public void processStartNotice(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.workerLunchScheduler));
		startContinualAssistant(message);
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
		case Mc.lunchRR:
			processLunchRR(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.registrationProcess:
				processFinishRegistrationProcess(message);
			break;

			case Id.workerLunchScheduler:
				processFinishWorkerLunchScheduler(message);
			break;
			}
		break;

		case Mc.registrationRR:
			processRegistrationRR(message);
		break;

		case Mc.startNotice:
			processStartNotice(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public RegistrationAgent myAgent()
	{
		return (RegistrationAgent)super.myAgent();
	}

	private void startWork(MessageForm message, AdminWorker worker)
	{
		worker.setOccupied(mySim().currentTime());
		((MyMessage)message).setWorker(worker);
		message.setAddressee(myAgent().findAssistant(Id.registrationProcess));
		startContinualAssistant(message);
	}

	private AdminWorker getAvailableWorker() {
		LinkedList<AdminWorker> availableWorkers = myAgent().getAvailableAdminWorkers();
		if(availableWorkers.isEmpty()) {
			return null;
		}
		if(availableWorkers.size() == 1) {
			return availableWorkers.remove(0);
		}

		double randValue = m_adminWorkersGen.get(availableWorkers.size() - 2 ).sample();
		for(int i = 0; i < availableWorkers.size(); i++) {
			if(randValue < ((i + 1) / (double)availableWorkers.size())) {
				return availableWorkers.remove(i);
			}
		}
		return null;
	}

	private void sendWorkerToLunch(MyMessage message, AdminWorker worker)
	{
		message.setLunchEmployee(worker);
		message.getLunchEmployee().goToLunch(mySim().currentTime());
		message.setCode(Mc.lunchRR);
		message.setAddressee(mySim().findAgent(Id.vaccinationCenterAgent));
		m_currentLunchingEmp++;
		request(message);
	}

}