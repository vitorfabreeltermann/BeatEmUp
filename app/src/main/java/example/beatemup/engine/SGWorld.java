package example.beatemup.engine;

import android.graphics.Point;
import android.util.SparseArray;

public abstract class SGWorld 
{
	protected SGMap mCurrentMap;
	
	protected Point mSceneDimensions;
	
	protected SparseArray<SGMap> mMapList = new SparseArray<SGMap>();
	
	public SGWorld(Point sceneDimensions){
		mSceneDimensions = sceneDimensions;
	}
	
	public abstract void setup();
	
	public abstract void step(float elapsedTimeInSeconds);
	
	public SGMap getCurrentMap(){ return mCurrentMap; }
	
	public void setCurrentMap(SGMap map){ mCurrentMap = map; }
	
	public SparseArray<SGMap> getMapList(){ return mMapList; }
	
	public Point getSceneDimensions(){ return mSceneDimensions; }
	
	public abstract void resetWorld();
}

