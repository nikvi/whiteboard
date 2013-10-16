package dst.four.ui;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class JoinWBWindow extends JFrame {

    private JPanel contentPane;
    private DefaultListModel<String> hostListModel = new DefaultListModel<String>();
    

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
                    JoinWBWindow frame = new JoinWBWindow(null);
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
    @SuppressWarnings("unused")
	public JoinWBWindow(List<String> hosts) {
    	setTitle("Join WhiteBoard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 480);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblWhiteboardName = new JLabel("Select a WhiteBoard");
        lblWhiteboardName.setForeground(new Color(255, 255, 255));
        lblWhiteboardName.setBounds(20, 20, 164, 38);
        contentPane.add(lblWhiteboardName);
        lblWhiteboardName.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        
        JButton btnStart = new JButton("Join");
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
        //TODO add users list
        final JList<String> list = new JList<String>();
        list.setBounds(30, 68, 140, 169);
        list.setModel(hostListModel);
        if (hosts!=null){
        	System.out.println(hosts.get(0));
        	addHosts(hosts);
        }
        else {
        	hostListModel.addElement("No hosted boards");
        }
        contentPane.add(list);
       
    }

	private void addHosts(List<String> hosts) {
		Iterator<String> iterator = hosts.iterator();
		while(iterator.hasNext()){
			hostListModel.addElement(iterator.next());
		}
		
	}

	
}