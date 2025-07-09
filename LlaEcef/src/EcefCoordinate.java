public class EcefCoordinate {

    //we want to give it the epoch time as well
    private double epochTime;
    private double x;
    private double y;
    private double z;


    public EcefCoordinate(double epochTime,double x, double y, double z) {
        this.epochTime = epochTime;
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public double getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(double epochTime) {
        this.epochTime = epochTime;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
