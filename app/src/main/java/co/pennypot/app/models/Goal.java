package co.pennypot.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Goal implements Parcelable {

    private String name;

    private int balance;

    private int target;

    private int id;

    public Goal() { }

    public Goal(String name, int balance, int target) {
        this.name = name;
        this.balance = balance;
        this.target = target;
    }

    public int getProgressPercentage() {
        return (int) Math.round(getBalance() / (1.0 * getTarget()) * 100);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    /** Parcelable Implementation */

    private Goal(Parcel in) {
        this.setId(in.readInt());
        this.setName(in.readString());
        this.setBalance(in.readInt());
        this.setTarget(in.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(balance);
        dest.writeInt(target);
    }

    public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel source) {
            return new Goal(source);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };
}
