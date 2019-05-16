package example.beatemup.engine;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.PointF;

@SuppressLint("RtlHardcoded")
public class SGWidgetDPadButton extends SGWidgetButton {
 
	enum Directional{
		NONE(0, new Point(0, 0)),
		UP_LEFT(1, new Point(1, 1)), UP(2, new Point(2, 1)), UP_RIGHT(3, new Point(3, 1)),
		LEFT(4, new Point(1, 2)), MIDDLE(5, new Point(2, 2)), RIGHT(6, new Point(3, 2)),
		DOWN_LEFT(7, new Point(1, 3)), DOWN(8, new Point(2, 3)), DOWN_RIGHT(9, new Point(3, 3));
		
		public int tile;
		public Point position;
		Directional(int tile, Point position){
			this.position = position;
			this.tile = tile;
		}
	}
	
	private Directional mCurrentDirectional = Directional.NONE;
	
	public SGWidgetDPadButton(Alignment alignment, PointF position, PointF dimensions, SGTileset tileset, SGGui gui) {
		super("dpadbutton", alignment, position, dimensions, tileset, gui);
	}
	
	private Directional getDirectional(PointF position){
		
		for (Directional dir : Directional.values()) {
			if(position.x >= mArea.left + (mArea.right - mArea.left) * (dir.position.x  - 1)/ 3 &&
			   position.x <= mArea.left + (mArea.right - mArea.left) * dir.position.x/ 3   &&
			   position.y >= mArea.top + (mArea.bottom - mArea.top) * (dir.position.y  - 1)/ 3  &&
			   position.y <= mArea.top + (mArea.bottom - mArea.top) * dir.position.y / 3){
				return dir;
			}
		}
		return Directional.NONE;
	}
	
	@Override
	public boolean injectDown(PointF position, int Id) {
		if(mIsEnabled){
			mGui.setCurrentButton(this, Id);
			
			Directional dir = getDirectional(position);
			
			mCurrentDirectional = dir;
			
			mCurrentTile = dir.tile;
			
			return onDown(position);
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean injectScroll(PointF position, int Id) {
		if(mIsEnabled){
			if(mGui.getCurrentButton(Id) == this){
				Directional dir = getDirectional(position);
				if(dir == mCurrentDirectional){
					return true;
				}else{
					onUp(position);
					mCurrentTile = dir.tile;
					mCurrentDirectional = dir;
					return injectDown(position, Id);
				}
			}
			else if(mGui.getCurrentButton(Id) != null){
				return false;
			}
			else{
				Directional dir = getDirectional(position);
				mCurrentTile = dir.tile;
				mCurrentDirectional = dir;
				return injectDown(position, Id);
			}
		}
		else{
			if(mGui.getCurrentButton(Id) != null){
				mGui.getCurrentButton(Id).reset();
				mGui.removeCurrentButton(Id);
			}
			return false;
		}
	}
	
	public boolean upLeftOnDown(PointF position){
		return true;
	}
	
	public boolean upOnDown(PointF position){
		return true;
	}
	
	public boolean upRightOnDown(PointF position){
		return true;
	}
	
	public boolean leftOnDown(PointF position){
		return true;
	}
	
	public boolean middleOnDown(PointF position){
		return true;
	}
	
	public boolean rightOnDown(PointF position){
		return true;
	}
	
	public boolean downLeftOnDown(PointF position){
		return true;
	}
	
	public boolean downOnDown(PointF position){
		return true;
	}
	
	public boolean downRightOnDown(PointF position){
		return true;
	}
	
	public boolean upLeftOnUp(PointF position){
		return true;
	}
	
	public boolean upOnUp(PointF position){
		return true;
	}
	
	public boolean upRightOnUp(PointF position){
		return true;
	}
	
	public boolean leftOnUp(PointF position){
		return true;
	}
	
	public boolean middleOnUp(PointF position){
		return true;
	}
	
	public boolean rightOnUp(PointF position){
		return true;
	}
	
	public boolean downLeftOnUp(PointF position){
		return true;
	}
	
	public boolean downOnUp(PointF position){
		return true;
	}
	
	public boolean downRightOnUp(PointF position){
		return true;
	}
	
	@Override
	public final boolean onDown(PointF position) {
		switch (mCurrentDirectional) {
		case UP_LEFT:
			return upLeftOnDown(position);
		case UP:
			return upOnDown(position);
		case UP_RIGHT:
			return upRightOnDown(position);
		case LEFT:
			return leftOnDown(position);
		case MIDDLE:
			return middleOnDown(position);
		case RIGHT:
			return rightOnDown(position);
		case DOWN_LEFT:
			return downLeftOnDown(position);
		case DOWN:
			return downOnDown(position);
		case DOWN_RIGHT:
			return downRightOnDown(position);
		default:
			return false;
		}
	}
	
	@Override
	public final boolean onUp(PointF position) {
		switch (mCurrentDirectional) {
		case UP_LEFT:
			return upLeftOnUp(position);
		case UP:
			return upOnUp(position);
		case UP_RIGHT:
			return upRightOnUp(position);
		case LEFT:
			return leftOnUp(position);
		case MIDDLE:
			return middleOnUp(position);
		case RIGHT:
			return rightOnUp(position);
		case DOWN_LEFT:
			return downLeftOnUp(position);
		case DOWN:
			return downOnUp(position);
		case DOWN_RIGHT:
			return downRightOnUp(position);
		default:
			return false;
		}
	}
	
	@Override
	public void reset() {
		mCurrentDirectional = Directional.NONE;
		super.reset();
	}
}
