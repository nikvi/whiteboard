package dst.four.graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JColorChooser;
import javax.swing.JTextArea;

public class WhiteBoard extends Frame implements ActionListener, MouseMotionListener, MouseListener, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x = -1, y = -1;
	int con = 1;// the size of draw pen
	int Econ = 5;// the size of eraser

	int toolFlag = 0;// toolFlag
	// toolFlag table
	// （0--draw pen）；（1--eraser）；（2--delete）；
	// （3--line）；（4--circle）；（5--rectangle）；

	Color c = new Color(0, 0, 0); // the color of draw pen
	BasicStroke size = new BasicStroke(con, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);// the width of draw pen
	Point cutflag = new Point(-1, -1, c, 7, con);// the stop signal

	Vector<Point> paintInfo = null;// the vector group of the points information
	int n = 1;

	FileInputStream picIn = null;
	FileOutputStream picOut = null;

	ObjectInputStream VIn = null;
	ObjectOutputStream VOut = null;

	// *panel of tools -- draw pen, line, circle, rectangle, polygon, eraser */
	Panel toolPanel;
	//Panel fra
	Button eraser, drLine, drCircle, drRect,textA;
	Button clear, pen;
	Choice ColChoice, SizeChoice, EraserChoice;
	Button colchooser;
	Label color, bigB, bigE;
	// the saving function
	Button openPic, savePic;
	FileDialog openPicture, savePicture;

	public WhiteBoard(String s) {
		super(s);
		addMouseMotionListener(this);
		addMouseListener(this);

		paintInfo = new Vector<Point>();

		/*     tools buttons and options*/
		// color options
		ColChoice = new Choice();
		ColChoice.add("black");
		ColChoice.add("red");
		ColChoice.add("blue");
		ColChoice.add("green");
		ColChoice.addItemListener(this);
		// draw pen size options
		SizeChoice = new Choice();
		SizeChoice.add("1");
		SizeChoice.add("3");
		SizeChoice.add("5");
		SizeChoice.add("7");
		SizeChoice.add("9");
		SizeChoice.addItemListener(this);
		//   eraser size option
		EraserChoice = new Choice();
		EraserChoice.add("5");
		EraserChoice.add("9");
		EraserChoice.add("13");
		EraserChoice.add("17");
		EraserChoice.addItemListener(this);
		// //////////////////////////////////////////////////
		toolPanel = new Panel();

		clear = new Button("Reset");
		eraser = new Button("Eraser");
		pen = new Button("Pencil");
		drLine = new Button("Line");
		textA = new Button("Text");
		drCircle = new Button("Circle");
		drRect = new Button("Rectangle");

		openPic = new Button("Open");
		savePic = new Button("Save");

		colchooser = new Button("Palette");

		JTextArea jt = new JTextArea();
		this.add(jt);
		jt.setVisible(false);
		
		//   listen to the actions
		clear.addActionListener(this);
		eraser.addActionListener(this);
		pen.addActionListener(this);
		drLine.addActionListener(this);
		textA.addActionListener(this);
		drCircle.addActionListener(this);
		drRect.addActionListener(this);
		openPic.addActionListener(this);
		savePic.addActionListener(this);
		colchooser.addActionListener(this);

		color = new Label("Paint Colour", Label.CENTER);
		bigB = new Label("Paint Size", Label.CENTER);
		bigE = new Label("Eraser Size", Label.CENTER);
		//  add elements in the panel
		toolPanel.add(openPic);
		toolPanel.add(savePic);

		toolPanel.add(pen);
		toolPanel.add(drLine);
		toolPanel.add(drCircle);
		toolPanel.add(drRect);
		toolPanel.add(textA);

		toolPanel.add(color);
		toolPanel.add(ColChoice);
		toolPanel.add(bigB);
		toolPanel.add(SizeChoice);
		toolPanel.add(colchooser);

		toolPanel.add(eraser);
		toolPanel.add(bigE);
		toolPanel.add(EraserChoice);

		toolPanel.add(clear);
		//  add tool panel to the applet panel
		add(toolPanel, BorderLayout.NORTH);

		setBounds(60, 60, 1000, 600);
		setVisible(true);
		validate();
		// dialog for save and load

		openPicture = new FileDialog(this, "Open", FileDialog.LOAD);
		openPicture.setVisible(false);
		savePicture = new FileDialog(this, "Save", FileDialog.SAVE);
		savePicture.setVisible(false);

		openPicture.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				openPicture.setVisible(false);
			}
		});

		savePicture.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				savePicture.setVisible(false);
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Point p1, p2;
		if (flagdrag == 1)
			g.clearRect(0, 0, getSize().width, getSize().height);
		n = paintInfo.size();

		if (toolFlag == 2)
			g.clearRect(0, 0, getSize().width, getSize().height);// clear

		for (int i = 0; i < n-1; i++) {
			p1 = (Point) paintInfo.elementAt(i);
			p2 = (Point) paintInfo.elementAt(i + 1);
			size = new BasicStroke(p1.boarder, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL);

			g2d.setColor(p1.col);
			g2d.setStroke(size);

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

				case 7:// stop and skip
					i = i + 1;
					break;

				default:
				}// end switch
			}// end if
		}// end for
	}

	int flagdrag = 0;
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == ColChoice)// color choice
		{
			String name = ColChoice.getSelectedItem();

			if (name == "black") {
				c = new Color(0, 0, 0);
			} else if (name == "red") {
				c = new Color(255, 0, 0);
			} else if (name == "green") {
				c = new Color(0, 255, 0);
			} else if (name == "blue") {
				c = new Color(0, 0, 255);
			}
		} else if (e.getSource() == SizeChoice)// 画笔大小 the size of draw pen
		{
			String selected = SizeChoice.getSelectedItem();

			if (selected == "1") {
				con = 1;
				size = new BasicStroke(con, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL);

			} else if (selected == "3") {
				con = 3;
				size = new BasicStroke(con, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL);

			} else if (selected == "5") {
				con = 5;
				size = new BasicStroke(con, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL);

			} else if (selected == "7") {
				con = 7;
				size = new BasicStroke(con, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL);

			} else if (selected == "9") {
				con = 9;
				size = new BasicStroke(con, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL);

			}
		} else if (e.getSource() == EraserChoice)// 橡皮大小 the size of eraser
		{
			String Esize = EraserChoice.getSelectedItem();

			if (Esize == "5") {
				Econ = 5 * 2;
			} else if (Esize == "9") {
				Econ = 9 * 2;
			} else if (Esize == "13") {
				Econ = 13 * 2;
			} else if (Esize == "17") {
				Econ = 17 * 3;
			}

		}

	}

	public void mouseDragged(MouseEvent e) {
		Point p1;
		switch (toolFlag) {
		case 0://  draw pen
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p1);
			repaint();
			break;

		case 1:// eraser
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, null, toolFlag, Econ);
			paintInfo.addElement(p1);
			repaint();
			break;
			
		case 3://  line
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, c, toolFlag, con);

			if (flagdrag == 1)
				paintInfo.removeElementAt((paintInfo.size()-1));
			flagdrag = 1;
			paintInfo.addElement(p1);
			repaint();
			break;
		case 4://  circle
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, c, toolFlag, con);

			if (flagdrag == 1)
				paintInfo.removeElementAt((paintInfo.size()-1));
			flagdrag = 1;
			
			paintInfo.addElement(p1);
			repaint();
			break;
		case 5://  rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p1 = new Point(x, y, c, toolFlag, con);

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
		paint(g);
	}
	JTextArea jt = new JTextArea();
	//this.add(jt);
	public void mousePressed(MouseEvent e) {
		Point p2;
		switch (toolFlag) {
		case 3:// line
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;

		case 4: //  circle
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;

		case 5: // rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p2 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p2);
			flagdrag = 0;
			break;
		case 6: // text
			x = (int) e.getX();
			y = (int) e.getY();
			//p2 = new Point(x, y, c, toolFlag, con);
			jt.setVisible(true);
			this.add(jt);
			jt.setBounds(x,y,210,210);
			this.setLayout(null);
			//jt.setLocation(200,200);
			//Rectangle a = new Rectangle();
			//a.setBounds(x, y, 10, 10);
			//this.add(a);
			//jt.setSize(10, 10);
			//p3 = new Point(x, y, c, toolFlag, con);
			//paintInfo.addElement(p3);
			//paintInfo.addElement(cutflag);
			repaint();
			break;

		default:
		}
	}

	public void mouseReleased(MouseEvent e) {
		Point p3;
		switch (toolFlag) {
		case 0:// draw pen
			paintInfo.addElement(cutflag);
			break;

		case 1: // eraser
			paintInfo.addElement(cutflag);
			break;

		case 3://  line
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p3);
			paintInfo.addElement(cutflag);
			flagdrag=0;
			repaint();
			break;

		case 4: // circle
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p3);
			paintInfo.addElement(cutflag);
			repaint();
			break;

		case 5: // rectangle
			x = (int) e.getX();
			y = (int) e.getY();
			p3 = new Point(x, y, c, toolFlag, con);
			paintInfo.addElement(p3);
			paintInfo.addElement(cutflag);
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
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == pen)// draw pen
		{
			toolFlag = 0;
		}

		if (e.getSource() == eraser)//  eraser
		{
			toolFlag = 1;
		}

		if (e.getSource() == clear)//  delete
		{
			toolFlag = 2;
			paintInfo.removeAllElements();
			repaint();
		}

		if (e.getSource() == drLine)//  line
		{
			toolFlag = 3;
		}

		if (e.getSource() == drCircle)// draw circle 
		{
			toolFlag = 4;
		}

		if (e.getSource() == drRect)// draw rectangle
		{
			toolFlag = 5;
		}
		if (e.getSource() == textA)//  line
		{
			toolFlag = 6;
		}

		if (e.getSource() == colchooser)// palette
		{
			Color newColor = JColorChooser.showDialog(this, "Palette", c);
			c = newColor;
		}

		if (e.getSource() == openPic)// open the paint
		{

			openPicture.setVisible(true);

			if (openPicture.getFile() != null) {
				int tempflag;
				tempflag = toolFlag;
				toolFlag = 2;
				repaint();

				try {
					paintInfo.removeAllElements();
					File filein = new File(openPicture.getDirectory(),
							openPicture.getFile());
					picIn = new FileInputStream(filein);
					VIn = new ObjectInputStream(picIn);
					paintInfo = (Vector<Point>) VIn.readObject();
					VIn.close();
					repaint();
					toolFlag = tempflag;

				}

				catch (ClassNotFoundException IOe2) {
					repaint();
					toolFlag = tempflag;
					System.out.println("can not read object");
				} catch (IOException IOe) {
					repaint();
					toolFlag = tempflag;
					System.out.println("can not read file");
				}
			}

		}

		if (e.getSource() == savePic)// save the paint
		{
			savePicture.setVisible(true);
			try {
				File fileout = new File(savePicture.getDirectory(),
						savePicture.getFile());
				picOut = new FileOutputStream(fileout);
				VOut = new ObjectOutputStream(picOut);
				VOut.writeObject(paintInfo);
				VOut.close();
			} catch (IOException IOe) {
				System.out.println("can not write object");
			}

		}
	}
}// end paintboard
