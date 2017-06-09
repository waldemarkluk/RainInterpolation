package struktury;

import java.util.ArrayList;
import java.util.List;

public class DoubleTriangle {
	
	public List<DoublePoint> dt=new ArrayList<DoublePoint>();
	
	public DoubleTriangle(DoublePoint dp1, DoublePoint dp2, DoublePoint dp3){
		dt.add(dp1);
		dt.add(dp2);
		dt.add(dp3);
		DoublePoint dp4=null;
		try {
			dp4 = (DoublePoint) dp1.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dt.add(dp4);
	}
}
