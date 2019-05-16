package example.beatemup.game;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGPointF3D;
import example.beatemup.engine.SGTrigger;

public class TrgBoundaryLeft extends SGTrigger{
	
	public TrgBoundaryLeft(MapBase map, int id, SGPointF3D position, SGPointF3D dimensions) 
	{
		super(map, id, position, dimensions);
	}
	
	@Override
	public void onHit(SGEntity entity, float elapsedTimeInSeconds) 
	{
		entity.setPosition(mPosition.x + mDimensions.x, entity.getPosition().y, entity.getPosition().z);
	}
}
