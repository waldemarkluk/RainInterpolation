import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import struktury.DoublePoint;
import struktury.DoubleTriangle;
import struktury.HSB;
import struktury.Image;
import struktury.RainFall;

public class SecondPanel extends JPanel {
	
	public SecondPanel(){
		
		
		setPreferredSize(new Dimension(400,650));
		
	
		JButton reset = new JButton("     Resetuj     ");
		add(reset);
		
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=0; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady[i][j]=0;
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady[i]=0;
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpady((JTable) getComponent(7));
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		JRadioButton alg1 = new JRadioButton("wieloboków równego zadaszenia                        ");
		JRadioButton alg2 = new JRadioButton("odwrotnych odległości (\u03bb=2)                   ");
		JRadioButton alg6 = new JRadioButton("\u03bb=3");
		JRadioButton alg3 = new JRadioButton("izohiet                                                                        ");
		JRadioButton alg4 = new JRadioButton("triangulacji                                                                ");
		JRadioButton alg5 = new JRadioButton("średniej arytmetycznej                                           ");
		ButtonGroup group = new ButtonGroup();
		group.add(alg1); 
		group.add(alg2);
		group.add(alg3);
		group.add(alg4);
		group.add(alg5);
		group.add(alg6);
		
		add(alg1);
		add(alg2);
		add(alg6);
		add(alg3);
		add(alg4);
		add(alg5);

		
		alg1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				double minDistance=100;
        				int index=0;
        				for(DoublePoint dp: MyFrame.PunktyPomiarowe){
        					if(Functions.distance(dp.d1,dp.d2,i,j)<minDistance)
        						index=MyFrame.PunktyPomiarowe.indexOf(dp);
        					minDistance = Math.min(Functions.distance(dp.d1, dp.d2, i, j), minDistance);
        				}
        				DoublePoint dp =MyFrame.PunktyPomiarowe.get(index);
        				RainFall.opady_interpolowane[i][j]=RainFall.opady[dp.d1.intValue()][dp.d2.intValue()];
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		alg2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));

        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				double current_distance=0.0;
        				double counter=0.0;
        				double denominator =0.0 ;
        				for(DoublePoint dp: MyFrame.PunktyPomiarowe){
        					if((current_distance=Functions.distance(dp.d1,dp.d2,i,j))==0.0){
        						RainFall.opady_interpolowane[i][j]=RainFall.opady[dp.d1.intValue()][dp.d2.intValue()];
        						break;
        					}else{
        						denominator += 1.0/Math.pow(current_distance, 2);
        						counter += RainFall.opady[dp.d1.intValue()][dp.d2.intValue()]/Math.pow(current_distance, 2);
        					}
        				}    
        				if(current_distance!=0.0)
        					RainFall.opady_interpolowane[i][j]=counter/denominator;
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		alg6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));

        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				double current_distance=0.0;
        				double counter=0.0;
        				double denominator =0.0 ;
        				for(DoublePoint dp: MyFrame.PunktyPomiarowe){
        					if((current_distance=Functions.distance(dp.d1,dp.d2,i,j))==0.0){
        						RainFall.opady_interpolowane[i][j]=RainFall.opady[dp.d1.intValue()][dp.d2.intValue()];
        						break;
        					}else{
        						denominator += 1.0/Math.pow(current_distance, 3);
        						counter += RainFall.opady[dp.d1.intValue()][dp.d2.intValue()]/Math.pow(current_distance, 3);
        					}
        				}    
        				if(current_distance!=0.0)
        					RainFall.opady_interpolowane[i][j]=counter/denominator;
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		alg3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));

        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				double current_distance=0.0;
        				double counter=0.0;
        				double denominator =0.0 ;
        				for(DoubleTriangle de: MyFrame.triangulation){
        					if(Functions.pointInPolygon(i, j, de.dt)){
        						double P = Functions.round(Functions.heron(de),2);
        						double P1 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(1), de.dt.get(2), 
        							new DoublePoint(i, j))),2);
        						double P2 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(2), de.dt.get(0), 
        							new DoublePoint(i, j))),2);
        						double P3 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(0), de.dt.get(1), 
        							new DoublePoint(i, j))),2);
        						RainFall.opady_interpolowane[i][j]=
        							RainFall.opady[de.dt.get(0).d1.intValue()][de.dt.get(0).d2.intValue()]*
        							((double)P1/(double)P)+
        							RainFall.opady[de.dt.get(1).d1.intValue()][de.dt.get(1).d2.intValue()]*
        							((double)P2/(double)P)+
        							RainFall.opady[de.dt.get(2).d1.intValue()][de.dt.get(2).d2.intValue()]*
        							((double)P3/(double)P);
        						double temp = RainFall.opady_interpolowane[i][j];
        						temp=Math.round(temp);
        						RainFall.opady_interpolowane[i][j]=temp;
        						break;
        					}
        				}    
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		alg4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));

        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				double current_distance=0.0;
        				double counter=0.0;
        				double denominator =0.0 ;
        				for(DoubleTriangle de: MyFrame.triangulation){
        					if(Functions.pointInPolygon(i, j, de.dt)){
        						double P = Functions.round(Functions.heron(de),2);
        						double P1 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(1), de.dt.get(2), 
        							new DoublePoint(i, j))),2);
        						double P2 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(2), de.dt.get(0), 
        							new DoublePoint(i, j))),2);
        						double P3 =  Functions.round(Functions.heron(
        							new DoubleTriangle(de.dt.get(0), de.dt.get(1), 
        							new DoublePoint(i, j))),2);
        						RainFall.opady_interpolowane[i][j]=
        							RainFall.opady[de.dt.get(0).d1.intValue()][de.dt.get(0).d2.intValue()]*
        							((double)P1/(double)P)+
        							RainFall.opady[de.dt.get(1).d1.intValue()][de.dt.get(1).d2.intValue()]*
        							((double)P2/(double)P)+
        							RainFall.opady[de.dt.get(2).d1.intValue()][de.dt.get(2).d2.intValue()]*
        							((double)P3/(double)P);
        						break;
        					}
        				}    
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		
		alg5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
        		for(int i=0; i<460; i++){
        			for(int j=315; j<635; j++){
        				Image.image[i][j]= new HSB(0.0f, 0.0f, 1.0f);
        			}
        		}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				RainFall.opady_interpolowane[i][j]=0;
        			}
        		}
        		for(int i=0; i<16; i++){
        			MyFrame.opady_interpolowane[i]=0;
        		}
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));

				double []suma = new double[16];
				int [] licznik =new int[16];
				int drugi_licznik =0;
				for (ArrayList<DoublePoint> zlewnia: MyFrame.Zlewnie){
					drugi_licznik++;
    				for(DoublePoint dp: MyFrame.PunktyPomiarowe){
    					if(Functions.pointInPolygon(dp.d1, dp.d2, zlewnia)){
    						suma[drugi_licznik]+=RainFall.opady[dp.d1.intValue()][dp.d2.intValue()];
    						licznik[drugi_licznik]++;
    					}
    				}
				}
        		for(int i=0; i<450; i++){
        			for(int j=0; j<316; j++){
        				int numer2 = RainFall.zlewnia[i][j];
        				RainFall.opady_interpolowane[i][j]=suma[numer2]/(double)licznik[numer2];
        				if(RainFall.zlewnia[i][j]>0 && RainFall.opady_interpolowane[i][j]>0)
	        				Image.image[i][j+315] = new HSB(
	        						(float)0.67f,((float)RainFall.opady_interpolowane[i][j]/10.1f)*1.0f, 1.0f);
	        			int numer = RainFall.zlewnia[i][j];
	        			double opad = RainFall.opady_interpolowane[i][j];
	        			MyFrame.zmiana_opadow[numer] += opad;
        			}
        		}

        		for(int i=1; i<16; i++){
        			MyFrame.opady_interpolowane[i] = MyFrame.zmiana_opadow[i];
        			MyFrame.zmiana_opadow[i]=0;
        		}
        		
        		Functions.liczOpadyInterpolowane((JTable) getComponent(7));
        		
        		getParent().getParent().getParent().getParent().repaint();
			}
		});
		

		JTable wyniki = new JTable(16,5);
		add(wyniki);
		wyniki.setValueAt("A", 0, 1);
		wyniki.setValueAt("B", 0, 2);
		wyniki.setValueAt("Bł. bezwzgl.", 0, 3);
		wyniki.setValueAt("Bł. wzgl.", 0, 4);
		wyniki.setValueAt("1.", 1, 0);
		wyniki.setValueAt("2.", 2, 0);
		wyniki.setValueAt("3.", 3, 0);
		wyniki.setValueAt("4.", 4, 0);
		wyniki.setValueAt("5.", 5, 0);
		wyniki.setValueAt("6.", 6, 0);
		wyniki.setValueAt("7.", 7, 0);
		wyniki.setValueAt("8.", 8, 0);
		wyniki.setValueAt("9.", 9, 0);
		wyniki.setValueAt("10.", 10, 0);
		wyniki.setValueAt("11.", 11, 0);
		wyniki.setValueAt("12.", 12, 0);
		wyniki.setValueAt("13.", 13, 0);
		wyniki.setValueAt("14.", 14, 0);
		wyniki.setValueAt("15.", 15, 0);
		wyniki.setValueAt(0.0, 1, 1);
		wyniki.setValueAt(0.0, 2, 1);
		wyniki.setValueAt(0.0, 3, 1);
		wyniki.setValueAt(0.0, 4, 1);
		wyniki.setValueAt(0.0, 5, 1);
		wyniki.setValueAt(0.0, 6, 1);
		wyniki.setValueAt(0.0, 7, 1);
		wyniki.setValueAt(0.0, 8, 1);
		wyniki.setValueAt(0.0, 9, 1);
		wyniki.setValueAt(0.0, 10, 1);
		wyniki.setValueAt(0.0, 11, 1);
		wyniki.setValueAt(0.0, 12, 1);
		wyniki.setValueAt(0.0, 13, 1);
		wyniki.setValueAt(0.0, 14, 1);
		wyniki.setValueAt(0.0, 15, 1);
		
		
		JRadioButton wolno = new JRadioButton("wolno");
		JRadioButton szybko = new JRadioButton("szybko");
		ButtonGroup group2= new ButtonGroup();
		group2.add(wolno);
		group2.add(szybko);
		
		add(wolno);
		add(szybko);
		
		wolno.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				MyFrame.szybko=false;
			}
		});
		
		szybko.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				MyFrame.szybko=true;
			}
		});


	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Color temp = g2d.getColor();
		g2d.setColor(Color.orange);
		g2d.fillRect(8, 598, 15+11*34+4,12);
		for(int i=0; i<35; i++){
			g2d.setColor(Color.getHSBColor(0.67f, (i/34.0f)*1.0f, 1.0f));
			g2d.fillRoundRect(10+11*i, 600, 15, 8,4,4);
		}
		g2d.setColor(temp);
		g2d.drawString("0.0", 10, 622);
		g2d.drawString("10.0", 355, 622);
		g2d.finalize();
	}
	
}
