import java.awt.Rectangle;

public class Enemy extends Rectangle {
	boolean hasCrossed;
	String animal;
	public Enemy(int x, int y, int wid, int hei, boolean cross, String type) {
		super(x, y, wid, hei);
		hasCrossed = cross;
		animal = type;
	}

	public void cross() {
		hasCrossed = true;
	}

	public boolean hasCrossed() {
		return hasCrossed;
	}
	
	public String getType() {
		return animal;
	}
}
