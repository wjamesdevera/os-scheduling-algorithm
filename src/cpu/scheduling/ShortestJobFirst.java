package cpu.scheduling;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class ShortestJobFirst {
    // test comments

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.print("Number of Process/es: ");

        int process = input.nextInt();
        int time = 0;
        int processed = 0;
        int[] completionTime = new int[process];
        int[] waitingTime = new int[process];
        int[] turnAroundTime = new int[process];
        int totalCT = 0;
        int totalTAT = 0;
        int totalWT = 0;
        double AveCT;
        double AveTAT;
        double AveWT;
        boolean[] completeProcess = new boolean[process];
        int[][] SJFMatrix = new int[process][3];


        //Get Arrival Time and assign it to a Process
        System.out.println("Arrival Time:");
       for(int i=0; i < process; i++){
            System.out.print("AT" + (i+1) + ": ");
            SJFMatrix[i][1] = input.nextInt();
            SJFMatrix[i][0] = i + 1;

        }

       //Get Burst Time
        System.out.println("Burst Time:");
        for(int i=0; i < process; i++){
            System.out.print("BT" + (i+1) + ": ");
            SJFMatrix[i][2] = input.nextInt();

        }

        //Check if all processes were processed
       while(processed < process){
           int index = -1;
           int minBurstTime = Integer.MAX_VALUE;

           for (int i = 0; i < process; i++) {

               //checks if the process is not completed and if it has already arrived
               if(!completeProcess[i] && SJFMatrix[i][1] <= time) {
                   if(SJFMatrix[i][2] < minBurstTime) {
                       minBurstTime = SJFMatrix[i][2]; //minBurstTime changes if the current process' burst is smaller
                       index = i; //index changes to the number of the current process
                   }
               }
           }
               if(index == -1) {
                   time++; //time increments if there is no process in ready queue
               } else {

                   time += SJFMatrix[index][2]; //computes for completion time
                   completionTime[index] = time;
                   turnAroundTime[index] = completionTime[index] - SJFMatrix[index][1]; //computes for turn around time
                   waitingTime[index] = turnAroundTime[index] - SJFMatrix[index][2]; //computes for waiting time
                   completeProcess[index] = true; //the current process is now processed.
                   processed++;
               }
       }


        //displays SJF in table format
        System.out.println(String.format("%-10s %-10s %-10s %-20s %-20s %-20s",
                "Process", "Arrival", "Burst", "Completion Time", "Turn Around Time", "Waiting Time"));

        for (int i = 0; i < process; i++) {
            System.out.println(String.format("P%-9d %-10d %-10d %-20d %-20d %-20d",
                    SJFMatrix[i][0], SJFMatrix[i][1], SJFMatrix[i][2],
                    completionTime[i], turnAroundTime[i], waitingTime[i]));
        }


        //gets the total
        for (int i = 0; i < process; i++) {
            totalCT += completionTime[i];
            totalTAT += turnAroundTime[i];
            totalWT += waitingTime[i];
        }
        //rounds off in 2 decimal places
        AveCT = Double.parseDouble(String.format("%.2f", (double) totalCT / process));
        AveTAT = Double.parseDouble(String.format("%.2f", (double) totalTAT / process));
        AveWT = Double.parseDouble(String.format("%.2f", (double) totalWT / process));

        //prints the final ave and total
        System.out.println("Total CT: " + totalCT + " | " + "Ave CT: " + AveCT);
        System.out.println("Total TAT: " + totalTAT + " | " + "Ave TAT: " + AveTAT);
        System.out.println("Total WT: " + totalWT + " | " + "Ave WT: " + AveWT);

    }


}