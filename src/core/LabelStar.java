package core;

public class LabelStar extends Label {
	public LabelStar(boolean marquage, float cout, int pere, Noeud courant) {
		super(marquage, cout, pere, courant);

	}

	public int compareTo(Label other) {
		if (this.getCout() + this.getEstimation() < other.getCout() + other.getEstimation()) {
			return -1;
		} else {
			return 0;
		}
	}
}
