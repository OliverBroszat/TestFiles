import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
	protected List<Block> bloeckeList = new ArrayList<Block>();
	private Block b;
	private String direction;
	private static int ID = 1;
	private int playerID;
	private long old;
	private Color color;

	public Block getB() {
		return b;
	}

	public void setB(Block b) {
		this.b = b;
	}

	/**
	 * Erstellt den ersten Block des Spielers und setzt die entsprechenden
	 * Werte.
	 */
	public Player() {
		b = new Block();
		bloeckeList.add(b);
		// TODO spawn pattern verbessern
		b.setX(((int) (Math.random() * 25)) * Spielfeld.BLOCKWIDTH + 25);
		b.setY(((int) (Math.random() * 25)) * Spielfeld.BLOCKSIZE + 25);
		setPlayerID(ID);
		incrementID();
		old = System.currentTimeMillis();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getPunkte() {
		return bloeckeList.size() - 1;
	}

	public void removeLastBlock() {
		long now = System.currentTimeMillis();
		if ((old - now) <= -100) {
			System.out.println("ID " + playerID + " :" + bloeckeList.size());
			bloeckeList.remove((bloeckeList.size() - 1));
			old = now;
		}
	}

	protected static void resetID() {
		ID = 1;
	}

	/**
	 * Gibt ein bestimmtes Block Element aus der BlockList zurück
	 * 
	 * @param i
	 *            index des zurückgegebenen Blocks
	 * @return bestimmter Block
	 */
	public Block getListBlock(int i) {
		return bloeckeList.get(i);
	}

	/**
	 * gibt die Länge der Spielerschlange zurück
	 * 
	 * @return Länge der Spielerschlange
	 */
	public int getPlayerLength() {
		return (bloeckeList.size());
	}

	public Block getFirst() {
		return bloeckeList.get(0);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	private void incrementID() {
		Player.ID++;
	}

	/**
	 * bewegung des SPielers
	 * 
	 * @param c
	 *            Richtung in die sich der Spieler bewegen soll
	 */
	public void move() {
		if (direction != null) {
			b.setLastLoccation(b.getX(), b.getY());
			if (direction.equals("up")) {
				moveUp();
			} else if (direction.equals("left")) {
				moveLeft();
			} else if (direction.equals("down")) {
				moveDown();
			} else if (direction.equals("right")) {
				moveRight();
			}
			for (int i = 1; i < bloeckeList.size(); i++) {
				Block tempBlock = bloeckeList.get(i);
				tempBlock.setLastLoccation(tempBlock.getX(), tempBlock.getY());
				tempBlock.setLoccation(bloeckeList.get(i - 1).getLastX(),
						bloeckeList.get(i - 1).getLastY());
			}
		}
	}

	public void moveLeft() {
		b.setX(b.getX() - 25);
		if (b.getX() < 0) {
			b.setX(Spielfeld.LENGTH * Spielfeld.SCALE);
		}
	}

	public void moveRight() {
		b.setX(b.getX() + 25);
		if(b.getX() > (Spielfeld.LENGTH * Spielfeld.SCALE)){
			b.setX(0);
		}
	}

	public void moveDown() {
		b.setY(b.getY() + 25);
		if (b.getY() > (Spielfeld.HEIGHT * Spielfeld.SCALE) - 25) {
			b.setY(25);
		}
	}

	public void moveUp() {
		b.setY(b.getY() - 25);
		if (b.getY() < 25) {
			b.setY(Spielfeld.HEIGHT * Spielfeld.SCALE);
		}
	}

	public void addBlock() {
		Block block = new Block();
		bloeckeList.add(block);
		Block tempBlock = bloeckeList.get(0);
		block.setLoccation(tempBlock.getX(), tempBlock.getY() + 25);
	}
}