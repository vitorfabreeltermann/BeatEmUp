package example.beatemup.engine;

import example.beatemup.engine.SGWidget.Alignment;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.SparseArray;

public class SGGui {
	private SGRenderer         mRenderer;
	private SGWidgetContainer  mRoot;
	private PointF             mTempPosition = new PointF();
	
	private SparseArray<SGWidgetButton> mCurrentButtons = new SparseArray<SGWidgetButton>();
	
	public SGGui(SGRenderer renderer, Point sceneDimensions){
		mRenderer = renderer;
		
		PointF position = new PointF(0, 0);
		PointF dimensions = new PointF(sceneDimensions.x, sceneDimensions.y);
		
		mRoot = new SGWidgetContainer(Alignment.Left, position, dimensions, this);
		mRoot.setSceneDimensions(sceneDimensions);
	}
	
	public boolean injectDown(PointF position, int Id){
		return mRoot.injectDown(_screenToScene(position), Id);
	}
	
	public boolean injectUp(PointF position, int Id){
		boolean result = mRoot.injectUp(_screenToScene(position), Id);
		if(result == false && mCurrentButtons.get(Id) != null){
			mCurrentButtons.get(Id).reset();
			mCurrentButtons.remove(Id);
		}
		return result;
	}
	
	public boolean injectScroll(PointF position, int Id){
		boolean result = mRoot.injectScroll(_screenToScene(position), Id);
		return result;
	}
	
	public void render(){
		mRoot.render(mRenderer);
	}
	
	public void update(){
		mRoot.update();
	}
	
	public SGWidgetContainer getRoot(){ return mRoot; }
	
	private PointF _screenToScene(PointF position){
		Point offsetFromOrigin = mRenderer.getViewport().getOffsetFromOrigin();
		PointF scalingFactor = mRenderer.getViewport().getScalingFactor();
		
		mTempPosition.x = (position.x - offsetFromOrigin.x) / scalingFactor.x;
		mTempPosition.y = (position.y - offsetFromOrigin.y) / scalingFactor.y;
		
		return mTempPosition;
	}
	
	public SGWidgetButton getCurrentButton(int Id){ return mCurrentButtons.get(Id); }
	public SparseArray<SGWidgetButton> getCurrentButtons(){ return mCurrentButtons; }
	public void setCurrentButton(SGWidgetButton currentButton, int Id){
		mCurrentButtons.put(Id, currentButton);
	}
	public void removeCurrentButton(int Id){
		mCurrentButtons.remove(Id);
	}
}
