package frc.io.raspberryPi;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RPI {
    private static NetworkTable rpiTable = NetworkTableInstance.getDefault().getTable("RPITable");

    public static void init() {

        rpiTable = NetworkTableInstance.getDefault().getTable("RPITable");
    }

    public static void update() {
        sdbUpdate();
    }

    public static boolean rpiHasTarget() {
        return rpiTable.getEntry("validity").getBoolean(false);
    }

    public static double getRPIHeight() {
        return rpiTable.getEntry("height").getDouble(999);
    }

    public static double getRPIWidth() {
        return rpiTable.getEntry("width").getDouble(0);
    }

    public static double getRPIArea() {
        return rpiTable.getEntry("area").getDouble(0);
    }

    public static double getRPIcenterX() {
        return rpiTable.getEntry("vcX").getDouble(0);
    }

    public static double getRPIcenterY() {
        return rpiTable.getEntry("vcY").getDouble(0);
    }

    public static double getNumContours() {
        return rpiTable.getEntry("nC").getDouble(0);
    }

    /**
     * Update the sdb.  NOTE: not sure we need this.  Should be accessable in RPITable.
     */
    public static void sdbUpdate() {
        SmartDashboard.putBoolean("RPI/has target", rpiHasTarget());
        SmartDashboard.putNumber("RPI/bb height", getRPIHeight());
        SmartDashboard.putNumber("RPI/bb width", getRPIWidth());
        SmartDashboard.putNumber("RPI/num contours", getNumContours());
        SmartDashboard.putNumber("RPI/center X", getRPIcenterX());
        SmartDashboard.putNumber("RPI/center Y", getRPIcenterY());
        SmartDashboard.putNumber("RPI/area", getRPIArea());
    }
}
