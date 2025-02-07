package com.main.entities;

public abstract class Platform {

	public abstract int getCeilingY();

	public abstract int getFloorY();

	public abstract int getStartX();

	public abstract int getNextPosition();

	public abstract void addPerson(Person person);

	public abstract void takePerson(Person person);

}
