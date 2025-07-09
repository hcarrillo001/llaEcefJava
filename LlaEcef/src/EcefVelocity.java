public class EcefVelocity {

    private double vx;
    private double vy;
    private double vz;

    // This will make it easier to print all the solutions
    private double x1 = 0;
    private double y1 = 0;
    private double z1 = 0;
    private double x2 = 0;
    private double y2 =0;
    private double z2 =0;

    private double deltaTime;
    //use leading second coordinate time
    private double epochTime;



    public EcefVelocity(double vx, double vy, double vz, double x1, double y1, double z1, double x2, double y2, double z2 ,
                        double deltaTime, double epochTime) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.deltaTime = deltaTime;
        this.epochTime = epochTime;
    }

    public EcefVelocity(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public EcefVelocity(double vx, double vy, double vz, double epochTime) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.epochTime = epochTime;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVz() {
        return vz;
    }

    public void setVz(double vz) {
        this.vz = vz;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getZ1() {
        return z1;
    }

    public void setZ1(double z1) {
        this.z1 = z1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getZ2() {
        return z2;
    }

    public void setZ2(double z2) {
        this.z2 = z2;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public double getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(double epochTime) {
        this.epochTime = epochTime;
    }
}
