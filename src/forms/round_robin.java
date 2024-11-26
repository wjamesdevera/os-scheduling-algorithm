package forms;

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
    private ArrayList<Process> processes = new ArrayList<>();
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final ArrayList<Process> completedProcess = new ArrayList<>();

    public round_robin(ArrayList<Process> processes, double quanta) {
        this.processes = processes;
        this.quanta = quanta;
    }

    public round_robin(ArrayList<Process> processList, double quantum, double quanta, ArrayList<Process> processes) {
        this.quanta = quanta;
        this.processes = processes;
    }

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
                    processes.add(new Process(process, arrivalTime, burstTime, burstTime));
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
//        readyQueue.clear();
//        completedProcess.clear();
        sortProcessesByArrivalTime();
        double time = 0;

        while (this.completedProcess.size() < this.processes.size()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && !completedProcess.contains(process) && !readyQueue.contains(process)) {
                    readyQueue.offer(process);
                }
            }
            Process nextProcess = readyQueue.poll();
            if (nextProcess == null) {
                time++;
            } else {
                nextProcess.setRemainingBurstTime(nextProcess.getRemainingBurstTime() - quanta);
                if (nextProcess.getRemainingBurstTime() == 0) {
                    time += quanta;
                    nextProcess.setCompletionTime(time);
                    nextProcess.setTurnAroundTime(nextProcess.getCompletionTime() - nextProcess.getArrivalTime());
                    nextProcess.setWaitingTime(nextProcess.getTurnAroundTime() - nextProcess.getBurstTime());
                    completedProcess.add(nextProcess);
                } else if (nextProcess.getRemainingBurstTime() < 0) {
                    time += quanta - Math.abs(nextProcess.getRemainingBurstTime());
                    nextProcess.setCompletionTime(time);
                    nextProcess.setTurnAroundTime(nextProcess.getCompletionTime() - nextProcess.getArrivalTime());
                    nextProcess.setWaitingTime(nextProcess.getTurnAroundTime() - nextProcess.getBurstTime());
                    completedProcess.add(nextProcess);
                } else {
                    time += quanta;
                    for (Process process : processes) {
                        if (process.getArrivalTime() <= time && !completedProcess.contains(process) && !readyQueue.contains(process) && process != nextProcess) {
                            readyQueue.offer(process);
                        }
                    }
                    readyQueue.offer(nextProcess);
                }
            }
        }
    }

    private void sortProcessesByArrivalTime() {
        for(int i = 1; i < this.processes.size(); i++) {
            int j = i;
            while(j > 0 && processes.get(j).getArrivalTime() < processes.get(j-1).getArrivalTime()) {
                Process temp = processes.get(j);
                processes.set(j, processes.get(j-1));
                processes.set(j-1, temp);
                j--;
            }
        }
    }

    private void sortCompletedProcessById() {
        for(int i = 1; i < this.completedProcess.size(); i++) {
            int j = i;
            while(j > 0 && completedProcess.get(j).getpNum() < completedProcess.get(j-1).getpNum()) {
                Process temp = completedProcess.get(j);
                completedProcess.set(j, completedProcess.get(j-1));
                completedProcess.set(j-1, temp);
                j--;
            }
        }
    }

    private double getTotalCompletionTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
            total += process.getCompletionTime();
        }
        return total;
    }

    private double getTotalTurnAroundTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
            total += process.getTurnAroundTime();
        }
        return total;
    }

    private double getTotalWaitingTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
            total += process.getWaitingTime();
        }
        return total;
    }

    private void displayOutput() {
//        if (completedProcess.isEmpty()) {
//            System.out.println("Simulation has not been run. Please run the simulation first.");
//            return;
//        }
//
//        // Sort the completed processes by their IDs
//        sortCompletedProcessById();
//
//        // Print Header
//        System.out.printf("%-10s %-10s %-10s %-20s %-20s %-20s%n",
//                "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time");
//
//        // Print Details for Each Process
//        for (Process process : completedProcess) {
//            System.out.printf("P%-9d %-10.2f %-10.2f %-20.2f %-20.2f %-20.2f%n",
//                    process.getpNum(), process.getArrivalTime(), process.getBurstTime(),
//                    process.getCompletionTime(), process.getTurnAroundTime(), process.getWaitingTime());
//        }
//
//        // Calculate Totals and Averages
//        double totalCompletionTime = getTotalCompletionTime();
//        double totalTurnAroundTime = getTotalTurnAroundTime();
//        double totalWaitingTime = getTotalWaitingTime();
//
//        double averageCompletionTime = totalCompletionTime / completedProcess.size();
//        double averageTurnAroundTime = totalTurnAroundTime / completedProcess.size();
//        double averageWaitingTime = totalWaitingTime / completedProcess.size();
//
//        // Print Totals and Averages
//        System.out.println("\nSummary:");
//        System.out.printf("%-20s: %-10.2f %-20s: %-10.2f%n", "Total Completion Time", totalCompletionTime, "Average Completion Time", averageCompletionTime);
//        System.out.printf("%-20s: %-10.2f %-20s: %-10.2f%n", "Total Turn Around Time", totalTurnAroundTime, "Average Turn Around Time", averageTurnAroundTime);
//        System.out.printf("%-20s: %-10.2f %-20s: %-10.2f%n", "Total Waiting Time", totalWaitingTime, "Average Waiting Time", averageWaitingTime);
        startCompute.addActionListener(e -> {
            try {
                // Check if simulation is possible
                if (completedProcess.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Simulation has not been run. Please add processes and start computation.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Clear previous output
                resultArea.setText("");

                // Sort the completed processes by their IDs
                sortCompletedProcessById();

                // Prepare output
                StringBuilder output = new StringBuilder();
                output.append(String.format("%-10s %-10s %-10s %-20s %-20s %-20s%n",
                        "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time"));

                for (Process process : completedProcess) {
                    output.append(String.format("P%-9d %-10.2f %-10.2f %-20.2f %-20.2f %-20.2f%n",
                            process.getpNum(), process.getArrivalTime(), process.getBurstTime(),
                            process.getCompletionTime(), process.getTurnAroundTime(), process.getWaitingTime()));
                }

                // Calculate Totals and Averages
                double totalCompletionTime = getTotalCompletionTime();
                double totalTurnAroundTime = getTotalTurnAroundTime();
                double totalWaitingTime = getTotalWaitingTime();

                double averageCompletionTime = totalCompletionTime / completedProcess.size();
                double averageTurnAroundTime = totalTurnAroundTime / completedProcess.size();
                double averageWaitingTime = totalWaitingTime / completedProcess.size();

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

    // Inner Class to Represent a Process
    static class Process {
        private final int pNum;
        private final double arrivalTime;
        private final double burstTime;
        private double remainingBurstTime;
        private double completionTime;
        private double turnAroundTime;
        private double waitingTime;

        public Process(int pNum, double arrivalTime, double burstTime, double remainingBurstTime) {
            this.pNum = pNum;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingBurstTime = remainingBurstTime;
        }

        public int getpNum() {
            return pNum;
        }

        public double getArrivalTime() {
            return arrivalTime;
        }

        public double getBurstTime() {
            return burstTime;
        }

        public double getRemainingBurstTime() {
            return remainingBurstTime;
        }

        public void setRemainingBurstTime(double remainingBurstTime) {
            this.remainingBurstTime = remainingBurstTime;
        }

        public double getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(double completionTime) {
            this.completionTime = completionTime;
        }

        public double getTurnAroundTime() {
            return turnAroundTime;
        }

        public void setTurnAroundTime(double turnAroundTime) {
            this.turnAroundTime = turnAroundTime;
        }

        public double getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(double waitingTime) {
            this.waitingTime = waitingTime;
        }
    }
}



