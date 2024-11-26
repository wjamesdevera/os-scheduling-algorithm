package cpu.scheduling;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import utils.Process;

public class RoundRobin {
    private final double quanta;
    private final ArrayList<Process> processes;
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final ArrayList<Process> completedProcess = new ArrayList<>();

    public RoundRobin(ArrayList<Process> processes, double quanta) {
        this.processes = processes;
        this.quanta = quanta;
    }

    public RoundRobin(ArrayList<java.lang.Process> processList, double quantum, double quanta, ArrayList<Process> processes) {
        this.quanta = quanta;
        this.processes = processes;
    }

    public void simulate() {
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
            while(j > 0 && completedProcess.get(j).getID() < completedProcess.get(j-1).getID()) {
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

    public void printCompletedProcess() throws Exception {
        sortCompletedProcessById();
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

