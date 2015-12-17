import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class AI extends Player {
	ArrayList<Point> optimalPath;
	ArrayList<Point> actualPath;
	private String lastMove = "";
	
	//delete me
	static boolean gelbInUse = false;

	public AI() {
		if(!gelbInUse){
		setColor(Color.YELLOW);
		gelbInUse = true;
		
		} else {
			setColor(Color.RED);
		}
		optimalPath = new ArrayList<Point>();
		actualPath = new ArrayList<Point>();
	}
	
	public void newDirection(){
		AI tmpAI = copy();
		tmpAI.moveUp();
		if(!Collision.fullCollision(tmpAI) && !tmpAI.lastMove.equals("up") && !lastMove.equals("left")){
			moveUp();
			lastMove = "up";
		} else {
			tmpAI = copy();
			tmpAI.moveLeft();
			if(!Collision.fullCollision(tmpAI) && !tmpAI.lastMove.equals("left")){
				moveLeft();
				lastMove = "left";
			} else {
				tmpAI = copy();
				tmpAI.moveRight();
				if(!Collision.fullCollision(tmpAI) && !tmpAI.lastMove.equals("right")){
					moveRight();
					lastMove = "right";
				} else {
					moveDown();
				}
			}
		}
		
	}

	private FoodBlock getFood() {

		int i = 0;
		while (!(Spielfeld.specialList.get(i).getClass()
				.isInstance(new FoodBlock()))) {
			i++;
		}

		return (FoodBlock) Spielfeld.specialList.get(i);
	}
	
	public AI copy(){
		AI tmp = new AI();
		tmp.bloeckeList.get(0).setX(getFirst().getX());
		tmp.bloeckeList.get(0).setY(getFirst().getY());
		tmp.lastMove = lastMove;
		
		tmp.setDirection(getDirection());
		
		return tmp;
	}

	@Override
	public void move() {
			getB().setLastLoccation(getB().getX(), getB().getY());
			AI tmpPlayer = copy();
			System.out.println("move");

			if (getDirection() != null) {
				if (getDirection().equals("up")) {
					System.out.println("tmp UP");
					tmpPlayer.moveUp();
					if (tmpPlayer.getB().getY() < 25) {
						tmpPlayer.getB().setY(Spielfeld.HEIGHT * Spielfeld.SCALE);
					}

					if (!(Collision.fullCollision(tmpPlayer))) {
						System.out.println("up");
						moveUp();
						if (getB().getY() < 25) {
							getB().setY(Spielfeld.HEIGHT * Spielfeld.SCALE);
						}
					} else { newDirection();}
				} else if (getDirection().equals("left")) {
					System.out.println("tmp LEFT");
					tmpPlayer.moveLeft();
					if (tmpPlayer.getB().getX() < 0) {
						tmpPlayer.getB().setX(
								Spielfeld.LENGTH * Spielfeld.SCALE);
					} 
					if (!(Collision.fullCollision(tmpPlayer))) {
						System.out.println("left");
						moveLeft();
						if (getB().getX() < 0) {
							getB().setX(Spielfeld.LENGTH * Spielfeld.SCALE);
						} 
					} else { newDirection(); }
				} else if (getDirection().equals("down")) {
					System.out.println("tmp DOWN");
					tmpPlayer.moveDown();
					if (tmpPlayer.getB().getY() > (Spielfeld.HEIGHT * Spielfeld.SCALE) - 25) {
						tmpPlayer.getB().setY(25);
					}

					if (!(Collision.fullCollision(tmpPlayer))) {
						System.out.println("down");
						moveDown();
						if (getB().getY() > (Spielfeld.HEIGHT * Spielfeld.SCALE) - 25) {
							getB().setY(25);
						} 
					} else { newDirection(); }
				} else if (getDirection().equals("right")) {
					System.out.println("tmp RIGHT");
					tmpPlayer.moveRight();
					if (tmpPlayer.getB().getX() > (Spielfeld.LENGTH * Spielfeld.SCALE)) {
						tmpPlayer.getB().setX(0);
					}

					if (!(Collision.fullCollision(tmpPlayer))) {
						System.out.println("right");
						moveRight();
						if (getB().getX() > (Spielfeld.LENGTH * Spielfeld.SCALE)) {
							getB().setX(0);
						} 
					} else { newDirection(); }
				}
				for (int i = 1; i < bloeckeList.size(); i++) {
					Block tempBlock = bloeckeList.get(i);
					tempBlock.setLastLoccation(tempBlock.getX(),
							tempBlock.getY());
					tempBlock.setLoccation(bloeckeList.get(i - 1).getLastX(),
							bloeckeList.get(i - 1).getLastY());
				}
			}
			System.out.println("Direction is: " + getDirection());
	}

	public void findPath() {
		if (getFood().getX() > bloeckeList.get(0).getX()) {
			setDirection("right");
		} else if (getFood().getX() < bloeckeList.get(0).getX()) {
			setDirection("left");
		} else {
			findPathY();
		}
	}

	public void findPathY() {
		if (getFood().getY() > bloeckeList.get(0).getY()) {
			setDirection("down");
		} else if (getFood().getY() < bloeckeList.get(0).getY()) {
			setDirection("up");
		}
	}
}
