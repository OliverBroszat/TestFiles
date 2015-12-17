import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SpecialBlock extends Block {
	private String special;
	private Color color;
	private BufferedImage image;

	public SpecialBlock(String special) {
		this.special = special;
		boolean validLocation = true;

		do {
			validLocation = true;
			setX(((int) (Math.random() * 25)) * Spielfeld.BLOCKWIDTH + 25);
			setY(((int) (Math.random() * 25)) * Spielfeld.BLOCKSIZE + 25);

			if (Collision.specialBlockCollision(this)) {
				validLocation = false;
			}
		} while (!validLocation);

		setColor(Color.pink);
	}

	public SpecialBlock() {
		this("SpecialBlock");
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(String imageString) {
		try {
			image = ImageIO.read(new File("res/" + imageString + ".png"));
		} catch (Exception e) {
			System.out.println("Image konnte nicht geladen werden.");
		}
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
