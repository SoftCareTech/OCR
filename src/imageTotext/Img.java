package imageTotext;

public class Img {
	int cols;
	int rows;
	int color;
	int point;
	boolean value[];
	
	public Img(int rows, int cols, int color, boolean[] value, int point) {
		super();
		this.cols = cols;
		this.rows = rows;
		this.color = color;
		this.value = value;
		this.point = point;
	}
	
	public Img() {
		
	}	
}
