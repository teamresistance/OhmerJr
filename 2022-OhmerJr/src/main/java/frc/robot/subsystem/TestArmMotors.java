package frc.robot.subsystem;

import com.playingwithfusion.CANVenom;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.hdw_io.IO;
import frc.io.hdw_io.util.Encoder_Neo;
import frc.io.hdw_io.util.Encoder_Pwf;
import frc.io.hdw_io.util.Encoder_Tln;
import frc.io.joysticks.JS_IO;
import frc.io.joysticks.util.Axis;
import frc.io.joysticks.util.Button;
import frc.util.Timer;

/**Class for testing the new NEO motor w/ Spark Max controller
 * and the Venom motor w/ built in controller using raw motor control.
 * <p>Only 1 style can be tested at a time.
 * <p>Example of using 2 in tandem for arm control
 */
public class TestArmMotors {
    // Hardware defintions:
    private static CANSparkMax armMtr = IO.armMtrNeo_Lead;
    private static CANSparkMax armMtrFoll = IO.armMtrNeo_Foll;
    private static Encoder_Neo armEnc_L = IO.armEncNeo_L;
    private static Encoder_Neo armEnc_F = IO.armEncNeo_F;

    // private static CANVenom armMtr = IO.armMtrPwf_Lead;
    // private static CANVenom armMtrFoll = IO.armMtrPwf_Foll;
    // private static Whl_Enc_Pwf armEnc_L = IO.armEncPwf_L;
    // private static Whl_Enc_Pwf armEnc_F = IO.armEncPwf_F;

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
        cmdUpdate(0.0); // select goal, left trigger, right trigger
        state = 1; // Start at state 0
    }

    /**
     * Update Shooter. Called from teleopPeriodic in robot.java.
     * <p>
     * Determine any state that needs to interupt the present state, usually by way
     * of a JS button but can be caused by other events.
     */
    public static void update() {
        //Add code here to start state machine or override the sm sequence
        smUpdate();
        sdbUpdate();
    }

    private static void smUpdate() { // State Machine Update

        switch (state) {
            case 0: // Everything is off
                armMtr.set(0.0);
                break;
            case 1: // Tank
                cmdUpdate(axLeftY.get());
                break;
            default: // all off
                cmdUpdate(0.0);
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
    public static void cmdUpdate(double dblSig) {
        armMtr.set(dblSig);
    }

    /*-------------------------  SDB Stuff --------------------------------------
    /**Initialize sdb */
    public static void sdbInit() {
        SmartDashboard.putNumber("Test Arm/10. tpf", armEnc_L.getTPF());
    }

    /**Update the Smartdashboard. */
    public static void sdbUpdate() {
        SmartDashboard.putNumber("Test Arm/1. state", state);
        SmartDashboard.putNumber("Test Arm/2. JSLY", axLeftY.get());
        SmartDashboard.putNumber("Test Arm/3. Mtr Lead Current", armMtr.getOutputCurrent());
        SmartDashboard.putNumber("Test Arm/4. Mtr Foll Current", armMtrFoll.getOutputCurrent());
        SmartDashboard.putNumber("Test Arm/5. Mtr Foll Temp", armMtrFoll.getMotorTemperature());
        SmartDashboard.putNumber("Test Arm/6. Lead Enc", armEnc_L.ticks());
        SmartDashboard.putNumber("Test Arm/7. Foll Enc", armEnc_F.ticks());
        SmartDashboard.putNumber("Test Arm/8. Lead Ft", armEnc_L.feet());
        SmartDashboard.putNumber("Test Arm/9. Foll Ft", armEnc_F.feet());
        armEnc_L.setTPF(SmartDashboard.getNumber("Test Arm/10. tpf", 1.0));
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
        return state != 0;      //This example says the sm is running, not idle.
    }

}