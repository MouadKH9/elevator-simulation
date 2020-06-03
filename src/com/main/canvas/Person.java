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
	
	public int goToElevator(Floor floor, Elevator elevator) {
		this.position = elevator.getNextPosition();
		floor.takePerson(this);
		elevator.addPerson(this);
		this.platform = elevator;
		this.canvas.repaint();
		
		int randomDest; 
		do{
			randomDest = (new Random()).nextInt(canvas.floors.size());
		}while(randomDest == floor.getNumber());
		System.out.println(this + " wants to go to floor #"+ randomDest);
		return randomDest;
	}
	
	public void goToFloor(Floor floor, Elevator elevator) {
		this.position = floor.getNextPosition();
		floor.addPerson(this);
		elevator.takePerson(this);
		this.platform = floor;
		this.canvas.repaint();
	}
	
	public void draw(Graphics2D g2d) {
		int startX = platform.getStartX() - (position + 1) * (WIDTH + 20);
		
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
	
	
}
