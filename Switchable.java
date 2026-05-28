package game;

public interface Switchable {
	/**
	 * @return true (on) or false (off)
	 */
	public boolean switchedOn();
	
	/**
	 * Switches interactable object on or off
	 */
	public void interact();
}
