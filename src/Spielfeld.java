import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Spielfeld extends JFrame implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	static final int BLOCKWIDTH = 50;
	static final int BLOCKHEIGHT = 30;
	static final int BLOCKSIZE = 25;
	static final int LENGTH = BLOCKWIDTH * BLOCKSIZE;
	static final int HEIGHT = BLOCKHEIGHT * BLOCKSIZE;
	static final int SCALE = 1;
	private static final int playerAmmount = 0;
	private static final int AiAmmount = 2;
	static List<SpecialBlock> specialList;
	static List<Player> playerList;
	private boolean running;
	private BufferStrategy strategy;
	private Thread t;
	private String[] specialBlocks = { "SteinBlock" };

	public Spielfeld() {
		setTitle("Schlangenspiel");
		setVisible(true);
		setSize(LENGTH * SCALE, HEIGHT * SCALE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		specialList = new ArrayList<SpecialBlock>();
		startGame();
	}

	private void startGame() {
		playerList = new ArrayList<Player>();
		// TODO vernünftige SPielererstellung
		for (int i = 0; i < playerAmmount; i++) {
			newPlayer();
		}
		
		for (int i = 0; i < AiAmmount; i++) {
			newAI();
		}

		// Threadstuff
		t = new Thread(this);
		t.start();
		running = true;

		createBufferStrategy(3);
		strategy = getBufferStrategy();

		specialList.add(new FoodBlock());
		addKeyListener(this);
	}

	/**
	 * Erstellt einen neuen Spieler
	 */
	public void newPlayer() {
		Player player = new Player();
		
		switch (player.getPlayerID()) {
		case 1:
			player.setColor(Color.GREEN);
			break;
		case 2:
			player.setColor(Color.MAGENTA);
			break;
		default:
			player.setColor(Color.gray);
			break;
		}
		playerList.add(player);
	}
	
	public void newAI(){
		AI ai = new AI();
		playerList.add(ai);
	}

	public void render() {
		if(strategy != null){
			Graphics g = strategy.getDrawGraphics();

			renderBackground(g);
			renderPlayers(g);
			renderSpecialBlocks(g);

			strategy.show();
		}
	}

	/**
	 * Rendermethode für das Spielfeld Hintergrund, Gelände
	 */
	public void renderBackground(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, LENGTH * SCALE, HEIGHT * SCALE);

		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.setColor(Color.GREEN);
		
		//TODO neuer punktebildschirm
		g.drawString("Player 1: " + playerList.get(0).getPunkte(), 30, 70);

//		g.setColor(Color.MAGENTA);
//		g.drawString("Player 1: " + playerList.get(1).getPunkte(),
//				(LENGTH * SCALE) - 200, 70);
	}

	public void spawnNewSpecial() {
		int i = (int) (Math.random() * 50);
		if (i == 1){
			specialList.add(new SteinBlock());
		}
	}

	/**
	 * Rendermethode für die SpielerBlöcke
	 */
	public void renderPlayers(Graphics g) {
		for (int i = 0; i < playerList.size(); i++) {
			Player player = playerList.get(i);
			int length = player.getPlayerLength();

			for (int j = 0; j < length; j++) {
				g.setColor(player.getColor());
				g.fillRect(player.getListBlock(j).getX(), player
						.getListBlock(j).getY(), 25, 25);
			}
		}
	}

	/**
	 * Rendermethode für Specialblocks
	 */
	public void renderSpecialBlocks(Graphics g) {
		for (int i = 0; i < specialList.size(); i++) {
			SpecialBlock tempBlock = specialList.get(i);
			g.setColor(((SpecialBlock) tempBlock).getColor());

			if (tempBlock.getImage() != null) {
				g.drawImage(tempBlock.getImage(), tempBlock.getX(),
						tempBlock.getY(), null);
			} else {
				g.fillRect(tempBlock.getX(), tempBlock.getY(), 25, 25);
			}

		}
	}

	public void gameLoop() {
		long start = System.currentTimeMillis();
		while (running) {

			long now = System.currentTimeMillis();

			if ((now - start) > 100) {
				start = System.currentTimeMillis();
				collision();
				movePlayers();
				spawnNewSpecial();
			}
			render();
		}
	}

	public void foodCheat() {
		for (int j = 0; j < 100; j++) {
			generateNewFoodBlock();
		}
	}
	
	public void stoneCheat(){
		for (int i = 0; i < 100; i++) {
			spawnNewSpecial();
		}
	}
	
	public static int[] getFoodLocation(){
		int array[] = {specialList.get(0).getX(), specialList.get(0).getY()};
		return array;
	}

	public static boolean collision() {
		boolean tmp = false;
	
		for (int i = 0; i < playerList.size(); i++) {
			Player collisionPlayer = playerList.get(i);
			Rectangle headRect = new Rectangle(collisionPlayer.getFirst()
					.getX(), collisionPlayer.getFirst().getY(), 25, 25);

			// Collision mit sich selbst
			for (int j = 1; j < collisionPlayer.getPlayerLength(); j++) {
				Block collisionPlayerBlock = collisionPlayer.getListBlock(j);
				Rectangle collisionPlayerRect = new Rectangle(
						collisionPlayerBlock.getX(),
						collisionPlayerBlock.getY(), 25, 25);
				if (collisionPlayerRect.intersects(headRect)) {
					tmp = true;
					collisionPlayer.removeLastBlock();
					i = playerList.size();
				}
			}
			
			specialBlockCollision(collisionPlayer);

			// Collision mit Gegner
			for (int j = 0; j < playerList.size(); j++) {
				// Es soll nicht gefragt werden, ob der Spieler mit sich selbst
				// kollidiert
				// Muss vorher gemachst werden, da sonst Probleme mit der Kopf
				// Kollision auftreten können
				if (j != i) {
					Player collisionOpponent = playerList.get(j);

					// Collsion mit dem Gegner
					// TODO Lösung für Kopfkollision finden
					for (int k = 1; k < collisionOpponent.getPlayerLength(); k++) {
						Block collisionOpponentBlock = collisionOpponent
								.getListBlock(k);
						Rectangle collisionOpponentRect = new Rectangle(
								collisionOpponentBlock.getX(),
								collisionOpponentBlock.getY(), 25, 25);
						if (collisionOpponentRect.intersects(headRect)) {
							tmp = true;
							collisionPlayer.removeLastBlock();
							i = collisionOpponent.getPlayerLength();
						}
					}
				}
			}
		}
		return tmp;
	}

	public static void specialBlockCollision(Player collisionPlayer) {
		Rectangle headRect = new Rectangle(collisionPlayer.getFirst()
				.getX(), collisionPlayer.getFirst().getY(), 25, 25);
		
		// Collision mit SpecialBlöcken
		for (int j = 0; j < specialList.size(); j++) {
			SpecialBlock tempBlock = specialList.get(j);
			Rectangle sBlockRect = new Rectangle(tempBlock.getX(),
					tempBlock.getY(), 25, 25);
			if (headRect.intersects(sBlockRect)) {
				if (tempBlock.getSpecial().equals("food")) {	
					generateNewFoodBlock();
					collisionPlayer.addBlock();
				} else if (tempBlock.getSpecial().equals("SteinBlock")) {
					collisionPlayer.removeLastBlock();
				}
				specialList.remove(j);
			}
		}
	}

	private static void generateNewFoodBlock() {
		specialList.add(new FoodBlock());
	}

	public void movePlayers() {
		for (int i = 0; i < playerList.size(); i++) {
			if(playerList.get(i).getClass().toString().contains("AI")){
				((AI) playerList.get(i)).findPath();
			}
			playerList.get(i).move();
		}
	}

	public void addPlayer(Player p) {
		playerList.add(p);
	}

	@Override
	public void run() {
		gameLoop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/**
		 * Alles rund um Threads und den gameLoop
		 */

		if (e.getKeyChar() == 'w') {
			if (playerList.get(0).getDirection() != "down")
				playerList.get(0).setDirection("up");
		} else if (e.getKeyChar() == 'a') {
			if (playerList.get(0).getDirection() != "right")
				playerList.get(0).setDirection("left");
		} else if (e.getKeyChar() == 's') {
			if (playerList.get(0).getDirection() != "up")
				playerList.get(0).setDirection("down");
		} else if (e.getKeyChar() == 'd') {
			if (playerList.get(0).getDirection() != "left")
				playerList.get(0).setDirection("right");
		}

		if (e.getKeyChar() == 'p') {
			if (playerList.get(1).getDirection() != "down")
				playerList.get(1).setDirection("up");
		} else if (e.getKeyChar() == 'l') {
			if (playerList.get(1).getDirection() != "right")
				playerList.get(1).setDirection("left");
		} else if (e.getKeyChar() == 'ö') {
			if (playerList.get(1).getDirection() != "up")
				playerList.get(1).setDirection("down");
		} else if (e.getKeyChar() == 'ä') {
			if (playerList.get(1).getDirection() != "left")
				playerList.get(1).setDirection("right");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '1') {
			foodCheat();
		}
		
		if (e.getKeyChar() == '2'){
			stoneCheat();
		}
	}
}
