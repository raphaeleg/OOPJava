/**
 * This is the Robot class
 * 
 * @author Raphaele Guillemot
 */

public class Robot {
	String name;
	int power;		// power of robot's attacks
	int lifeIndex;	// robot's current health
	
	/**
	 * Robot constructor
	 * 
	 * @param name, a robot's name
	 * @param power, a robot's power
	 * @param lifeIndex, a robot's health
	 */
	public Robot(String name, int power, int lifeIndex){
		this.name = name;
		this.power = power;
		this.lifeIndex = lifeIndex;
	}
	
	/**
	 * This method returns the name of the robot
	 * 
	 * @return name, name of robot as a string
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method is called whenever the robot 
	 * takes damage of a certain amount
	 * 
	 * @param amount, takes an integer of the damage amount
	 */
	public void damage(int amount) {
		System.out.println(name+" takes a damage of "+amount+"!");
		lifeIndex=lifeIndex-amount;
	}
	
	/**
	 * This method is called whenever the robot attacks
	 * 
	 * @return power, an integer of the robot's power
	 */
	public int attack() {
		System.out.println(name+" launches an attack!");
		return power;
	}
	
	/**
	 * Checks whether the robot has no more health
	 * 
	 * @return boolean, true if its life drops to zero or below.
	 */
	public boolean isBroken() {
		if (lifeIndex <= 0){
			return true;
		}
		else {
			return false;
		}
	}
}