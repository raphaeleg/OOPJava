import java.util.ArrayList;
import java.util.Collections;

public class CmdRejectCoins implements Command {

	@Override
	public String execute(VendingMachine v, String cmdPart) {
		int c = v.getTotalCoins();
		
		if (c <= 0) {
			return "Rejected no coin!";
		}
		
		ArrayList<Integer> cArray = v.getInsertedCoins();
		Collections.sort(cArray);
		
		String result = "Rejected";
		for(int i = 0; i < cArray.size(); i++) {
			if(i == 0) {
				result = result+ " $"+cArray.get(0);
			} else {
			    result = result+ ", $"+cArray.get(i);
			}
		}
		
		result = result+". $"+c+" in total.";
		
		v.clearSlot();
		
		return result;
	}
}