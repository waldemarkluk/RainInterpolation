package struktury;

import java.util.ArrayList;
import java.util.List;

public class DoubleEdge {
	
	public List<DoublePoint> dt=new ArrayList<DoublePoint>();	
	
	public DoubleEdge(DoublePoint dp1, DoublePoint dp2){
		dt.add(dp1);
		dt.add(dp2);
	}
}
