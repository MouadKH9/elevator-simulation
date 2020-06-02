package com.main.canvas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
		
		Person person1 = new Person(floors.get(2),this);
		floors.get(2).addPerson(person1);
		
		Thread thread = new Thread(() -> { 
			try {
				Thread.sleep(2000);
				person1.callElevator("down");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		floors.forEach(f->f.draw(g2d));

		g2d.setColor(Color.GRAY);
		g2d.fillRect(Floor.WIDTH, 0, Elevator.WIDTH, 1000);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(Floor.WIDTH + Elevator.WIDTH / 2 - 5, 0, 3, 1000);
		
		elevator.draw(g2d);
	}
}
