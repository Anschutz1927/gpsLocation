package by.black_pearl.cheloc.location;

public class SettsServiceLocationsCounter {
    private int count;

    public SettsServiceLocationsCounter() {
        this.count = 0;
    }

    public int getCount() {
        return this.count;
    }

    public void setCountToZero() {
        this.count = 0;
    }

    public void addCount() {
        this.count++;
    }
}
