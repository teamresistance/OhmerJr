package frc.robot.subsystem.drive.trajFunk;

import frc.io.hdw_io.IO;
import frc.robot.subsystem.drive.Drive;
import frc.util.PIDXController;

/**
 * This AutoFunction uses Arcade to turn to heading THEN moves distance.
 */
public class CoorOffset extends ATrajFunction {

    // private boolean finished = false;
    private double hdgSP = 0.0;
    private double distSP = 0.0;
    private double pwrMx = 0.0;

    
    public CoorOffset(double hdg_OS, double coorX_OS, double coorY_OS) {
        IO.navX.setAngleAdjustment(hdg_OS);
        IO.coorXY.setXY_OS(coorX_OS, coorY_OS);

        setDone();
    }

    public void execute() {
        // update();
       
        updSDB();
    }
}