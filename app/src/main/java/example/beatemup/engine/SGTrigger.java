package example.beatemup.engine;

import java.util.ArrayList;

import android.graphics.Color;

public class SGTrigger extends SGEntity 
{
	private ArrayList<SGEntity> mObservedEntities = new ArrayList<SGEntity>();
	
	public SGTrigger(SGMap map, int id, SGPointF3D position, SGPointF3D dimensions) 
	{
		super(map, id, "trigger", position, dimensions);
		setDebugColor(Color.MAGENTA);
		setDebugDrawingStyle(DebugDrawingStyle.OUTLINE);
	}
	
	public boolean addObservedEntity(SGEntity entity) 
	{
		if(mObservedEntities.contains(entity)) 
		{
			return false;
		}
		else 
		{
			mObservedEntities.add(entity);
			return true;
		}
	}
	public boolean removeObservedEntity(SGEntity entity) 
	{
		return mObservedEntities.remove(entity);
	}
	
	public void onHit(SGEntity entity, float elapsedTimeInSeconds) { }
	
	@Override
	public void step(float elapsedTimeInSeconds) 
	{
		SGEntity currentEntity;
		SGMap map = getMap();
		SGCuboidF triggerBoundingBox = getBoundingBox();
		int arraySize = mObservedEntities.size();
		for(int i = 0; i < arraySize; i++) 
		{
			currentEntity = mObservedEntities.get(i);
			if(map.collisionTest(triggerBoundingBox, currentEntity.getBoundingBox())) 
			{
				onHit(currentEntity, elapsedTimeInSeconds);
			}
		}
	}
}
