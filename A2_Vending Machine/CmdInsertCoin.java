public class CmdInsertCoin implements Command {

	@Override
	public String execute(VendingMachine v, String cmdPart) {
		Integer c = Integer.valueOf(cmdPart);
		v.insertCoin(c);
		int total = v.getTotalCoins();
		return String.format("Inserted a $%d coin. $%d in total.",c, total);
	}
}