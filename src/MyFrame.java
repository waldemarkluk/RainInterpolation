import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import struktury.DoublePoint;
import struktury.DoubleTriangle;
import struktury.HSB;
import struktury.Image;
import struktury.RainFall;

public class MyFrame extends JFrame {
		
	int x1,y1,x2,y2;

	double a=169.179, b=-3120.93, c=-257.934, d=13244.4;
	String cvsSplitBy = ",";
	boolean first = true;
	double lo=0.0,la=0.0,lo2=0.0,la2=0.0;
	double kat = -Math.PI/100.0;
	List<String> tekst = null;
	double krok = 0.1;
	
	static boolean szybko = false;
	
	static double [] powierzchnie = new double[16];
	static double [] zmiana_opadow =  new double[16];
	static double [] opady		  = new double[16];  //dla kazdej ze zlewni z osobna
	static double [] opady_interpolowane  = new double[16];

	public static ArrayList<ArrayList<DoublePoint>> Zlewnie= new ArrayList<ArrayList<DoublePoint>>();
	int niewazne = Functions.zlewnie();

	public static List<DoublePoint>PunktyPomiarowe = new ArrayList<DoublePoint>();
	int niewazne2 = Functions.punktyPomiarowe();
	public static List<DoubleTriangle> triangulation = new ArrayList<DoubleTriangle>();
	int niewazne3 = Functions.delaunayAlgorithm(PunktyPomiarowe);
	Image newimage = new Image();
	
	public MyFrame myReference(){
		return this;
	}
	
	public MyFrame() {
		super("RainInterpolation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setSize(800, 800);
		setVisible(true);
		setLayout(new FlowLayout(3));
	
		for(int i=0; i<16; i++){
			powierzchnie[i]=0.0;
		}

		for(int i=10; i<450; i++){
			for(int j=10; j<300; j++){
				for(ArrayList<DoublePoint> aldp: Zlewnie){
					if(Functions.pointInPolygon(i, j, aldp)){
//						float numer = Zlewnie.indexOf(aldp);
//						float kolor = (numer/Zlewnie.size());
//						Image.image[i][j] = new HSB(kolor, 1.0f, 1.0f); 
						powierzchnie[Zlewnie.indexOf(aldp)+1] +=1;
						RainFall.zlewnia[i][j]=Zlewnie.indexOf(aldp)+1; //LUT dla punktów zlewni
						break;
					}
				}
			}
		}
//		int ilosc_pikseli=0;
//		for(int i=1; i<16; i++){
//			ilosc_pikseli+=powierzchnie[i];
//		}
//		System.out.println("Ilość pikseli: " + ilosc_pikseli);

		JPanel panel = new MyPanel();
		add(panel);

		JPanel panel2 = new SecondPanel();
		add(panel2);
		
		final JPanel panel2_5 = panel2;
		
		
		 MouseAdapter mouseAdapter = new MouseAdapter ()
	        {
	            public void mousePressed ( MouseEvent e )
	            {
	            	x1=e.getX();
	            	y1=e.getY();
	            	repaint ();
	            }

	            public void mouseDragged ( MouseEvent e )
	            {
	            	x2=e.getX();
	            	y2=e.getY();
	        		for(int i=0; i<460; i++){
	        			for(int j=0; j<350; j++){
	    	        		if(Math.pow(i-x1,2)+Math.pow(j-y1, 2)<Math.pow(x2-x1,2)+Math.pow(y2-y1, 2)){
	    	        			double chance = 1-(Math.pow(i-x1,2)+Math.pow(j-y1, 2))/
	    	        					(Math.pow(x2-x1,2)+Math.pow(y2-y1, 2));
	    	        			chance *= chance*Math.pow(Math.random(), 5);
	    	        			if(szybko == true) chance*=10;
	    	        			if(chance>0.05){
	    	        				Image.image[i][j] = new HSB(1.0f, 0.0f, 1.0f);
	    	        				if(RainFall.opady[i][j]<10.0)
	    	        					RainFall.opady[i][j]+=0.1;
	    	        				Image.image[i][j] = new HSB(
	    	        						(float)0.67f,((float)(RainFall.opady[i][j])/10.1f)*1.0f, 1.0f);
	    	        			}
	        				}
	        			}
	        		}
	            repaint ();
	            }

	            public void mouseReleased ( MouseEvent e )
	            {
	            	x2=e.getX();
	            	y2=e.getY();
	        		for(int i=0; i<460; i++){
	        			for(int j=0; j<350; j++){
	    	        		if(Math.pow(i-x1,2)+Math.pow(j-y1, 2)<Math.pow(x2-x1,2)+Math.pow(y2-y1, 2)){
	    	        			double chance = 1-(Math.pow(i-x1,2)+Math.pow(j-y1, 2))/
	    	        					(Math.pow(x2-x1,2)+Math.pow(y2-y1, 2));
	    	        			chance *= chance*Math.pow(Math.random(), 5);
	    	        			if(chance>0.1){
	    	        				if(RainFall.opady[i][j]<10.0)
	    	        					RainFall.opady[i][j]+=0.1;
	    	        				Image.image[i][j] = new HSB(
	    	        						(float)0.67f,((float)(RainFall.opady[i][j])/10.1f)*1.0f, 1.0f); 
	    	        			}
	        				}
	    	        		if(j<316 && i<450){
	    	        			int numer = RainFall.zlewnia[i][j];
	    	        			double opad = RainFall.opady[i][j];
	    	        			zmiana_opadow[numer] += opad;
	    	        		}
	    	        	}
	        		}
	        		for(int i=1; i<16; i++){
	        			opady[i] = zmiana_opadow[i];
	        			zmiana_opadow[i]=0;
	        		}
	        			

	        		Functions.liczOpady((JTable) panel2_5.getComponent(7));
	        		
	            	repaint ();
	            }
	        };
		
		panel.addMouseListener(mouseAdapter);
		panel.addMouseMotionListener(mouseAdapter);
		
		
		pack();
		setVisible(true);
	}

	
}