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

    public static void main(String[] args)
    {
//        Scanner input = new Scanner(System.in);
//
//        System.out.print("Number of Process/es: ");
//
//        int process = input.nextInt();
//        int time = 0;
//        int processed = 0;
//        int[] completionTime = new int[process];
//        int[] waitingTime = new int[process];
//        int[] turnAroundTime = new int[process];
//        int totalCT = 0;
//        int totalTAT = 0;
//        int totalWT = 0;
//        double AveCT;
//        double AveTAT;
//        double AveWT;
//        boolean[] completeProcess = new boolean[process];
//        int[][] SJFMatrix = new int[process][3];
//        int PROCESS_ID = 0;
//        int ARRIVAL_TIME = 1;
//        int BURST_TIME = 2;
//
//        //Get Arrival Time and assign it to a Process
//        System.out.println("Arrival Time:");
//        for(int i=0; i < process; i++){
//            System.out.print("AT" + (i+1) + ": ");
//            SJFMatrix[i][ARRIVAL_TIME] = input.nextInt();
//            SJFMatrix[i][PROCESS_ID] = i + 1;
//
//        }
//
//       //Get Burst Time
//        System.out.println("Burst Time:");
//        for(int i=0; i < process; i++){
//            System.out.print("BT" + (i+1) + ": ");
//            SJFMatrix[i][BURST_TIME] = input.nextInt();
//
//        }
//
//        // Check if all processes were processed
//       while(processed < process){
//           int index = -1;
//           int minBurstTime = Integer.MAX_VALUE;
//
//           for (int i = 0; i < process; i++) {
//
//               // checks if the process is not completed and if it has already arrived
//               if(!completeProcess[i] && SJFMatrix[i][ARRIVAL_TIME] <= time) {
//                   if(SJFMatrix[i][BURST_TIME] < minBurstTime) {
//                       minBurstTime = SJFMatrix[i][BURST_TIME]; //minBurstTime changes if the current process' burst is smaller
//                       index = i; //index changes to the number of the current process
//                   }
//               }
//           }
//
//           if(index == -1) {
//               time++; //time increments if there is no process in ready queue
//           } else {
//               time += SJFMatrix[index][BURST_TIME]; //computes for completion time
//               completionTime[index] = time;
//               turnAroundTime[index] = completionTime[index] - SJFMatrix[index][ARRIVAL_TIME]; //computes for turn around time
//               waitingTime[index] = turnAroundTime[index] - SJFMatrix[index][BURST_TIME]; //computes for waiting time
//               completeProcess[index] = true; //the current process is now processed.
//               processed++;
//           }
//       }


        //displays SJF in table format
//        System.out.println(String.format("%-10s %-10s %-10s %-20s %-20s %-20s",
//                "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time"));
//
//        for (int i = 0; i < process; i++) {
//            System.out.println(String.format("P%-9d %-10d %-10d %-20d %-20d %-20d",
//                    SJFMatrix[i][0], SJFMatrix[i][1], SJFMatrix[i][2],
//                    completionTime[i], turnAroundTime[i], waitingTime[i]));
//        }
//
//
//        //gets the total
//        for (int i = 0; i < process; i++) {
//            totalCT += completionTime[i];
//            totalTAT += turnAroundTime[i];
//            totalWT += waitingTime[i];
//        }
//        //rounds off in 2 decimal places
//        AveCT = Double.parseDouble(String.format("%.2f", (double) totalCT / process));
//        AveTAT = Double.parseDouble(String.format("%.2f", (double) totalTAT / process));
//        AveWT = Double.parseDouble(String.format("%.2f", (double) totalWT / process));
//
//        //prints the final ave and total
//        System.out.println("Total CT: " + totalCT + " | " + "Ave CT: " + AveCT);
//        System.out.println("Total TAT: " + totalTAT + " | " + "Ave TAT: " + AveTAT);
//        System.out.println("Total WT: " + totalWT + " | " + "Ave WT: " + AveWT);

    }

    public ArrayList<Process> getCompletedProcess() throws Exception {
        if (completedProcess.isEmpty()) {
            throw new Exception("Simulate the process first");
        }
        return this.completedProcess;
    }
}