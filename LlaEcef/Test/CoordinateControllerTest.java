import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CoordinateControllerTest {

    @Test
    public void covertLlaToEcef() {

        double epochTime = 1532332859.04;
        final double a = 6378137;

        double lat = 34.0522;
        double lon = -118.2437;
        double alt = 100.00;
        CoordinateController coordinateController = new CoordinateController();
        EcefCoordinate ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertEquals(-2503396.5198597223,ecefCoordinateTest.getX(),0);
        assertEquals(-4660276.422746513,ecefCoordinateTest.getY(),0);
        assertEquals(3551301.35408728,ecefCoordinateTest.getZ(),0);

        //DEBUG tested an invalid value assertEquals(-1503396.5198597223,ecefCoordinateTest.getX(),0);
        lat = 0;
        lon = 0;
        alt = 0;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);


        //Case for equator and prime meridian at sea level
        assertEquals(6378137.0,ecefCoordinateTest.getX(),0);
        assertEquals(0.0,ecefCoordinateTest.getY(),0);
        assertEquals(0.0,ecefCoordinateTest.getZ(),0);

        //Edge chase at 90 lat (North pole)
        lat = 90;
        lon = 0;
        alt = 0;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertEquals(0.0,ecefCoordinateTest.getX(),1e-3); //we need the delta because this will return 3.918620924814471E-10 (close to zero)
        assertEquals(0.0,ecefCoordinateTest.getY(),0);
        assertEquals(6356752.31424518,ecefCoordinateTest.getZ(),0);

        //Edge chase at 90 lat (South pole)
        lat = -90;
        lon = 0;
        alt = 0;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertEquals(0.0,ecefCoordinateTest.getX(),1e-3); //we need the delta because this will return 3.918620924814471E-10 (close to zero)
        assertEquals(0.0,ecefCoordinateTest.getY(),0);
        assertEquals(-6356752.31424518,ecefCoordinateTest.getZ(),0);


        //Longitude edge cases +/- 180 should be the same
        lat = 0;
        lon = 180;
        alt = 0;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertEquals(-6378137.0,ecefCoordinateTest.getX(),0);
        assertEquals(0.0,ecefCoordinateTest.getY(),1e-3); //we need the delta because this will return 7.810965061573302E-10 (close to zero)
        assertEquals(0.0,ecefCoordinateTest.getZ(),0);

        lat = 0;
        lon = -180;
        alt = 0;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertEquals(-6378137.0,ecefCoordinateTest.getX(),0);
        assertEquals(0.0,ecefCoordinateTest.getY(),1e-3); //we need the delta because this will return 7.810965061573302E-10 (close to zero)
        assertEquals(0.0,ecefCoordinateTest.getZ(),0);


        //Test altitude
        lat = 0;
        lon = 0;
        alt = 100;

        ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);

        assertTrue(ecefCoordinateTest.getX() > a);

        //test invalid value
        lat = 100;
        lon = 0;
        alt = 0;


        try{
            ecefCoordinateTest = coordinateController.covertLlaToEcef(epochTime,lat,lon,alt);
            fail("IllegalArgumentException Expected");
        }catch (IllegalArgumentException e){
            //If catch occurs this is a pass
        }

    }


    @Test
    public void calculateEcefVelocities() {

        //Basic example (1 second differnce)
        double epochTime1 = 1532332859.04;
        double epochTime2 = 1532332860.04;
        EcefCoordinate ecefCoordinate1 = new EcefCoordinate (epochTime1, 1000.00, 2000.00,3000);
        EcefCoordinate ecefCoordinate2 = new EcefCoordinate (epochTime2, 1000.00, 4000.00,6000);
        ArrayList<EcefCoordinate> ecefCoordinates = new ArrayList<>();
        ecefCoordinates.add(ecefCoordinate1);
        ecefCoordinates.add(ecefCoordinate2);

        CoordinateController coordinateController = new CoordinateController();
        ArrayList<EcefVelocity> ecefVelocityArray = coordinateController.calculateEcefVelocities(ecefCoordinates);

        //Index 1, because at index 0 we have (0,0,0)
        assertEquals(0,ecefVelocityArray.get(1).getVx(),0);
        assertEquals(2000,ecefVelocityArray.get(1).getVy(),0);
        assertEquals(3000,ecefVelocityArray.get(1).getVz(),0);

        //Bad data where the data shows movement but 0 movement
        epochTime1 = 1532332859.04;
        epochTime2 = 1532332859.04;
        ecefCoordinate1 = new EcefCoordinate (epochTime1, 1000.00, 2000.00,3000);
        ecefCoordinate2 = new EcefCoordinate (epochTime2, 1000.00, 4000.00,6000);
        ecefCoordinates = new ArrayList<>();
        ecefCoordinates.add(ecefCoordinate1);
        ecefCoordinates.add(ecefCoordinate2);


        try{
            coordinateController = new CoordinateController();
            ecefVelocityArray = coordinateController.calculateEcefVelocities(ecefCoordinates);
            fail("IllegalArgumentException Expected");
        }catch (IllegalArgumentException e){
            //If catch occurs this is a pass
        }


    }

    @Test
    public void calculateInterpolatingVelocities() {

        double epochTime = 1532332862;

        //Simple Example and using the center point for easy calculation
        EcefVelocity ecefVelocity0 = new EcefVelocity(0,0,0,1532332859);
        EcefVelocity ecefVelocity1 = new EcefVelocity(1000.00,0,0,1532332861);
        EcefVelocity ecefVelocity2 = new EcefVelocity(1000.00,1000.00,0,1532332863);
        EcefVelocity ecefVelocity3 = new EcefVelocity(1000.00,1000.00,1000.00,1532332865);

        ArrayList<EcefVelocity> ecefVelocitiesKnownTest = new ArrayList<>();
        ecefVelocitiesKnownTest.add(ecefVelocity0);
        ecefVelocitiesKnownTest.add(ecefVelocity1);
        ecefVelocitiesKnownTest.add(ecefVelocity2);
        ecefVelocitiesKnownTest.add(ecefVelocity3);

        CoordinateController coordinateController = new CoordinateController();
        EcefVelocity ecefVelocity = coordinateController.calculateInterpolatingVelocities(ecefVelocitiesKnownTest,epochTime);


        assertEquals(1000.00,ecefVelocity.getVx(),0);
        assertEquals(500.00,ecefVelocity.getVy(),0);
        assertEquals(0.00,ecefVelocity.getVz(),0);


        //Vx: 1000.0        (no movement in this direction so remains 1000.00)
        //Vy: 500.0         (This is expected which is half the distance)
        //Vz: 0.0           (no movement in this direction)


    }


}