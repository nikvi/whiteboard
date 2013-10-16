package dst.four.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import dst.four.ui.WhiteBoardWindow;

public class DrawPanel extends Panel implements ActionListener, MouseMotionListener, MouseListener, ItemListener {
	
//	public TextArea jt = new TextArea();
//	public TextArea jt1 = new TextArea();
	//this.add(jt);
	public static Vector<Point> paintInfo =null;// null;// the vector group of the points information
	//paintInfo = new Vector<Point>();
	int n = 1;
	public void delete(){
		this.removeAll();
		repaint();
	}
	public DrawPanel(){
		paintInfo = new Vector<Point>();
		addMouseMotionListener(this);
		addMouseListener(this);
		System.out.print("DrawPanel");
		//TextArea jt = new TextArea();

		//g2d.drawString("asdasd", p1.x, p1.y);
		//this.add(jt);
	}
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		System.out.print("paint");
		Point p1, p2;
		if (flagdrag == 1)
			g.clearRect(0, 0, getSize().width, getSize().height);
		n = paintInfo.size();

		if (WhiteBoardWindow.toolFlag == 2)
			g.clearRect(0, 0, getSize().width, getSize().height);// clear

		for (int i = 0; i < n-1; i++) {
			p1 = (Point) paintInfo.elementAt(i);
			p2 = (Point) paintInfo.elementAt(i + 1);
			WhiteBoardWindow.size = new BasicStroke(p1.boarder, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL);

			g2d.setColor(p1.col);
			g2d.setStroke(WhiteBoardWindow.size);
			if (p1.tool == p2.tool) {
				switch (p1.tool) {
				case 0:// draw pen

					Line2D line1 = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
					g2d.draw(line1);
					break;

				case 1:// eraser
					g.clearRect(p1.x, p1.y, p1.boarder, p1.boarder);
					break;

				case 3:// draw the line
					Line2D line2 = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
					g2d.draw(line2);
					break;

				case 4:// draw the circle
					Ellipse2D ellipse = new Ellipse2D.Double(p1.x, p1.y,
							Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
					g2d.draw(ellipse);
					break;

				case 5:// draw the rectangle
					Rectangle2D rect = new Rectangle2D.Double(p1.x, p1.y,
							Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
					g2d.draw(rect);
					break;
				case 6:// text
					System.out.print(i);
//					TextArea jt = new TextArea();
//					this.add(jt);
//					jt.setBounds(p1.x, p1.y,
//							Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
//					//boolean equals = ;
//					if(WhiteBoardWindow.c.getRGB() == Color.BLACK.getRGB())
//						jt.setBackground(Color.white);
//					else
//						jt.setBackground(WhiteBoardWindow.c);
//					jt.setVisible(true);
					break;

				case 7:// stop and skip
					i = i + 1;
					break;

				default:
				}// end switch
			}// end if
		}// end for
	}

	int flagdrag = 0;
	public void mouseDragged(MouseEvent e) {
		System.out.print("drag");
		Point p1;
		int x,y;
		switch (WhiteBoardWindow.toolFlag) {
		case 0://  draw pen
			System.out.print("asdasd");
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p1);
			repaint();
			break;

		case 1:// eraser
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, null, WhiteBoardWindow.toolFlag, WhiteBoardWindow.Econ);
			paintInfo.addElement(p1);
			repaint();
			break;

		case 3://  line
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);

			if (flagdrag == 1)
				paintInfo.removeElementAt((paintInfo.size()-1));
			flagdrag = 1;
			paintInfo.addElement(p1);
			repaint();
			break;
		case 4://  circle
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);

			if (flagdrag == 1)
				paintInfo.removeElementAt((paintInfo.size()-1));
			flagdrag = 1;

			paintInfo.addElement(p1);
			repaint();
			break;
		case 5://  rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);

			if (flagdrag == 1)
				paintInfo.removeElementAt((paintInfo.size()-1));
			flagdrag = 1;
			paintInfo.addElement(p1);
			repaint();
			break;

		default:
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void update(Graphics g) {
		System.out.print("update");
		  Image offScreenImage = null;
		 // offScreenImage = g;
		  if (offScreenImage == null)
		        offScreenImage = createImage(1000, 600);
		        Graphics gOff = offScreenImage.getGraphics();
		        // 4.调用paint(),将缓冲图象的画笔传入
		        paint(gOff);
		        // 5.再将此缓冲图像一次性绘到代表屏幕的Graphics对象，即该方法传入的“g”上
		        g.drawImage(offScreenImage, 0, 0, null);
		//paint(g);
		//paintInfo.removeAllElements();
	}

	//this.add(jt);
	public void mousePressed(MouseEvent e) {
		System.out.print("press");
		int x, y;
		Point p2;
		switch (WhiteBoardWindow.toolFlag) {
		case 3:// line
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;

		case 4: //  circle
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;

		case 5: // rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;
		case 6: // text
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p2);
			flagdrag = 0;
//			this.add(jt);
//			jt.setVisible(false);
			break;
			
//			jt.setVisible(true);
//			this.add(jt);
//			jt.setBounds(x,y,210,210);
//			this.setLayout(null);
//			Color background = new Color (20,30,0);
//			jt.setBackground(background);
			//jt.setLocation(200,200);
			//Rectangle a = new Rectangle();
			//a.setBounds(x, y, 10, 10);
			//this.add(a);
			//jt.setSize(10, 10);
			//p3 = new Point(x, y, c, toolFlag, con);
			//paintInfo.addElement(p3);
			//paintInfo.addElement(cutflag);
			//repaint();
			

		default:
		}
	}

	public void mouseReleased(MouseEvent e) {
		System.out.print("release");
		int x,y;
		Point p3;
		switch (WhiteBoardWindow.toolFlag) {
		case 0:// draw pen
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			break;

		case 1: // eraser
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			break;

		case 3://  line
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p3);
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			flagdrag=0;
			repaint();
			break;

		case 4: // circle
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p3);
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			repaint();
			break;

		case 5: // rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p3);
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			repaint();
			break;
		case 6: //text
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, WhiteBoardWindow.c, WhiteBoardWindow.toolFlag, WhiteBoardWindow.con);
			paintInfo.addElement(p3);
			paintInfo.addElement(WhiteBoardWindow.cutflag);
			repaint();
			break;

		default:
		}
	}
	void sendgraph(Point start, Point end, int type){
		//type represent the graph
		if (type == 0){//pencil
			;
		}

		if (type == 1){//rectangle
			;//the filled or unfilled is in the point
		}
		if (type == 2){//circle
			;//the filled or unfilled is in the point
		}
		if (type == 3){//eraser
			;
		}
		if (type == 4){//text?
			;
		}

	}
	void sendpoint(Point location){
		//point contain color and size;
	}


	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
}
