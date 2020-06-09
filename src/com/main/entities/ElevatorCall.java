package com.main.entities;

public class ElevatorCall {
	private String direction;
	private Floor floor;
	private Person person;
	private Elevator elevator;

	public ElevatorCall(String direction, Person person, Floor floor) {
		this.direction = direction;
		this.person = person;
		this.floor = floor;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	@Override
	public String toString() {
		return "Call: " + person + " => " + floor + " @ " + elevator;
	}

}
