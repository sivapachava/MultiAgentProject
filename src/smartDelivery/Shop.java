package smartDelivery;


import java.util.concurrent.ThreadLocalRandom;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

//Create shop agent which includes the packages for delivery
public class Shop {
	private ContinuousSpace<Object> space;
	private int id;
	private boolean deliveryRequested;
	private boolean isArrived;
	public Request myRequest;	
	
	public Shop(ContinuousSpace<Object> space,  int id) {
		super();
		this.space = space;
		this.id = id;
		this.deliveryRequested = false;
		myRequest = null;

		isArrived  = false;
	}
	
	@ScheduledMethod(start = 10, interval = 10)
	public void askRequest(){
		
		if (!deliveryRequested && RandomHelper.nextIntFromTo(0, 100) > 80 && !isArrived)
			{
			myRequest = randomRequest();
			deliveryRequested = true;
			}
	}

	public boolean isRequested() {
		return deliveryRequested;
	}
	
	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public Request getMyRequest() {
		return myRequest;
	}

	
	public NdPoint getLocation(){
		NdPoint location = space.getLocation(this);
		return location;
	}

	@Override
	public String toString() {
		return "Package [id=" + id + ", deliveryRequested=" + deliveryRequested + ", myRequest=" + myRequest + "]";
	}

	//Create random request
	public Request randomRequest(){
		NdPoint origin = space.getLocation(this);
		double nX = RandomHelper.nextDoubleFromTo(0, space.getDimensions().getWidth());
		double nY = RandomHelper.nextDoubleFromTo(0, space.getDimensions().getHeight());
		
		int timewindow = ThreadLocalRandom.current().nextInt(50, 200 + 1);
		NdPoint dest = new NdPoint(nX,nY); 
		return new Request(this.id, origin, dest, timewindow,(int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount());	
	}
	
	public void arrived() {
		myRequest = null;
		deliveryRequested = false;
		isArrived = true;
	}
	
	public boolean isArrived() {
		return isArrived;
	}
}
