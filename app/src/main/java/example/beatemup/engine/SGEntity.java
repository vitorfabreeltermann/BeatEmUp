package example.beatemup.engine;

import android.graphics.Color;
import android.util.SparseArray;

public class SGEntity 
{
	public enum DebugDrawingStyle 
	{
		FILLED,
		OUTLINE
	};
	
	protected SparseArray<SGEvent>
								mEvents = new SparseArray<SGEvent>();
	protected SGCuboidF			mBBoxPadding = new SGCuboidF();
	protected SGCuboidF			mBoundingBox = new SGCuboidF();
	private String				mCategory;
	protected int 				mDebugColor = Color.RED;
	private DebugDrawingStyle	mDebugDrawingStyle = DebugDrawingStyle.FILLED;
	protected SGPointF3D 		mDimensions = new SGPointF3D();
	private int 				mFlags;
	private int 				mId;
	private boolean				mIsActive = true;
	protected SGPointF3D		mPosition = new SGPointF3D();
	private SGMap   			mMap;
	
	public SGEntity(SGMap map, int id, String category, SGPointF3D position, SGPointF3D dimensions) 
	{
		mMap = map;
		mId = id;
		mCategory = category;
		mPosition.set(position);
		mDimensions.set(dimensions);
		
		_updateBoundingBox();
	}
	
	public void addFlags(int flags) 
	{ 
		mFlags |= flags;
	}

	public boolean hasFlag(int flag) 
	{
		return (mFlags & flag) != 0;
	}

	public void removeFlags(int flags) 
	{
		flags = ~flags;
		mFlags &= flags;
	}
			
	public void move(float offsetX, float offsetY, float offsetZ) 
	{
		mPosition.x += offsetX;
		mPosition.y += offsetY;
		mPosition.z += offsetZ;
		
		_updateBoundingBox();
	}

	public void step(float timeElapsedInSeconds) {}
	
	public SGCuboidF 	getBBoxPadding() {	return mBBoxPadding; }
	public SGCuboidF 	getBoundingBox() {	return mBoundingBox; }
	public String		getCategory() { return mCategory; }
	public int 			getDebugColor() { return mDebugColor; }
	public DebugDrawingStyle	
						getDebugDrawingStyle() { return mDebugDrawingStyle; }
	public SGPointF3D 	getDimensions() { return mDimensions; }
	public int			getId() { return mId; }
	public SGPointF3D 	getPosition() { return mPosition; }
	public SGMap		getMap() { return mMap; }
	
	public boolean	isActive() { return mIsActive; }
	
	public void 	setBBoxPadding(SGCuboidF padding) 
	{
		mBBoxPadding.set(padding);
		_updateBoundingBox();
	}
	
	public void 	setDebugColor(int color) { mDebugColor = color; }	
	public void 	setDebugDrawingStyle(DebugDrawingStyle drawingStyle) { mDebugDrawingStyle = drawingStyle; }
	public void 	setDimensions(float x, float y, float z) { 	mDimensions.set(x, y, z); }
	public void 	setDimensions(SGPointF3D dimensions) { mDimensions.set(dimensions); }
	public void 	setIsActive(boolean isActive) { mIsActive = isActive; }
	
	public void 	setPosition(float x, float y, float z) 
	{ 
		mPosition.set(x, y, z);		
		_updateBoundingBox();
	}
	
	public void 	setPosition(SGPointF3D position) 
	{ 
		mPosition.set(position);		
		_updateBoundingBox();
	}

	private void _updateBoundingBox() 
	{
		mBoundingBox.set(mPosition.x + mBBoxPadding.left, 
						 mPosition.y + mBBoxPadding.top,
						 mPosition.z + mBBoxPadding.front,
						 (mPosition.x + mDimensions.x) - mBBoxPadding.right, 
						 (mPosition.y + mDimensions.y) - mBBoxPadding.bottom,
						 (mPosition.z + mDimensions.z) - mBBoxPadding.back);
	}
}
