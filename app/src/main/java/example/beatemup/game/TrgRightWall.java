package example.beatemup.game;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGPointF3D;
import example.beatemup.engine.SGTrigger;

public class TrgRightWall extends SGTrigger 
{
	public TrgRightWall(MapBase map, int id, SGPointF3D position, SGPointF3D dimensions) 
	{
		super(map, id, position, dimensions);
	}
	
	@Override
	public void onHit(SGEntity entity, float elapsedTimeInSeconds) 
	{
		SGPointF3D worldDimensions = getMap().getDimensions();
		entity.setPosition(worldDimensions.x - entity.getDimensions().x, entity.getPosition().y, entity.getPosition().z);
	}
}
