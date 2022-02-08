package frc.io.hdw_io;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.hdw_io.util.CoorSys;
import frc.io.hdw_io.util.NavX;
import frc.io.hdw_io.util.Encoder_Neo;
import frc.io.hdw_io.util.Encoder_Pwf;
import edu.wpi.first.wpilibj.SPI;
import frc.io.hdw_io.util.Encoder_Tln;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import com.playingwithfusion.CANVenom;
import com.playingwithfusion.CANVenom.BrakeCoastMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IO {
    // PDP
    public static PowerDistribution pdp = new PowerDistribution(0,ModuleType.kCTRE);

    // Drive
    public static WPI_TalonSRX drvTSRX_L = new WPI_TalonSRX(56); // Cmds left wheels. Includes encoders
    public static WPI_TalonSRX drvTSRX_R = new WPI_TalonSRX(57); // Cmds right wheels. Includes encoders
    public static final double drvTPF_L = 433.20; // 1024 t/r (0.5' * 3.14)/r 9:60 gr
    public static final double drvTPF_R = 433.20; // 1024 t/r (0.5' * 3.14)/r 9:60 gr
    public static Encoder_Tln drvEnc_L = new Encoder_Tln(drvTSRX_L, drvTPF_L);  //Interface for feet, ticks, reset
    public static Encoder_Tln drvEnc_R = new Encoder_Tln(drvTSRX_R, drvTPF_R);

    // Assignments used by DiffDrv. Slaves sent same command.  Slaves set to follow Masters in IO.
    public static DifferentialDrive diffDrv_M = new DifferentialDrive(IO.drvTSRX_L, IO.drvTSRX_R);

    // navX
    public static NavX navX = new NavX(SPI.Port.kMXP);
    public static CoorSys coorXY = new CoorSys(navX, drvEnc_L, drvEnc_R);

    //Test NEO brushless motor with a Spark Max controller
    public static CANSparkMax drvMtrNeo_L = new CANSparkMax(2, MotorType.kBrushless); // Test drv mtr left whl
    public static CANSparkMax drvMtrNeo_R = new CANSparkMax(3, MotorType.kBrushless); // Test drv mtr right whl
    public static DifferentialDrive diffDrv_Neo = new DifferentialDrive(drvMtrNeo_L, drvMtrNeo_R);

    public static Encoder_Neo WhlEncNeo_L = new Encoder_Neo(drvMtrNeo_L, 866.4);
    public static Encoder_Neo WhlEncNeo_R = new Encoder_Neo(drvMtrNeo_R, 866.4);

    public static CANSparkMax armMtrNeo_Lead = new CANSparkMax(4, MotorType.kBrushless); // Lead motor for arm rotation
    public static CANSparkMax armMtrNeo_Foll = new CANSparkMax(5, MotorType.kBrushless); // Follower motor for arm rotation

    public static Encoder_Neo armEncNeo_L = new Encoder_Neo(armMtrNeo_Lead, 866.4);
    public static Encoder_Neo armEncNeo_F = new Encoder_Neo(armMtrNeo_Foll, 866.4);

    //Test Venom brushless motor built-in controller from Playing With Fusion, PWF
    public static CANVenom drvMtrPwf_L = new CANVenom(11); // Test drv mtr left whl
    public static CANVenom drvMtrPwf_R = new CANVenom(12); // Test drv mtr right whl
    public static DifferentialDrive diffDrv_Pwf = new DifferentialDrive(drvMtrPwf_L, drvMtrPwf_R);

    public static Encoder_Pwf WhlEncPwf_L = new Encoder_Pwf(drvMtrPwf_L, 866.4);
    public static Encoder_Pwf WhlEncPwf_R = new Encoder_Pwf(drvMtrPwf_R, 866.4);

    public static CANVenom armMtrPwf_Lead = new CANVenom(4); // Lead motor for arm rotation
    public static CANVenom armMtrPwf_Foll = new CANVenom(5); // Follower motor for arm rotation

    public static Encoder_Pwf armEncPwf_L = new Encoder_Pwf(armMtrPwf_Lead, 866.4);
    public static Encoder_Pwf armEncPwf_F = new Encoder_Pwf(armMtrPwf_Foll, 866.4);

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

        // Test arm motor
        drvMtrNeo_L.restoreFactoryDefaults();
        drvMtrNeo_R.restoreFactoryDefaults();
        drvMtrNeo_L.setIdleMode(IdleMode.kBrake);
        drvMtrNeo_R.setIdleMode(IdleMode.kBrake);
        drvMtrNeo_L.setInverted(false);
        drvMtrNeo_R.setInverted(true);

        armMtrNeo_Lead.restoreFactoryDefaults();
        armMtrNeo_Foll.restoreFactoryDefaults();
        armMtrNeo_Lead.setIdleMode(IdleMode.kBrake);
        armMtrNeo_Foll.setIdleMode(IdleMode.kBrake);
        armMtrNeo_Lead.setInverted(false);
        armMtrNeo_Foll.setInverted(true);
        armMtrNeo_Foll.follow(armMtrNeo_Lead);        

        // drvMtrPwf_L.restoreFactoryDefaults();
        // drvMtrPwf_R.restoreFactoryDefaults();
        drvMtrPwf_L.setBrakeCoastMode(BrakeCoastMode.Brake);
        drvMtrPwf_R.setBrakeCoastMode(BrakeCoastMode.Brake);
        drvMtrPwf_L.setInverted(false);
        drvMtrPwf_R.setInverted(true);

        // armMtrPwf_Lead.restoreFactoryDefaults();
        // armMtrPwf_Foll.restoreFactoryDefaults();
        armMtrPwf_Lead.setBrakeCoastMode(BrakeCoastMode.Brake);
        armMtrPwf_Foll.setBrakeCoastMode(BrakeCoastMode.Brake);
        armMtrPwf_Lead.setInverted(false);
        armMtrPwf_Foll.setInverted(true);
        armMtrPwf_Foll.follow(armMtrPwf_Lead);        

    }

    public static void sdbInit() {
        SmartDashboard.putBoolean("Coor/Reset", false);
        SmartDashboard.putNumber("NavX/Angle Adj", 0);
        SmartDashboard.putBoolean("NavX/Zero Yaw", false);   //Set to yaw value?
        SmartDashboard.putBoolean("NavX/Reset", false);   //Set to yaw value?
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
        SmartDashboard.putNumber("NavX/Hdg Angle 360", navX.getNormalizedAngle());  //Z continueous
        SmartDashboard.putNumber("NavX/Hdg Angle 180", navX.getNormalizedTo180());  //Z continueous
        SmartDashboard.putNumber("NavX/Heading Yaw", navX.getYaw());    //Z -180 to 180
        SmartDashboard.putNumber("NavX/Heading Raw Z", navX.getRawGyroZ());//Z raw vel deg/sec
        SmartDashboard.putNumber("NavX/Pitch", navX.getPitch());  //X? -180 to 180
        SmartDashboard.putNumber("NavX/Roll", navX.getRoll());   //X? -180 to 180
        navX.setAngleAdjustment(SmartDashboard.getNumber("NavX/Angle Adj", 0));

        if (SmartDashboard.getBoolean("NavX/Reset", false)) {
            navX.reset();     //Reset yaw, angle??
            SmartDashboard.putBoolean("NavX/Reset", false);
        }
        if (SmartDashboard.getBoolean("NavX/Zero Yaw", false)) {
            navX.zeroYaw();     //Set to yaw value?
            SmartDashboard.putBoolean("NavX/Zero Yaw", false);
        }
    }

}
