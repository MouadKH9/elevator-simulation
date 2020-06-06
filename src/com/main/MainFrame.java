package com.main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.main.canvas.Canvas;

public class MainFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
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
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		Canvas canvas = new Canvas();
		frame.getContentPane().add(canvas);
		
		frame.setBounds(100, 100, 967, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
