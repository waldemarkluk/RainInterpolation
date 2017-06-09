import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import struktury.DoublePoint;
import struktury.DoubleTriangle;

public class Functions {

	public static double distance(double x1, double y1, double x2, double y2){
		double distance=0;
		if(x1==x2 && y1==y2)
			return distance;
		else{
			distance=Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
			return distance;
		}
	}
	
	public static double relativeAngle(double x, double y, double x1, double y1, double x2, double y2){
		double angle1 = Math.atan2(y1-y,x1-x);
		double angle2 = Math.atan2(y2-y,x2-x);
		double anglecritical = 0.0;
		if(angle1>0){
			anglecritical = angle1 - Math.PI;
			if(angle2<anglecritical)
				return angle2-angle1+2*Math.PI;
			else
				return angle2-angle1;
		}
		else{
			anglecritical = angle1 + Math.PI;
			if(angle2>anglecritical)
				return angle2-angle1-2*Math.PI;
			else
				return angle2-angle1;
		}
	}
	

	
	public static boolean pointInPolygon(double x, double y, List<DoublePoint> l){
		double angle=0;
		for(DoublePoint d: l){
			if(l.indexOf(d) == l.size()-1)
				continue;
			DoublePoint dn = l.get(l.indexOf(d) + 1);
			angle += Functions.relativeAngle(x, y, d.d1, d.d2, dn.d1, dn.d2);
		}
		if(Math.abs(angle)<0.001)
			return false;
		else 
			return true;
	}
	
	public static int delaunayAlgorithm(List<DoublePoint> PunktyPomiarowe){
		
		int n = 0;
		int[] x = null;
		int[] y = null;
		int[] z = null;
		
		if ((PunktyPomiarowe != null) && (PunktyPomiarowe.size() > 0))
	    {
			n = PunktyPomiarowe.size();

			x = new int[n];
			y = new int[n];
			z = new int[n];
			
			int i = 0;

			for(DoublePoint dp: PunktyPomiarowe){
				
				x[i] = dp.d1.intValue();
				y[i] = dp.d2.intValue();
				z[i] = (x[i] * x[i] + y[i] * y[i]);
				
				i++;
				
			}

	    }
		
//		List<DoubleTriangle> result = new ArrayList<DoubleTriangle>();


		for (int i = 0; i < n - 2; i++) {
			for (int j = i + 1; j < n; j++) {
				for (int k = i + 1; k < n; k++) 
				{
					if (j == k) {
						continue;
					}
					int xn = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i]) * (z[j] - z[i]);

					int yn = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i]) * (z[k] - z[i]);		

					int zn = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i]) * (y[j] - y[i]);
					boolean flag;
					if (flag = (zn < 0 ? 1 : 0) != 0) {
						for (int m = 0; m < n; m++) {// petla do sprawdzenia, czy w okregu opisanym na
													// trzech punktach znajduja sie inne punkty
							flag = (flag) && ((x[m] - x[i]) * xn + (y[m] - y[i]) * yn + (z[m] - z[i]) * zn <= 0);
						}

					}

					if (!flag)
					{
						continue;
					}                         // jeśli nie, to taki trójkąt dodajemy do wyniku
//					result.add(new DoubleTriangle(new DoublePoint(x[i], y[i]),
//							new DoublePoint(x[j], y[j]), new DoublePoint(x[k],y[k])));
					MyFrame.triangulation.add(new DoubleTriangle(new DoublePoint(x[i], y[i]),
							new DoublePoint(x[j], y[j]), new DoublePoint(x[k],y[k])));
				}	

			}

		}
		return 0;
	}
	
	public static int punktyPomiarowe(){
//		//punkty pomiarowe
		List<String> tekst = null;
		String cvsSplitBy = null;
		boolean first = true;
		double lo=0.0,la=0.0,lo2=0.0,la2=0.0;
		double a=169.179, b=-3120.93, c=-257.934, d=13244.4;
		double kat = -Math.PI/100.0;
		try {
			tekst = new ArrayList<String>(Files.readAllLines(FileSystems.getDefault().
					getPath("plik5.txt"), StandardCharsets.UTF_8));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cvsSplitBy = "  ";
		StringBuilder sb = new StringBuilder();
		first = true;
		lo=0.0;la=0.0;
		for(String s:tekst){
			String [] lola = s.split(cvsSplitBy);
			if(Double.parseDouble(lola[0])>18.7 && Double.parseDouble(lola[1])<51.5){
				lo = a*Double.parseDouble(lola[0])+b;
				la = c*Double.parseDouble(lola[1])+d;
				lo = lo*Math.cos(kat)-la*Math.sin(kat);
				la = lo*Math.sin(kat)+la*Math.cos(kat);
				
				lo/=2.0;la/=2.0;
//				g2d.setColor(Color.BLACK);
				for(ArrayList<DoublePoint> aldp: MyFrame.Zlewnie){
					if(Functions.pointInPolygon(lo, la, aldp)){
						MyFrame.PunktyPomiarowe.add(new DoublePoint(lo,la));
						if(first == true){
							first = false;
							continue;
						}
//						g2d.drawOval((int)lo, (int)la, 2, 2);
//						g2d.drawOval((int)lo, (int)la+315, 2, 2);
					}
				}
			}
		}
		return 0;
	}
	
	public static int zlewnie(){
		//zlewnie
		List<String> tekst = null;
		String cvsSplitBy = null;
		boolean first = true;
		double lo=0.0,la=0.0,lo2=0.0,la2=0.0;
		double a=169.179, b=-3120.93, c=-257.934, d=13244.4;
		double kat = -Math.PI/100.0;
		String [] zlewnie = {"zlewnie/1.txt","zlewnie/2.txt","zlewnie/3.txt","zlewnie/4.txt","zlewnie/5.txt",
				"zlewnie/6.txt","zlewnie/7.txt","zlewnie/8.txt","zlewnie/9.txt","zlewnie/10.txt",
				"zlewnie/11.txt","zlewnie/12.txt","zlewnie/13.txt","zlewnie/14.txt","zlewnie/15.txt"};
		int Licznik=0;
		for(String zlewnia:zlewnie){
			tekst = null;
			try {
				tekst = new ArrayList<String>(Files.readAllLines(FileSystems.getDefault().
				getPath(zlewnia), StandardCharsets.UTF_8));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			cvsSplitBy = ",";
			first = true;
			lo=0.0;la=0.0;lo2=0.0;la2=0.0;
			MyFrame.Zlewnie.add(new ArrayList<DoublePoint>());
			for(String s:tekst){
				String [] lola = s.split(cvsSplitBy);
				lo = Double.parseDouble(lola[0]);
				la = Double.parseDouble(lola[1]);
				lo/=2.0;la/=2.0;
				MyFrame.Zlewnie.get(MyFrame.Zlewnie.size() -1).add(new DoublePoint(lo,la));
				if(first == true){
					lo2=lo; la2=la;
					first = false;
					continue;
				}
//				Line2D line = new Line2D.Double(lo, la, lo2, la2);
//				Line2D line2= new Line2D.Double(lo, la+315, lo2, la2+315);
//				g2d.draw(line);
//				g2d.draw(line2);
				lo2=lo; la2=la;
			}
			Licznik++;
		}
		return 0;
	}
	
	public static void liczOpady(JTable wyniki){
		for(int i=1; i<16; i++){
			wyniki.setValueAt(Functions.round(MyFrame.opady[i]/MyFrame.powierzchnie[i],2), i, 1);
		}
	}
	
	public static void liczOpadyInterpolowane(JTable wyniki){
		
		for(int i=1; i<16; i++){
			wyniki.setValueAt(Functions.round(MyFrame.opady_interpolowane[i]/MyFrame.powierzchnie[i],2), i, 2);
			wyniki.setValueAt(Math.abs(Functions.round((double)wyniki.getValueAt(i, 2)-(double)wyniki.getValueAt(i, 1), 2)), i, 3);
		}
		
		for(int i=1; i<16; i++){
			if((double)wyniki.getValueAt(i, 1) == 0)
				if((double)wyniki.getValueAt(i, 2) == 0)
					wyniki.setValueAt("0.0", i, 4);
				else
					wyniki.setValueAt("Infinity", i, 4);
			else
				wyniki.setValueAt(String.valueOf(Math.abs(Functions.round((double)(wyniki.getValueAt(i, 3))/(double)(wyniki.getValueAt(i, 1))*100, 2))) , i, 4);
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = null;
	    if(value!=value)
	    	bd = new BigDecimal(0.0);
	    else
	    	bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static double heron(DoubleTriangle de){
		double p=0;
		double a=Functions.distance(de.dt.get(0).d1, de.dt.get(0).d2,
				de.dt.get(1).d1, de.dt.get(1).d2);
		double b=Functions.distance(de.dt.get(1).d1, de.dt.get(1).d2,
				de.dt.get(2).d1, de.dt.get(2).d2);
		double c=Functions.distance(de.dt.get(0).d1, de.dt.get(0).d2,
				de.dt.get(2).d1, de.dt.get(2).d2);
		p=(a+b+c)/2.0;
		double P = Math.sqrt(p*(p-a))*Math.sqrt((p-b)*(p-c));
		return P;
	}
	
}

