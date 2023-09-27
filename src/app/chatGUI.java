package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class chatGUI {

	public JFrame frame;
	public JTextField textField;
	public JTextField textInput;
	public JButton btnSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chatGUI window = new chatGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		frame.setBounds(100, 100, 600, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 10, 566, 215);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textInput = new JTextField();
		textInput.setBounds(10, 235, 478, 43);
		frame.getContentPane().add(textInput);
		textInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBorder(new LineBorder(new Color(0, 128, 255), 1, true));
		btnSend.setBounds(498, 235, 78, 43);
		frame.getContentPane().add(btnSend);
	}
}
