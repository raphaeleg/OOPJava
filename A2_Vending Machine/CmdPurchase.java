import java.util.ArrayList;
import java.util.Collections;

public class CmdPurchase implements Command {

	@Override
	public String execute(VendingMachine v, String cmdPart) {
		String targetProduct = cmdPart;
		ArrayList<Product> products = v.getProducts();
		int totalInsertedCoins = v.getTotalCoins();

		// initialisation
		Product productObj = null;
		String name = "";
		int price = 0;
		int stock = 0;
		ArrayList<Integer> outputtedChange = new ArrayList<Integer>();

		// getting product from vending machine list
		for (Product p : products) {
			String current = p.getName();
			if (current.equals(targetProduct)) {
				productObj = p;
				name = current;
				price = p.getPrice();
				stock = p.getQuantity();
				break;
			}
		}

		// if not enough money, don't proceed
		if (totalInsertedCoins < price)
			return String.format("Not enough credit to buy %s! Inserted $%d but needs $%d.", targetProduct,
					totalInsertedCoins, price);

		// if still in stock
		else if (stock <= 0)
			return String.format("%s is out of stock!", name);

		else {
			// -1 of product in vending machine & clear slot
			v.clearSlot();
			productObj.buyItem();

			// change
			int changeTotal = totalInsertedCoins - price;
			if (changeTotal == 0) {
				v.clearSlot();
				return String.format("Dropped %s. Paid $%d. No change.", targetProduct, price);
			}
			ArrayList<Integer> validCoins = new ArrayList<Integer>();
			validCoins.add(10);
			validCoins.add(5);
			validCoins.add(2);
			validCoins.add(1);

			int current = 0;
			while (changeTotal > 0) {
				int largestCoin = validCoins.get(current);

				if (changeTotal >= largestCoin) {
					changeTotal -= largestCoin;
					outputtedChange.add(largestCoin);
				} else if (current<4)
					current++;
			}
			Collections.sort(outputtedChange);
		}
		String result = String.format("Dropped %s. Paid $%d. Your change:", targetProduct, totalInsertedCoins);
		for (int i = 0; i < outputtedChange.size(); i++) {
			if (i == 0) {
				result = result + " $" + outputtedChange.get(0);
			} else {
				result = result + ", $" + outputtedChange.get(i);
			}
		}
		result = result + ".";
		return result;
	}
}