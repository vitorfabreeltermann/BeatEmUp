package example.beatemup.engine;

import android.util.SparseArray;

public abstract class SGMap {
	
	protected SGPointF3D   mPosition = new SGPointF3D();
	protected SGPointF3D   mDimensions = new SGPointF3D();
	protected int	   	   mId;
	protected SGWorld  	   mWorld;
	
	protected SparseArray<SGEntity> mEntities = new SparseArray<SGEntity>();
	
	public SGMap(SGWorld world, int id, SGPointF3D inicialPosition, SGPointF3D dimensions) 
	{
		mWorld = world;
		mId = id;
		mPosition.set(inicialPosition);
		mDimensions.set(dimensions);
	}
	
	public abstract void step(float timeElapsedInSeconds);
	public abstract void setup();

	public SGPointF3D getPosition() { return mPosition; }
	public SGPointF3D getDimensions() { return mDimensions; }
	public int getId(){ return mId; }
	public SGWorld getWorld(){ return mWorld; }
	public SparseArray<SGEntity> getEntities(){ return mEntities; }
	
	public void setPosition(SGPointF3D position){ mPosition.set(position); }
	public void setDimensions(SGPointF3D dimensions){ mDimensions.set(dimensions); }
	
	public boolean collisionTest(SGCuboidF R1, SGCuboidF R2)
	{
		return	R1.left < R2.right &&
	   	   		R1.right > R2.left &&
	  	   		R1.top < R2.bottom &&
	  	   		R1.bottom > R2.top &&
	  	   		R1.front < R2.back &&
	  	   		R1.back > R2.front;
	}
	
	public void addEntity(int id, SGEntity entity){ mEntities.put(id, entity); }
	
	public SGEntity getEntity(int id){ return mEntities.get(id); }
	
	public abstract void resetMap();
}
