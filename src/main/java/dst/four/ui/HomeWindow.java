package dst.four.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import dst.four.RegisteryNode;
import dst.four.Variables;
import dst.four.person.UserPofile;

public class HomeWindow extends JFrame {

	private JPanel contentPane;
	private NewWBWindow newWBWindow;
	private JoinWBWindow joinWBWindow;
	private MyProfileWindow myprofileWindow;
	private JLabel lblABigWelcome;
	private JLabel lblDiscription;
	private JLabel lblNickname;

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
					HomeWindow frame = new HomeWindow();
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
	public HomeWindow() {
		setTitle("WhiteBoard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 480);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("New WhiteBoard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newWBWindow = new NewWBWindow();
				newWBWindow.setVisible(true);
			}
		});
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setBackground(new Color(255, 215, 0));
		btnNewButton.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		btnNewButton.setBounds(20, 220, 176, 40);
		contentPane.add(btnNewButton);

		JButton btnJoinWhiteboard = new JButton("Join WhiteBoard");
		btnJoinWhiteboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisteryNode clientNode = new RegisteryNode("Client",Variables.JOIN,"5556");
				joinWBWindow = new JoinWBWindow(clientNode.requestHostedBoards());
				joinWBWindow.setVisible(true);
			}
		});
		btnJoinWhiteboard.setForeground(Color.BLACK);
		btnJoinWhiteboard.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		btnJoinWhiteboard.setBackground(new Color(255, 215, 0));
		btnJoinWhiteboard.setBounds(20, 270, 176, 40);
		contentPane.add(btnJoinWhiteboard);

		JButton btnMyProfile = new JButton("My Profile");
		btnMyProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				myprofileWindow = new MyProfileWindow();
				myprofileWindow.setVisible(true);
			}
		});
		btnMyProfile.setForeground(Color.BLACK);
		btnMyProfile.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		btnMyProfile.setBackground(new Color(255, 215, 0));
		btnMyProfile.setBounds(20, 320, 176, 40);
		contentPane.add(btnMyProfile);

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnQuit.setForeground(Color.BLACK);
		btnQuit.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		btnQuit.setBackground(new Color(255, 215, 0));
		btnQuit.setBounds(20, 370, 176, 40);
		contentPane.add(btnQuit);

		lblABigWelcome = new JLabel(
				"<html>A Big Welcome to WhiteBoard! \r\n" +
				"<br> Haven't set up your profile yet?\r\n" +
				"<br> Just click \"My Profile\" to get Started!</html>");
		lblABigWelcome.setVerticalAlignment(SwingConstants.TOP);
		lblABigWelcome.setForeground(Color.WHITE);
		lblABigWelcome.setFont(new Font("Segoe UI Light", Font.PLAIN, 20));
		lblABigWelcome.setBounds(270, 120, 327, 160);
		contentPane.add(lblABigWelcome);
		
		lblNickname = new JLabel("NickName");
		lblNickname.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
		lblNickname.setForeground(Color.WHITE);
		lblNickname.setBounds(618, 17, 100, 21);
		lblNickname.setVisible(false);
		contentPane.add(lblNickname);
		
		lblDiscription = new JLabel("Discription");
		lblDiscription.setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
		lblDiscription.setForeground(Color.WHITE);
		lblDiscription.setBounds(618, 38, 114, 30);
		lblDiscription.setVisible(false);
		contentPane.add(lblDiscription);
		
		final UserPofile up = new UserPofile();
		if (up.loadProfile()) {
			lblABigWelcome.setText("Welcome back, " + up.getNickname() + "!");
			lblNickname.setText(up.getNickname());
			lblDiscription.setText(up.getDiscription());
			lblNickname.setVisible(true);
			lblDiscription.setVisible(true);
		}
	}
}