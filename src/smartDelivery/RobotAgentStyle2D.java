package smartDelivery;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class RobotAgentStyle2D extends DefaultStyleOGL2D {

	@Override
	public Color getColor(Object o){
		if (o instanceof Robot)
		{
			if (((Robot) o).isHasRequestforPackage())
			{
				return Color.RED;
			} else {
				return Color.GREEN;
			}
		}
		else if (o instanceof Package){
			if (((Shop) o).isArrived())
				return Color.BLUE;
			else if (((Shop) o).isRequested())
				return Color.RED;
			else
				return Color.BLACK;
		}	
		return Color.WHITE;
	}

	
}
