import java.util.ArrayList;
import utils.Process;

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 3, 2));
        processes.add(new Process(2, 4, 3));
        processes.add(new Process(3, 2, 4));
        processes.add(new Process(4, 2, 2));
    }
}