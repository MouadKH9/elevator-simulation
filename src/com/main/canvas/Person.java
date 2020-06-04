package com.main.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Person {
	
	public static int HEIGHT = 80;
	public static int WIDTH = 40;
	public static int numberOfPersons = 0;
	
	private int ID;
	private int position;
	private Platform platform;
	private Canvas canvas;
	
	private boolean done = false;
	private int doneX = 0;
	
	public Person(Platform platform,Canvas canvas) {
		this.canvas = canvas;
		this.platform = platform;
		this.position = platform.getNextPosition();
		this.ID = ++Person.numberOfPersons;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public void callElevator(String direction) {
		ElevatorCall call = new ElevatorCall(direction,this,(Floor) platform,canvas.elevator);
		canvas.elevator.callMade(call);
		System.out.println(this + " is calling the elevator, direction: " + direction);
	}
	
	public int goToElevator(Floor floor, Elevator elevator,String direction) {
		this.position = elevator.getNextPosition();
		floor.takePerson(this);
		elevator.addPerson(this);
		this.platform = elevator;
		this.canvas.repaint();
		
		int randomDest; 
		do {
			if(direction.equals("down"))
				randomDest = (new Random()).nextInt(floor.getNumber());
			else
				randomDest = (new Random()).nextInt(canvas.floors.size() - floor.getNumber() - 2);
		}
		while(randomDest == floor.getNumber());
		System.out.println(this + " wants to go to floor #"+ randomDest);
		return randomDest;
	}
	
	public void goToFloor(Floor floor, Elevator elevator) {
		this.position = floor.getNextPosition();
		floor.addPerson(this);
		elevator.takePerson(this);
		this.platform = floor;
		this.canvas.repaint();
		
		Thread thread = new Thread(()->{
			try {
				Thread.sleep(1000);
				this.disappear();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}
	
	public void draw(Graphics2D g2d) {
		int startX = !done ? platform.getStartX() - (position + 1) * (WIDTH + 20) : doneX;
		
		try {
			g2d.drawImage(ImageIO.read(getClass().getResource("/com/main/assets/person.png")),
					startX, platform.getFloorY() - HEIGHT, WIDTH, HEIGHT, canvas);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.setColor(Color.WHITE);
		int stringWidth = g2d.getFontMetrics().stringWidth(""+ID);
		g2d.setFont(new Font("Arial",Font.PLAIN,18));
		g2d.drawString(""+ID, startX + WIDTH/2 - stringWidth/2, platform.getFloorY()-HEIGHT/2-2);
		
	}

	@Override
	public String toString() {
		return "Person [ID=" + ID + "]";
	}
	
	public void disappear() throws InterruptedException {
		doneX = platform.getStartX() - (position + 1) * (WIDTH + 20);
		done = true;
		while(doneX > 20) {
			Thread.sleep(25);
			doneX -= 5;
			canvas.repaint();
		}
		
		platform.takePerson(this);
		canvas.repaint();
	}
	
	
}
