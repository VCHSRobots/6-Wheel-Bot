package org.usfirst.frc.team4415.robot;

import ecommon.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class DriveTrain {
	private Joystick m_joystick;
	
	//PDP
	//PowerDistributionPanel pdp;
	//Right Side Motors
	WPI_TalonSRX m_rMaster;
	WPI_VictorSPX m_rMotor1;
	WPI_VictorSPX m_rMotor2;
	//Left Side Motors
	WPI_VictorSPX m_lMotor1;
	WPI_VictorSPX m_lMotor2;
	WPI_TalonSRX m_lMaster;
	
	//Current Variables
//	double rMasterCurrent, rMotor1Current, rMotor2Current;
//	double lMasterCurrent, lMotor1Current, lMotor2Current;	
//	double rSideCurrent, lSideCurrent;
	
	//Encoders
	Encoder rightEncoder;
	Encoder leftEncoder;
	String shiftStatus;
	
	//Buttons
	Button m_gearShift;
	Button m_encoderBut;
	
	//Pneumatics
	DoubleSolenoid gears;
	
	//Booleans
	boolean gearLoop = false;
	boolean encoderLoop = false;
	
	public void RunRobotInit(Joystick j) {
		//Joystick
		m_joystick = j;
		
		//PDP
		//pdp = new PowerDistributionPanel();
		
		//Buttons
		m_gearShift = new JoystickButton(m_joystick, 1);
		m_encoderBut = new JoystickButton(m_joystick, 2);
		
		
		//Right Side Motors Talon Assign
		m_rMaster = new WPI_TalonSRX(RobotMap.rMaster);
		m_rMotor1 = new WPI_VictorSPX(RobotMap.rMotor1);
		m_rMotor2 = new WPI_VictorSPX(RobotMap.rMotor2);
		//Left Side Motors Talon Assign
		m_lMotor1= new WPI_VictorSPX(RobotMap.lMotor1);
		m_lMotor2 = new WPI_VictorSPX(RobotMap.lMotor2);
		m_lMaster = new WPI_TalonSRX(RobotMap.lMaster);
		
//		//Pneumatics
		gears = new DoubleSolenoid(0, 1);
		
		
		//Current Gear
		shiftStatus = "NULL";
	}
	
	public void RunTeleopInit() {
		gears.set(DoubleSolenoid.Value.kReverse);
		m_rMaster.setSelectedSensorPosition(0, 0, 0);
		m_lMaster.setSelectedSensorPosition(0, 0, 0);
	}

	public void RunTeleopPeriodic(Joystick j) {
		
//		double rMasterCurrent = pdp.getCurrent(RobotMap.rMasterCurrent);
//		double rMotor1Current = pdp.getCurrent(RobotMap.rMotor1Current);
//		double rMotor2Current = pdp.getCurrent(RobotMap.rMotor2Current);
//		
//		double lMasterCurrent = pdp.getCurrent(RobotMap.lMasterCurrent);
//		double lMotor1Current = pdp.getCurrent(RobotMap.lMotor1Current);
//		double lMotor2Current = pdp.getCurrent(RobotMap.lMotor2Current);
//		
//		double rSideCurrent = rMasterCurrent + rMotor1Current + rMotor2Current;
//		double lSideCurrent = lMasterCurrent + lMotor1Current + lMotor2Current;
		
		
		//Encoders
		m_rMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		m_lMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		
		m_joystick = j;
		//Retrieves Axis value from controller (-1 to 1)
				double xAxis, yAxis;
				xAxis = m_joystick.getRawAxis(5);
				yAxis = m_joystick.getRawAxis(0);
				
				//Equation for Arcade Drive
				double leftSide, rightSide;
				rightSide = yAxis + xAxis;
				leftSide = xAxis - yAxis;
				
				//Right Side Motors Control
				m_rMaster.set(ControlMode.PercentOutput, rightSide);
				m_rMotor1.set(ControlMode.PercentOutput, rightSide);
				m_rMotor2.set(ControlMode.PercentOutput, rightSide);

				//Left Side Motors Control
				m_lMaster.set(ControlMode.PercentOutput, leftSide);
				m_lMotor1.set(ControlMode.PercentOutput, leftSide);
				m_lMotor2.set(ControlMode.PercentOutput, leftSide);
				
				//Configurates Acceleration slowly (Neutral to full, Timeout MS)
//				m_rMaster.configOpenloopRamp(0.7, 0);
//				m_rMotor1.configOpenloopRamp(0.7, 0);
//				m_rMotor2.configOpenloopRamp(0.7, 0);
//				
//				m_lMaster.configOpenloopRamp(0.7, 0);
//				m_lMotor1.configOpenloopRamp(0.7, 0);
//				m_lMotor2.configOpenloopRamp(0.7, 0);
				
				
		//Gear Shift
				if (m_gearShift.get() == true && gearLoop == false) {
					gearLoop = true;
					if (gears.get() == DoubleSolenoid.Value.kReverse) {
						gears.set(DoubleSolenoid.Value.kForward);
					} else {
						gears.set(DoubleSolenoid.Value.kReverse);
					}
					
				}
				if (m_gearShift.get() == false) {
					gearLoop = false;
				}
				
				if (gears.get() == DoubleSolenoid.Value.kReverse) {
					shiftStatus = "Low Gear";
				} else if (gears.get() == DoubleSolenoid.Value.kForward) {
					shiftStatus = "High Gear";
				}
				
		//Sets Encoder value too 0;	
				if (m_encoderBut.get() == true && encoderLoop == false) {
					encoderLoop = true;
					m_rMaster.setSelectedSensorPosition(0, 0, 0);
					m_lMaster.setSelectedSensorPosition(0, 0, 0);
					
				}
				if (m_encoderBut.get() == false) {
					encoderLoop = false;
				}
				
			}
	
	
		public void Report() {
			
			
			SmartDashboard.putString("Current Gear", shiftStatus);
//			SmartDashboard.putNumber("Right Side Current", rSideCurrent);
//			SmartDashboard.putNumber("Left Side Current", lSideCurrent);
			
			SmartDashboard.putNumber("Right Encoder Position", m_rMaster.getSelectedSensorPosition());
			SmartDashboard.putNumber("Left Encoder Position", m_lMaster.getSelectedSensorPosition());
		}
	}
