import cpu.scheduling.ShortestJobFirst;
import utils.Process;
import java.util.Scanner;


public class Main {
    private static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        printWelcomeMessage();
        String input = requestUserInput();
    }

    private static void printWelcomeMessage() {
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
}