package com.main.entities;

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

import com.main.Canvas;

public class Elevator extends Platform {

	public static int HEIGHT = 130;
	public static int WIDTH = 200;

	public static int MAX_PERSONS = 4;

	private Canvas canvas;
	private ArrayList<Person> persons = new ArrayList<Person>();
	int elevatorY = -1;

	private ArrayList<ElevatorCall> calls = new ArrayList<ElevatorCall>();
	private ArrayList<ElevatorCall> currentCalls = new ArrayList<ElevatorCall>();

	private ArrayList<DestCall> dests = new ArrayList<DestCall>();

	private boolean busy = false;
	private boolean second;

	private int currentFloor = 0;

	public Elevator(Canvas canvas, boolean second) {
		this.canvas = canvas;
		this.second = second;
	}

	public void goToFloor(ElevatorCall call) {
		if (persons.size() == MAX_PERSONS)
			return;
		Thread thread = new Thread(() -> {
			try {
				busy = true;
				Floor floor = call.getFloor();

				ArrayList<DestCall> extraDests = moveToY(floor.getCeilingY());
				currentCalls.remove(call);

				extraDests.forEach(dest -> {
					dests.add(dest);
				});

				int destination = call.getPerson().goToElevator(call.getFloor(), call.getElevator(),
						call.getDirection());
				goToDestination(destination, call.getPerson());
				busy = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "goToFloor");
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
				person.goToFloor(floor, this);
				extraDests.forEach(dest -> {
					dests.add(dest);
				});
				busy = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "goToFloor");
		thread.start();
	}

	public void draw(Graphics2D g2d) {
		if (elevatorY == -1)
			this.elevatorY = canvas.getBounds().height - Floor.HEIGHT;

		g2d.setColor(Color.BLACK);
		try {
			g2d.drawImage(ImageIO.read(getClass().getResource("/com/main/assets/elevator.png")),
					second ? 0 : Floor.WIDTH, elevatorY, WIDTH, HEIGHT, canvas);

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
		while (elevatorY < Y - 5 || elevatorY > Y + 5) {
			Thread.sleep(40);
			this.elevatorY += 5 * ((elevatorY < Y - 5) ? 1 : -1);
			canvas.repaint();

			canvas.floors.forEach(floor -> {
				if (persons.size() < MAX_PERSONS && elevatorY >= floor.getCeilingY() - 20
						&& elevatorY <= floor.getCeilingY() + 20) {

					this.currentFloor = floor.getNumber();

					Iterator<DestCall> itExtraDest = extraDests.iterator();
					while (itExtraDest.hasNext()) {
						DestCall call = itExtraDest.next();
						if (!call.getFloor().equals(floor))
							continue;
						call.getPerson().goToFloor(floor, this);
						itExtraDest.remove();
					}

					Iterator<DestCall> itDest = dests.iterator();
					while (itDest.hasNext()) {
						DestCall call = itDest.next();
						if (!call.getFloor().equals(floor))
							continue;
						call.getPerson().goToFloor(floor, this);
						itDest.remove();
					}

					// Checking if any calls are in this floor
					Iterator<ElevatorCall> it = canvas.controller.getAllCalls().iterator();
					while (it.hasNext()) {
						ElevatorCall call = it.next();
						if (!call.getFloor().equals(floor) || !direction.equals(call.getDirection()))
							continue;
						if (persons.size() == MAX_PERSONS)
							break;
						Floor destFloor = canvas.floors
								.get(call.getPerson().goToElevator(call.getFloor(), this, call.getDirection()));
						DestCall destCall = new DestCall(call.getPerson(), destFloor, this);
						extraDests.add(destCall);
						call.getElevator().getCalls().remove(call);
					}
				}
			});
		}
		this.elevatorY = Y;
		return extraDests;
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
		return second ? WIDTH : Floor.WIDTH + WIDTH;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public String toString() {
		return "E" + getNumber();
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public int getPersonsCount() {
		return this.persons.size();
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}

	public int getElevatorY() {
		return elevatorY;
	}

	public void setElevatorY(int elevatorY) {
		this.elevatorY = elevatorY;
	}

	public ArrayList<ElevatorCall> getCalls() {
		return calls;
	}

	public void setCalls(ArrayList<ElevatorCall> calls) {
		this.calls = calls;
	}

	public ArrayList<ElevatorCall> getCurrentCalls() {
		return currentCalls;
	}

	public void setCurrentCalls(ArrayList<ElevatorCall> currentCalls) {
		this.currentCalls = currentCalls;
	}

	public ArrayList<DestCall> getDests() {
		return dests;
	}

	public void setDests(ArrayList<DestCall> dests) {
		this.dests = dests;
	}

	public boolean isBusy() {
		return busy;
	}

	public boolean isBusyAndFree() {
		return busy || calls.size() > 0;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public int getNumber() {
		return this.second ? 2 : 1;
	}

	public String getCallsAsString() {
		StringBuilder sb = new StringBuilder("");
		calls.forEach(call -> sb.append(call.getPerson() + " "));
		return sb.toString();
	}

	public String getDestsAsString() {
		StringBuilder sb = new StringBuilder("");
		calls.forEach(call -> sb.append(call.getFloor() + " "));
		return sb.toString();
	}

}
