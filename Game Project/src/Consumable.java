public class Consumable extends Item {

	protected int restoreAmt;
	
	public Consumable(String name, int restoreAmt) {
		super(name);
		this.restoreAmt = restoreAmt;
	}

	@Override
	public void use(Character c) {
		c.setHealth(c.getHealth() + restoreAmt);
	}	
	
}
