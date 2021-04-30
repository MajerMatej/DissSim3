package VaccinationCenter.GUI;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import simulation.MySimulation;

import java.util.LinkedList;

public class Controller implements ISimDelegate {
    private MySimulation m_simCore;

    private ISimDelegate m_observer = null;
    private boolean exp3Running = false;
    private boolean exp2Running = false;

    private int m_replications = 1;
    private double m_repTime = 32400.0;

    public Controller() {

    }

    public void init(int numberOfReplications, int seed,
                     int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime, int speed, int maxCustomers) {
        m_replications = numberOfReplications;
        m_repTime = repTime;
        m_simCore =new MySimulation(numOfAdminWorkers,numOfDoctors,numOfNurses, repTime, maxCustomers);
        m_simCore.registerDelegate(this);
    }

    public void subscribeToSimCore(ISimDelegate observer) {
        m_observer = observer;
    }

    public void run() {
        m_simCore.registerDelegate(m_observer);
        m_simCore.simulateAsync(m_replications, 9000000d);
    }


    public void pauseSim() {
        m_simCore.pauseSimulation();
    }

    public void resumeSim() {
        m_simCore.resumeSimulation();
    }

    public void stopSim() {
        m_simCore.stopSimulation();
    }

    public void setTurbo(boolean turbo) {
        m_simCore.setTurbo(turbo);
    }

    public boolean getExp3run() {
        return exp3Running;
    }

    public boolean getExp2run() {
        return exp2Running;
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation simulation) {

    }

    public MySimulation getSimCore() {
        return m_simCore;
    }

    public void setSimSpeed(double interval, double duration)
    {

        if(m_simCore != null)
        {
            m_simCore.setSpeed(interval, duration);
        }
    }
}
