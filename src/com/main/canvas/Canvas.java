package com.main.canvas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Canvas extends java.awt.Canvas {
	
	public ArrayList<Floor> floors = new ArrayList<>();
	
	public Canvas(){
		floors.add(new Floor(0,this));
		floors.add(new Floor(1,this));
		floors.add(new Floor(2,this));
		floors.add(new Floor(3,this));
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		floors.forEach(f->f.draw(g2d));
		
	}
}
