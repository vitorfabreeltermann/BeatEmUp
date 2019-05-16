package example.beatemup.engine;

import java.util.HashMap;

import android.graphics.PointF;
import android.graphics.RectF;

public class SGWidgetContainer extends SGWidget {
	
	protected HashMap<String, SGWidget> mChildren = new HashMap<String, SGWidget>();
	protected SGGui     				mGui;

	public SGWidgetContainer(Alignment alignment, PointF position, PointF dimensions, SGGui gui) {
		this("container", alignment, position, dimensions, gui);
	}
	
	protected SGWidgetContainer(String type, Alignment alignment, PointF position, PointF dimensions, SGGui gui) {
		super(type, alignment, position, dimensions);
		
		mGui = gui;
		
		if(dimensions.x == 0 || dimensions.y == 0){
			mArea.set(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
		}
	}
	
	public void addChild(String name, SGWidget widget){
		mChildren.put(name, widget);
		
		widget.setSceneDimensions(mSceneDimensions);
		widget.setParent(this);
		
		_updateArea();
	}
	
	public SGWidget removeChild(String name){
		SGWidget widget = mChildren.remove(name);
		widget.setParent(null);
		
		_updateArea();
		
		return widget;
	}
	
	private void _updateArea(){
		if(!mChildren.isEmpty()){
			for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
				SGWidget widget = entry.getValue();
				
				RectF childArea = widget.getArea();
				if(Float.isNaN(mArea.left) || childArea.left < mArea.left){
					mArea.left = childArea.left;
				}
				if(Float.isNaN(mArea.right) || childArea.right > mArea.right){
					mArea.right = childArea.right;
				}
				if(Float.isNaN(mArea.bottom) || childArea.bottom > mArea.bottom){
					mArea.bottom = childArea.bottom;
				}
				if(Float.isNaN(mArea.top) || childArea.top < mArea.top){
					mArea.top = childArea.top;
				}
			}
		}
		else{
			mArea.set(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
		}
	}
	
	@Override
	public boolean injectDown(PointF position, int Id) {
		for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
			SGWidget widget = entry.getValue();
			
			if(widget.isEnabled()){
				RectF area = widget.getArea();
				
				if(position.x >= area.left && position.x <= area.right &&
					position.y >= area.top && position.y <= area.bottom){
					if(entry.getValue().injectDown(position, Id) == true){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean injectUp(PointF position, int Id) {
		for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
			SGWidget widget = entry.getValue();
			
			if(widget.isEnabled()){
				RectF area = widget.getArea();
				
				if(position.x >= area.left && position.x <= area.right &&
					position.y >= area.top && position.y <= area.bottom){
					if(entry.getValue().injectUp(position, Id) == true){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean injectScroll(PointF position, int Id) {
		for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
			SGWidget widget = entry.getValue();
			
			if(widget.isEnabled()){
				RectF area = widget.getArea();
				
				if(position.x >= area.left && position.x <= area.right &&
						position.y >= area.top && position.y <= area.bottom){
					if(entry.getValue().injectScroll(position, Id) == true){
						return true;
					}
				}
			}
		}
		if(mGui.getCurrentButton(Id) != null){
			if(mGui.getCurrentButton(Id) instanceof SGWidgetDPadButton){
				mGui.getCurrentButton(Id).onUp(position);
				mGui.getCurrentButton(Id).reset();
				mGui.removeCurrentButton(Id);
			}
		}
		return false;
	}
	
	@Override
	public void render(SGRenderer renderer) {
		for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
			SGWidget widget = entry.getValue();
			
			if(widget.isVsible()){
				widget.render(renderer);
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		for(HashMap.Entry<String, SGWidget> entry : mChildren.entrySet()){
			SGWidget widget = entry.getValue();
			widget.update();
		}
		_updateArea();
	}
	
	public SGWidget getChild(String name){ return mChildren.get(name); }
}
