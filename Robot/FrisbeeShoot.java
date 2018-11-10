package org.usfirst.frc.team4415.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ecommon.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FrisbeeShoot {
	
	double m_pow;
	
	//Solenoid that fires frisbee
	DoubleSolenoid m_shoot;
	
	//Toggle Button that fires frisbee
	Button m_fire;
	Joystick m_joystick;
	
	//Motor for Frisbee shooter
	WPI_TalonSRX m_frisbee;
	WPI_TalonSRX m_frisTurn;
	
	Button m_frisSpeed;
	
	Button m_frisStop;
	
	double m_rTrig, m_lTrig;
	//Boolean
	boolean m_frisLoop = false;
	boolean m_frisSpdLoop = false;
	boolean m_frisStopLoop = false;
	
	
	private int m_motor_mode = 0;  // 0=manual  1=750 RPM  2=1500 RPM
	private double m_rpm = 0.0;    // Current RPM.
	private double m_demand = 0.0; // Current Demand.
	
	
	private double calculateDemand(double rpm) {
		double counts_per_rev = 1024.0;  // By experiment with SmartDashboard and Encoder.
		double counts_per_rpm = rpm * counts_per_rev;
		double counts_per_sec = counts_per_rpm / 60.0;
		double counts_per_demand_period = counts_per_sec * 0.100;  
		return counts_per_demand_period;
	}
	
	public void RobotInit(Joystick j) {
		m_joystick = j;
		
		m_fire = new JoystickButton(m_joystick, 8);
		m_frisStop = new JoystickButton(m_joystick, 7);
		
		m_shoot = new DoubleSolenoid(2, 3);
		
		m_fire = new JoystickButton(m_joystick, 6);
		
		m_frisbee = new WPI_TalonSRX(RobotMap.frisbee);
		m_frisTurn = new WPI_TalonSRX(RobotMap.frisTurn);
		
	
	}
	
	public void TeleopPeriodic() {
		
		//Pneumatic Engage
		if (m_fire.get() && !m_frisLoop) {
			m_frisLoop = true;
			if (m_shoot.get() == DoubleSolenoid.Value.kForward) {
				m_shoot.set(DoubleSolenoid.Value.kReverse);
			} else if (m_shoot.get() == DoubleSolenoid.Value.kReverse) {
				m_shoot.set(DoubleSolenoid.Value.kForward);
			}
			
		} else if (!m_fire.get()) {
			m_frisLoop = false;
		}
		
		//Frisbee Go
		if (m_frisStop.get() && !m_frisStopLoop) {
			m_frisStopLoop = true;
			m_motor_mode = 0;
		} else if (!m_frisStop.get()) {
			m_frisStopLoop = false;
		}
		
		if (m_frisSpeed.get() && !m_frisSpdLoop) {
			m_motor_mode++;
			m_frisSpdLoop = true;
			if (m_motor_mode > 2) {
				m_motor_mode = 0;
			}
			if (m_motor_mode >= 1 && m_motor_mode <= 2) {
			m_rpm = 100 * m_motor_mode;
			m_demand = calculateDemand(m_rpm);
			
			m_frisbee.set(ControlMode.Velocity, m_demand);
			}
		}
		if (m_motor_mode == 0) {
			m_rpm = 0.0;
			m_frisbee.set(ControlMode.PercentOutput, 0);
		}
		
		
		//FRISBEE TURN TABLE
		m_rTrig = m_joystick.getRawAxis(3);
		m_lTrig = m_joystick.getRawAxis(2);
		m_pow = 0;
		if (m_rTrig == 1 && m_lTrig == 0) {
			m_pow = 0.5;
		} else if (m_lTrig == 1 && m_rTrig == 0) {
			m_pow = -0.5;
		} else {
			m_pow = 0;
		}
		m_frisTurn.set(ControlMode.PercentOutput, m_pow);
		
	}
	public void Report() {
		SmartDashboard.putNumber("Turn Table Value", m_pow);
		SmartDashboard.putNumber("RPM", m_rpm);
		SmartDashboard.putNumber("RPM demand", m_demand);
		SmartDashboard.putNumber("Motor Mode", m_motor_mode);
	}

}
