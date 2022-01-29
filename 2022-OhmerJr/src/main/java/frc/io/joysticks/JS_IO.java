package frc.io.joysticks;
/*
Original Author: Joey & Anthony
Rewite Author: Jim Hofmann
History:
J&A - 11/6/2019 - Original Release
JCH - 11/6/2019 - Original rework
TODO: Exception for bad or unattached devices.
      Auto config based on attached devices and position?
      Add enum for jsID & BtnID?  Button(eLJS, eBtn6) or Button(eGP, eBtnA)
Desc: Reads joystick (gamePad) values.  Can be used for different stick configurations
    based on feedback from Smartdashboard.  Various feedbacks from a joystick are
    implemented in classes, Button, Axis & Pov.
    This version is using named joysticks to istantiate axis, buttons & axis
*/

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Declares all joysticks, buttons, axis & pov's.
public class JS_IO {
    public static int jsConfig = 0; // 0=Joysticks, 1=gamePad only, 2=left Joystick only
                                    // 3=Mixed LJS & GP, 4=Nintendo Pad
    // Declare all possible Joysticks
    public static Joystick leftJoystick = new Joystick(0);  // Left JS
    public static Joystick rightJoystick = new Joystick(1); // Right JS
    public static Joystick coJoystick = new Joystick(2);    // Co-Dvr JS
    public static Joystick gamePad = new Joystick(3);       // Normal mode only (not Dual Trigger mode)
    // public static Joystick neoPad = new Joystick(4); // Nintendo style gamepad
    // public static Joystick arJS[] = { leftJoystick, rightJoystick, coJoystick, gamePad };

    // Declare all stick control
    // Drive
    public static Axis axLeftX = new Axis();                // Left Arcade rotation
    public static Axis axLeftY = new Axis();                // Tank Left or Left Arcade fwd
    public static Axis axRightX = new Axis();               // Right Arcade rotation
    public static Axis axRightY = new Axis();               // Tank Right or Right Arcade fwd
    public static Button btnSelDrv = new Button();          //Select Drive type; off, tank, arcade, curvature

    public static Button btnScaledDrive = new Button();     // scale the drive
    public static Button btnInvOrientation = new Button();  // invert the orientation of the robot (joystick: forwards
                                                            // becomes backwards for robot and same for backwards)
    public static Button btnHoldZero = new Button();        // In teleop drv hold heading at 0
    public static Button btnHold180 = new Button();         // In teleop drv hold heading at 180

    // All
    public static Button btnStop = new Button();
    public static Pov pov_SP = new Pov();

    // Misc
    public static Button limeLightOnOff = new Button();     //Turn the LL LEDs On and Off
    public static Button record = new Button();

    // Constructor
    public JS_IO() {
        init();
    }

    /**Initialize joysticks, axises, buttons & pov assignments. */
    public static void init() {
        caseDefault();                                      //Clear all assignments
        SmartDashboard.putNumber("JS_Config", jsConfig);    //Get assignment
        configJS();                                         //Set assignments
    }

    /**Check to see if the joystick assignments have been changed. */
    public static void update() { // Chk for Joystick configuration
        if (jsConfig != SmartDashboard.getNumber("JS_Config", 0)) {     //If ex. config != sdb
            jsConfig = (int) SmartDashboard.getNumber("JS_Config", 0);  //get new config
            caseDefault();                                              //clear ex. assignments
            configJS();                                                 //set new assignments
        }
    }

    /**Configure to the selected joystick and assinments. */
    public static void configJS() { // Default Joystick else as gamepad
        jsConfig = (int) SmartDashboard.getNumber("JS_Config", 0);

        switch (jsConfig) {
            case 0: // Normal 3 joystick config
                norm3JS();
                break;

            case 1: // Gamepad only
                a_GP();
                break;

            default: // Bad assignment
                caseDefault();
                break;

        }
    }

    // ================ Controller actions ================

    // ----------- Normal 3 Joysticks -------------
    /**Configure axises, buttons & pov for normal 3 Logitec joysticks. */
    private static void norm3JS() {

        // All stick axisesssss
        axLeftX.setAxis(leftJoystick, 0);       // Left Arcade rotation
        axLeftY.setAxis(leftJoystick, 1);       // Tank Left or Left Arcade fwd
        axRightX.setAxis(rightJoystick, 0);     // Right Arcade rotation
        axRightY.setAxis(rightJoystick, 1);     // Tank Right or Right Arcade fwd

        // Drive buttons
        btnSelDrv.setButton(rightJoystick, 12);         //Select Drive type; off, tank, arcade, curvature
        btnScaledDrive.setButton(rightJoystick, 3);
        btnInvOrientation.setButton(rightJoystick, 1);

        btnStop.setButton(coJoystick, 11);

        pov_SP.setPov(coJoystick, 1);
    }

    // ----- gamePad only --------
    /**Configure axises, buttons & pov for a single gamepad joysticks. */
    private static void a_GP() {

        // All stick axisesssss
        axLeftX.setAxis(gamePad, 0);
        axLeftY.setAxis(gamePad, 1);        // Left Arcade rotation
        axRightX.setAxis(gamePad, 4);       // Tank Right or Right Arcade fwd (can't use as arc)
        axRightY.setAxis(gamePad, 5);       // Tank Left or Left Arcade fwd

        // Drive buttons
        btnSelDrv.setButton(gamePad, 1);         //Select Drive type; sdb, tank, arcade, curvature
        btnScaledDrive.setButton(gamePad, 5);       // LBump
        btnInvOrientation.setButton(gamePad, 6);    // RBump
        btnHoldZero.setButton(gamePad, 3);
        btnHold180.setButton(gamePad, 2);

        //All
        limeLightOnOff.setButton(gamePad, 7);
        btnStop.setButton(gamePad, 8); // start
        pov_SP.setPov(gamePad, 0);
    }

    // ----------- Case Default -----------------
    /**Set all axises, buttons & pov to a default.
     *<p>Just incase called when not assigned to the selected joystick.
     */
    private static void caseDefault() {
        // Drive
        axLeftX.setAxis(null, 0);
        axLeftY.setAxis(null, 0);
        axRightX.setAxis(null, 0);
        axRightY.setAxis(null, 0);

        btnSelDrv.setButton(null, 0);               //Select Drive type; off, tank, arcade, curvature
        btnScaledDrive.setButton(null, 0);          // scale the drive
        btnInvOrientation.setButton(null, 0);    // invert the orientation of the robot (joystick: fwd is bkwd for robot)

        btnHoldZero.setButton();
        btnHold180.setButton();

        // All
        limeLightOnOff.setButton();
        btnStop.setButton();
        pov_SP.setPov();
    
    }

    //----------- Methods to get JS values by name of drive type --------------
    /**Get the assigned Tank Left JS value */
    public static double tnkLeft() {return JS_IO.axLeftY.get();}       //Tank Left
    /**Get the assigned Tank Right JS value */
    public static double tnkRight() {return JS_IO.axRightY.get();}     //Tank Right
    /**Get the assigned Tank Left & Right JS values */
    public static double[] tnkLR() { double[] tmp = { tnkLeft(), tnkRight() }; return tmp; }   //Tank Left & Right

    /**Get the assigned Arcade Move JS value */
    public static double arcMove() {return JS_IO.axLeftY.get();}       //Arcade move, fwd/bkwd
    /**Get the assigned Arcade Rotate JS value */
    public static double arcRot() {return JS_IO.axLeftX.get();}        //Arcade Rotation
    /**Get the assigned Arcade Move & Rotate JS values */
    public static double[] arcMR() { double[] tmp = { arcMove(), arcRot() }; return tmp; }   //Arcade Left & Right

    /**Get the assigned Curvature Move JS value */
    public static double curMove() {return JS_IO.axLeftY.get();}       //Curvature move, pwr applied
    /**Get the assigned Curvature Rotate JS value */
    public static double curRot() {return JS_IO.axRightX.get();}       //Curvature direction, left/right
    /**Get the assigned Curvature Move & Rotate JS values */
    public static double[] curMR() { double[] tmp = { curMove(), curRot() }; return tmp; }   //Curvature Left & Right

}