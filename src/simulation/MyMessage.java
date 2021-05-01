package simulation;

import Employee.AdminWorker;
import Employee.Doctor;
import Employee.Nurse;
import OSPABA.*;

public class MyMessage extends MessageForm
{
	private double m_startWaitingReg;
	private double m_totalWaitingReg;

	private AdminWorker m_worker;

	private double m_startWaitingExa;
	private double m_totalWaitingExa;
	private Doctor m_doctor;

	private double m_startWaitingVacc;
	private double m_totalWaitingVacc;
	private Nurse m_nurse;

	private double m_startWaitingRoom;
	private double m_totalWaitingRoom;

	private double m_startWaitingRefill;
	private double m_totalWaitingRefill;
	private Nurse m_refillNurse;

	public MyMessage(Simulation sim)
	{
		super(sim);
		m_startWaitingReg = 0.0;
		m_totalWaitingReg = 0.0;

		m_startWaitingExa = 0.0;
		m_totalWaitingExa = 0.0;
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
		m_startWaitingReg = ((MyMessage)original).m_startWaitingReg;
		m_totalWaitingReg = ((MyMessage)original).m_totalWaitingReg;

		m_startWaitingExa = ((MyMessage)original).m_startWaitingExa;
		m_totalWaitingExa = ((MyMessage)original).m_totalWaitingExa;

		m_worker = ((MyMessage)original).m_worker;
		m_doctor = ((MyMessage)original).m_doctor;
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);

	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		m_startWaitingReg = ((MyMessage)original).m_startWaitingReg;
		m_totalWaitingReg = ((MyMessage)original).m_totalWaitingReg;

		m_startWaitingExa = ((MyMessage)original).m_startWaitingExa;
		m_totalWaitingExa = ((MyMessage)original).m_totalWaitingExa;

		m_worker = ((MyMessage)original).m_worker;
		m_doctor = ((MyMessage)original).m_doctor;
		// Copy attributes
	}

	public double getStartWaitingReg() {
		return m_startWaitingReg;
	}

	public void setStartWaitingReg(double startWaiting) {
		this.m_startWaitingReg = startWaiting;
	}

	public double getTotalWaitingReg() {
		return m_totalWaitingReg;
	}

	public void setTotalWaitingReg(double totalWaiting) {
		this.m_totalWaitingReg = totalWaiting;
	}

	public AdminWorker getWorker() {
		return m_worker;
	}

	public void setWorker(AdminWorker worker) {
		this.m_worker = worker;
	}

	public double getStartWaitingExa() {
		return m_startWaitingExa;
	}

	public void setStartWaitingExa(double startWaitingExa) {
		this.m_startWaitingExa = startWaitingExa;
	}

	public double getTotalWaitingExa() {
		return m_totalWaitingExa;
	}

	public void setTotalWaitingExa(double totalWaitingExa) {
		this.m_totalWaitingExa = totalWaitingExa;
	}

	public Doctor getDoctor() {
		return m_doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.m_doctor = doctor;
	}

	public double getStartWaitingVacc() {
		return m_startWaitingVacc;
	}

	public void setStartWaitingVacc(double startWaitingVacc) {
		this.m_startWaitingVacc = startWaitingVacc;
	}

	public double getTotalWaitingVacc() {
		return m_totalWaitingVacc;
	}

	public void setTotalWaitingVacc(double totalWaitingVacc) {
		this.m_totalWaitingVacc = totalWaitingVacc;
	}

	public Nurse getNurse() {
		return m_nurse;
	}

	public void setNurse(Nurse nurse) {
		this.m_nurse = nurse;
	}

	public double getStartWaitingRoom() {
		return m_startWaitingRoom;
	}

	public void setStartWaitingRoom(double startWaitingRoom) {
		this.m_startWaitingRoom = startWaitingRoom;
	}

	public double getTotalWaitingRoom() {
		return m_totalWaitingRoom;
	}

	public void setTotalWaitingRoom(double totalWaitingRoom) {
		this.m_totalWaitingRoom = totalWaitingRoom;
	}

	public double getStartWaitingRefill() {
		return m_startWaitingRefill;
	}

	public void setStartWaitingRefill(double startWaitingRefill) {
		this.m_startWaitingRefill = startWaitingRefill;
	}

	public double getTotalWaitingRefill() {
		return m_totalWaitingRefill;
	}

	public void setTotalWaitingRefill(double totalWaitingRefill) {
		this.m_totalWaitingRefill = totalWaitingRefill;
	}

	public Nurse getRefillNurse() {
		return m_refillNurse;
	}

	public void setRefillNurse(Nurse refillNurse) {
		this.m_refillNurse = refillNurse;
	}
}