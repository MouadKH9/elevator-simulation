package com.main.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Canvas extends JPanel {
	
	public ArrayList<Floor> floors = new ArrayList<>();
	public Elevator elevator;
	
	public Canvas(){
		elevator = new Elevator(this);
		
		floors.add(new Floor(0,this));
		floors.add(new Floor(1,this));
		floors.add(new Floor(2,this));
		floors.add(new Floor(3,this));
		
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
		
		addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseClicked(MouseEvent mouseEvent) {
		    	 System.out.println("(" + mouseEvent.getX() + "," + mouseEvent.getY() + ")");
		     }
		});
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(25,163,255));
		g2d.fillRect(0, 0, 1000, 1000);

		g2d.setColor(new Color(255,204,0));
		g2d.fillOval(40, 20, 40, 40);
		
		floors.forEach(f->f.draw(g2d));

		g2d.setColor(Color.GRAY);
		g2d.fillRect(Floor.WIDTH, 0, Elevator.WIDTH, 1000);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(Floor.WIDTH + Elevator.WIDTH / 2 - 5, 0, 3, 1000);
		
		elevator.draw(g2d);
	}
}
