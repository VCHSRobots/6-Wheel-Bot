/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4415.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private DriveTrain m_drivetrain = new DriveTrain();
//	Button m_gearShift;
	//DoubleSolenoid gears;
	
	//Booleans
	boolean gearLoop = false;
	//Xbox Controller
	Joystick m_controller;

	@Override
	public void robotInit() {
		//Assigns USB port for Controller
		m_controller = new Joystick(0);
		
		//Runs Drive Train Initialization
		m_drivetrain.RunRobotInit(m_controller);
		

		//Assigns USB port for Controller
		
	}
	
	@Override
	public void teleopInit() {
		m_drivetrain.RunTeleopInit();
	}

	@Override
	public void teleopPeriodic() {
		m_drivetrain.RunTeleopPeriodic(m_controller);
		m_drivetrain.Report();
		
	}
}
