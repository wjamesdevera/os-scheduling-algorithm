package cpu.scheduling;
import utils.Process;
import java.util.*;

public class ShortestJobFirst {
    private final ArrayList<Process> processes;
    private final ArrayList<Process> readyQueue = new ArrayList<>();
    private final ArrayList<Process> completedProcess = new ArrayList<>();

    public ShortestJobFirst(ArrayList<Process> processes) {
        this.processes = processes;
    }

    private Process electNextProcess() {
        double minBurstTime = Double.MAX_VALUE;
        Process electedProcess = null;
        for(Process process: this.readyQueue) {
            if (process.getBurstTime() < minBurstTime) {
                minBurstTime = process.getBurstTime();
                electedProcess = process;
            }
        }
        return electedProcess;
    }

    public ArrayList<Process> simulate() {
        sortProcessesByArrivalTime(); // Ensure processes are sorted by arrival time
        double time = 0;

        while (this.completedProcess.size() < this.processes.size()) {
            // Add processes that have arrived to the ready queue
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && !completedProcess.contains(process) && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            // Elect the next process (shortest burst time)
            Process nextProcess = electNextProcess();

            if (nextProcess == null) {
                // If no process is ready, advance time to the next arrival
                time++;
            } else {
                // Process execution
                readyQueue.remove(nextProcess);
                time += nextProcess.getBurstTime();
                nextProcess.setCompletionTime(time);
                nextProcess.setTurnAroundTime(nextProcess.getCompletionTime() - nextProcess.getArrivalTime());
                nextProcess.setWaitingTime(nextProcess.getTurnAroundTime() - nextProcess.getBurstTime());
                completedProcess.add(nextProcess);
            }
        }

        sortCompletedByProcessID(); // Sort processes by ID for final output
        return this.completedProcess;
    }

    public double getTotalCompletionTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
           total += process.getCompletionTime();
        }
        return total;
    }

    public double getTotalTurnAroundTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
            total += process.getTurnAroundTime();
        }
        return total;
    }

    public double getTotalWaitingTime() {
        double total = 0;
        for (Process process: this.completedProcess) {
            total += process.getWaitingTime();
        }
        return total;
    }

    // Insertion Sort
    private void sortCompletedByProcessID() {
        for(int i = 1; i < this.completedProcess.size(); i++) {
            int j = i;
            while(j > 0 && completedProcess.get(j).getArrivalTime() < completedProcess.get(j-1).getArrivalTime()) {
                Process temp = completedProcess.get(j);
                completedProcess.set(j, completedProcess.get(j-1));
                completedProcess.set(j-1, temp);
                j--;
            }
        }
    }

    // Insertion Sort
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

    public void printCompletedProcess() throws Exception {
        String format = "%s: %.2f %20s: %.2f%n";
        if (completedProcess.isEmpty()) {
            throw new Exception("Simulate the process first");
        }
        System.out.printf("%-10s %-10s %-10s %-20s %-20s %-20s%n",
                "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time");
        for (Process process : completedProcess) {
            System.out.printf("P%-9d %-10.2f %-10.2f %-20.2f %-20.2f %-20.2f%n",
                    process.getID(), process.getArrivalTime(), process.getBurstTime(),
                    process.getCompletionTime(), process.getTurnAroundTime(), process.getWaitingTime());
        }
        double totalCompletionTime = getTotalCompletionTime();
        double totalTurnAroundTime = getTotalTurnAroundTime();
        double totalWaitingTime = getTotalWaitingTime();
        double averageCompletionTime = totalCompletionTime / completedProcess.size();
        double averageTurnAroundTime = totalTurnAroundTime / completedProcess.size();
        double averageWaitingTime = totalWaitingTime / completedProcess.size();
        System.out.printf(format, "Total CT", totalCompletionTime, "Average CT", averageCompletionTime);
        System.out.printf(format, "Total TAT", totalTurnAroundTime, "Average TAT", averageTurnAroundTime);
        System.out.printf(format, "Total WT", totalWaitingTime, "Average WT", averageWaitingTime);
    }
}