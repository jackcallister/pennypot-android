package co.pennypot.app.models;

public class Goal {

    private String name;

    private int balance;

    private int target;

    public Goal(String name, int balance, int target) {
        this.name = name;
        this.balance = balance;
        this.target = target;
    }

    public int getProgressPercentage() {
        return (int) Math.round(getBalance() / (1.0 * getTarget()) * 100);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

}
