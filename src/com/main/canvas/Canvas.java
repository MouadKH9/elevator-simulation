package com.main.canvas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Canvas extends java.awt.Canvas {
	
	public ArrayList<Floor> floors = new ArrayList<>();
	public Elevator elevator;
	
	public Canvas(){
		elevator = new Elevator(this);
		
		floors.add(new Floor(0,this));
		floors.add(new Floor(1,this));
		floors.add(new Floor(2,this));
		floors.add(new Floor(3,this));
		
		elevator.goToFloor(2);
		
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		floors.forEach(f->f.draw(g2d));
		elevator.draw(g2d);
	}
}
