import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import struktury.DoublePoint;
import struktury.DoubleTriangle;
import struktury.Image;

public class MyPanel extends JPanel {
	
	private boolean begin = true;
	
	public MyPanel() {
		setPreferredSize(new Dimension(460, 650));
	}


	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;


		for(int i=0; i<460; i++){
			for(int j=0; j<635; j++){
				float h=Image.image[i][j].h;
				float s=Image.image[i][j].s;
				float b=Image.image[i][j].b;
				g2d.setColor(Color.getHSBColor(h, s, b));		
				g2d.drawOval(i, j, 0, 0);
//				g2d.drawOval(i, j+315, 0, 0);
			}
		}
		
		g2d.setColor(Color.black);
//		// prostokat
		Rectangle2D rectangle = new Rectangle2D.Double(10, 10, 440, 306);
		Rectangle2D rectangle2 = new Rectangle2D.Double(10, 325, 440, 306);
//		// kolo
//		Ellipse2D circle = new Ellipse2D.Double(10, 10, 380, 380);
//
		g2d.draw(rectangle);
		g2d.draw(rectangle2);
//		g2d.draw(circle);	
		

		
		for(List<DoublePoint> ldp: MyFrame.Zlewnie){
			boolean first = true;
			for(DoublePoint dp: ldp){
				if(first == true){
					first = false;
					continue;
				}
				Line2D line = new Line2D.Double(dp.d1, dp.d2, ldp.get(ldp.indexOf(dp)-1).d1, ldp.get(ldp.indexOf(dp)-1).d2);
				Line2D line2= new Line2D.Double(dp.d1, dp.d2+315, ldp.get(ldp.indexOf(dp)-1).d1, ldp.get(ldp.indexOf(dp)-1).d2+315);
				g2d.draw(line);
				g2d.draw(line2);
			}
		}
		
		for(DoublePoint dp: MyFrame.PunktyPomiarowe){
			g2d.drawOval(dp.d1.intValue(),dp.d2.intValue(),2,2);
			g2d.drawOval(dp.d1.intValue(),dp.d2.intValue()+315,2,2);
		}
		
		//triangulacja Delaunay'a
		for(DoubleTriangle de: MyFrame.triangulation){
			Line2D line = new Line2D.Double(de.dt.get(0).d1, de.dt.get(0).d2, de.dt.get(1).d1, de.dt.get(1).d2);
			Line2D line2 = new Line2D.Double(de.dt.get(1).d1, de.dt.get(1).d2, de.dt.get(2).d1, de.dt.get(2).d2);
			Line2D line3 = new Line2D.Double(de.dt.get(2).d1, de.dt.get(2).d2, de.dt.get(0).d1, de.dt.get(0).d2);
//			Line2D line2= new Line2D.Double(de.dp1.d1, de.dp1.d2+315, de.dp2.d1, de.dp2.d2+315);
			g2d.draw(line);
			g2d.draw(line2);
			g2d.draw(line3);
		}
		
		g2d.drawString("A. Rzeczywiste opady", 20, 30);
		g2d.drawString("B. Wynik interpolacji", 20, 345);
		
		begin = false;
		
		
		
		g2d.finalize();
	}
}