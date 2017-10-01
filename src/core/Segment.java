package core;

public class Segment {
	private float debutLongitude;
	private float debutLatitude;
	private float finLongitude;
	private float finLatitude;

	public Segment(float debutLongitude, float debutLatitude, float finLongitude, float finLatitude) {
		this.debutLongitude = debutLongitude;
		this.debutLatitude = debutLatitude;
		this.finLongitude = finLongitude;
		this.finLatitude = finLatitude;

	}

	public float getDebutLongitude() {
		return this.debutLongitude;
	}

	public float getDebutLatitude() {
		return this.debutLatitude;
	}

	public float getFinLongitude() {
		return this.finLongitude;
	}

	public float getFinLatitude() {
		return this.finLatitude;
	}

}