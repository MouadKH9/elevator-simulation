package com.main.canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Elevator extends Platform{
	
	public static int HEIGHT = 150;
	public static int WIDTH = 250;
	
	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	private int elevatorY = -1;
	
	public Elevator(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public int getFloorY() {
		return elevatorY + HEIGHT;
	}
	
	public void goToFloor(int number) {
		Thread thread = new Thread(() -> { 
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(this.elevatorY);
			System.out.println(canvas.floors.get(number).getFloorY());
			while(this.elevatorY + HEIGHT != canvas.floors.get(number).getFloorY()) {
				this.elevatorY -= 1;
				canvas.repaint();
			}
		});
		thread.start();
	}
	
	public void draw(Graphics2D g2d) {
		if(elevatorY == -1) 
			this.elevatorY = canvas.getBounds().height - Floor.HEIGHT;
		
		g2d.setColor(Color.BLACK);
		g2d.drawRect(Floor.WIDTH, elevatorY, WIDTH, HEIGHT);
		
		persons.forEach(p->p.draw(g2d));
	}
}
