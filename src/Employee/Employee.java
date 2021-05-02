package Employee;

public abstract class Employee {
    private boolean m_availability;
    protected EmployeeState m_state;
    private double m_totalTimeOccupied;
    private double m_lastTimeAvailable;

    private int m_servicedCustomers;
    private int m_id;

    public Employee(int id) {
        m_availability = true;
        m_totalTimeOccupied = 0.0;
        m_lastTimeAvailable = 0.0;
        m_servicedCustomers = 0;
        m_state = EmployeeState.AVAILABLE;
        m_id = id;
    }

    public boolean isAvailable() {
        return m_availability;
    }

    public void setOccupied(double actualTime) {
        m_lastTimeAvailable = actualTime;
        m_availability = false;
        m_state = EmployeeState.OCCUPIED;
    }

    public void setAvailable(double actualTime) {
        m_totalTimeOccupied += actualTime - m_lastTimeAvailable;
        m_lastTimeAvailable = 0.0;
        m_availability = true;
        m_state = EmployeeState.AVAILABLE;
        m_servicedCustomers++;
    }

    public double getTotalTimeOccupied() {
        return m_totalTimeOccupied;
    }

    public void resetTime() {
        m_totalTimeOccupied = 0.0;
        m_lastTimeAvailable = 0.0;
    }

    public String toStringWithTime(double time) {
        String str;
        //str = String.format("Serviced: %d ,util: %.2f%% available: %b", m_servicedCustomers, (m_totalTimeOccupied / time) * 100, m_availability);
        str = String.format("Util: %.2f%% status: %s serviced: %d", (m_totalTimeOccupied / time) * 100, m_state.toString(), m_servicedCustomers);
        return str;
    }

    protected int getID() {
        return m_id;
    }
}
