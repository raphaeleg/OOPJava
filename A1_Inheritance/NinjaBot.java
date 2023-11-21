/**
 * This is a NinjaBot class, a subclass of the Robot class
 * 
 * @author Raphaele Guillemot
 */

public class NinjaBot extends Robot {
	boolean isHidden=false;		// keeps track of whether the robot is hidden

	/**
	 * NinjaBot constructor taken from Robot constructor
	 * 
	 * @param name, a robot's name
	 * @param power, a robot's power
	 * @param lifeIndex, a robot's health
	 */
	public NinjaBot(String name, int power, int lifeIndex) {
		super(name, power, lifeIndex);
	}
	
	/**
	 * NinjaBot can activate hide to avoid the next attack
	 */
	public void hide() {
		isHidden = true;
		System.out.println(name+" hides itself from attacks!");
	}
	
	/**
	 * NinjaBot takes damage if they're not hiding
	 * 
	 * @param amount, takes an integer of the damage amount
	 */
	public void damage(int amount) {
		if (isHidden) {
			System.out.println(name+" hides from the attack!");
			isHidden=false;
		}
		else {
			System.out.println(name+" takes a damage of "+amount+"!");
			lifeIndex=lifeIndex-amount;
		}
	}

}
