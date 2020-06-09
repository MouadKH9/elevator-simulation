package com.main.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.main.Canvas;

public class Person {

	public static int HEIGHT = 70;
	public static int WIDTH = 30;
	public static int numberOfPersons = 0;

	private int ID;
	private int position;
	private Platform platform;
	private Canvas canvas;

	private boolean done = false;
	private float scale = 1;

	private boolean woman;

	public Person(Platform platform, Canvas canvas) {
		this.canvas = canvas;
		this.platform = platform;
		this.position = platform.getNextPosition();
		this.ID = ++Person.numberOfPersons;
		this.woman = (new Random()).nextBoolean();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void callElevator(String direction) {
		ElevatorCall call = new ElevatorCall(direction, this, (Floor) platform);
		canvas.controller.callMade(call);
		System.out.println(this + " , direction: " + direction);
	}

	public int goToElevator(Floor floor, Elevator elevator, String direction) {
		this.position = elevator.getNextPosition();
		floor.takePerson(this);
		elevator.addPerson(this);
		this.platform = elevator;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.canvas.repaint();

		int randomDest;
		do {
			if (direction.equals("down"))
				randomDest = (new Random()).nextInt(floor.getNumber());
			else
				randomDest = (new Random()).nextInt(canvas.floors.size() - floor.getNumber() - 2);
		} while (randomDest == floor.getNumber());
		System.out.println(this + " => " + randomDest + " @ E" + elevator.getNumber());
		return randomDest;
	}

	public void goToFloor(Floor floor, Elevator elevator) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.position = floor.getNextPosition();
		floor.addPerson(this);
		elevator.takePerson(this);
		this.platform = floor;
		this.canvas.repaint();

		Thread thread = new Thread(() -> {
			try {
				this.disappear();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}

	public void draw(Graphics2D g2d) {
		int startX = platform.getStartX() - (position + 1) * (WIDTH + 10);

		try {
			g2d.drawImage(
					ImageIO.read(getClass().getResource("/com/main/assets/" + (woman ? "woman" : "man") + ".png")),
					startX, platform.getFloorY() - HEIGHT, (int) (scale * WIDTH), (int) (scale * HEIGHT), canvas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.setColor(Color.BLACK);
		int stringWidth = g2d.getFontMetrics().stringWidth("" + ID);
		if (!done) {
			g2d.setFont(new Font("Arial", Font.PLAIN, 16));
			g2d.drawString("" + ID, startX + WIDTH / 2 - stringWidth / 2, platform.getFloorY() - HEIGHT / 2 - 2);
		}

	}

	@Override
	public String toString() {
		return "P#" + ID;
	}

	public void disappear() throws InterruptedException {
		done = true;

		Thread.sleep(600);
		while (scale > 0.3) {
			Thread.sleep(25);
			scale -= 0.01;
			canvas.repaint();
		}

		platform.takePerson(this);
		canvas.repaint();

		numberOfPersons--;
		if (numberOfPersons < 2)
			canvas.addPersons();

	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
