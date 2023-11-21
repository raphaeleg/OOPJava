/**
 * This is a PowerBot class, a subclass of the Robot class
 * 
 * @author Raphaele Guillemot
 */

public class PowerBot extends Robot {
	boolean isBoosted=false;	// keeps track of whether the robot is boosted

	/**
	 * PowerBot constructor taken from Robot constructor
	 * 
	 * @param name, a robot's name
	 * @param power, a robot's power
	 * @param lifeIndex, a robot's health
	 */
	public PowerBot(String name, int power, int lifeIndex) {
		super(name, power, lifeIndex);
	}
	
	/**
	 * PowerBot can boost itself to deal twice the damage
	 */
	public void boost() {
		isBoosted = true;
		System.out.println(name+" boosts itself!");
	}
	
	/**
	 * PowerBot can attack twice the damage if boosted
	 * 
	 * @return power, an integer representing it's damage
	 */
	public int attack() {
		if(isBoosted) {
			System.out.println(name+" makes a heavy strike!");
			isBoosted=false;
			return power*2;
		}
		else {
			System.out.println(name+" strikes hard!");
			return power;
		}
	}
}
