package com.main.canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Elevator extends Platform{
	
	public static int HEIGHT = 150;
	public static int WIDTH = 250;
	
	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	private int elevatorY = -1;
	
	
	private ArrayList<ElevatorCall> calls = new ArrayList<ElevatorCall>();
	private ArrayList<ElevatorCall> currentCalls = new ArrayList<ElevatorCall>();

	private Queue<DestCall> dests = new LinkedList<DestCall>();
	
	private boolean busy = false;
	
	public Elevator(Canvas canvas) {
		this.canvas = canvas;
		
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	System.out.println(busy);
		    	if(busy) return;
		    	
		    	if(dests.size()>0) {
		    		System.out.println(dests.size());
		    		DestCall dest = dests.poll();
		    		dropPersonAt(dest.getPerson(), dest.getFloor());
		    	}else{
		    		Iterator<ElevatorCall> it = calls.iterator();
			    	while(it.hasNext()) {
			    		ElevatorCall call = it.next();
			        	currentCalls.add(call);
			        	goToFloor(call);
			        	it.remove();
			        	break;
			    	}
		    	}
		    	
		    }
		};
		Timer timer = new Timer(1000 ,taskPerformer);
		timer.start();
	}
	
	public void goToFloor(ElevatorCall call) {
		Thread thread = new Thread(() -> {
			try {
				busy = true;
				Floor floor = call.getFloor();

				ArrayList<DestCall> extraDests = moveToY(floor.getCeilingY());
				currentCalls.remove(call);

				extraDests.forEach(dest -> {
					dests.add(dest);
				});
				
				int destination = call.getPerson().goToElevator(call.getFloor(),call.getElevator(),call.getDirection());
				goToDestination(destination, call.getPerson());
				busy=false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"goToFloor");
		thread.start();
	}
	
	public void goToDestination(int number, Person person) {
		dests.add(new DestCall(person, canvas.floors.get(number), this));
	}
	
	public void dropPersonAt(Person person, Floor floor) {
		Thread thread = new Thread(() -> {
			try {
				busy = true;
				ArrayList<DestCall> extraDests = moveToY(floor.getCeilingY());
				person.goToFloor(floor,this);
				extraDests.forEach(dest -> {
					dests.add(dest);
				});
				busy = false;
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
		
		// to avoid ConcurrentModification
		ArrayList<Person> tempP = new ArrayList<Person>(persons);

		
		tempP.forEach(p -> p.draw(g2d));
	}
	
	public ArrayList<DestCall> moveToY(int Y) throws InterruptedException {
		String direction = elevatorY - Y > 0 ? "up" : "down";
		ArrayList<DestCall> extraDests = new ArrayList<DestCall>();
		while(elevatorY < Y - 5 || elevatorY > Y + 5 ) {
			Thread.sleep(50);
			this.elevatorY += 5 * ((elevatorY < Y - 5) ? 1 : -1);
			canvas.repaint();
			
			canvas.floors.forEach(floor -> {
				if(elevatorY >= floor.getCeilingY() - 5 && elevatorY <= floor.getCeilingY() + 5) {
					Iterator<ElevatorCall> it = calls.iterator();
			    	while(it.hasNext()) {
			    		ElevatorCall call = it.next();
			    		if(!call.getFloor().equals(floor) || !direction.equals(call.getDirection())) continue;
			    		Floor destFloor = canvas.floors.get(call.getPerson().goToElevator(call.getFloor(), this,call.getDirection()));
			    		DestCall destCall = new DestCall(call.getPerson(), destFloor, this);
			        	extraDests.add(destCall);
			        	it.remove();
			    	}
				}
			});
		}
		this.elevatorY = Y;
		return extraDests;
	}
	
	
	public void callMade(ElevatorCall call) {
		calls.add(call);
	}
	
	public void addPerson(Person person) {
		persons.add(person);
	}
	
	public void takePerson(Person person) {
		persons.remove(person);
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
	

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public String toString() {
		return "Elevator [number of persons: " + persons.size() + "]";
	}
	
	

}
