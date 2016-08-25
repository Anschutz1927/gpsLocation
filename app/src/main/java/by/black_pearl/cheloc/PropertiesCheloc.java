package by.black_pearl.cheloc;

public class PropertiesCheloc {

    private int serviceRunTime;
    private double updateTime;
    private double speed;
    private double defaultLatitude;
    private double defaultLongtitude;
    private int defaultAltitude;
    private boolean isMockNetworkProvider;

    public PropertiesCheloc() {
        this.serviceRunTime = 0;
        this.updateTime = 399;
        this.speed = 0;
        this.defaultLatitude = 53.923269;
        this.defaultLongtitude = 27.596573;
        this.defaultAltitude = 203;
        this.isMockNetworkProvider = false;
    }

    public void setServiceRunTime(int serviceRunTime) {
        this.serviceRunTime = serviceRunTime;
    }

    public void setUpdateTime(double updateTime) {
        this.updateTime = updateTime;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDefaultLatitude(double defaultLatitude) {
        this.defaultLatitude = defaultLatitude;
    }

    public void setDefaultLongtitude(double defaultLongtitude) {
        this.defaultLongtitude = defaultLongtitude;
    }

    public void setDefaultAltitude(int defaultAltitude) {
        this.defaultAltitude = defaultAltitude;
    }

    public boolean isMockNetworkProvider() {
        return isMockNetworkProvider;
    }
}
