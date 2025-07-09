import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CoordinateController {


    //constants
    private static final double a = 6378137;            //semi-major axis
    private static final double b = 6356752.31424518;   //semi-minor axis

    private static double e1 = Math.sqrt( ((a*a) - (b*b))  / ( (a*a))  );   //first eccentricity



    public static void main(String[] args) throws FileNotFoundException {

        /* DEBUG - Leaving this to show some of my early testing for the lla to ecef conversion
        double epochTime = 1532332859.04;
        double lat = 34.0522;
        double lon = -118.2437;
        double alt = 100.00;
        System.out.println("Testing");
        EcefCoordinate ecefCoordinateTest = covertLlaToEcef(epochTime,lat,lon,alt);
         */

        //Get Data from file and return List of LlaCoordinate objects
        String filePath = "resources/data.csv";
        ArrayList<LLaCoordinate> lLaCoordinates = readDataFromFile(filePath);

        ArrayList<EcefCoordinate> ecefCoordinates = new ArrayList<EcefCoordinate>();
        for(LLaCoordinate lLaCoordinate : lLaCoordinates){
            EcefCoordinate ecefCoordinate = covertLlaToEcef(lLaCoordinate.getEpochTime(),lLaCoordinate.getLatDegree(),lLaCoordinate.getLonDegree(),lLaCoordinate.getAltitudeMeters());
            ecefCoordinates.add(ecefCoordinate);
        }

        ArrayList<EcefVelocity> ecefVelocities = calculateEcefVelocities(ecefCoordinates);
        /*DEBUG Leaving this to show my debug process
        for(EcefVelocity ecefVelocity: ecefVelocities){
            System.out.println("EcefVelocity " );
            System.out.println(" " + ecefVelocity.getVx());
            System.out.println(" " + ecefVelocity.getVy());
            System.out.println(" " + ecefVelocity.getVz());
        }
        */

        //Ecef velocity between two points from input file
        //We have to start at one because at index 0 we have velocity 0,0,0
        System.out.println("\n\n***Ecef Coordinates Velocities Between Two points from Input File***");
        for(int i = 1; i < ecefVelocities.size(); i++){

            System.out.println("First Coordinate ");
            System.out.println(" x1: " + ecefVelocities.get(i).getX1());
            System.out.println(" y1: " + ecefVelocities.get(i).getY1());
            System.out.println(" z1: " + ecefVelocities.get(i).getZ1());

            System.out.println("        Ecef Velocity Between both coordinates ");
            System.out.println("            vx: " + ecefVelocities.get(i).getVx() + " meters");
            System.out.println("            vy: " + ecefVelocities.get(i).getVy() + " meters");
            System.out.println("            vz: " + ecefVelocities.get(i).getVz() + " meters");
            System.out.println("            Delta time: " + ecefVelocities.get(i).getDeltaTime() + " seconds");
            System.out.println("            Epoch time: " + ecefVelocities.get(i).getEpochTime() + " seconds");

            System.out.println("Second Coordinate ");
            System.out.println(" x2: " + ecefVelocities.get(i).getX2());
            System.out.println(" y2: " + ecefVelocities.get(i).getY2());
            System.out.println(" z2: " + ecefVelocities.get(i).getZ2());

            System.out.println("");
            System.out.println("");
        }

        //some data to test on smaller predictable numbers
        /*
        EcefVelocity ecefVelocity0 = new EcefVelocity(0,0,0,1532332859);
        EcefVelocity ecefVelocity1 = new EcefVelocity(1000.00,0,0,1532332861);
        EcefVelocity ecefVelocity2 = new EcefVelocity(1000.00,1000.00,0,1532332863);
        EcefVelocity ecefVelocity3 = new EcefVelocity(1000.00,1000.00,1000.00,1532332865);

        ArrayList<EcefVelocity> ecefVelocitiesKnownTest = new ArrayList<>();
        ecefVelocitiesKnownTest.add(ecefVelocity0);
        ecefVelocitiesKnownTest.add(ecefVelocity1);
        ecefVelocitiesKnownTest.add(ecefVelocity2);
        ecefVelocitiesKnownTest.add(ecefVelocity3);

            This test for epoch time = 1532332862 returns the expected result of
            Epoch Time Give: 1.532332862E9
            Vx: 1000.0        (no movement in this direction so remains 1000.00)
            Vy: 500.0         (This is expected which is half the distance)
            Vz: 0.0           (no movement in this direction)

        */


        //Prompt user to get two epochtime to calculate the interpolated velocities
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> inputEpochTimes = getValidatedEpochTimeFromUser(scanner,lLaCoordinates.get(0).getEpochTime(),lLaCoordinates.get(lLaCoordinates.size()-1).getEpochTime());

        //Interpolating velocities for two different times
        System.out.println("***Ecef Velocities for your Epoch Time Inputs");
        for(double epochTime : inputEpochTimes){
            //DEBUG TEST DATA above EcefVelocity ecefVelocityInput = calculateInterpolatingVelocities(ecefVelocitiesKnownTest,epochTime);
            EcefVelocity ecefVelocityInput = calculateInterpolatingVelocities(ecefVelocities,epochTime);
            System.out.println("Epoch Time Give: " + ecefVelocityInput.getEpochTime());
            System.out.println("    Vx: " + ecefVelocityInput.getVx() + " meters");
            System.out.println("    Vy: " + ecefVelocityInput.getVy() + " meters");
            System.out.println("    Vz: " + ecefVelocityInput.getVz() + " meters");
        }

    }


    /**
     * covertLlaToEcef() - Converts Lla Coordinates to Ecef Coodrindates
     * @param epochTime
     * @param lat
     * @param lon
     * @param altitude
     * @return - returns the converted coordinate
     */
    public static EcefCoordinate covertLlaToEcef(double epochTime,double lat, double lon, double altitude){
        //Check for lat/lon bounds
        if(lat < -90.0 || lat > 90){
            throw new IllegalArgumentException("Invalid Latitude range is -90.0 - > 90.0");
    }
        if(lon < -180 || lon > 180){
        throw new IllegalArgumentException("Invalid Longitude range is -180.0 - > 180.0 ");
    }

    //trigonometry functions take radians, conversion from lat/lon degrees is needed
    //SOURCE: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html

    double latRad = Math.toRadians(lat);
    double lonRad = Math.toRadians(lon);
    //Calculate Radius of curvature
    double n = a / Math.sqrt(1 - (e1 * e1) * Math.sin(latRad) * Math.sin(latRad)) ;

    //calculate x,y,z ecef coordinates
    double x = (n + altitude) * Math.cos(latRad) * Math.cos(lonRad);
    double y = (n + altitude) * Math.cos(latRad) * Math.sin(lonRad);
    double z = ( (( ((b*b) / (a*a)) * n ) + altitude ) * Math.sin(latRad) );

        //DEBUG during development, debugging using  print statements and comparing to ecef caculator online
        //DEBUG System.out.println("Ecef Convert");

        //DEBUG System.out.println(" x: " + x);
        //DEBUG System.out.println(" y: " + y);
        //DEBUG System.out.println(" z: " + z);

        EcefCoordinate ecefCoordinate = new EcefCoordinate(epochTime,x,y,z);

        return ecefCoordinate;


    }

    /**
     * readDataFromFile() - reads data from a file data is formated in the following format
     *                      Epoch Time, Latitude, Longitude, altitude (km)
     * @param filePath - file path hard coded. File can be found in the resources directory
     * @return - ArrayList of LlaCordinates
     * @throws FileNotFoundException
     */
    public static ArrayList<LLaCoordinate> readDataFromFile(String filePath) throws FileNotFoundException {
        ArrayList llaCoorindates = new ArrayList();
        String nextLine = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while((nextLine = bufferedReader.readLine()) != null){
                //DEBUG System.out.println("Testing next line: " + nextLine);

                String[] dataSplit = nextLine.split(",");

                /*DEBUG Leaving this to show my thought proceess. Making sure reading from a file was working properly.
                System.out.println("Data");
                System.out.println("  Epoch:" + dataSplit[0] );
                System.out.println("  Lat:" + dataSplit[1] );
                System.out.println("  Lon:" + dataSplit[2] );
                System.out.println("  Alt:" + dataSplit[3] );
                 */


                double epocTime =  Double.parseDouble(dataSplit[0].replace(" ", ""));
                double lat =  Double.parseDouble(dataSplit[1].replace(" ", ""));
                double lon =  Double.parseDouble(dataSplit[2].replace(" ", ""));

                //converting altitude to meters
                double altitude =  Double.parseDouble(dataSplit[3].replace(" ", "")) * 1000;

                LLaCoordinate newLlaCoordinate = new LLaCoordinate(epocTime,lat,lon,altitude);

                llaCoorindates.add(newLlaCoordinate);

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return llaCoorindates;

    }

    /**
     * calculateEcefVelocities() - calculates Ecef Velocities - formula deltaPosition/deltaTime of two points for each x y and z
     * @param ecefCoordinates  - Take an ArrayList of EcefCoodinates
     * @return - Returns the EcefVelocities in an ArrayList
     */
    public static ArrayList<EcefVelocity> calculateEcefVelocities(ArrayList<EcefCoordinate> ecefCoordinates){
        //Formula given as deltaPosition/deltaTime

        //the first Ecef velocity is (0,0,0) - No Movement (before the first coordinate) velocity
        ArrayList<EcefVelocity> ecefVelocities = new ArrayList<EcefVelocity>();

        //first velocity
        ecefVelocities.add(new EcefVelocity(0,0,0,ecefCoordinates.get(0).getEpochTime()));

        //Start loop at the 1 because we want to look ahead
        for(int i = 1; i < ecefCoordinates.size(); i++){
            EcefCoordinate laggingCoordinate = ecefCoordinates.get(i-1);
            EcefCoordinate leadingCoordinate = ecefCoordinates.get(i);

            double deltaTime = leadingCoordinate.getEpochTime() - laggingCoordinate.getEpochTime();
            //DEBUG System.out.println("Delta: " + deltaTime);
            if(deltaTime <= 0){
                throw new IllegalArgumentException("Time Difference delta t must be not be zero or less");
            }
             double vx = (leadingCoordinate.getX() - laggingCoordinate.getX()) / deltaTime;
             double vy = (leadingCoordinate.getY() - laggingCoordinate.getY()) / deltaTime;
             double vz = (leadingCoordinate.getZ() - laggingCoordinate.getZ()) / deltaTime;

             //use leading time as the epoch time for the velocity
             double leadingEpochTime = leadingCoordinate.getEpochTime();

             EcefVelocity ecefVelocity = new EcefVelocity(vx,vy,vz, laggingCoordinate.getX(),laggingCoordinate.getY(),
                        laggingCoordinate.getZ(), leadingCoordinate.getX(),leadingCoordinate.getY(), leadingCoordinate.getZ(),deltaTime, leadingEpochTime);
             ecefVelocities.add(ecefVelocity);
        }

        return ecefVelocities;
    }


    /**
     * getValidatedEpochTimeFromUser - takes two inputs from the user. Two epoch times. -1 ones allows user to quit.
     * @param scanner
     * @param minEpochTime
     * @param maxEpochTime
     * @return - the inputs in the form of an ArrayList.
     */
    public static ArrayList<Double> getValidatedEpochTimeFromUser(Scanner scanner, double minEpochTime, double maxEpochTime){
        double epochTimeFirst = 0 ;
        double epochTimeSecond = 0;
        boolean invalidValue = true;
        System.out.println("Please enter two Epoch times between " + Double.toString(minEpochTime) + " and " + Double.toString(maxEpochTime) + " we will calculate the Interpolarity betwen them. Enter the values in order");
        System.out.println("If you enter anything lower than " + Double.toString(minEpochTime) + " your ecef Velocity will be (0,0,0)");
        System.out.println("If you enter anything higher than " + Double.toString(maxEpochTime) + " your ecef Velocity will be the last known ecef velocity available)");
        System.out.println("Enter first Epoch time (or press -1 to quit) ");
        epochTimeFirst = scanner.nextDouble();
        if (epochTimeFirst == -1.0){
            System.out.println("Ending program");
            return null;
        }

        System.out.println("Enter Second Epoch time ");
        epochTimeSecond = scanner.nextDouble();
        if (epochTimeSecond == -1.0){
            System.out.println("Ending program");
            return null;
        }

        ArrayList<Double> inputEpochValues = new ArrayList<>(2);
        inputEpochValues.add(epochTimeFirst);
        inputEpochValues.add(epochTimeSecond);

        return inputEpochValues;

    }

    /**
     * calculateInterpolatingVelocities - Calculates the linear Interpolating Velocity for each user input epoch time
     * @param knownEcefVelocities
     * @param epochTime
     * @return - return the EcefVelocity between two points
     */
    public static EcefVelocity calculateInterpolatingVelocities(ArrayList<EcefVelocity> knownEcefVelocities, double epochTime){
        if(epochTime <= knownEcefVelocities.get(0).getEpochTime()){
            //Here we are at 0,0,0 velocity (start, no movement)
            return knownEcefVelocities.get(0);
        }
        if(epochTime >= knownEcefVelocities.get(knownEcefVelocities.size() - 1).getEpochTime()){
            //Here we are the end, we cannot calculate between two points, return the last of the velocities.
            return knownEcefVelocities.get(knownEcefVelocities.size()-1);
        }
        //index 0 contains velocity 0,0,0, start at index = 1
        for(int i = 1; i < knownEcefVelocities.size(); i++){
            EcefVelocity laggingVelocity = knownEcefVelocities.get(i -1);
            EcefVelocity leadingVelocity = knownEcefVelocities.get(i);
            if(epochTime >= laggingVelocity.getEpochTime() && epochTime <= leadingVelocity.getEpochTime()){

                ////https://en.wikipedia.org/wiki/Linear_interpolation (Linear interpolation as an approximation-section)


                double slope = (epochTime - laggingVelocity.getEpochTime() ) / (leadingVelocity.getEpochTime() - laggingVelocity.getEpochTime());

                double vx = laggingVelocity.getVx() + slope * (leadingVelocity.getVx() - laggingVelocity.getVx());
                double vy = laggingVelocity.getVy() + slope * (leadingVelocity.getVy() - laggingVelocity.getVy());
                double vz = laggingVelocity.getVz() + slope * (leadingVelocity.getVz() - laggingVelocity.getVz());

                EcefVelocity ecefVelocity = new EcefVelocity(vx,vy,vz,epochTime);
                return ecefVelocity;
            }

        }

        return null;
    }



}

