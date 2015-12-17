import java.awt.Rectangle;

public class Collision {

	public static boolean fullCollision(Player collisionPlayer) {
		boolean tmp = false;

		tmp = selfCollision(collisionPlayer);

		if (tmp == false)
			tmp = opponentCollision(collisionPlayer);

		if (tmp == false) {
			tmp = specialBlockCollision(collisionPlayer);
		}

		return tmp;
	}

	public static boolean selfCollision(Player collisionPlayer) {
		boolean tmp = false;
		Rectangle headRect = new Rectangle(collisionPlayer.getFirst().getX(),
				collisionPlayer.getFirst().getY(), 25, 25);

		// Collision mit sich selbst
		for (int j = 1; j < collisionPlayer.getPlayerLength(); j++) {
			Block collisionPlayerBlock = collisionPlayer.getListBlock(j);
			Rectangle collisionPlayerRect = new Rectangle(
					collisionPlayerBlock.getX(), collisionPlayerBlock.getY(),
					25, 25);
			if (collisionPlayerRect.intersects(headRect)) {
				tmp = true;
				j = collisionPlayer.getPlayerLength();
			}
		}
		return tmp;
	}

	public static boolean opponentCollision(Player collisionPlayer) {
		boolean tmp = false;
		Rectangle headRect = new Rectangle(collisionPlayer.getFirst().getX(),
				collisionPlayer.getFirst().getY(), 25, 25);

		// Collision mit Gegner
		for (int j = 0; j < Spielfeld.playerList.size(); j++) {
			// Es soll nicht gefragt werden, ob der Spieler mit sich selbst
			// kollidiert
			// Muss vorher gemachst werden, da sonst Probleme mit der Kopf
			// Kollision auftreten können
			if (Spielfeld.playerList.get(j).getPlayerID() != collisionPlayer
					.getPlayerID()) {
				Player collisionOpponent = Spielfeld.playerList.get(j);

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
						k = collisionOpponent.getPlayerLength();
					}
				}
			}
		}
		return tmp;
	}

	public static boolean specialBlockCollision(Player collisionPlayer) {
		Boolean tmp = false;

		Rectangle headRect = new Rectangle(collisionPlayer.getFirst().getX(),
				collisionPlayer.getFirst().getY(), 25, 25);

		// Collision mit SpecialBlöcken
		for (int j = 0; j < Spielfeld.specialList.size(); j++) {
			SpecialBlock tempBlock = Spielfeld.specialList.get(j);

			if (!(tempBlock.getSpecial().equals("food"))) {
				Rectangle sBlockRect = new Rectangle(tempBlock.getX(),
						tempBlock.getY(), 25, 25);
				if (headRect.intersects(sBlockRect)) {
					tmp = true;
					j = Spielfeld.specialList.size();
				}
			}
		}
		return tmp;
	}

	public static boolean specialBlockCollision(Block b) {
		Boolean tmp = false;

		Rectangle headRect = new Rectangle(b.getX(), b.getY(), 25, 25);

		// Collision mit SpecialBlöcken
		for (int j = 0; j < Spielfeld.specialList.size(); j++) {
			SpecialBlock tempBlock = Spielfeld.specialList.get(j);

			Rectangle sBlockRect = new Rectangle(tempBlock.getX(),
					tempBlock.getY(), 25, 25);
			if (headRect.intersects(sBlockRect)) {
				tmp = true;
				j = Spielfeld.specialList.size();
			}
		}
		return tmp;
	}
}
