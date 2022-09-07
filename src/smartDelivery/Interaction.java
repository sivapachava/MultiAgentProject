package smartDelivery;

import java.util.List;
import repast.simphony.context.Context;

public class Interaction {
	
	public static final int RADIUS = 20;
	public  static Context<Object> CONTEXT;

	public Interaction(Context<Object> context) {
		Interaction.CONTEXT = context;
	}
	public void send(Robot robot, AgentMessage m) {
		for (Object obj : robot.getContext())
			if (obj instanceof Robot && robot.distance((Robot) obj) < RADIUS);
		}
	//Share requests between robots
	public void sendPackages(Robot robot, List<Object> objl)
	{
		for (Object obj : robot.getContext())
			if (obj instanceof Robot && robot.distance((Robot) obj) < 30)
				((Robot) obj).receiveRequestPackages(objl);
	}
}