package Employee;

public class Nurse extends Employee {
    public Nurse() {
        m_injections = 20;
        m_fillWaitingTimeStart = 0.0;
        m_fillWaitingTimeTotal = 0.0;
    }

    private int m_injections;
    private double m_fillWaitingTimeStart;
    private double m_fillWaitingTimeTotal;

    @Override
    public String toStringWithTime(double time) {
        String str = "Nurse " + super.toStringWithTime(time);
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
}
