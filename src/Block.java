
public class Block {
	private int x;
	private int y;
	private int lastX;
	private int lastY;
	
	//Ich bin ein klener Kommentar ohne Sinn
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}
	
	public void setLoccation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setLastLoccation(int x, int y){
		this.lastX = x;
		this.lastY = y;
	}
	
	
}
