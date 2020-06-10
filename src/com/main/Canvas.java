package com.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import com.main.entities.Controller;
import com.main.entities.Elevator;
import com.main.entities.Floor;
import com.main.entities.Person;

public class Canvas extends JPanel {

	public ArrayList<Floor> floors = new ArrayList<>();
	public Elevator elevator1;
	public Elevator elevator2;

	int rectWidth = 300;
	int rectHeight = 50;

	public static int WIDTH = 750;
	public static int HEIGHT = 850;

	boolean hovered = false;

	boolean startingScreen = true;

	public Controller controller;

	public Canvas() {
		elevator1 = new Elevator(this, false);
		elevator2 = new Elevator(this, true);

		controller = new Controller(elevator1, elevator2);

		floors.add(new Floor(0, this));
		floors.add(new Floor(1, this));
		floors.add(new Floor(2, this));
		floors.add(new Floor(3, this));
		floors.add(new Floor(4, this));

		// For the button

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if (!startingScreen)
					return;

				if (e.getX() < WIDTH / 2 - rectWidth / 2 || e.getX() > WIDTH / 2 - rectWidth / 2 + rectWidth
						|| e.getY() < HEIGHT / 2 - rectHeight / 2
						|| e.getY() > HEIGHT / 2 - rectHeight / 2 + rectHeight) {
					hovered = false;
					repaint();
					return;
				}
				hovered = true;
				repaint();
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (hovered) {
					startingScreen = false;
					addPersons();
				}
			}
		});

	}

	public int getWaitingNumber() {
		int number = 0;
		for (Floor floor : floors) {
			for (Person person : floor.getPersons()) {
				if (!person.isDone())
					number++;
			}
		}
		return number;
	}

	public void addPersons() {
		if (Person.numberOfPersons >= 2)
			return;
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(1000);
				Random random = new Random();
				for (int i = 0; i < 3; i++) {
					int rand = random.nextInt(floors.size());
					Person person = new Person(floors.get(rand), this);
					floors.get(rand).addPerson(person);
					repaint();

					person.callElevator(rand >= floors.size() / 2 ? "down" : "up");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"Add Persons");
		thread.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(new Color(72, 47, 247));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		

		floors.forEach(f -> f.draw(g2d));

		this.drawElevatorTrack(g2d);
		elevator1.draw(g2d);
		elevator2.draw(g2d);

		this.drawStatus(g2d);

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, HEIGHT - floors.size() * (Floor.HEIGHT + 7), WIDTH, 3);
		
		if (startingScreen)
			this.drawMenu(g2d);
		
	}

	public void drawMenu(Graphics2D g2d) {
		int stringWidth = g2d.getFontMetrics().stringWidth("Commencer");

		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		g2d.setColor(!hovered ? new Color(39, 174, 96) : new Color(30, 127, 70));
		g2d.fillRect(WIDTH / 2 - rectWidth / 2, HEIGHT / 2 - rectHeight / 2, rectWidth, rectHeight);

		g2d.setColor(Color.WHITE);
		g2d.drawString("Commencer", WIDTH / 2 - stringWidth / 2, HEIGHT / 2 + 7);
	}

	public void drawElevatorTrack(Graphics2D g2d) {
		int startY = floors.get(floors.size() - 1).getCeilingY();

		g2d.setColor(Color.GRAY);
		g2d.fillRect(Floor.WIDTH, startY, Elevator.WIDTH, HEIGHT);
		g2d.fillRect(0, startY, Elevator.WIDTH, HEIGHT);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(Floor.WIDTH + Elevator.WIDTH / 2 - 5, startY, 3, HEIGHT);
		g2d.fillRect(Elevator.WIDTH / 2 - 5, startY, 3, HEIGHT);
	}

	public void drawStatus(Graphics2D g2d) {

		g2d.setFont(new Font("Arial", Font.PLAIN, 30));
		g2d.setColor(Color.white);

		String title = "Simulation d'ascenseur";
		int titleWidth = g2d.getFontMetrics().stringWidth(title);
		g2d.drawString(title, WIDTH / 2 - titleWidth / 2, 32);

		g2d.setFont(new Font("Arial", Font.PLAIN, 22));
		g2d.drawString("Personnes en attente: " + getWaitingNumber(), 10, 70);
		g2d.drawString("Personnes dans l'ascenseur: " + (elevator1.getPersonsCount() + elevator1.getPersonsCount()), 10, 92);
		g2d.drawString("Ascenseur gauche au étage #" + elevator2.getCurrentFloor(), 10, 114);
		g2d.drawString("Ascenseur droit au étage #" + elevator1.getCurrentFloor(), 10, 136);

	}
}
