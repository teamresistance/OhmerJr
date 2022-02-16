package frc.robot.subsystem.drive;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.vision.RPI;
import frc.robot.subsystem.drive.trajFunk.*;

public class Trajectories {
    private static double dfltPwr = 0.9;
    private static SendableChooser<String> chsr = new SendableChooser<String>();
    private static String[] chsrDesc = {
        "getEmpty", "getCargo1", "getCargo2"
    };
    
    /**Initialize Traj chooser */
    public static void chsrInit(){
        for(int i = 0; i < chsrDesc.length; i++){
            chsr.addOption(chsrDesc[i], chsrDesc[i]);
        }
        chsr.setDefaultOption(chsrDesc[0] + " (Default)", chsrDesc[0]);   //Default MUST have a different name
        SmartDashboard.putData("Drv/Traj/Choice", chsr);
    }

    /**Show on sdb traj chooser info.  Called from robotPeriodic  */
    public static void chsrUpdate(){
        SmartDashboard.putString("Drv/Traj/Choosen", chsr.getSelected());
        SmartDashboard.putNumber("Drv/Traj/Gxtc Num", RPI.galacticShooter());
    }

    /**
     * Get the trajectory array that is selected in the chooser Traj/Choice.
     * @param pwr - default pwr to be usedin trajectories
     * @return The active, selected, Chooser Trajectory for use by AutoSelector
     */
    public static ATrajFunction[] getTraj(double pwr){
        switch(chsr.getSelected()){
            case "getEmpty":
            return getEmpty(pwr);
            case "getCargo1":
            return getCargo1(pwr);
            case "getCargo2":
            return getCargo2(pwr);
            default:
            System.out.println("Traj/Bad Traj Desc - " + chsr.getSelected());
            return getEmpty(0);
        }
    }

    /**
     * Get the trajectory array that is selected in the chooser Traj/Choice.
     * <p>Use a default power, 0.9.
     * 
     * @return The active, selected, Chooser Trajectory for use by AutoSelector
     */
    public static ATrajFunction[] getTraj(){
        return getTraj(dfltPwr);
    }


    public static String getChsrDesc(){
        return chsr.getSelected();
    }

    //------------------ Trajectories -------------------------------
    // each trajectory/path/automode is stored in each method
    // name each method by the path its doing

    public static ATrajFunction[] getEmpty(double pwr) {
        ATrajFunction[] traj = { new TurnNMove(0.0, 0.0, pwr) };
        return traj;
    }

    public static ATrajFunction[] getCargo1(double pwr) {
        ATrajFunction traj[] = {
            new CoorOffset(24, -1.5, 3), 
            new Waypt(-1.5, 10)            // new MoveOnHdg(0  , 15, pwr), // Moves 15 ft
            // new MoveOnHdg(0  ,-0.2,pwr), // Brakes
            // new TankTurnHdg(-90, 0.65, -0.65), //Turns
            // new Move/;00p;/OnHdg(-90, 10, pwr), //Moves
            // new MoveOnHdg(-90  ,-0.2,pwr), // Brakes
        };
        return traj;
    }
    public static ATrajFunction[] getCargo2(double pwr) {
        ATrajFunction traj[] = {
            new Waypt(0, 15, pwr, 5),
            new Waypt(-10, 15, pwr, 5),
        };
        System.out.println("HEre2: ");
        return traj;
    }
}