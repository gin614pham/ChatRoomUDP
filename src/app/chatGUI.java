package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class chatGUI {

	public JFrame frame;
	public JTextPane textChat, textTB;
	public JTextField textInput;
	public JButton btnSend;
	public JComboBox list;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					chatGUI window = new chatGUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public chatGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 925, 325);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textChat = new JTextPane();
		
		textChat.setEditable(false);
		textChat.setBounds(180, 10, 566, 215);
		frame.getContentPane().add(textChat);

		textInput = new JTextField();
		textInput.setBounds(180, 235, 478, 43);
		frame.getContentPane().add(textInput);
		textInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBorder(new LineBorder(new Color(0, 128, 255), 1, true));
		btnSend.setBounds(668, 235, 78, 43);
		frame.getContentPane().add(btnSend);

		textTB = new JTextPane();
		textTB.setEditable(false);
		textTB.setBounds(10, 10, 160, 268);
		frame.getContentPane().add(textTB);

		list = new JComboBox<>();
		list.setBounds(756, 10, 145, 35);
		frame.getContentPane().add(list);
	}
}
