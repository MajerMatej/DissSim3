package Employee;

public class Nurse extends Employee {
    private final int m_maxInjections = 1;
    public Nurse(int id) {
        super(id);

        m_injections = m_maxInjections;
        m_fillWaitingTimeStart = 0.0;
        m_fillWaitingTimeTotal = 0.0;
    }

    private int m_injections;
    private double m_fillWaitingTimeStart;
    private double m_fillWaitingTimeTotal;

    @Override
    public String toStringWithTime(double time) {
        String str = "Nurse" + getID() + " " + super.toStringWithTime(time) + " injections: " + getInjections();
        return str;
    }

    @Override
    public void setOccupied(double actualTime) {
        m_injections--;
        super.setOccupied(actualTime);
    }

    public int getInjections() {
        return m_injections;
    }

    public double getFillWaitingTimeStart() {
        return m_fillWaitingTimeStart;
    }

    public void setFillWaitingTimeStart(double fillWaitingTimeStart) {
        this.m_fillWaitingTimeStart = fillWaitingTimeStart;
    }

    public double getFillWaitingTimeTotal() {
        return m_fillWaitingTimeTotal;
    }

    public void setFillWaitingTimeTotal(double fillWaitingTimeTotal) {
        this.m_fillWaitingTimeTotal = fillWaitingTimeTotal;
    }

    public void goRefillInjections()
    {
        m_state = EmployeeState.GOING_TO_REFILL;
    }

    public void goBackFromRefill()
    {
        m_state = EmployeeState.GOING_BACK_FROM_REFILL;
    }

    public void waitForRefill()
    {
        m_state = EmployeeState.WAITING_FOR_REFILL;
    }

    public void refillInjections()
    {
        m_state = EmployeeState.REFILLING;
        m_injections = m_maxInjections;
    }
}
