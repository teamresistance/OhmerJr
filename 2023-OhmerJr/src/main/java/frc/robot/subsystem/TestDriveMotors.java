// package frc.robot.subsystem;

// import com.playingwithfusion.CANVenom;
// import com.revrobotics.CANSparkMax;

// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.io.hdw_io.IO;
// import frc.io.hdw_io.util.Encoder_Neo;
// import frc.io.hdw_io.util.Encoder_Pwf;
// import frc.io.hdw_io.util.Encoder_Tln;
// import frc.io.joysticks.JS_IO;
// import frc.io.joysticks.util.Axis;
// import frc.io.joysticks.util.Button;
// import frc.util.Timer;

// /**Class for testing the new NEO motor w/ Spark Max controller
//  * and the Venom motor w/ built in controller using DifferentialDrive.
//  * <p>Only 1 style can be tested at a time.
//  * <p>Example of using 2 in tandem for arm control
//  */
// public class TestDriveMotors {
//     // Hardware defintions:
//     private static DifferentialDrive diffDrv = IO.diffDrv_Neo;
//     private static CANSparkMax drvMtr_L = IO.drvMtrNeo_L;   //Only needed these 
//     private static CANSparkMax drvMtr_R = IO.drvMtrNeo_R;   //for
//     private static Encoder_Neo whlEnc_L = IO.WhlEncNeo_L;   //Smartdashboard
//     private static Encoder_Neo whlEnc_R = IO.WhlEncNeo_R;   //

//     // private static DifferentialDrive diffDrv = IO.diffDrv_Pwf;
//     // private static CANVenom    drvMtr_L = IO.drvMtrPwf_L;
//     // private static CANVenom    drvMtr_R = IO.drvMtrPwf_R;
//     // private static Whl_Enc_Pwf whlEnc_L = IO.WhlEncPwf_L;
//     // private static Whl_Enc_Pwf whlEnc_R = IO.WhlEncPwf_R;

//     // Joystick axis, buttons & povs:
//     private static Axis axLeftX = JS_IO.axLeftX;    //Used w/ Arcade
//     private static Axis axLeftY = JS_IO.axLeftY;    //Used w/ Arcade or Tank
//     private static Axis axRightX = JS_IO.axRightX;  //not used
//     private static Axis axRightY = JS_IO.axRightY;  //Tank only

//     // variables:
//     private static int state; // Shooter state machine. 0=Off by pct, 1=On by velocity, RPM
//     private static Timer stateTmr = new Timer(.05); // Timer for state machine

//     /**
//      * Initialize Shooter stuff. Called from telopInit (maybe robotInit(?)) in
//      * Robot.java
//      */
//     public static void init() {
//         sdbInit();
//         cmdUpdate(0.0, false, false); // select goal, left trigger, right trigger
//         state = 1; // Start at state 1, Tank default
//     }

//     /**
//      * Update Shooter. Called from teleopPeriodic in robot.java.
//      * <p>
//      * Determine any state that needs to interupt the present state, usually by way
//      * of a JS button but can be caused by other events.
//      */
//     private static void update() {
//         //Add code here to start state machine or override the sm sequence
//         smUpdate();
//         sdbUpdate();
//     }

//     public static void smUpdate() { // State Machine Update

//         switch (state) {
//             case 0: // Everything is off
//                 diffDrv.tankDrive(0.0, 0.0);
//                 break;
//             case 1: // Tank
//                 diffDrv.tankDrive(axLeftY.get(), axRightY.get());
//                 break;
//             case 2: // Arcade
//                 diffDrv.arcadeDrive(axLeftY.get(), axLeftX.get());
//                 break;
//             default: // all off
//                 cmdUpdate(0.0, false, false);
//                 break;

//         }
//     }

//     /**
//      * Issue spd setting as rpmSP if isVelCmd true else as percent cmd.
//      * 
//      * @param select_low    - select the low goal, other wise the high goal
//      * @param left_trigger  - triggers the left catapult
//      * @param right_trigger - triggers the right catapult
//      * 
//      */
//     public static void cmdUpdate(double dblSig, boolean trigger1, boolean trigger2) {
//         //Check any safeties, mod passed cmds if needed.
//         //Send commands to hardware
//     }

//     /*-------------------------  SDB Stuff --------------------------------------
//     /**Initialize sdb */
//     public static void sdbInit() {
//         SmartDashboard.putNumber("Test Drv/8. TPF Left",  IO.WhlEncNeo_L.getTPF());
//         SmartDashboard.putNumber("Test Drv/9. TPF Right", IO.WhlEncNeo_R.getTPF());
//     }

//     /**Update the Smartdashboard. */
//     public static void sdbUpdate() {
//         SmartDashboard.putNumber("Test Drv/1. state", state);
//         SmartDashboard.putNumber("Test Drv/2. Cmd Left",    drvMtr_L.get());
//         SmartDashboard.putNumber("Test Drv/3. Cmd Right",   drvMtr_R.get());
//         SmartDashboard.putNumber("Test Drv/4. Ticks Left",  whlEnc_L.ticks());
//         SmartDashboard.putNumber("Test Drv/5. Ticks Right", whlEnc_R.ticks());
//         SmartDashboard.putNumber("Test Drv/6. Feet Left",   whlEnc_L.feet());
//         SmartDashboard.putNumber("Test Drv/7. Feet Right",  whlEnc_R.feet());
//         whlEnc_L.setTPF(SmartDashboard.getNumber("Test Drv/8. TPF Left", 1));
//         whlEnc_R.setTPF(SmartDashboard.getNumber("Test Drv/9. TPF Right", 1));
//     }

//     // ----------------- Shooter statuses and misc.-----------------
//     /**
//      * Probably shouldn't use this bc the states can change. Use statuses.
//      * 
//      * @return - present state of Shooter state machine.
//      */
//     public static int getState() {
//         return state;
//     }

//     /**
//      * @return If the state machine is running, not idle.
//      */
//     public static boolean getStatus(){
//         return state != 0;      //This example says the sm is runing, not idle.
//     }

// }