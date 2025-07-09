public class LLaCoordinate {

    private double epochTime;
    private double latDegree;
    private double lonDegree;
    private double altitudeMeters;

    public LLaCoordinate(double epochTime,double latDegree, double lonDegree, double altitudeMeters) {
        this.epochTime = epochTime;
        this.latDegree = latDegree;
        this.lonDegree = lonDegree;
        this.altitudeMeters = altitudeMeters;
    }

    public double getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(double epochTime) {
        this.epochTime = epochTime;
    }

    public double getLatDegree() {
        return latDegree;
    }

    public void setLatDegree(double latDegree) {
        this.latDegree = latDegree;
    }

    public double getLonDegree() {
        return lonDegree;
    }

    public void setLonDegree(double lonDegree) {
        this.lonDegree = lonDegree;
    }

    public double getAltitudeMeters() {
        return altitudeMeters;
    }

    public void setAltitudeMeters(double altitudeMeters) {
        this.altitudeMeters = altitudeMeters;
    }



}
