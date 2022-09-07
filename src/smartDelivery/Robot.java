package smartDelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import repast.simphony.context.Context;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

//Create robot agent
public class Robot {
	private boolean hasRequestforPackage;
	private Context<Object> context;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	private String name;
	private double speed;
	private Shop pkg;
	private ArrayList<AgentMessage> mailbox;
	private static final double THRESHOLD = 2;
	Interaction i;
	
	// Define the array list of package requests from shop, then it can be shared between robots
	private ArrayList<Object> sharedRequest;
	
	public Robot(Context<Object> context, Grid<Object> grid, ContinuousSpace<Object> space, String name, Interaction i ) {
		this.context = context;
		this.grid = grid;
		this.space = space;
		this.name = name;
		UUID.randomUUID().toString();
		this.hasRequestforPackage = false;
		pkg = null;
		mailbox = new ArrayList<AgentMessage>();
		this.i = i;
		sharedRequest=new ArrayList<Object>() ;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		AgentMessage m = read();
		GridPoint pt = grid.getLocation(this);

		while (m != null) {
			System.out.println(m);
			m = read();
		}
		if (!hasRequestforPackage) {
			List<Object> packages = new ArrayList<Object>();
			
			// Robot will select the nearest request from the shop.If there is no nearest request then used 'shared_request'
			if(!sharedRequest.isEmpty())
			{
				for ( Object cli : sharedRequest) {
					packages.add(cli);
				}	
			}
			
			// Increases the shared area of a request from shop
			 GridCellNgh <Package> nghCreator = new GridCellNgh <Package>( grid , pt , Package . class , 2 , 2);
			 List < GridCell < Package > > gridCells = nghCreator . getNeighborhood ( true );
			 SimUtilities . shuffle ( gridCells , RandomHelper . getUniform ());
			 GridPoint pointOfPackage = null ;
			
			 for ( GridCell < Package > cell : gridCells ) {
				 pointOfPackage = cell . getPoint ();
				 for (Object obj : grid.getObjectsAt(pointOfPackage.getX(), pointOfPackage.getY())) {
					if (obj instanceof Package) {
						if (((Shop) obj).isRequested()) {
							packages.add(obj);
						}
					}
				}	 
			 }			

			if (packages.size() > 0) {

				// Find the shortest request for delivery from shop and select that request
				int count = 0 ;
				int shortDIstanceCount = 0;
				double min = Double.MAX_VALUE;
				GridPoint myPoint = grid.getLocation(this);
				for (Object pkg : packages) {
					
					// check for requests
					if(((Shop) pkg).isRequested()) {
						NdPoint cpt = ((Shop) pkg).getMyRequest().origin;
						double xdistance = Math.abs((myPoint.getX() - cpt.getX()));
						double ydistance = Math.abs((myPoint.getY() - cpt.getY()));
						double xy = Math.sqrt(xdistance * xdistance + ydistance * ydistance);
						if (xy <= min) {
							shortDIstanceCount  = count;
							min = xy;
						}
						count++;
					}
				}
				
				System.out.println("Short Distance Request is :"+ packages.get(shortDIstanceCount ).toString());
				this.pkg = (Shop) packages.get(shortDIstanceCount );
				
				context.remove(packages.get(shortDIstanceCount ));
				hasRequestforPackage = true;
				
				// Remove selected request from request list
				packages.remove(shortDIstanceCount);
				
			}
			else {
				space.moveByVector(this, 1, RandomHelper.nextDoubleFromTo(0, Math.PI * 2),
						RandomHelper.nextDoubleFromTo(0, Math.PI * 2));
				grid.moveTo(this, (int) space.getLocation(this).getX(), (int) space.getLocation(this).getY());
			}

		} else {
			if (Math.abs((pt.getX() - pkg.getMyRequest().getDestination().getX())) < THRESHOLD
					&& Math.abs((pt.getY() - pkg.getMyRequest().getDestination().getY())) < THRESHOLD) {
				pkg.arrived();
				send(new AgentMessage(this.name, "arrived", "inform"));
				context.add(pkg);
				
				space.moveTo(pkg, pt.getX(), pt.getY());
				grid.moveTo(pkg, (int) pt.getX(), (int) pt.getY());
				hasRequestforPackage = false;
				pkg = null;
			} 
			else 
			{
				double angle = SpatialMath.calcAngleFor2DMovement(space, space.getLocation(this),
						pkg.getMyRequest().getDestination());
				space.moveByVector(this, 1, angle, 0);
				grid.moveTo(this, (int) space.getLocation(this).getX(), (int) space.getLocation(this).getY());
			}
		}
	}

	protected void send(AgentMessage m) {
		i.send(this, m);
	}
	
	public void receive(AgentMessage m) {
		mailbox.add(m);
	}
	
	// Receive other agent request to this robot
	public void receiveRequestPackages(List<Object> obj) {
		if(!obj.isEmpty())
		{
			for (Object o: obj)
			{
				sharedRequest.add(o);
			}	
		}
	}

	private AgentMessage read() {
		if (mailbox.size() > 0)
			return mailbox.remove(0);
		return null;
	}

	
	public boolean isHasRequestforPackage() {
		return hasRequestforPackage;
	}

	
	public void setHasCustomer(boolean hasPackage) {
		this.hasRequestforPackage = hasPackage;
	}

	public int calculateTravelTime(Request request) {
		NdPoint currentLoc = space.getLocation(this);
		NdPoint location1 = request.getOrigin();
		NdPoint location2 = request.getDestination();

		// Calculate time between current position and first source
		double distance1 = this.space.getDistance(currentLoc, location1);
		// Calculate time between first source and second source
		double distance2 = this.space.getDistance(location1, location2);
		// Added times
		double totaldistance = distance1 + distance2;
		int totaltime = (int) (totaldistance / (this.speed * 0.5));

		return totaltime;
	}

	public Context<Object> getContext() {
		return context;
	}

	/**
	 * Getter
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}


	public double distance(Robot obj) {
		NdPoint currentLocation = space.getLocation(this);
		NdPoint otherLocation = space.getLocation(obj);
		return this.space.getDistance(currentLocation, otherLocation);
	}
}
