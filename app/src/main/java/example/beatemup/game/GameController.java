package example.beatemup.game;

import android.graphics.PointF;
import android.view.MotionEvent;

import example.beatemup.engine.SGGui;
import example.beatemup.engine.SGInputSubscriber;

public class GameController implements SGInputSubscriber 
{	
	private GameModel mModel;
	
	private SGGui     mGui;
	private PointF    mTempPosition = new PointF();
	
	public GameController(GameModel model, SGGui gui) 
	{
		mModel = model;
		
		mGui = gui;
	}
	
	@Override
	public void onDown(MotionEvent event, int Id) 
	{
		mTempPosition.set(event.getX(event.findPointerIndex(Id)), event.getY(event.findPointerIndex(Id)));
		mGui.injectDown(mTempPosition, Id);
	}

	@Override
	public void onScroll(MotionEvent event, int Id) 
	{
		mTempPosition.set(event.getX(event.findPointerIndex(Id)), event.getY(event.findPointerIndex(Id)));
		mGui.injectScroll(mTempPosition, Id);
	}

	@Override
	public void onUp(MotionEvent event, int Id) 
	{
		mTempPosition.set(event.getX(event.findPointerIndex(Id)), event.getY(event.findPointerIndex(Id)));
		mGui.injectUp(mTempPosition, Id);
	}

	public GameModel getModel() { return mModel; }
}
