package example.beatemup.engine;

import android.graphics.PointF;

public class SGWidgetButton extends SGWidget {
	
	protected int       mCurrentTile = 0;
	protected SGGui     mGui;
	protected SGTileset mTileset = null;

	public SGWidgetButton(Alignment alignment, PointF position,
			PointF dimensions, SGTileset tileset, SGGui gui) {
		this("button", alignment, position, dimensions, tileset, gui);
	}
	
	protected SGWidgetButton(String type, Alignment alignment, PointF position,
			PointF dimensions, SGTileset tileset, SGGui gui) {
		super(type, alignment, position, dimensions);
		
		mTileset = tileset;
		mGui = gui;
	}
	
	@Override
	public void render(SGRenderer renderer) {
		renderer.drawImage(mTileset.getImage(), mTileset.getTile(mCurrentTile), mArea);
	}
	
	@Override
	public boolean injectDown(PointF position, int Id) {
		if(mIsEnabled){
			mGui.setCurrentButton(this, Id);
			mCurrentTile = 1;
			
			return onDown(position);
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean injectUp(PointF position, int Id) {
		if(mIsEnabled){
			if(mGui.getCurrentButton(Id) == this){
				mGui.removeCurrentButton(Id);
				
				mCurrentTile = 0;
				
				return onUp(position);
			}
			else if(mGui.getCurrentButton(Id) != null){
				mGui.getCurrentButton(Id).reset();
				
				mGui.removeCurrentButton(Id);
				
				return false;
			}
			else{
				return false;
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
	
	@Override
	public boolean onDown(PointF position) {
		return true;
	}
	
	@Override
	public boolean onUp(PointF position) {
		return true;
	}
	
	public void reset(){
		mCurrentTile = 0;
	}
}
