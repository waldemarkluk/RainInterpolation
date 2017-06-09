package struktury;

public class DoublePoint {

	public Double d1;
	public Double d2;

	public DoublePoint(double d1, double d2){
		this.d1=d1;
		this.d2=d2;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		double _d1 = this.d1;
		double _d2 = this.d2;
		return new DoublePoint(_d1, _d2);
	}
}
