package VaccinationCenter;

import VaccinationCenter.GUI.Controller;
import VaccinationCenter.GUI.MainWindow;
import simulation.MySimulation;

public class Main {

    public static void main(String[] args) {
	// write your code here
        /*MySimulation sim = new MySimulation(21,30,10, 32400, 4000);

        sim.onSimulationWillStart(s ->{
            System.out.println("Simulating...");
        });

        sim.simulate(100, 32400d);
        */
        MainWindow mainWindow = new MainWindow(new Controller());
    }
}
