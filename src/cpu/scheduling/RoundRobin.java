package cpu.scheduling;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import utils.Process;

public class RoundRobin {
    private static double quanta;
    private final ArrayList<Process> processes;
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final ArrayList<Process> completedProcess = new ArrayList<>();

    public RoundRobin(ArrayList<Process> processes) {
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
                nextProcess.setBurstTime(nextProcess.getBurstTime() - quanta);
                if (nextProcess.getBurstTime() == 0) {
                    time += quanta;
                    completedProcess.add(nextProcess);
                } else if (nextProcess.getBurstTime() < 0) {
                    time += Math.abs(nextProcess.getBurstTime());
                    completedProcess.add(nextProcess);
                } else {
                    time += quanta;
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



//    public static void main(String[] args) {
//        // Create a Scanner object for user input
//        Scanner inp = new Scanner(System.in);
//
//        // Input time quantum and number of processes
//        System.out.print("Enter the time quanta: ");
//        int tq = inp.nextInt();  // Time quantum for Round Robin
//        System.out.print("Enter the number of processes: ");
//        int n = inp.nextInt();   // Number of processes
//
//        int[] arrival = new int[n];  // Arrival time for each process
//        int[] burst = new int[n];    // Burst time for each process
//        int[] wait = new int[n];     // Wait time for each process
//        int[] turn = new int[n];     // Turnaround time for each process
//        int[] completion = new int[n]; // Completion time for each process
//        boolean[] complete = new boolean[n]; // Track completion status of processes
//        int[] remainingBurst = new int[n];  // Remaining burst time for each process
//
//        int timer = 0;  // Current time (clock)
//        int ganttIndex = 0;  // For Gantt chart tracking
//        int[] ganttChart = new int[100];  // Array to store the Gantt chart (sequence of processes)
//
//        // Input Arrival and Burst times
//        System.out.print("Enter the arrival time of the processes: ");
//        for (int i = 0; i < n; i++) arrival[i] = inp.nextInt();
//
//        System.out.print("Enter the burst time of the processes: ");
//        for (int i = 0; i < n; i++) {
//            burst[i] = inp.nextInt();  // Store the original burst times
//            remainingBurst[i] = burst[i];  // Initialize remaining burst time
//        }
//
//        // Queue for managing ready processes
//        Queue<Integer> readyQueue = new LinkedList<>();
//        boolean[] isInQueue = new boolean[n]; // To track processes that are already in the queue
//
//        // Process the execution
//        while (true) {
//            // Add processes to the ready queue when their arrival time is reached
//            for (int i = 0; i < n; i++) {
//                if (arrival[i] <= timer && !isInQueue[i] && remainingBurst[i] > 0) {
//                    readyQueue.add(i);
//                    isInQueue[i] = true;
//                }
//            }
//
//            if (readyQueue.isEmpty()) {
//                // If no process is in the queue, just increment the timer
//                timer++;
//                continue;
//            }
//
//            // Process from the queue
//            int currentProcess = readyQueue.poll();
//            ganttChart[ganttIndex++] = currentProcess; // Record in Gantt chart
//            int timeSpent = Math.min(tq, remainingBurst[currentProcess]);
//            remainingBurst[currentProcess] -= timeSpent; // Decrease remaining burst time
//            timer += timeSpent; // Increment global time
//
//            // If the process is complete, record the completion time and calculate TAT
//            if (remainingBurst[currentProcess] == 0 && !complete[currentProcess]) {
//                complete[currentProcess] = true;
//                completion[currentProcess] = timer; // Completion time is the current time
//                turn[currentProcess] = completion[currentProcess] - arrival[currentProcess]; // Turnaround time
//                wait[currentProcess] = turn[currentProcess] - burst[currentProcess]; // Wait time
//            }
//
//            // Add process back to the queue if it's not completed
//            if (remainingBurst[currentProcess] > 0) {
//                readyQueue.add(currentProcess);
//            }
//
//            // Exit condition: If all processes are complete, break out of the loop
//            boolean allComplete = true;
//            for (int i = 0; i < n; i++) {
//                if (!complete[i]) {
//                    allComplete = false;
//                    break;
//                }
//            }
//            if (allComplete) {
//                break;
//            }
//        }
//
//        // Output Results
//        System.out.println("\nProcess   Arrival Time   Burst Time   Completion Time   Turnaround Time   Waiting Time");
//        float  avgComp = 0, avgTurn = 0, avgWait = 0;
//        for (int i = 0; i < n; i++) {
//            System.out.printf("%d             %d             %d           %d           %d                %d\n",
//                    i + 1, arrival[i], burst[i], completion[i], turn[i], wait[i]);
//            avgComp += completion[i];
//            avgTurn += turn[i];
//            avgWait += wait[i];
//
//        }
//
//        // Average times
//        System.out.printf("\nAverage CT: %.2f\nAverage TAT: %.2f\nAverage WT: %.2f\n",
//                avgComp / n, avgTurn / n, avgWait / n);
//
//        // Output the Gantt chart
//        System.out.println("\nGantt Chart:");
//        for (int i = 0; i < ganttIndex; i++) {
//            System.out.print("P" + (ganttChart[i] + 1) + " ");
//        }
//    }
}

