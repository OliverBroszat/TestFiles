import java.awt.Color;


public class FoodBlock extends SpecialBlock{

	public FoodBlock(String special) {
		super(special);
		setColor(Color.CYAN);
		setImage("FoodBlock");
	}
	
	public FoodBlock(){
		this("food");
	}
}
