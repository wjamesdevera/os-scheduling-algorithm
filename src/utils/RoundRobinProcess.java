package utils;

public class RoundRobinProcess extends Process{
    private double remainingBurstTime;

    public RoundRobinProcess(int ID, double arrivalTime, double burstTime) {
        super(ID, arrivalTime, burstTime);
    }
}
