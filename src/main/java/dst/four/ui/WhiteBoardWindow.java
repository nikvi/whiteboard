package dst.four.ui;


import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import dst.four.graphics.DrawPanel;
import dst.four.graphics.Point;
import dst.four.graphics.WhiteBoard;

public class WhiteBoardWindow extends JFrame implements ItemListener,
		ActionListener {

	private JPanel contentPane;
	private WhiteBoard wb;
	private DefaultListModel<String> userListModel = new DefaultListModel<String>();


	FileInputStream picIn = null;
	FileOutputStream picOut = null;

	ObjectInputStream VIn = null;
	ObjectOutputStream VOut = null;

	int x = -1, y = -1;
	public static int con = 1;// the size of draw pen
	public static int Econ = 5;// the size of eraser

	public static Color c = new Color(0, 0, 0); // the color of draw pen

	public static Point cutflag = new Point(-1, -1, c, 7, con);// the stop
																// signal

	public static BasicStroke size = new BasicStroke(con, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL);// the width of draw pen

	public static int toolFlag = 0;// toolFlag
	// toolFlag table
	// （0--draw pen）；（1--eraser）；（2--delete）；
	// （3--line）；（4--circle）；（5--rectangle）；

	// Panel fra
	Button eraser, drLine, drCircle, drRect, textA;
	Button clear, pen;
	Choice ColChoice, SizeChoice, EraserChoice;
	Button colchooser;
	// Label color, bigB, bigE;
	// the saving function
	Button openPic, savePic;
	FileDialog openPicture, savePicture;
	DrawPanel DrawPanel = new DrawPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoardWindow frame = new WhiteBoardWindow("WB");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WhiteBoardWindow(String name) {
		super(name);
		setTitle(name);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(10, 10, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		openPic = new Button("Open");
		savePic = new Button("Save");
		pen = new Button("Pencil");
		drLine = new Button("Line");
		drCircle = new Button("Circle");
		drRect = new Button("Rectangle");
		textA = new Button("Text");
		colchooser = new Button("Palette");
		eraser = new Button("Eraser");
		clear = new Button("Reset");


		/* tools buttons and options */
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

		// listen to the actions
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

		JLabel lblUsersList = new JLabel("Users List");
		lblUsersList.setForeground(Color.WHITE);
		lblUsersList.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		lblUsersList.setBounds(1031, 204, 150, 38);
		contentPane.add(lblUsersList);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(1031, 49, 218, 145);
		contentPane.add(textPane);

		// DrawPanel = new JPanel();
		DrawPanel.setBounds(5, 50, 1000, 609);
		DrawPanel.setBackground(Color.WHITE);
		contentPane.add(DrawPanel);

		Panel panel_1 = new Panel();
		panel_1.setBounds(5, 5, 933, 37);
		contentPane.add(panel_1);
		panel_1.setBackground(Color.GRAY);
		panel_1.setLayout(null);


		openPic.setBounds(5, 5, 44, 23);
		openPic.setBackground(Color.ORANGE);
		panel_1.add(openPic);


		savePic.setBounds(54, 5, 41, 23);
		savePic.setBackground(Color.ORANGE);
		panel_1.add(savePic);


		pen.setBounds(100, 5, 48, 23);
		pen.setForeground(Color.BLACK);
		pen.setBackground(Color.ORANGE);
		panel_1.add(pen);


		drLine.setBounds(153, 5, 38, 23);
		drLine.setBackground(Color.ORANGE);
		panel_1.add(drLine);


		drCircle.setBounds(196, 5, 46, 23);
		drCircle.setBackground(Color.ORANGE);
		panel_1.add(drCircle);


		drRect.setBounds(247, 5, 70, 23);
		drRect.setBackground(Color.ORANGE);
		panel_1.add(drRect);


		textA.setBounds(322, 5, 36, 23);
		textA.setBackground(Color.ORANGE);
		panel_1.add(textA);

		Label label = new Label("Paint Colour", Label.CENTER);
		label.setBounds(527, 5, 82, 23);
		label.setForeground(Color.WHITE);
		panel_1.add(label);


		ColChoice.setBounds(609, 5, 51, 21);
		panel_1.add(ColChoice);

		Label label_1 = new Label("Paint Size", Label.CENTER);
		label_1.setBounds(663, 5, 68, 23);
		label_1.setForeground(Color.WHITE);
		panel_1.add(label_1);


		SizeChoice.setBounds(732, 5, 40, 21);
		panel_1.add(SizeChoice);


		colchooser.setBounds(361, 5, 52, 23);
		colchooser.setBackground(Color.ORANGE);
		panel_1.add(colchooser);


		eraser.setBounds(417, 5, 51, 23);
		eraser.setBackground(Color.ORANGE);
		panel_1.add(eraser);

		Label label_2 = new Label("Eraser Size", Label.CENTER);
		label_2.setBounds(784, 5, 77, 23);
		label_2.setForeground(Color.WHITE);
		panel_1.add(label_2);


		clear.setBounds(473, 5, 47, 23);
		clear.setBackground(Color.ORANGE);
		panel_1.add(clear);
		// eraser size option
		EraserChoice = new Choice();
		EraserChoice.setBounds(865, 5, 56, 21);
		panel_1.add(EraserChoice);
		EraserChoice.add("5");
		EraserChoice.add("9");
		EraserChoice.add("13");
		EraserChoice.add("17");
		EraserChoice.addItemListener(this);

		JLabel lblWhiteboardName = new JLabel("Notifications");
		lblWhiteboardName.setBounds(1031, 10, 150, 38);
		contentPane.add(lblWhiteboardName);
		lblWhiteboardName.setForeground(new Color(255, 255, 255));
		lblWhiteboardName.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));

		final JList<String> list = new JList<String>();
		list.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
		list.setBounds(1031, 252, 218, 407);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(userListModel);
		contentPane.add(list);

		addUser("Alice");
		addUser("Bob");
		JMenuItem jmi1, jmi2;
		final JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(jmi1 = new JMenuItem("Kick Out"));
		popupMenu.add(new JPopupMenu.Separator());
		popupMenu.add(jmi2 = new JMenuItem("Show Profile"));

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// if right mouse button clicked (or me.isPopupTrigger())
				if (SwingUtilities.isRightMouseButton(me)
						&& !list.isSelectionEmpty()
						&& list.locationToIndex(me.getPoint()) == list
								.getSelectedIndex()) {
					popupMenu.show(list, me.getX(), me.getY());
				}
			}
		});

		jmi1.addActionListener(this);
		jmi2.addActionListener(this);
	}

	// Add a user by name
	private void addUser(String username) {
		userListModel.addElement(username);
	}

	// Delete a user by name
	private void deleteUser(String username) {
		int index = userListModel.indexOf(username);
		if (index != -1)
			userListModel.remove(index);
		else
			return;
	}


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

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == pen)// draw pen
		{
			toolFlag = 0;
		}

		if (e.getSource() == eraser)// eraser
		{
			toolFlag = 1;
		}

		if (e.getSource() == clear)// delete
		{
			toolFlag = 2;
			DrawPanel.paintInfo.removeAllElements();
			// Graphics g;
			DrawPanel.delete();
			// DrawPanel a = null ;
			// a.delete();
		}

		if (e.getSource() == drLine)// line
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
		if (e.getSource() == textA)// line
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
					DrawPanel.paintInfo.removeAllElements();
					File filein = new File(openPicture.getDirectory(),
							openPicture.getFile());
					picIn = new FileInputStream(filein);
					VIn = new ObjectInputStream(picIn);
					DrawPanel.paintInfo = (Vector<Point>) VIn.readObject();
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
			DrawPanel.delete();

		}

		if (e.getSource() == savePic)// save the paint
		{
			savePicture.setVisible(true);
			try {
				File fileout = new File(savePicture.getDirectory(),
						savePicture.getFile());
				picOut = new FileOutputStream(fileout);
				VOut = new ObjectOutputStream(picOut);
				VOut.writeObject(DrawPanel.paintInfo);
				VOut.close();
			} catch (IOException IOe) {
				System.out.println("can not write object");
			}
			DrawPanel.delete();

		}
	}


	public Font getPenFont() {
		return pen.getFont();
	}

	public void setPenFont(Font font) {
		pen.setFont(font);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}