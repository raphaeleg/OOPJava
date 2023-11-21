public class RobotCombat {
	public static void main(String[] args) {
		PowerBot pBot = new PowerBot("Super Droid", 10, 15);
		NinjaBot nBot = new NinjaBot("Ninja Sakura", 10, 15);
		System.out.println("Now fighting: " + pBot.getName() + " vs. " +
				nBot.getName());
		int round = 0;
		while (!pBot.isBroken() && !nBot.isBroken()) {
			if (round % 2 == 0) {
				int attackAmount = pBot.attack();
				nBot.damage(attackAmount);
				if (round % 3 == 0) {
					nBot.hide();
				}
			} else {
				int attackAmount = nBot.attack();
				pBot.damage(attackAmount);
				if (round % 3 == 1) {
					pBot.boost();
				}
			}
			round++;
		}
		if (pBot.isBroken())
			System.out.println(nBot.getName() + " wins!");
		else
			System.out.println(pBot.getName() + " wins!");
	}
}