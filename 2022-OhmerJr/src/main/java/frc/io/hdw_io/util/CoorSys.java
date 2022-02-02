package frc.io.hdw_io.util;

public class CoorSys {
    // Hardware
    private NavX navX;
    private Whl_Encoder whlEnc_L;
    private Whl_Encoder whlEnc_R;

    // XY Coordinates
    private double prstDist;     //Present distance traveled since last reset.
    private double prvDist;      //previous distance traveled since last reset.
    private double deltaD;       //Distance traveled during this period.
    private double coorX = 0;    //Calculated X (Left/Right) coordinate on field
    private double coorY = 0;    //Calculated Y (Fwd/Bkwd) coordinate on field.
    private double coorX_OS = 0; //X offset.  Added to coorX before returning getCoor
    private double coorY_OS = 0; //Y offset.  Added to coorY before returning getCoor

    /**
     * Constructor to use navX and wheel encoders to calc distance & XY coordinates.
     * @param hdg navX
     * @param left wheel encaoder
     * @param right wheel encoder
     */
    public CoorSys(NavX hdg, Whl_Encoder left, Whl_Encoder right){
        navX = hdg;
        whlEnc_L = left;
        whlEnc_R = right;
    }

    /**Reset left & right encoders to 0.  Feet calc reads 0 */
    public void drvFeetRst() { whlEnc_L.reset(); whlEnc_R.reset(); }
    /**Use L/R encoders to calc average distance */
    public double drvFeet() { return (whlEnc_L.feet() + whlEnc_R.feet()) / 2.0; }

    /**Calculates the XY coordinates by taken the delta distance and applying the sinh/cosh 
     * of the gyro heading.
     * <p>Initialize by calling resetLoc.
     * <p>Needs to be called periodically from IO.update called in robotPeriodic in Robot.
     */
    public void update(){
        // prstDist = (drvEnc_L.feet() + drvEnc_R.feet())/2;   //Distance since last reset.
        prstDist = drvFeet();   //Distance since last reset.
        deltaD = prstDist - prvDist;                        //Distancce this pass
        prvDist = prstDist;                                 //Save for next pass

        //If encoders are reset by another method, may cause large deltaD.
        //During testing deltaD never exceeded 0.15 on a 20mS update.
        if (Math.abs(deltaD) > 0.2) deltaD = 0.0;       //Skip this update if too large.

        if (Math.abs(deltaD) > 0.0){    //Deadband for encoders if needed (vibration?).  Presently set to 0.0
            coorY += deltaD * Math.cos(Math.toRadians(navX.getAngle())) * 1.0;
            coorX += deltaD * Math.sin(Math.toRadians(navX.getAngle())) * 1.1;
        }
    }

    /**Reset the location on the field to 0.0, 0.0.
     * If needed navX.Reset must be called separtely.
     */
    public void reset(){
        // IO.navX.reset();
        whlEnc_L.reset();
        whlEnc_R.reset();
        coorX = 0;
        coorY = 0;
        prstDist = (whlEnc_L.feet() + whlEnc_R.feet())/2;
        prvDist = prstDist;
        
        deltaD = 0;
    }
    
    /**
     * @param x Value to added to coorX before returning getCoorXY.
     * @param y Value to added to coorY before returning getCoorXY.
     */
    public void setXY_OS(double x, double y){coorX_OS = x;   coorY_OS = y;}
    /**
     * @param xy Values to added to coorX, [0] & Y [1] before returning getCoorXY.
     */
    public void setXY_OS(double[] xy){coorX_OS = xy[0];   coorY_OS = xy[1];}
    /**
     * @param x Value to added to coorX before returning getCoorXY.
     */
    public void setX_OS(double x){coorX_OS = x;}
    /**
     * @param x Value to added to coorX before returning getCoorXY.
     */
    public void setY_OS(double y){coorY_OS = y;}

    /**
     * @return an array of the calculated X and Y coordinates on the field since the last reset.
     */
    public double[] get(){
        double[] coorXY = {coorX + coorX_OS, coorY + coorY_OS};
        return coorXY;
    }

    /**
     * @return the calculated X (left/right) coordinate on the field since the last reset.
     */
    public double getX(){
        return coorX + coorX_OS;
    }

    /**
     * @return the calculated Y (fwd/bkwd) coordinate on the field since the last reset.
     */
    public double getY(){
        return coorY + coorY_OS;
    }

    /**
     * @return the calculated Y (fwd/bkwd) coordinate on the field since the last reset.
     */
    public double getDeltaD(){
        return deltaD;
    }

}
