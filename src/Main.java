import cpu.scheduling.RoundRobin;
import cpu.scheduling.ShortestJobFirst;
import utils.*;
import utils.Process;

import java.util.ArrayList;
import java.util.Scanner;



public class Main {
    private final static Integer MAX_PROCESS = 10;
    private final static Integer MIN_PROCESS = 2;
    private static final ArrayList<Process> processes = new ArrayList<>();
    private static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        while (true) {
            displayWelcomeMessage();
            String input = requestUserInput();
            switch (input) {
                case "help" -> displayHelp();
                case "sjf" -> sjfMenu();
                case "rr" -> cpuSchedulingMenu();
                case "scan" -> cpuSchedulingMenu();
                case "exit" -> System.exit(1);
            }
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("\u001B[35m\n   ____    _____           ___     __    ______   ____");
        System.out.println("  / __ \\  / ___/          /   |   / /   / ____/  / __ \\");
        System.out.println(" / / / /  \\__ \\          / /| |  / /   / / __   / / / /");
        System.out.println("/ /_/ /  ___/ /         / ___ | / /___/ /_/ /  / /_/ /");
        System.out.println("\\____/  /____/         /_/  |_|/_____/\\____/   \\____/\u001B[37m");
        System.out.println("==============================================================");
        System.out.println("  Welcome to the CPU Scheduling Simulator v1.0");
        System.out.println();
        System.out.println("  Supported Scheduling Algorithms:");
        System.out.println("    🔹 Shortest Job First (SJF)");
        System.out.println("    🔹 Round Robin (RR)");
        System.out.println("    🔹 SCAN Disk Scheduling");
        System.out.println();
        System.out.println("  🚀 Let's get started! Type 'help' for available commands.");
        System.out.println("  🛑 Type 'exit' at any time to quit the application.");
        System.out.println("==============================================================\n");
    }

    private static String requestUserInput() {
        System.out.print(":");
        return scan.nextLine();
    }

    private static void displayHelp() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("=============================================");
        System.out.println("              CPU Scheduling Simulator       ");
        System.out.println("=============================================");
        System.out.println("Available Commands:");
        System.out.println();
        System.out.println("  ▶ help            - Display this help menu.");
        System.out.println("  ▶ sjf             - Simulate the Shortest Job First algorithm.");
        System.out.println("  ▶ rr              - Simulate the Round Robin algorithm.");
        System.out.println("  ▶ scan            - Simulate the SCAN Disk Scheduling algorithm.");
        System.out.println("  ▶ exit            - Quit the application.");
        System.out.println();
        System.out.println("Usage Examples:");
        System.out.println("  Type 'sjf' to start a simulation for the Shortest Job First algorithm.");
        System.out.println("  Type 'exit' to quit the simulator.");
        System.out.println();
        System.out.println("=============================================");
        System.out.println("💡 Tip: Ensure all inputs are provided correctly as prompted.");
        System.out.println("=============================================");
    }

    private static void displayCpuSchedulingMenu() {
        System.out.println("=============================================");
        System.out.println("              CPU Scheduling Simulator       ");
        System.out.println("=============================================");
        System.out.println("Please select an option:");
        System.out.println();
        System.out.println("  1 - Shortest Job First (SJF)");
        System.out.println("      ▶ Shortest Job First (SJF)");
        System.out.println("      ▶ Round Robin (RR)");
        System.out.println();
        System.out.println("  2 - Round Robin (RR)");
        System.out.println("      ▶ SCAN Disk Scheduling");
        System.out.println();
        System.out.println("  3 - Help");
        System.out.println("      ▶ Display this menu.");
        System.out.println();
        System.out.println("  4 - Exit");
        System.out.println("      ▶ Return to main menu.");
        System.out.println();
    }

    private static void sjfMenu() throws Exception {
        Validator processValidator = new ProcessCountValidator(MIN_PROCESS, MAX_PROCESS);
        InputHandler inputHandler = new InputHandler(scan);

        int numberOfProcesses = inputHandler.requestInput(
                "Input the number of processes [" + MIN_PROCESS + "-" + MAX_PROCESS + "]: ",
                processValidator
        );

        int[] arrivalTimes = requestArrivalTime(numberOfProcesses);
        int[] burstTimes = requestBurstTime(numberOfProcesses);
        generateProcessesArray(numberOfProcesses, arrivalTimes, burstTimes);

        ShortestJobFirst sjf = new ShortestJobFirst(processes);
        sjf.simulate();
        sjf.printCompletedProcess();
    }

    private static void rrMenu() {
        Validator processValidator = new ProcessCountValidator(MIN_PROCESS, MAX_PROCESS);
        Validator timeQuantumValidator = new TimeQuantumValidator();
        InputHandler inputHandler = new InputHandler(scan);

        int numberOfProcesses = inputHandler.requestInput(
                "Input the number of processes [" + MIN_PROCESS + "-" + MAX_PROCESS + "]: ",
                processValidator
        );
        int[] arrivalTimes = requestArrivalTime(numberOfProcesses);
        int[] burstTimes = requestBurstTime(numberOfProcesses);
        generateProcessesArray(numberOfProcesses, arrivalTimes, burstTimes);

        int timeQuantum = inputHandler.requestInput(
                "Input time quantum: ",
                timeQuantumValidator
        );
        RoundRobin rr = new RoundRobin(timeQuantum);
    }

    private static int[] requestArrivalTime(int numberOfProcesses) {
        Validator arrivalTimeValidator = new ArrivalTimeValidator();
        InputHandler inputHandler = new InputHandler(scan);
        int[] arrivalTimes = new int[numberOfProcesses];
        System.out.println("Input individual arrival time: ");
        for (int i = 0; i < numberOfProcesses; i++) {
            int processNumber = i + 1;
            int arrivalTime = inputHandler.requestInput(
                    "AT" + processNumber + ":" ,
                    arrivalTimeValidator
            );
            arrivalTimes[i] = arrivalTime;
        }
        return arrivalTimes;
    }

    private static int[] requestBurstTime(int numberOfProcesses) {
        Validator burstTimeValidator = new BurstTimeValidator();
        InputHandler inputHandler = new InputHandler(scan);
        int[] burstTimes = new int[numberOfProcesses];
        System.out.println("Input individual burst time: ");
        for (int i = 0; i < numberOfProcesses; i++) {
            int processNumber = i + 1;
            int burstTime = inputHandler.requestInput(
                    "BT" + processNumber + ":" ,
                    burstTimeValidator
            );
            burstTimes[i] = burstTime;
        }
        return burstTimes;
    }

    private static void generateProcessesArray(int numberOfProcesses, int[] arrivalTimes, int[] burstTimes) {
        for (int i = 0; i < numberOfProcesses; i++) {
            int processNumber = i + 1;
            processes.add(new Process(processNumber, arrivalTimes[i], burstTimes[i]));
        }
    }
}