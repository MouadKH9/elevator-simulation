package com.main.canvas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Canvas extends JPanel {
	
	public ArrayList<Floor> floors = new ArrayList<>();
	public Elevator elevator;
	
	int rectWidth = 300;
	int rectHeight = 50;
	
	public static int WIDTH = 967;
	public static int HEIGHT = 900;
	
	boolean hovered = false;
	
	boolean startingScreen = true;
	
	public Canvas(){
		elevator = new Elevator(this);
		
		floors.add(new Floor(0,this));
		floors.add(new Floor(1,this));
		floors.add(new Floor(2,this));
		floors.add(new Floor(3,this));
		floors.add(new Floor(4,this));
		
		// For the button
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e){
				if(!startingScreen) return;
				
				if(e.getX() < WIDTH/2 - rectWidth/2 || e.getX() > WIDTH/2 - rectWidth/2 + rectWidth || 
					e.getY() < HEIGHT/2 - rectHeight/2 || e.getY() > HEIGHT/2 - rectHeight/2 + rectHeight) {
					hovered = false;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					repaint();
					return;
				}
				hovered = true;
				repaint();
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(hovered) {
					startingScreen = false;
					addPersons();
				}
			}
		});
		
	}
	
	public void addPersons(){
		if(Person.numberOfPersons >= 2 ) return;
		Thread thread = new Thread(() -> { 
			try {
				Thread.sleep(1000);
				Random random = new Random();
				for (int i = 0; i < 5; i++) {
					int rand = random.nextInt(floors.size());
					Person person = new Person(floors.get(rand), this);
					floors.get(rand).addPerson(person);
					repaint();
					
					person.callElevator(rand >= floors.size()/2 ? "down" : "up");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(25,163,255));
		g2d.fillRect(0, 0, 1000, 1000);

		
		floors.forEach(f->f.draw(g2d));

		int startY = floors.get(floors.size() - 1).getCeilingY();
		
		g2d.setColor(Color.GRAY);
		g2d.fillRect(Floor.WIDTH, startY, Elevator.WIDTH, 1000);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(Floor.WIDTH + Elevator.WIDTH / 2 - 5, startY, 3, 1000);
		
		elevator.draw(g2d);
		
		if(!startingScreen) return;
		
		this.drawMenu(g2d);
		
	}
	
	public void drawMenu(Graphics2D g2d){
		int stringWidth = g2d.getFontMetrics().stringWidth("Commencer");
		
		g2d.setColor(new Color(0,0,0,200));
		g2d.fillRect(0, 0, WIDTH , HEIGHT);
		
		g2d.setColor(!hovered ? new Color(39, 174, 96) : new Color(30, 127, 70));
		g2d.fillRect(WIDTH/2 - rectWidth/2, HEIGHT/2 - rectHeight/2, rectWidth, rectHeight);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Commencer", WIDTH/2 - stringWidth/2, HEIGHT/2 + 7);
	}
}
