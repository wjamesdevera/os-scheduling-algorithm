import cpu.scheduling.RoundRobin;
import cpu.scheduling.ShortestJobFirst;
import utils.Process;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 3, 4));
        processes.add(new Process(2, 5, 9));
        processes.add(new Process(3, 8, 4));
        processes.add(new Process(4, 0, 7));
        processes.add(new Process(5, 12, 6));
        RoundRobin rr = new RoundRobin(processes, 3);
        rr.simulate();
        rr.printCompletedProcess();
    }
}