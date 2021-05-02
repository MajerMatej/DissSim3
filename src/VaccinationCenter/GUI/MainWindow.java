package VaccinationCenter.GUI;

import Employee.*;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulation.MySimulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.HashMap;
import java.util.LinkedList;

public class MainWindow extends JFrame implements ISimDelegate {
    private JPanel rootPanel;
    private Controller app;
    private boolean pause;
    private boolean running;
    private double systemTime;
    //DefaultListModel<String> model;

    private JTextField seedTF;
    private JTextField replicationsTF;
    private JTextField adminWorkTF;
    private JTextField doctorsTF;
    private JButton runBTN;
    private JButton pauseBTN;
    private JTextField nursesTF;
    private JLabel pplInRegQL;
    private JLabel avgTimeInRegQL;
    private JLabel avgPplInRegQL;
    private JLabel empRegL;
    private JLabel availWorkL;
    private JLabel utilRegL;
    private JLabel pplInMedQL;
    private JLabel avgPplInMedQL;
    private JLabel avgTimeInMedQ;
    private JLabel empMedL;
    private JLabel availDocL;
    private JLabel utilDocL;
    private JLabel pplInVaccQL;
    private JLabel avgPplInVaccQL;
    private JLabel avgTimeInVaccQL;
    private JLabel empVaccL;
    private JLabel availNurL;
    private JLabel utilNurL;
    private JLabel pplInWRL;
    private JLabel avgPplInWRL;
    private JLabel avgTimeInWRL;
    private JCheckBox turboCB;
    private JTextField repTimeTB;
    private JLabel replicationL;
    private JLabel systemTimeL;
    private JSlider slider1;
    private JLabel simSpeedL;
    private JTabbedPane QueueStats;
    private JPanel customersTab;
    private JList list2;
    private JTable workTable;
    private JLabel customersLabel;
    private JTextField customersTF;
    private JTextField exp2CustTF;
    private JTextField exp2RepTF;
    private JTable exp2Table;
    private JButton exp2BTN;
    private JPanel employeeTab;
    private JPanel chartPane;
    private JTextField maxDocTF;
    private JTextField repExp3TF;
    private JTextField minDocTF;
    private JButton exp3BTN;
    private JTable exp3Table;
    private JScrollPane exp3;
    private JButton refreshButton;
    private JLabel ciL;
    private JSlider slider2;
    private JLabel regQLCI;
    private JLabel regWTCI;
    private JLabel regUtilCI;
    private JLabel exaQLCI;
    private JLabel exaWTCI;
    private JLabel exaUtilCI;
    private JLabel vaccQLCI;
    private JLabel vaccWTCI;
    private JLabel vaccUtilCI;
    private JLabel vaccAvgRefillWT;
    private JLabel vaccRefillCI;
    private JTable doctorTable;
    private JTable nurseTable;
    private JPanel chartTab;

    private int m_workers;
    private int m_doctors;
    private int m_nurses;
    private DefaultTableModel workTableModel;
    private DefaultTableModel docTableModel;
    private DefaultTableModel nursTableModel;
    private  XYSeries xyseries;
    private XYSeriesCollection datasetXY;
    JFreeChart chart;
    DefaultTableModel tableModelEXP3;
    DefaultTableModel tableModelEXP2;


    public MainWindow(Controller app) {
        this.app = app;

        add(rootPanel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setBounds(300,100, 1250, 900);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        pause = false;
        running = false;
        pauseBTN.setEnabled(false);
        systemTime = 8*60*60.0;

        String[] columnNames = {"Employee", "Status", "Utilization", "Serviced"};
        workTableModel = new DefaultTableModel(columnNames, m_workers);
        docTableModel = new DefaultTableModel(columnNames, m_doctors);
        nursTableModel = new DefaultTableModel(columnNames, m_nurses);

        tableModelEXP2 = new DefaultTableModel(columnNames, 3);

        //JTableHeader header = new JTableHeader(columnNames, 3);
        //exp2Table.setModel(tableModel);
        //exp2Table.setAutoCreateRowSorter(true);

        m_workers = 1;
        m_doctors = 1;
        m_nurses = 1;

        String[] columnNamesEXP3 = {"Doctors", "Average Q length"};
        tableModelEXP3 = new DefaultTableModel(columnNamesEXP3, 0);
        //tableModelEXP2 = new DefaultTableModel(columnNamesEXP2, 3);


        //JTableHeader header = new JTableHeader(columnNames, 3);
        exp3Table.setModel(tableModelEXP3);

        xyseries = new XYSeries("Average examination queue length");
        datasetXY = new XYSeriesCollection(xyseries);
        chart = ChartFactory.createXYLineChart(
                "Average medical examination Queue length",
                "Doctors count",
                "Average queue len",
                datasetXY,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(450, 300));
        chartPane.add(chartPanel);
        workTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        doctorTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        nurseTable.setDefaultRenderer(Object.class, new CustomCellRendererNurse());


        runBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_workers = Integer.parseInt(adminWorkTF.getText());
                m_doctors = Integer.parseInt(doctorsTF.getText());
                m_nurses = Integer.parseInt(nursesTF.getText());
                String[] columnNames = {"Employee", "Status", "Utilization", "Serviced",
                        "Available", "Occupied", "To lunch", "Eating", "From lunch"};

                workTableModel = new DefaultTableModel(columnNames, m_workers);
                docTableModel = new DefaultTableModel(columnNames, m_doctors);

                String[] columnNamesNurse = {"Employee", "Status", "Utilization", "Serviced", "Injections",
                        "Available", "Occupied", "To lunch", "Eating", "From lunch", "To refill", "Waiting",
                        "Refilling", "From refill"};
                nursTableModel = new DefaultTableModel(columnNamesNurse, m_nurses);

                if(running) {
                    app.stopSim();
                    runBTN.setText("Run");
                    running = false;
                    pauseBTN.setEnabled(false);
                } else {
                    runSimulation(Integer.parseInt(replicationsTF.getText()),
                            Integer.parseInt(seedTF.getText()), Integer.parseInt(adminWorkTF.getText()),
                            Integer.parseInt(doctorsTF.getText()), Integer.parseInt(nursesTF.getText()),
                            Integer.parseInt(repTimeTB.getText()), slider1.getValue() + 1, Integer.parseInt(customersTF.getText()));
                    pauseBTN.setEnabled(true);
                    runBTN.setText("St♥p");
                    running = true;
                }
            }
        });
        pauseBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pause) {
                    pause = false;
                    pauseBTN.setText("Pause");
                    app.resumeSim();
                } else {
                    pause = true;
                    pauseBTN.setText("Resume");
                    app.pauseSim();
                }
            }
        });

        exp3BTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runExperiment3();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = tableModelEXP3.getRowCount(); i > 0 ; i--) {
                    tableModelEXP3.removeRow(i - 1);
                }

            }
        });
        exp2BTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runExperiment2();
            }
        });
        slider1.addComponentListener(new ComponentAdapter() {
        });
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSimSpeed();
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSimSpeed();
            }
        });
        turboCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) { /*changeSimSpeed()*/;

            }
        });
    }

    private void runExperiment3() {
//        app.subscribeToSimCore(this);
//        app.experiment3(Integer.parseInt(minDocTF.getText()), Integer.parseInt(maxDocTF.getText()),
//                Integer.parseInt(repExp3TF.getText()), Integer.parseInt(customersTF.getText()),
//                Integer.parseInt(adminWorkTF.getText()), Integer.parseInt(nursesTF.getText()),
//                Integer.parseInt(repTimeTB.getText()));
    }
    private void runExperiment2() {
//        LinkedList<String> list = app.experiment2(Integer.parseInt(doctorsTF.getText()),
//                Integer.parseInt(exp2RepTF.getText()), Integer.parseInt(exp2CustTF.getText()),
//                Integer.parseInt(adminWorkTF.getText()), Integer.parseInt(nursesTF.getText()),
//                Integer.parseInt(repTimeTB.getText()));
//        int i = 0;
//        int j = 0;
//        try {
//            for (String string : list) {
//                for (Object obj : parseExp2(string)) {
//                    tableModelEXP2.setValueAt(obj, i, j);
//                    j++;
//                }
//                j = 0;
//                i++;
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        exp2Table.setModel(tableModelEXP2);
    }

    private void runSimulation(int numberOfReplications, int seed,
                               int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime,
                               int speed, int maxCustomers) {

        empRegL.setText("Admin workers: " + numOfAdminWorkers);
        empMedL.setText("Doctors: " + numOfDoctors);
        empVaccL.setText("Nursers: " + numOfNurses);

        app.init(numberOfReplications, seed, numOfAdminWorkers, numOfDoctors, numOfNurses, repTime, speed, maxCustomers);
        app.setTurbo(turboCB.isSelected());
        changeSimSpeed();
        app.subscribeToSimCore(this);
        //Simulation sim = app.getSimCore();

        app.getSimCore().onRefreshUI(simulation->refresh(simulation));
        app.run();
    }

    private void refreshEmployees(LinkedList<String> employees, DefaultTableModel tableModel, boolean isItNurse) {

        int i = 0;
        int j = 0;
        int stateIndex = -1;
        try {
            for (String string : employees) {
                for (Object obj : (isItNurse) ? parseNurseToRow(string) : parseEmployeeToRow(string)) {
                    tableModel.setValueAt(obj, i, j);
                    if(j == 1)
                    {
                        updateStateCells((String)obj, tableModel, i);
                    }

                    j++;
                }

                j = 0;
                i++;
            }
        } catch (Exception e) {
            System.out.println("Hallo" + e.toString());
        }
        //vaccTable.setModel(tableModel);

    }

    private Object[] parseEmployeeToRow(String string) {
        //String phrase = "the music made   it   hard      to        concentrate";
        String delims = "[ ]+";
        String[] tokens = string.split(delims);
        return new Object[]{tokens[0], tokens[4], tokens[2], tokens[6]};
    }

    private Object[] parseNurseToRow(String string) {
        //String phrase = "the music made   it   hard      to        concentrate";
        String delims = "[ ]+";
        String[] tokens = string.split(delims);
        return new Object[]{tokens[0], tokens[4], tokens[2], tokens[6], tokens[8]};
    }

    private Object[] parseExp2(String string) {
        //String phrase = "the music made   it   hard      to        concentrate";
        String delims = "[ ]+";
        String[] tokens = string.split(delims);
        return new Object[]{tokens[0], tokens[1], tokens[2]};
    }

    private void refreshEXP3(HashMap<String, Double> stats) {
        double replications = stats.get("CompleteReplications");
        double pplInQ = stats.get("ExaminedCustomersGlobal") / replications;
        int docs = (int)(double)stats.get("Doctors");
        //avgPplInMedQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("ExaminedCustomersGlobal") / replications));
        System.out.println("" + docs + ", " + pplInQ);
        xyseries.add(docs, pplInQ);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Doctors");
        yAxis.setLabel("Average Queue length");
        xAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRangeIncludesZero(false);
        XYPlot plot  = (XYPlot)chart.getPlot();
        xAxis.setTickUnit(new NumberTickUnit(1));
        plot.setRangeAxis(yAxis);
        plot.setDomainAxis(xAxis);

        tableModelEXP3.addRow(new Object[]{docs, pplInQ});

    }


    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {
        if(simState == SimState.stopped) {
            if (turboCB.isSelected()) {

                updateGUISimFinished(simulation);
            } else {
                updateGUIinRep(simulation);
            }
        }
    }

    @Override
    public void refresh(Simulation simulation) {
        updateGUIinRep(simulation);
    }

    private void updateGUIinRep(Simulation simulation)
    {
        MySimulation sim = ((MySimulation)simulation);

        try {
            double tmpTime = systemTime + sim.currentTime();

            int seconds = (int)(tmpTime % 60);
            int minutes = (int)(tmpTime / 60);
            int hours = (minutes / 60) % 24;
            minutes = minutes % 60;

            replicationL.setText(String.format("Actual replication: %d" , sim.currentReplication()));
            systemTimeL.setText(String.format("System time: %02d : %02d : %02d", hours, minutes, seconds));

            avgTimeInRegQL.setText(String.format("%.4f", sim.registrationAgent().getWaitingTimeStat().mean()));
            avgPplInRegQL.setText(String.format("%.4f", sim.registrationAgent().getWaitingTimeStat().mean()
                    * sim.registrationAgent().getWaitingTimeStat().sampleSize()
                    / sim.currentTime()));
            utilRegL.setText(String.format("%.4f ", sim.registrationAgent().getAdminWorkersUtilization() * 100) + "%");
            availWorkL.setText("Available: " + sim.registrationAgent().getAvailableAdminWorkers().size());
            pplInRegQL.setText("Queue length: " + sim.registrationAgent().getCustomersQueue().size());

            avgTimeInMedQ.setText(String.format("%.4f", sim.examinationAgent().getWaitingTimeStat().mean()));
            avgPplInMedQL.setText(String.format("%.4f", sim.examinationAgent().getWaitingTimeStat().mean()
                    * sim.examinationAgent().getWaitingTimeStat().sampleSize()
                    / sim.currentTime()));
            utilDocL.setText(String.format("%.4f ", sim.examinationAgent().getDoctorsUtilization() * 100) + "%");
            availDocL.setText("Available: " + sim.examinationAgent().getAvailableDoctors().size());
            pplInMedQL.setText("Queue length: " + sim.examinationAgent().getCustomersQueue().size());

            avgTimeInVaccQL.setText(String.format("%.4f", sim.vaccinationAgent().getWaitingTimeStat().mean()));
            avgPplInVaccQL.setText(String.format("%.4f ", sim.vaccinationAgent().getWaitingTimeStat().mean()
                    * sim.vaccinationAgent().getWaitingTimeStat().sampleSize()
                    / sim.currentTime()));

            utilNurL.setText(String.format("%.4f ", sim.vaccinationAgent().getNursesUtilization() * 100) + "%");
            availNurL.setText("Available: " + sim.vaccinationAgent().getAvailableNurses().size());
            pplInVaccQL.setText("Queue length: " + sim.vaccinationAgent().getCustomersQueue().size());

            pplInWRL.setText("Ppl in waiting room: " + sim.waitingRoomAgent().getCustomersWaiting());

            avgPplInWRL.setText(String.format("%.4f ", sim.waitingRoomAgent().getWaitingTimeStat().mean()
                    * sim.waitingRoomAgent().getWaitingTimeStat().sampleSize()
                    / sim.currentTime()));


            LinkedList<String> workers = new LinkedList<>();
            for (AdminWorker worker: sim.registrationAgent().getAdminWorkers()) {
                workers.add(worker.toStringWithTime(sim.currentTime()));
            }

            refreshEmployees(workers, workTableModel, false);
            workTable.setModel(workTableModel);

            LinkedList<String> doctors = new LinkedList<>();
            for (Doctor doctor: sim.examinationAgent().getDoctors()) {
                doctors.add(doctor.toStringWithTime(sim.currentTime()));
            }

            refreshEmployees(doctors, docTableModel, false);
            doctorTable.setModel(docTableModel);

            LinkedList<String> nurses = new LinkedList<>();
            for (Nurse nurse: sim.vaccinationAgent().getNurses()) {
                nurses.add(nurse.toStringWithTime(sim.currentTime()));
            }

            refreshEmployees(nurses, nursTableModel, true);
            nurseTable.setModel(nursTableModel);

        }catch (Exception e) {
            System.out.println("PEnis" + e.toString());
        }
    }

    private void updateGUISimFinished(Simulation simulation){
        try {
            MySimulation sim = ((MySimulation) simulation);
            replicationL.setText(String.format("Actual replication: %d", sim.currentReplication()));

            avgTimeInRegQL.setText(String.format("%.4f", sim.getWaitingTimeReg().mean()));
            avgPplInRegQL.setText(String.format("%.4f", sim.getAvgCustomersReg().mean()));
            utilRegL.setText(String.format("%.4f ", sim.getWorkerUtil().mean() * 100) + "%");

            avgTimeInMedQ.setText(String.format("%.4f", sim.getWaitingTimeExa().mean()));
            avgPplInMedQL.setText(String.format("%.4f", sim.getAvgCustomersExa().mean()));
            utilDocL.setText(String.format("%.4f ", sim.getDoctorUtil().mean() * 100) + "%");

            avgTimeInVaccQL.setText(String.format("%.4f", sim.getWaitingTimeVacc().mean()));
            avgPplInVaccQL.setText(String.format("%.4f ", sim.getAvgCustomersVacc().mean()));
            utilNurL.setText(String.format("%.4f ", sim.getNurseUtil().mean() * 100) + "%");

            avgPplInWRL.setText(String.format("%.4f ", sim.getAvgCustomersWR().mean()));
            updateConfidenceIntervals(simulation);
        }catch (Exception e) {
            System.out.println("Stopped: " + e.toString());
        }

    }

    public void changeSimSpeed()
    {
        double speedMax = slider2.getMaximum() * .1;
        double speedValue = slider2.getValue() * .1;
        double intervalValue = slider1.getValue();

        if (app != null)
        {
            if (! turboCB.isSelected())
            {
                app.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
            }
            else
            {
                app.setTurbo(true);
            }
        }
    }

    private void updateConfidenceIntervals(Simulation simulation)
    {
        MySimulation sim = ((MySimulation) simulation);
        /*95% Confidence interval:*/
        if (sim.currentReplication() > 1) {
            ciL.setText(String.format("<%.4f , %.4f>", sim.getAvgCustomersWR().confidenceInterval_95()[0],
                    sim.getAvgCustomersWR().confidenceInterval_95()[1]));

            regQLCI.setText(String.format("<%.4f , %.4f>",
                    sim.getAvgCustomersReg().confidenceInterval_95()[0],
                    sim.getAvgCustomersReg().confidenceInterval_95()[1]));
            regWTCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getWaitingTimeReg().confidenceInterval_95()[0],
                    sim.getWaitingTimeReg().confidenceInterval_95()[1]));
            regUtilCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getWorkerUtil().confidenceInterval_95()[0],
                    sim.getWorkerUtil().confidenceInterval_95()[1]));

            exaQLCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getAvgCustomersExa().confidenceInterval_95()[0],
                    sim.getAvgCustomersExa().confidenceInterval_95()[1]));
            exaWTCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getWaitingTimeExa().confidenceInterval_95()[0],
                    sim.getWaitingTimeExa().confidenceInterval_95()[1]));
            exaUtilCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getDoctorUtil().confidenceInterval_95()[0],
                    sim.getDoctorUtil().confidenceInterval_95()[1]));

            vaccQLCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getAvgCustomersVacc().confidenceInterval_95()[0],
                    sim.getAvgCustomersVacc().confidenceInterval_95()[1]));
            vaccWTCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getWaitingTimeVacc().confidenceInterval_95()[0],
                    sim.getWaitingTimeVacc().confidenceInterval_95()[1]));
            vaccUtilCI.setText(String.format(" <%.4f , %.4f>",
                    sim.getNurseUtil().confidenceInterval_95()[0],
                    sim.getNurseUtil().confidenceInterval_95()[1]));
        }
    }

    public void updateStateCells(String stringState, DefaultTableModel tableModel, int row) {

        EmployeeState state;
        try {
            state = EmployeeState.valueOf(stringState.toString());
        } catch (Exception e) {
            return;
        }
        for(int i = (tableModel == nursTableModel)? 5 : 4; i < tableModel.getColumnCount(); i++) {
            tableModel.setValueAt(" ", row, i);
        }
        int stateIndex = -1;

        if (state != null) {
            switch (state) {
                case AVAILABLE:
                    stateIndex = 4;
                    break;
                case OCCUPIED:
                    stateIndex = 5;
                    break;
                case GOING_TO_LUNCH:
                    stateIndex = 6;
                    break;
                case EATING_LUNCH:
                    stateIndex = 7;
                    break;
                case GOING_BACK_FROM_LUNCH:
                    stateIndex = 8;
                    break;
                case GOING_TO_REFILL:
                    stateIndex = 9;
                    break;
                case WAITING_FOR_REFILL:
                    stateIndex = 10;
                    break;
                case REFILLING:
                    stateIndex = 11;
                    break;
                case GOING_BACK_FROM_REFILL:
                    stateIndex = 12;
                    break;
            }
        }
        if(stateIndex != -1) {
            if(tableModel == nursTableModel) {
                stateIndex++;
            }
            tableModel.setValueAt("☺", row, stateIndex);
        }
        return;
    }

    public class CustomCellRendererNurse extends DefaultTableCellRenderer {
            public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.WHITE);

            if(value == "☺")
            {

                switch (column)
                {
                    case 5:
                        c.setBackground(Color.GREEN);
                        break;
                    case 6:
                        c.setBackground(Color.RED);
                        break;
                    case 12:
                        c.setBackground(Color.ORANGE);
                        break;
                    default:
                        c.setBackground(Color.YELLOW);
                }

            }
            super.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            return c;
        }

    }
    public class CustomCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(Color.WHITE);

            if(value == "☺")
            {

                switch (column)
                {
                    case 4:
                        c.setBackground(Color.GREEN);
                        break;
                    case 5:
                        c.setBackground(Color.RED);
                        break;
//                    case 11:
//                        c.setBackground(Color.ORANGE);
//                        break;
                    default:
                        c.setBackground(Color.ORANGE);
                }

            }
            super.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            return c;
        }

    }
}
