package smartDelivery;

import java.util.UUID;

import repast.simphony.space.continuous.NdPoint;


public class Request {
	String uuid;
	int packageNo;
	NdPoint destination;
	NdPoint origin;
	int time;
	int askedAt;
	int takenAt;
	int finishedAt;
	int estimatedTimeforTravel;
	String id;
	

	public Request(int packageNo, NdPoint origin, NdPoint destination, int time, int askedAt) {
		
		this.uuid = UUID.randomUUID().toString();
		this.packageNo = packageNo;
		
		this.origin = origin;
		this.destination = destination;
		
		this.time = time;
		this.askedAt = askedAt;
		this.takenAt = 0;
		this.finishedAt = 0;
		this.estimatedTimeforTravel = 0;
		this.id = "";

	}

	
	public String getUuid() {
		return uuid;
	}

	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	public int getSource() {
		return packageNo;
	}

	
	public void setSource(int source) {
		this.packageNo = source;
	}

	
	public NdPoint getDestination() {
		return destination;
	}

	
	public void setDestination(NdPoint destination) {
		this.destination = destination;
	}

	
	public int getAskedAt() {
		return askedAt;
	}

	
	public void setAskedAt(int askedAt) {
		this.askedAt = askedAt;
	}

	
	public int getFinishedAt() {
		return finishedAt;
	}

	
	public void setFinishedAt(int finishedAt) {
		this.finishedAt = finishedAt;
	}

	
	public int gettime() {
		return time;
	}

	
	public void settime(int time) {
		this.time = time;
	}

	
	public int getTakenAt() {
		return takenAt;
	}

	
	public void setTakenAt(int takenAt) {
		this.takenAt = takenAt;
	}

	
	public int getestimatedTimeforTravel() {
		return estimatedTimeforTravel;
	}

	
	public void setestimatedTimeforTravel(int estimatedTimeforTravel) {
		this.estimatedTimeforTravel = estimatedTimeforTravel;
	}

	
	public String getVehicleuuid() {
		return id;
	}

	
	public void setRobotuuid(String id) {
		this.id = id;
	}

	
	@Override
	public String toString() {
		return "Request [uuid=" + uuid + ", source=" + packageNo + ", destination=" + destination + ", time="
				+ time + ", askedAt=" + askedAt + ", takenAt=" + takenAt + ", finishedAt=" + finishedAt
				+ ", estimatedTimeforTravel=" + estimatedTimeforTravel + ", id=" + id + "]";
	}

	public NdPoint getOrigin() {
		
		return origin;
	}
}
