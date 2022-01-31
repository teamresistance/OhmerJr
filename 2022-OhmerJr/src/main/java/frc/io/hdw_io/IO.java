package frc.io.hdw_io;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class IO {
    // Drive
    public static WPI_TalonSRX drvTSRX_L = new WPI_TalonSRX(56); // Cmds left wheels. Includes encoders
    public static WPI_TalonSRX drvTSRX_R = new WPI_TalonSRX(57); // Cmds right wheels. Includes encoders
    public static final double drvTPF_L = 433.20; // 1024 t/r (0.5' * 3.14)/r 9:60 gr
    public static final double drvTPF_R = 433.20; // 1024 t/r (0.5' * 3.14)/r 9:60 gr
    public static Whl_Encoder drvEnc_L = new Whl_Encoder(drvTSRX_L, drvTPF_L);  //Interface for feet, ticks, reset
    public static Whl_Encoder drvEnc_R = new Whl_Encoder(drvTSRX_R, drvTPF_R);
    public static void drvFeetRst() { drvEnc_L.reset(); drvEnc_R.reset(); }
    public static double drvFeet() { return (drvEnc_L.feet() + drvEnc_R.feet()) / 2.0; }

    // Assignments used by DiffDrv. Slaves sent same command.  Slaves set to follow Masters in IO.
    public static DifferentialDrive diffDrv_M = new DifferentialDrive(IO.drvTSRX_L, IO.drvTSRX_R);

    // navX
    public static NavX navX = new NavX();

    // PDP
    public static PowerDistribution pdp = new PowerDistribution(0,ModuleType.kCTRE);

    // Initialize any hardware here
    public static void init() {
        drvsInit();
        SmartDashboard.putBoolean("Coor/Reset", false);
    }

    public static void drvsInit() {
        drvTSRX_L.configFactoryDefault();
        drvTSRX_R.configFactoryDefault();
        drvTSRX_L.setInverted(false); // Inverts motor direction and encoder if attached
        drvTSRX_R.setInverted(true); // Inverts motor direction and encoder if attached
        drvTSRX_L.setSensorPhase(false); // Adjust this to correct phasing with motor
        drvTSRX_R.setSensorPhase(false); // Adjust this to correct phasing with motor
        drvTSRX_L.setNeutralMode(NeutralMode.Brake); // change it back
        drvTSRX_R.setNeutralMode(NeutralMode.Brake); // change it back
    }

    public static void update() {
        coorUpdate();
        
        SmartDashboard.putNumber("Coor/X", coorX);
        SmartDashboard.putNumber("Coor/Y", coorY);
        SmartDashboard.putNumber("Coor/EncoderL", drvTSRX_L.getSelectedSensorPosition());
        SmartDashboard.putNumber("Coor/EncoderR", drvTSRX_R.getSelectedSensorPosition());
        if (SmartDashboard.getBoolean("Coor/Reset", false)) {
            coorReset();
            SmartDashboard.putBoolean("Coor/Reset", false);
        }
        SmartDashboard.putNumber("NavX/Heading A", navX.getAngle());  //Z continueous
        SmartDashboard.putNumber("NavX/Heading Y", navX.getYaw());    //Z -180 to 180
        SmartDashboard.putNumber("NavX/Heading Z", navX.getRawGyroZ());//Z raw vel deg/sec
        SmartDashboard.putNumber("NavX/Heading A", navX.getPitch());  //X? -180 to 180
        SmartDashboard.putNumber("NavX/Heading Y", navX.getRoll());   //X? -180 to 180
    }


    //--------------------  XY Coordinates -----------------------------------
    private static double prstDist;     //Present distance traveled since last reset.
    private static double prvDist;      //previous distance traveled since last reset.
    private static double deltaD;       //Distance traveled during this period.
    private static double coorX = 0;    //Calculated X (Left/Right) coordinate on field
    private static double coorY = 0;    //Calculated Y (Fwd/Bkwd) coordinate on field.
    
    /**Calculates the XY coordinates by taken the delta distance and applying the sinh/cosh 
     * of the gyro heading.
     * <p>Initialize by calling resetLoc.
     * <p>Needs to be called periodically from IO.update called in robotPeriodic in Robot.
     */
    public static void coorUpdate(){
        // prstDist = (drvEnc_L.feet() + drvEnc_R.feet())/2;   //Distance since last reset.
        prstDist = drvFeet();   //Distance since last reset.
        deltaD = prstDist - prvDist;                        //Distancce this pass
        prvDist = prstDist;                                 //Save for next pass

        //If encoders are reset by another method, may cause large deltaD.
        //During testing deltaD never exceeded 0.15 on a 20mS update.
        if (Math.abs(deltaD) > 0.2) deltaD = 0.0;       //Skip this update if too large.

        if (Math.abs(deltaD) > 0.0){    //Deadband for encoders if needed (vibration?).  Presently set to 0.0
            coorY += deltaD * Math.cos(Math.toRadians(IO.navX.getAngle())) * 1.0;
            coorX += deltaD * Math.sin(Math.toRadians(IO.navX.getAngle())) * 1.1;
        }
    }

    /**Reset the location on the field to 0.0, 0.0.
     * If needed navX.Reset must be called separtely.
     */
    public static void coorReset(){
        // IO.navX.reset();
        drvEnc_L.reset();
        drvEnc_R.reset();
        coorX = 0;
        coorY = 0;
        prstDist =
        
         (drvEnc_L.feet() + drvEnc_R.feet())/2;
        prvDist = prstDist;
        
        deltaD = 0;
    }
    

    /**
     * @return an array of the calculated X and Y coordinate on the field since the last reset.
     */
    public static double[] getCoor(){
        double[] coorXY = {coorX, coorY};
        return coorXY;
    }

    /**
     * @return the calculated X (left/right) coordinate on the field since the last reset.
     */
    public static double getCoorX(){
        return coorX;
    }

    /**
     * @return the calculated Y (fwd/bkwd) coordinate on the field since the last reset.
     */
    public static double getCoorY(){
        return coorY;
    }

    /**
     * @return the calculated Y (fwd/bkwd) coordinate on the field since the last reset.
     */
    public static double getDeltaD(){
        return deltaD;
    }
}
