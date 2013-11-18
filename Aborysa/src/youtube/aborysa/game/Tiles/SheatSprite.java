package youtube.aborysa.game.Tiles;

public class SheatSprite {
	private SpriteSheat sheat;
	private int sIndex, indexLength;
	private int imageIndex;

	public SheatSprite(SpriteSheat sheat, int sIndex, int indexL){
		this.sheat = sheat;
		this.indexLength= indexL;
		this.sIndex = sIndex;
	}
	public SpriteSheat getSheat(){
		return sheat;
	}
	public int getSindex(){
		return sIndex;
	}
	public int getIndexLength(){
		return indexLength;
	}
	public int getIndex(){
		return imageIndex;
	}
	public void setImageIndex(int index){
		imageIndex = sIndex + (index % indexLength);
	}
	public void advance(){
		imageIndex = sIndex + ( (imageIndex++) % indexLength);
	}
}