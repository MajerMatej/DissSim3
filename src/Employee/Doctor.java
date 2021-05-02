package Employee;

public class Doctor extends Employee{
    public Doctor(int id) {
        super(id);
    }

    @Override
    public String toStringWithTime(double time) {
        String str = "Doctor" + getID() + " " + super.toStringWithTime(time);
        return str;
    }
}
