package frc.robot.subsystem;

import com.playingwithfusion.CANVenom;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.hdw_io.IO;
import frc.io.hdw_io.util.Whl_Enc_Neo;
import frc.io.hdw_io.util.Whl_Enc_Pwf;
import frc.io.hdw_io.util.Whl_Encoder;
import frc.io.joysticks.JS_IO;
import frc.io.joysticks.util.Axis;
import frc.io.joysticks.util.Button;
import frc.util.Timer;

public class TestDriveMotors {
    // Hardware defintions:
    private static DifferentialDrive diffDrv = IO.diffDrv_M;
    // private static DifferentialDrive diffDrv = IO.diffDrv_Neo;
    // private static DifferentialDrive diffDrv = IO.diffDrv_Pwf;

    private static Whl_Encoder whlEnc_L = IO.drvEnc_L;
    private static Whl_Encoder whlEnc_R = IO.drvEnc_R;
    // private static Whl_Enc_Neo whlEnc_L = IO.WhlEncNeo_L;
    // private static Whl_Enc_Neo whlEnc_R = IO.WhlEncNeo_R;
    // private static Whl_Enc_Pwf whlEnc_L = IO.WhlEncPwf_L;
    // private static Whl_Enc_Pwf whlEnc_R = IO.WhlEncPwf_R;

    // Joystick axis, buttons & povs:
    private static Axis axLeftX = JS_IO.axLeftX;
    private static Axis axLeftY = JS_IO.axLeftY;
    private static Axis axRightX = JS_IO.axRightX;
    private static Axis axRightY = JS_IO.axRightY;

    // variables:
    private static int state; // Shooter state machine. 0=Off by pct, 1=On by velocity, RPM
    private static Timer stateTmr = new Timer(.05); // Timer for state machine

    /**
     * Initialize Shooter stuff. Called from telopInit (maybe robotInit(?)) in
     * Robot.java
     */
    public static void init() {
        sdbInit();
        cmdUpdate(0.0, false, false); // select goal, left trigger, right trigger
        state = 0; // Start at state 0
    }

    /**
     * Update Shooter. Called from teleopPeriodic in robot.java.
     * <p>
     * Determine any state that needs to interupt the present state, usually by way
     * of a JS button but can be caused by other events.
     */
    private static void update() {
        //Add code here to start state machine or override the sm sequence
        smUpdate();
        sdbUpdate();
    }

    public static void smUpdate() { // State Machine Update

        switch (state) {
            case 0: // Everything is off
                diffDrv.tankDrive(0.0, 0.0);
                break;
            case 1: // Tank
                diffDrv.tankDrive(axLeftY.get(), axRightY.get());
                break;
            case 2: // Arcade
                diffDrv.arcadeDrive(axLeftY.get(), axLeftX.get());
                break;
            default: // all off
                cmdUpdate(0.0, false, false);
                break;

        }
    }

    /**
     * Issue spd setting as rpmSP if isVelCmd true else as percent cmd.
     * 
     * @param select_low    - select the low goal, other wise the high goal
     * @param left_trigger  - triggers the left catapult
     * @param right_trigger - triggers the right catapult
     * 
     */
    public static void cmdUpdate(double dblSig, boolean trigger1, boolean trigger2) {
        //Check any safeties, mod passed cmds if needed.
        //Send commands to hardware
    }

    /*-------------------------  SDB Stuff --------------------------------------
    /**Initialize sdb */
    public static void sdbInit() {
        //Put stuff here on the sdb to be retrieved from the sdb later
        // SmartDashboard.putBoolean("ZZ_Template/Sumpthin", sumpthin.get());
    }

    /**Update the Smartdashboard. */
    public static void sdbUpdate() {
        SmartDashboard.putNumber("Test Drv/state", state);
    }

    // ----------------- Shooter statuses and misc.-----------------
    /**
     * Probably shouldn't use this bc the states can change. Use statuses.
     * 
     * @return - present state of Shooter state machine.
     */
    public static int getState() {
        return state;
    }

    /**
     * @return If the state machine is running, not idle.
     */
    public static boolean getStatus(){
        return state != 0;      //This example says the sm is runing, not idle.
    }

}