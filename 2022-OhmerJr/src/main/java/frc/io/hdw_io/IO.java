package frc.io.hdw_io;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.hdw_io.util.CoorSys;
import frc.io.hdw_io.util.NavX;
import frc.io.hdw_io.util.Whl_Encoder;
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

    // Assignments used by DiffDrv. Slaves sent same command.  Slaves set to follow Masters in IO.
    public static DifferentialDrive diffDrv_M = new DifferentialDrive(IO.drvTSRX_L, IO.drvTSRX_R);

    // navX
    public static NavX navX = new NavX();
    public static CoorSys coorXY = new CoorSys(navX, drvEnc_L, drvEnc_R);

    // PDP
    public static PowerDistribution pdp = new PowerDistribution(0,ModuleType.kCTRE);

    // Initialize any hardware here
    public static void init() {
        drvsInit();
        sdbInit();
    }

    // Update any hardware here
    public static void update() {
        coorXY.update();
        sdbUpdate();
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

    public static void sdbInit() {
        SmartDashboard.putBoolean("Coor/Reset", false);
        SmartDashboard.putNumber("NavX/Angle Adj", 0);
    }

    public static void sdbUpdate() {
        SmartDashboard.putNumber("Coor/X", coorXY.getX());
        SmartDashboard.putNumber("Coor/Y", coorXY.getY());
        SmartDashboard.putNumber("Coor/EncoderL", drvTSRX_L.getSelectedSensorPosition());
        SmartDashboard.putNumber("Coor/EncoderR", drvTSRX_R.getSelectedSensorPosition());
        if (SmartDashboard.getBoolean("Coor/Reset", false)) {
            coorXY.reset();
            SmartDashboard.putBoolean("Coor/Reset", false);
        }
        SmartDashboard.putNumber("NavX/Heading Angle", navX.getAngle());  //Z continueous
        SmartDashboard.putNumber("NavX/Heading Yaw", navX.getYaw());    //Z -180 to 180
        SmartDashboard.putNumber("NavX/Heading Raw Z", navX.getRawGyroZ());//Z raw vel deg/sec
        SmartDashboard.putNumber("NavX/Pitch", navX.getPitch());  //X? -180 to 180
        SmartDashboard.putNumber("NavX/Roll", navX.getRoll());   //X? -180 to 180
        navX.setAngleAdjustment(SmartDashboard.getNumber("NavX/Angle Adj", 0));
    }

}
