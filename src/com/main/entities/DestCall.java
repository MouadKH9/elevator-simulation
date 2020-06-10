package com.main.entities;

public class DestCall {
	private Floor floor;
	private Person person;
	private Elevator elevator;

	public DestCall(Person person, Floor floor, Elevator elevator) {
		this.person = person;
		this.floor = floor;
		this.elevator = elevator;
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

}
