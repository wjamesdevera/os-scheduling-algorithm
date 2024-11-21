package utils;

public class Process {
    private int ID;
    private double arrivalTime;
    private double burstTime;
    private double completionTime;
    private double turnAroundTime;
    private double waitingTime;

    public Process(int ID, double arrivalTime, double burstTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public double getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(double turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public double getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(double completionTime) {
        this.completionTime = completionTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(double burstTime) {
        this.burstTime = burstTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Process ID: " + this.ID;
    }
}
