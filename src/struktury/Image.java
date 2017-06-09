package struktury;

public class Image {
	
	static public HSB [][] image = new HSB [460][650];
	
	public Image(){
		for(int i=0; i<460; i++)
			for(int j=0; j<650; j++)
				image[i][j]=new HSB(0.0f,0.0f,1.0f);
	}

	
}
