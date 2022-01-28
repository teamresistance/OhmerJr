package frc.io.joysticks;
/*
Original Author: Sherya
Rewite Author: Jim Hofmann
History:
JCH - 11/6/2019 - rework
S - 3/6/2017 - Original release
TODO: more testing.  maybe add an array handler?
*/

import edu.wpi.first.wpilibj.Joystick;

/**
 * Allows use of various joystick/gamepad configurations using the same axis name.
 */
public class Axis{
	
	private Joystick joystick;	//Joystick associated with axis
	private int axisID;			//Axis on joystick (Logitec, 0 = X, 1 = Y, 3 = Z)
	private boolean exists;		//Does this axis assignment exist, return default if not.
	private double exDefault;	//Default value to return if axis does not exist (not initalized)
	private double zero_DB;		//Deadband around zero to return 0.0.
	
	/**
	 * Normal constructor for an axis object.
	 * @param injoystick - joystick axis is on
	 * @param inaxisID - axis ID  (Logitec, 0 = X, 1 = Y)
	 */
	public Axis(Joystick injoystick, int inaxisID) {
		joystick = injoystick;
		axisID = inaxisID;
		exists = joystick != null;
		exDefault = 0;	// default to 0
		zero_DB = 0.0;	// deadband around 0.0 get() returns 0.0
	}

	/**Constructor, defaults set to does not exist & 0.0
	 * <p> Used to deref the axis but keep the name.
	 */
	public Axis() {
		this.exists = false;
		this.exDefault = 0.0;
	}

	/**Constructor, defaults set to does not exist & passed default value */
	public Axis(double exDefault) {
		this.exists = false;
		this.exDefault = exDefault;
	}

	/**
	 * Create a axis ref to a joystick & axisID (Logitec, 0 = X, 1 = Y)
	 * @param injoystick
	 * @param inAxisID
	 */
	public void setAxis(Joystick injoystick, int inAxisID){
		joystick = injoystick;
		axisID = inAxisID;
		exists = joystick != null;
		exDefault = 0;
	}

	/**Clear assignment.  Joystick = null & axisID = 0. */
	public void setAxis(){
		setAxis(null, 0);
	}

	/**Returns axis value if GT the default zero DB */
	public double get() {
		return get(zero_DB);
	}

	/**Returns axis value if GT the passed zero DB */
	public double get(double z_DB) {
		if(Math.abs(getRaw()) < z_DB) return 0.0;
		return getRaw();
	}

	/**IF exists returns raw axis value else returns the default value. */
	public double getRaw() {
		return exists ? joystick.getRawAxis(axisID) : exDefault;
	}

	/**Set the default zero DB */
	public void set_ZDB(double z_DB) {zero_DB = z_DB;}

	/**Set the default zero DB */
	public double get_ZDB() {return zero_DB;}

}