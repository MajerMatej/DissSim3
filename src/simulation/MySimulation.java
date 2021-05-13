package simulation;

import Employee.AdminWorker;
import OSPABA.*;
import OSPStat.Stat;
import agents.*;
import managers.VaccRefillTransitionManager;

public class MySimulation extends Simulation
{
	private ModelAgent m_modelAgent;
	private EnviroAgent m_enviroAgent;
	private VaccinationCenterAgent m_vaccCenterAgent;
	private RegistrationAgent m_regAgent;

	private int m_numOfAdminWorkers;
	private int m_numOfDoctors;
	private int m_numOfNurses;
	private int m_numOfCustomers;

	private double m_endSimTime;

	private Stat m_waitingTimeReg;
	private Stat m_avgCustomersReg;

	private Stat m_waitingTimeExa;
	private Stat m_avgCustomersExa;

	private Stat m_waitingTimeVacc;
	private Stat m_avgCustomersVacc;

	private Stat m_waitingTimeWR;
	private Stat m_avgCustomersWR;


	private Stat m_workerUtil;
	private Stat m_doctorUtil;
	private Stat m_nurseUtil;

	private Stat m_waitingTimeRefill;

	private boolean m_turbo = false;
	private double m_speedInterval;
	private double m_speedDuration;

	private double m_finishTime;

	private boolean m_experiment3;


	public MySimulation(int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double endSimTime,
						int numOfCustomers, boolean experiment3)
	{
		init();
		m_numOfAdminWorkers = numOfAdminWorkers;
		m_numOfDoctors = numOfDoctors;
		m_numOfNurses = numOfNurses;
		m_endSimTime = endSimTime;
		m_numOfCustomers = numOfCustomers;
		m_experiment3 = experiment3;
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
		m_waitingTimeReg = new Stat();
		m_avgCustomersReg = new Stat();

		m_waitingTimeExa = new Stat();
		m_avgCustomersExa = new Stat();

		m_waitingTimeVacc = new Stat();
		m_avgCustomersVacc = new Stat();

		m_waitingTimeWR = new Stat();
		m_avgCustomersWR = new Stat();

		m_workerUtil = new Stat();
		m_doctorUtil = new Stat();
		m_nurseUtil = new Stat();

		m_waitingTimeRefill = new Stat();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		if(m_turbo) {
			setMaxSimSpeed();
		} else{
			setSimSpeed(m_speedInterval, m_speedDuration);
		}
		// Reset entities, queues, local statistics, etc...
		modelAgent().runSim();
		m_finishTime = 0;
	}

	@Override
	public double currentTime() {
		return (m_finishTime == 0) ? super.currentTime() : m_finishTime;
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...

		super.replicationFinished();
		m_waitingTimeReg.addSample(registrationAgent().getWaitingTimeStat().mean());
		//System.out.println("R " + currentReplication() + ": Time in reg queue" + m_waitingTimeReg.mean() + "(" + registrationAgent().getWaitingTimeStat().mean()+")");

		m_avgCustomersReg.addSample(registrationAgent().getWaitingTimeStat().mean() * registrationAgent().getWaitingTimeStat().sampleSize() / currentTime());
		//System.out.println("Average number of customers in reg queue :" + m_avgCustomersReg.mean());

		m_waitingTimeExa.addSample(examinationAgent().getWaitingTimeStat().mean());
		//System.out.println("R " + currentReplication() + ": Time in exa queue" + m_waitingTimeExa.mean() + "(" + examinationAgent().getWaitingTimeStat().mean()+")");

		m_avgCustomersExa.addSample(examinationAgent().getWaitingTimeStat().mean() * examinationAgent().getWaitingTimeStat().sampleSize() / currentTime());
		//System.out.println("Average number of customers in exa queue :" + m_avgCustomersExa.mean());

		m_waitingTimeVacc.addSample(vaccinationAgent().getWaitingTimeStat().mean());
		//System.out.println("R " + currentReplication() + ": Time in vacc queue" + m_waitingTimeVacc.mean() + "(" + vaccinationAgent().getWaitingTimeStat().mean()+")");

		m_avgCustomersVacc.addSample(vaccinationAgent().getWaitingTimeStat().mean() * vaccinationAgent().getWaitingTimeStat().sampleSize() / currentTime());
		//System.out.println("Average number of customers in vacc queue :" + m_avgCustomersVacc.mean());

		m_waitingTimeWR.addSample(waitingRoomAgent().getWaitingTimeStat().mean());
		m_avgCustomersWR.addSample(waitingRoomAgent().getWaitingTimeStat().mean() * waitingRoomAgent().getWaitingTimeStat().sampleSize() / currentTime());

		m_workerUtil.addSample(registrationAgent().getAdminWorkersUtilization());
		m_doctorUtil.addSample(examinationAgent().getDoctorsUtilization());
		m_nurseUtil.addSample(vaccinationAgent().getNursesUtilization());

		m_waitingTimeRefill.addSample(vaccinationFillAgent().getWaitingTimeStat().mean()
				* vaccinationFillAgent().getWaitingTimeStat().sampleSize() / currentTime());

	}

	@Override
	public void simulationFinished()
	{
		super.simulationFinished();
	}

	@Override
	public void simulate(int replicationCount, double simEndTime) {
		//m_endSimTime = simEndTime;
		super.simulate(replicationCount, simEndTime);
	}

	@Override
	public void simulateAsync(int replicationCount, double simEndTime) {
		//m_endSimTime = simEndTime;
		super.simulateAsync(replicationCount, simEndTime);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setModelAgent(new ModelAgent(Id.modelAgent, this, null));
		setEnviroAgent(new EnviroAgent(Id.enviroAgent, this, modelAgent()));
		setVaccinationCenterAgent(new VaccinationCenterAgent(Id.vaccinationCenterAgent, this, modelAgent()));
		setLunchTransitionAgent(new LunchTransitionAgent(Id.lunchTransitionAgent, this, vaccinationCenterAgent()));
		setLunchAgent(new LunchAgent(Id.lunchAgent, this, lunchTransitionAgent()));
		setExaTransitionAgent(new ExaTransitionAgent(Id.exaTransitionAgent, this, vaccinationCenterAgent()));
		setWaitTransitionAgent(new WaitTransitionAgent(Id.waitTransitionAgent, this, vaccinationCenterAgent()));
		setRegistrationAgent(new RegistrationAgent(Id.registrationAgent, this, vaccinationCenterAgent()));
		setExaminationAgent(new ExaminationAgent(Id.examinationAgent, this, exaTransitionAgent()));
		setVaccinationAgent(new VaccinationAgent(Id.vaccinationAgent, this, vaccinationCenterAgent()));
		setWaitingRoomAgent(new WaitingRoomAgent(Id.waitingRoomAgent, this, waitTransitionAgent()));
		setVaccRefillTransitionAgent(new VaccRefillTransitionAgent(Id.vaccRefillTransitionAgent, this, vaccinationAgent()));
		setVaccinationFillAgent(new VaccinationFillAgent(Id.vaccinationFillAgent, this, vaccRefillTransitionAgent()));
	}

	private ModelAgent _modelAgent;

public ModelAgent modelAgent()
	{ return _modelAgent; }

	public void setModelAgent(ModelAgent modelAgent)
	{_modelAgent = modelAgent; }

	private EnviroAgent _enviroAgent;

public EnviroAgent enviroAgent()
	{ return _enviroAgent; }

	public void setEnviroAgent(EnviroAgent enviroAgent)
	{_enviroAgent = enviroAgent; }

	private VaccinationCenterAgent _vaccinationCenterAgent;

public VaccinationCenterAgent vaccinationCenterAgent()
	{ return _vaccinationCenterAgent; }

	public void setVaccinationCenterAgent(VaccinationCenterAgent vaccinationCenterAgent)
	{_vaccinationCenterAgent = vaccinationCenterAgent; }

	private LunchTransitionAgent _lunchTransitionAgent;

public LunchTransitionAgent lunchTransitionAgent()
	{ return _lunchTransitionAgent; }

	public void setLunchTransitionAgent(LunchTransitionAgent lunchTransitionAgent)
	{_lunchTransitionAgent = lunchTransitionAgent; }

	private LunchAgent _lunchAgent;

public LunchAgent lunchAgent()
	{ return _lunchAgent; }

	public void setLunchAgent(LunchAgent lunchAgent)
	{_lunchAgent = lunchAgent; }

	private ExaTransitionAgent _exaTransitionAgent;

public ExaTransitionAgent exaTransitionAgent()
	{ return _exaTransitionAgent; }

	public void setExaTransitionAgent(ExaTransitionAgent exaTransitionAgent)
	{_exaTransitionAgent = exaTransitionAgent; }

	private WaitTransitionAgent _waitTransitionAgent;

public WaitTransitionAgent waitTransitionAgent()
	{ return _waitTransitionAgent; }

	public void setWaitTransitionAgent(WaitTransitionAgent waitTransitionAgent)
	{_waitTransitionAgent = waitTransitionAgent; }

	private RegistrationAgent _registrationAgent;

public RegistrationAgent registrationAgent()
	{ return _registrationAgent; }

	public void setRegistrationAgent(RegistrationAgent registrationAgent)
	{_registrationAgent = registrationAgent; }

	private ExaminationAgent _examinationAgent;

public ExaminationAgent examinationAgent()
	{ return _examinationAgent; }

	public void setExaminationAgent(ExaminationAgent examinationAgent)
	{_examinationAgent = examinationAgent; }

	private VaccinationAgent _vaccinationAgent;

public VaccinationAgent vaccinationAgent()
	{ return _vaccinationAgent; }

	public void setVaccinationAgent(VaccinationAgent vaccinationAgent)
	{_vaccinationAgent = vaccinationAgent; }

	private WaitingRoomAgent _waitingRoomAgent;

public WaitingRoomAgent waitingRoomAgent()
	{ return _waitingRoomAgent; }

	public void setWaitingRoomAgent(WaitingRoomAgent waitingRoomAgent)
	{_waitingRoomAgent = waitingRoomAgent; }

	private VaccRefillTransitionAgent _vaccRefillTransitionAgent;

public VaccRefillTransitionAgent vaccRefillTransitionAgent()
	{ return _vaccRefillTransitionAgent; }

	public void setVaccRefillTransitionAgent(VaccRefillTransitionAgent vaccRefillTransitionAgent)
	{_vaccRefillTransitionAgent = vaccRefillTransitionAgent; }

	private VaccinationFillAgent _vaccinationFillAgent;

public VaccinationFillAgent vaccinationFillAgent()
	{ return _vaccinationFillAgent; }

	public void setVaccinationFillAgent(VaccinationFillAgent vaccinationFillAgent)
	{_vaccinationFillAgent = vaccinationFillAgent; }
	//meta! tag="end"

	public int getNumOfAdminWorkers() {
		return m_numOfAdminWorkers;
	}

	public void setNumOfAdminWorkers(int numOfAdminWorkers) {
		this.m_numOfAdminWorkers = numOfAdminWorkers;
	}

	public int getNumOfDoctors() {
		return m_numOfDoctors;
	}

	public void setNumOfDoctors(int numOfDoctors) {
		this.m_numOfDoctors = numOfDoctors;
	}

	public int getNumOfNurses() {
		return m_numOfNurses;
	}

	public void setNumOfNurses(int numOfNurses) {
		this.m_numOfNurses = numOfNurses;
	}

	public double getEndSimTime() {
		return m_endSimTime;
	}

	public void setEndSimTime(double endSimTime) {
		this.m_endSimTime = endSimTime;
	}

	public int getNumOfCustomers() {
		return m_numOfCustomers;
	}

	public void setNumOfCustomers(int numOfCustomers) {
		this.m_numOfCustomers = numOfCustomers;
	}

	public Stat getWaitingTimeReg() {
		return m_waitingTimeReg;
	}

	public Stat getAvgCustomersReg() {
		return m_avgCustomersReg;
	}

	public Stat getWaitingTimeExa() {
		return m_waitingTimeExa;
	}

	public Stat getAvgCustomersExa() {
		return m_avgCustomersExa;
	}

	public Stat getWaitingTimeVacc() {
		return m_waitingTimeVacc;
	}

	public Stat getAvgCustomersVacc() {
		return m_avgCustomersVacc;
	}

	public Stat getWaitingTimeWR() {
		return m_waitingTimeWR;
	}

	public Stat getAvgCustomersWR() {
		return m_avgCustomersWR;
	}

	public Stat getWorkerUtil() {
		return m_workerUtil;
	}

	public Stat getDoctorUtil() {
		return m_doctorUtil;
	}

	public Stat getNurseUtil() {
		return m_nurseUtil;
	}

	public void setTurbo(boolean turbo) {
		this.m_turbo = turbo;
	}

	public void setSpeed(double interval, double duration)
	{
		m_speedInterval = interval;
		m_speedDuration = duration;

		setSimSpeed(interval, duration);

	}

	public void setFinishTime(double finishTime)
	{
		m_finishTime = finishTime;
	}

	public Stat getWaitingTimeRefill() {
		return m_waitingTimeRefill;
	}

	public boolean isExperiment3() {
		return m_experiment3;
	}
}