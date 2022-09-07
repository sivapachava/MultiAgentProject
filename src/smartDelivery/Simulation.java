package smartDelivery;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class Simulation implements ContextBuilder<Object>{
	
	
	@Override
	public Context<Object> build(Context<Object> context) {		
		Interaction in = new Interaction (context);
		context.setId("smartDelivery");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace <Object> space = spaceFactory.createContinuousSpace("space", context ,
				new RandomCartesianAdder<Object>() ,
				new repast.simphony.space.continuous.WrapAroundBorders(), 50 , 50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),	new SimpleGridAdder<Object>(), true, 50, 50));

		Parameters val = RunEnvironment.getInstance().getParameters();
		int numRobots = (int) val.getValue("numberRobot");
		for(int i = 1; i<=numRobots;i++)
		{
			context.add(new Robot(context, grid, space, "Robot"+i,in));
		}
		
		Shop sourceagent;
		for(int i=0; i<(int) val.getValue("numberRequest"); i++){
			sourceagent = new Shop(space, i+1 );
			context.add(sourceagent);
		}
		
		for (Object obj : context) {
			NdPoint point = space.getLocation(obj);
			grid.moveTo(obj, (int) point.getX(), (int) point.getY());
		}
		return context;
	}

	
	//Generate random values for simulation
	public Double[] generateRandoms(int iteration){
		Double[] tab = new Double[4];
		tab[0] = (double) ThreadLocalRandom.current().nextInt(65, 90 + 1);
		tab[1] = (double) ThreadLocalRandom.current().nextInt(25, 40 + 1);
		tab[2] = (double) ThreadLocalRandom.current().nextInt(40, 110 + 1);
		Random r = new Random();
		double randomValue;
		if(iteration < 125)
		{
			randomValue = 0.6 + (0.8 - 0.6) * r.nextDouble();
		} else if (iteration < 250) {
			randomValue = 0.98 + (1.02 - 0.98) * r.nextDouble();
		} else {
			randomValue = 2 + (6 - 2) * r.nextDouble();
		}
		tab[3] = randomValue;
		return tab;
	}
}