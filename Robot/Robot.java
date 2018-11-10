/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4415.robot;


import ecommon.RobotMap;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private DriveTrain m_drivetrain = new DriveTrain();
	private FrisbeeShoot m_frisbeeShoot = new FrisbeeShoot();
	
//	Button m_gearShift;
	//DoubleSolenoid gears;
	
	//PDP
	PowerDistributionPanel m_pdp;
	
	
	//Booleans
	boolean gearLoop = false;
	//Xbox Controller
	Joystick m_controller;

	@Override
	public void robotInit() {
		//Assigns USB port for Controller
		m_controller = new Joystick(0);
		
		//Runs Drive Train Initialization
		m_drivetrain.RobotInit(m_controller);
		m_frisbeeShoot.RobotInit(m_controller);
		
		//PDP
		m_pdp = new PowerDistributionPanel(RobotMap.pdp);
		

		//Assigns USB port for Controller
		
	}
	
	@Override
	public void robotPeriodic() {
		m_drivetrain.Report();
		m_frisbeeShoot.Report();
	}
	
	@Override
	public void teleopInit() {
		m_drivetrain.TeleopInit();
	}

	@Override
	public void teleopPeriodic() {
		m_drivetrain.TeleopPeriodic(m_controller, m_pdp);
		m_frisbeeShoot.TeleopPeriodic();
		
	}
}
