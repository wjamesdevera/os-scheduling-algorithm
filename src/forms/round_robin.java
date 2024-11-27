package forms;

import cpu.scheduling.RoundRobin;
import cpu.scheduling.ShortestJobFirst;
import utils.Process;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class round_robin extends JDialog {
    private JPanel contentPane;
    private JTextField textQuantum;
    private JTextField textArrival;
    private JTextField textBurst;
    private JTextField textProcess;
    private JButton buttonProcess;
    private JButton startCompute;
    private JTable processTable;
    private JPanel resultArea;

    private DefaultTableModel tableModel;
    private double quanta;
    private RoundRobin rr;
    private ArrayList<utils.Process> processes = new ArrayList<>();
    private ArrayList<utils.Process> result;

    public round_robin() {
        this.quanta = quanta;
        setTitle("Round Robin");
        setSize(700, 600);
        setContentPane(contentPane);

        // North Panel - Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Round Robin"));
        textProcess = new JTextField();
        textArrival = new JTextField();
        textBurst = new JTextField();
        textQuantum = new JTextField();
        inputPanel.add(new JLabel("Process No.:"));
        inputPanel.add(textProcess);
        inputPanel.add(new JLabel("Arrival Time:"));
        inputPanel.add(textArrival);
        inputPanel.add(new JLabel("Burst Time:"));
        inputPanel.add(textBurst);
        inputPanel.add(new JLabel("Time Quantum:"));
        inputPanel.add(textQuantum);

        buttonProcess = new JButton("Add Process");
        startCompute = new JButton("Start Compute");
        inputPanel.add(buttonProcess);
        inputPanel.add(startCompute);

        // Center Panel - Process Table
        String[] columnNames = {"Process No.", "Arrival Time", "Burst Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(processTable);

        // South Panel - Output Area
        resultArea = new JPanel();
        JScrollPane outputScrollPane = new JScrollPane(resultArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Result"));

        // Layout Management
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(tableScrollPane, BorderLayout.CENTER);
        contentPane.add(outputScrollPane, BorderLayout.SOUTH);

        // Add Process Button Action
        buttonProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int process = Integer.parseInt(textProcess.getText());
                    double arrivalTime = Double.parseDouble(textArrival.getText());
                    double burstTime = Double.parseDouble(textBurst.getText());
                    processes.add(new utils.Process(process, arrivalTime, burstTime));
                    tableModel.addRow(new Object[]{process, arrivalTime, burstTime});
                    clearInputs();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(round_robin.this, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Start Simulation Button Action
        startCompute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double quantum = Double.parseDouble(textQuantum.getText());
                    simulate(quantum);
                    displayOutput();
                    resultArea.revalidate();
                    resultArea.repaint();
                    contentPane.revalidate();
                    contentPane.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(round_robin.this, "Invalid Quantum! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }


    private void clearInputs() {
        textProcess.setText("");
        textArrival.setText("");
        textBurst.setText("");
    }

    private void onCancel() {
        setVisible(false); // Hide this dialog
        MainMenu mainMenu = new MainMenu(); // Show MainMenu again
        mainMenu.pack();
        mainMenu.setVisible(true);
    }

    public void simulate(final double quanta) throws Exception {
        rr = new RoundRobin(this.processes, quanta);
        this.result = rr.simulate();
        rr.printCompletedProcess();
    }

    private void displayOutput() {
        try {
            // Check if simulation is possible
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Simulation has not been run. Please add processes and start computation.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Clear previous output

            int numProcesses = result.size();
            // Prepare data for JTable
            String[] columns = {"Process", "AT", "BT", "CT", "TAT", "WT"};
            Object[][] data = new Object[numProcesses + 2][6]; // +1 for the averages row

            int PROCESS_ID = 0;
            int AT = 1;
            int BT = 2;
            int CT = 3;
            int TAT = 4;
            int WT = 5;

            double totalCT = 0;
            double totalTAT = 0;
            double totalWT = 0;


            for (int i = 0; i < numProcesses; i++) {
                utils.Process process = result.get(i);

                data[i][PROCESS_ID] = "P" + process.getID();
                data[i][AT] = process.getArrivalTime();
                data[i][BT] = process.getBurstTime();
                data[i][CT] = process.getCompletionTime();
                data[i][TAT] = process.getTurnAroundTime();
                data[i][WT] = process.getWaitingTime();

            }
            totalCT = rr.getTotalCompletionTime();
            totalTAT = rr.getTotalTurnAroundTime();
            totalWT = rr.getTotalWaitingTime();

            // Calculate averages
            double avgCT = Math.round(totalCT / numProcesses * 100.0) / 100.0;
            double avgTAT = Math.round(totalTAT / numProcesses * 100.0) / 100.0;
            double avgWT = Math.round(totalWT / numProcesses * 100.0) / 100.0;

            // Add average row to the table
            data[numProcesses][PROCESS_ID] = "Total";
            data[numProcesses][CT] = totalCT;
            data[numProcesses][TAT] = totalTAT;
            data[numProcesses][WT] = totalWT;

            data[numProcesses + 1][PROCESS_ID] = "Average";
            data[numProcesses + 1][CT] = avgCT;
            data[numProcesses + 1][TAT] = avgTAT;
            data[numProcesses + 1][WT] = avgWT;

            // Display the output in the JTextArea
            JPanel tablePanel = new JPanel();
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // All cells are non-editable
                }
            });
            table.setPreferredScrollableViewportSize(new Dimension(500, 100));
            table.setFillsViewportHeight(true);

            // Create a JScrollPane for the table
            JScrollPane tableScrollPane = new JScrollPane(table);

            // Create a panel for the table and add it to the content pane
            tablePanel.setName("table");
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
            tablePanel.add(tableScrollPane);
            for (Component component: resultArea.getComponents()) {
                if (component instanceof JPanel) {
                    if (component.getName() != null) {
                        resultArea.remove(component);
                    }
                }
            }
            resultArea.add(tablePanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "An error occurred during simulation: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new round_robin().setVisible(true));
    }
}



