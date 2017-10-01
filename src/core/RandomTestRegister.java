package core;

public class RandomTestRegister {
	private int origine;
	private int destination;

	public RandomTestRegister(int origine, int destination) {
		this.origine = origine;
		this.destination = destination;
	}

	public RandomTestRegister() {
	}

	public void setorigne(int or) {
		this.origine = or;
	}

	public void setdest(int dest) {
		this.destination = dest;
	}

	public int getorigne() {
		return this.origine;
	}

	public int getdest() {
		return this.destination;
	}
}
