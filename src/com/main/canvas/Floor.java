package com.main.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Floor extends Platform{
	
	public static int HEIGHT = 150;
	public static int WIDTH = 700;
	
	private int number;
	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	
	public Floor(int number,Canvas canvas) {
		this.canvas = canvas;
		this.number = number;
		
		persons.add(new Person(0,this,canvas));
		persons.add(new Person(1,this,canvas));
		persons.add(new Person(2,this,canvas));
		persons.add(new Person(3,this,canvas));
	}
	
	public int getFloorY() {
		return canvas.getBounds().height - number * HEIGHT;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public void draw(Graphics2D g2d) {
		int ceilingY = canvas.getBounds().height - (number + 1) * HEIGHT;
		int startX = 0;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(startX, ceilingY, WIDTH, 3);
		g2d.fillRect(WIDTH - 3, ceilingY, 3, 70);
		
		g2d.setFont(new Font("Arial",Font.PLAIN,20));
		g2d.drawString("Etage " + number, 5, ceilingY + 25);
		
		persons.forEach(p->p.draw(g2d));
	}
}
