package forms;

import cpu.scheduling.RoundRobin;
import utils.Process;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextArea resultArea;

    private DefaultTableModel tableModel;
    private double quanta;
    private RoundRobin rr;
    private ArrayList<utils.Process> processes = new ArrayList<>();
    private ArrayList<utils.Process> result;

//    public round_robin(ArrayList<Process> processes, double quanta) {
//        this.processes = processes;
//        this.quanta = quanta;
//    }
//
//    public round_robin(ArrayList<Process> processList, double quantum, double quanta, ArrayList<Process> processes) {
//        this.quanta = quanta;
//        this.processes = processes;
//    }

    public round_robin() {
        this.quanta = quanta;
        setTitle("Round Robin");
        setSize(700, 600);
        setLocationRelativeTo(null);
        contentPane = new JPanel(new BorderLayout());
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
        resultArea = new JTextArea();
        resultArea.setEditable(false);
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
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(round_robin.this, "Invalid Quantum! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private void clearInputs() {
        textProcess.setText("");
        textArrival.setText("");
        textBurst.setText("");
    }

    public void simulate(final double quanta) {
        rr = new RoundRobin(this.processes, quanta);
        this.result = rr.simulate();
    }

    private void displayOutput() {
        startCompute.addActionListener(e -> {
            try {
                // Check if simulation is possible
                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Simulation has not been run. Please add processes and start computation.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Clear previous output
                resultArea.setText("");

                // Prepare output
                StringBuilder output = new StringBuilder();
                output.append(String.format("%-10s %-10s %-10s %-20s %-20s %-20s%n",
                        "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time"));

                for (Process process : result) {
                    output.append(String.format("P%-9d %-10.2f %-10.2f %-20.2f %-20.2f %-20.2f%n",
                            process.getID(), process.getArrivalTime(), process.getBurstTime(),
                            process.getCompletionTime(), process.getTurnAroundTime(), process.getWaitingTime()));
                }

                // Calculate Totals and Averages
                double totalCompletionTime = this.rr.getTotalCompletionTime();
                double totalTurnAroundTime = this.rr.getTotalTurnAroundTime();
                double totalWaitingTime = this.rr.getTotalWaitingTime();

                double averageCompletionTime = totalCompletionTime / result.size();
                double averageTurnAroundTime = totalTurnAroundTime / result.size();
                double averageWaitingTime = totalWaitingTime / result.size();

                // Append summary
                output.append("\nSummary:\n");
                output.append(String.format("%-20s: %-10.2f %-20s: %-10.2f%n",
                        "Total Completion Time", totalCompletionTime, "Average Completion Time", averageCompletionTime));
                output.append(String.format("%-20s: %-10.2f %-20s: %-10.2f%n",
                        "Total Turn Around Time", totalTurnAroundTime, "Average Turn Around Time", averageTurnAroundTime));
                output.append(String.format("%-20s: %-10.2f %-20s: %-10.2f%n",
                        "Total Waiting Time", totalWaitingTime, "Average Waiting Time", averageWaitingTime));

                // Display the output in the JTextArea
                resultArea.setText(output.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An error occurred during simulation: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new round_robin().setVisible(true));
    }
}



