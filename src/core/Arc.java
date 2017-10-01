package core;

import java.util.*;

import base.Descripteur;

public class Arc {
	private int numSommetSource;
	private int numSommetDestination;
	private int numZoneSuccesseur;
	private Descripteur descripteurDeLArete;
	private int longeurRoute;
	private ArrayList<Segment> tabDeltaSegment;

	public Arc(int numSommetSource, int numSommetDestination, int numZoneSuccesseur, int longeurRoute,
			Descripteur descripteurDeLArete) {
		this.numSommetSource = numSommetSource;
		this.numSommetDestination = numSommetDestination;
		this.numZoneSuccesseur = numZoneSuccesseur;
		this.longeurRoute = longeurRoute;
		this.descripteurDeLArete = descripteurDeLArete;
		tabDeltaSegment = new ArrayList<Segment>();
	}

	public void ajouterSegment(float debutLongitude, float debutLatitude, float finLongitude, float finLatitude) {
		this.tabDeltaSegment.add(new Segment(debutLongitude, debutLatitude, finLongitude, finLatitude));
	}

	public float recupSegment(int numSegm, int numeroValeurARecuperer) {
		if (numeroValeurARecuperer == 0) {
			return this.tabDeltaSegment.get(numSegm).getDebutLongitude();
		} else if (numeroValeurARecuperer == 1) {
			return this.tabDeltaSegment.get(numSegm).getDebutLatitude();
		} else if (numeroValeurARecuperer == 2) {
			return this.tabDeltaSegment.get(numSegm).getFinLongitude();
		} else {
			return this.tabDeltaSegment.get(numSegm).getFinLatitude();
		}
	}

	public int getNumSource() {
		return numSommetSource;
	}

	public int getNumDest() {
		return numSommetDestination;
	}

	public Descripteur getDescripteur() {
		return descripteurDeLArete;
	}

	public int getLongueur() {
		return longeurRoute;
	}

	public ArrayList<Segment> getTableauDeSegment() {
		return tabDeltaSegment;
	}

	public int getNombreDeSegment() {
		return tabDeltaSegment.size();
	}

	public Segment getSegmentAemplacement(int numEmplacement) {
		return tabDeltaSegment.get(numEmplacement);
	}

	public int getNumzone() {
		return this.numZoneSuccesseur;
	}
}