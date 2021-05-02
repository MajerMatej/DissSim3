package Employee;

public class AdminWorker extends Employee{
    public AdminWorker(int id) {
        super(id);
    }

    @Override
    public String toStringWithTime(double time) {
        String str = "AdminWorker"+ getID() + " " + super.toStringWithTime(time);
        return str;
    }
}