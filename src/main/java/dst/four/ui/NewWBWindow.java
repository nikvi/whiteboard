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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import dst.four.RegisteryNode;
import dst.four.Variables;





public class NewWBWindow extends JFrame {

    private JPanel contentPane;
    private JTextField tfBoardName;
    private JTextField tfPort;
    private WhiteBoardWindow whiteBoardWindow;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NewWBWindow frame = new NewWBWindow();
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
    public NewWBWindow() {
    	setTitle("New WhiteBoard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 480);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblWhiteboardName = new JLabel("WhiteBoard Name");
        lblWhiteboardName.setForeground(new Color(255, 255, 255));
        lblWhiteboardName.setBounds(20, 20, 150, 38);
        contentPane.add(lblWhiteboardName);
        lblWhiteboardName.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        
        tfBoardName = new JTextField();
        tfBoardName.setBounds(172, 20, 160, 32);
        contentPane.add(tfBoardName);
        tfBoardName.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        tfBoardName.setColumns(10);
        
        JLabel lblMaxUsers = new JLabel("Port ");
        lblMaxUsers.setForeground(new Color(255, 255, 255));
        lblMaxUsers.setBounds(20, 72, 150, 38);
        contentPane.add(lblMaxUsers);
        lblMaxUsers.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        
        tfPort = new JTextField();
        tfPort.setBounds(172, 80, 160, 32);
        contentPane.add(tfPort);
        tfPort.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        tfPort.setColumns(10);
        
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		RegisteryNode hostNode = new RegisteryNode(tfBoardName.getText(),Variables.HOST,tfPort.getText());
        		hostNode.startHostConnection();
        		whiteBoardWindow= new WhiteBoardWindow(tfBoardName.getText());
        		whiteBoardWindow.setVisible(true);
        	}
        });
        btnStart.setBounds(180, 265, 140, 40);
        contentPane.add(btnStart);
        btnStart.setForeground(Color.BLACK);
        btnStart.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        btnStart.setBackground(new Color(255, 215, 0));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(180, 321, 140, 40);
        contentPane.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        btnCancel.setBackground(new Color(255, 215, 0));
    }
}