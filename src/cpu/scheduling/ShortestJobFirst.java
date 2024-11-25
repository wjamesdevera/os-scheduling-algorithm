package cpu.scheduling;
import utils.Process;
import java.util.*;

public class ShortestJobFirst {
    // test comments
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

    public void simulate() {
        sortProcessesByArrivalTime();
        double time = 0;
        while (this.completedProcess.size() < this.processes.size()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && !completedProcess.contains(process) && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }
            Process nextProcess = electNextProcess();
            if (nextProcess == null) {
                time++;
            } else {
                completedProcess.add(nextProcess);
                nextProcess.setCompletionTime(time + nextProcess.getBurstTime());
                nextProcess.setTurnAroundTime(nextProcess.getCompletionTime() - nextProcess.getArrivalTime());
                nextProcess.setWaitingTime(nextProcess.getTurnAroundTime() - nextProcess.getBurstTime());
                readyQueue.remove(nextProcess);
                time += nextProcess.getBurstTime();
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
            System.out.println(String.format("P%-9d %-10.2f %-10.2f %-20.2f %-20.2f %-20.2f",
                    process.getID(), process.getArrivalTime(), process.getBurstTime(),
                    process.getCompletionTime(), process.getTurnAroundTime(), process.getWaitingTime()));
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