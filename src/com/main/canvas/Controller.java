package com.main.canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;

public class Controller {
	
	public Elevator e1;
	public Elevator e2;
	
	public Controller(Elevator e1, Elevator e2) {
		this.e1 = e1;
		this.e2 = e2;
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
				checkElevator(e1);
	    		checkElevator(e2);
		    	
		    }
		};
		Timer timer = new Timer(1000 ,taskPerformer);
		timer.start();
	}
	
	public void checkElevator(Elevator elevator) {
		if(elevator.isBusy()) return;
		
		if(elevator.getDests().size()>0) {
    		checkElevatorDests(elevator);
    	}else{
    		checkElevatorCalls(elevator);
    	}
	}
	
	public void checkElevatorDests(Elevator elevator) {
		elevator.getDests().sort((c1,c2) -> {
			return elevator.elevatorY - c1.getFloor().getCeilingY() > elevator.elevatorY - c2.getFloor().getCeilingY() ? 1 : -1;
		});
		DestCall dest = elevator.getDests().get(0);
		elevator.dropPersonAt(dest.getPerson(), dest.getFloor());
		elevator.getDests().remove(0);
	}
	
	public void checkElevatorCalls(Elevator elevator) {
		elevator.getCalls().sort((c1,c2) -> {
			return elevator.elevatorY - c1.getFloor().getCeilingY() > elevator.elevatorY - c2.getFloor().getCeilingY() ? 1 : -1;
		});
		
		Iterator<ElevatorCall> it = elevator.getCalls().iterator();
    	while(it.hasNext()) {
    		ElevatorCall call = it.next();
    		elevator.getCurrentCalls().add(call);
    		elevator.goToFloor(call);
        	it.remove();
        	break;
    	}
	}

	public void callMade(ElevatorCall call) {
		Elevator elevator;
		
		if(e1.isBusyAndFree() && e2.isBusyAndFree())
			elevator = getLessCalls();
		else if(!e1.isBusyAndFree() && !e2.isBusyAndFree())
			elevator = getClosest(call);
		else 
			elevator = !e1.isBusyAndFree() ? e1 : e2; 
		
		call.setElevator(elevator);
		elevator.getCalls().add(call);
	}
	
	public ArrayList<ElevatorCall> getAllCalls(){
		ArrayList<ElevatorCall> result = new ArrayList<ElevatorCall>(e1.getCalls());
		result.addAll(e2.getCalls());
		return result;
	}
	
	public Elevator getClosest(ElevatorCall call) {
		return Math.abs(e1.getCurrentFloor() - call.getFloor().getNumber()) < 
				Math.abs(e2.getCurrentFloor() - call.getFloor().getNumber()) ? e1 : e2;
	}
	
	public Elevator getLessCalls() {
		return e1.getCalls().size() < e2.getCalls().size() ? e1 : e2;
	}
	
}
