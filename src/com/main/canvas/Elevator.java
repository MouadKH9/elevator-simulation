package com.main.canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Elevator extends Platform{
	
	public static int HEIGHT = 150;
	public static int WIDTH = 250;
	
	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	private int elevatorY = -1;
	
	
	ActionListener taskPerformer;
	
	
	private ArrayList<ElevatorCall> calls = new ArrayList<ElevatorCall>();
	private ArrayList<ElevatorCall> currentCalls = new ArrayList<ElevatorCall>();
	
	public Elevator(Canvas canvas) {
		this.canvas = canvas;
		
		taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	Iterator<ElevatorCall> it = calls.iterator();
		    	while(it.hasNext()) {
		    		ElevatorCall call = it.next();
		        	currentCalls.add(call);
		        	goToFloor(call);
		        	it.remove();
		    	}
		    }
		};
		Timer timer = new Timer(500 ,taskPerformer);
		timer.start();
	}
	
	public void callMade(ElevatorCall call) {
		calls.add(call);
	}
	
	public void addPerson(Person person) {
		persons.add(person);
	}
	
	
	@Override
	public int getNextPosition() {
		return persons.size();
	}
	
	@Override
	public int getCeilingY() {
		return getFloorY() - HEIGHT;
	}
	
	@Override
	public int getFloorY() {
		return elevatorY + HEIGHT;
	}
	
	@Override
	public int getStartX() {
		return Floor.WIDTH + WIDTH;
	}
	
	public void repaint() {
		canvas.repaint();
	}
	

	
	public void goToFloor(ElevatorCall call) {
		Thread thread = new Thread(() -> {
			try {
				Floor floor = call.getFloor();
				while(elevatorY < floor.getCeilingY() - 5 || elevatorY > floor.getCeilingY() + 5 ) {
					Thread.sleep(50);
					this.elevatorY -= 5;
					canvas.repaint();
				}
				this.elevatorY = floor.getCeilingY();
				
				currentCalls.remove(call);
				
				call.getFloor().takePerson(call.getPerson());
				call.getElevator().addPerson(call.getPerson());
				call.getFloor().getCanvas().repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"goToFloor");
		thread.start();
	}
	
	public void draw(Graphics2D g2d) {
		if(elevatorY == -1) 
			this.elevatorY = canvas.getBounds().height - Floor.HEIGHT;
		
		g2d.setColor(Color.BLACK);
		try {
			g2d.drawImage(ImageIO.read(getClass().getResource("/com/main/assets/elevator.png")),
					Floor.WIDTH, elevatorY , WIDTH, HEIGHT, canvas);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		persons.forEach(p -> p.draw(g2d));
	}
	

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public String toString() {
		return "Elevator []";
	}
	
	

}
