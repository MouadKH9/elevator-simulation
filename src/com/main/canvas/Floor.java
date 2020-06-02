package com.main.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Floor extends Platform{
	
	public static int HEIGHT = 150;
	public static int WIDTH = 700;
	
	private int number;
	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	
	public Floor(int number,Canvas canvas) {
		this.canvas = canvas;
		this.number = number;
	}
	

	public void repaint() {
		canvas.repaint();
	}
	
	public void addPerson(Person person) {
		persons.add(person);
	}
	
	public void takePerson(Person person) {
		persons.remove(person);
	}
	
	@Override
	public int getCeilingY() {
		return getFloorY() - HEIGHT;
	}
	
	public int getFloorY() {
		return canvas.getBounds().height - number * HEIGHT;
	}
	
	public int getStartX() {
		return WIDTH;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	@Override
	public int getNextPosition() {
		return persons.size();
	}
	
	public void draw(Graphics2D g2d) {
		int ceilingY = canvas.getBounds().height - (number + 1) * HEIGHT;
		int startX = 0;

		try {
			g2d.drawImage(ImageIO.read(getClass().getResource("/com/main/assets/wall.jpg")),
					startX, ceilingY , WIDTH, HEIGHT, canvas);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(startX, ceilingY, WIDTH, 3);
		
		g2d.setFont(new Font("Arial",Font.PLAIN,20));
		g2d.drawString("Etage " + number, 5, ceilingY + 25);
		
		persons.forEach(p->p.draw(g2d));
	}

	@Override
	public String toString() {
		return "Floor [number=" + number + "]";
	}


	public Canvas getCanvas() {
		return canvas;
	}


	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	
	
	
}
