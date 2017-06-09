package struktury;

public class RainFall {
	static public double [][] opady = new double [450][316];
	static public double [][] opady_interpolowane = new double [450][316];
	static public int [][] zlewnia = new int [450][316];
	
	public RainFall(){
		for(int i=0; i<450; i++){
			for(int j=0; j<316; j++){
				opady[i][j]=0;
				opady_interpolowane[i][j]=0;
				zlewnia[i][j]=0;
			}
		}
	}
}
