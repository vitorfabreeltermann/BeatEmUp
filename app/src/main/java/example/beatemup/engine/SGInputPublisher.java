package example.beatemup.engine;

import java.util.ArrayList;

import android.view.MotionEvent;

public class SGInputPublisher
{
	protected ArrayList<SGInputSubscriber> mSubscribers = new ArrayList<SGInputSubscriber>();
	
	public void registerSubscriber(SGInputSubscriber listener) 
	{
		mSubscribers.add(listener);
	}
	
	public boolean unregisterSubscriber(SGInputSubscriber listener) 
	{
		return mSubscribers.remove(listener);
	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{
		int action = event.getActionMasked();
		if(action == MotionEvent.ACTION_DOWN ||
			action == MotionEvent.ACTION_POINTER_DOWN){
			int eventId = event.getPointerId(event.getActionIndex());
			onDown(event, eventId);
			
		}
		else if(action == MotionEvent.ACTION_UP ||
				action == MotionEvent.ACTION_POINTER_UP) 
		{
			int eventId = event.getPointerId(event.getActionIndex());
			onUp(event, eventId);
			
		}
		else if(action == MotionEvent.ACTION_MOVE) 
		{
			int pointerIndex = 0;
			int eventId = 0;
			int pointerCount = event.getPointerCount();
	        for(int i = 0; i < pointerCount; ++i)
	        {
	            pointerIndex = i;
	            eventId = event.getPointerId(pointerIndex);
	            onScroll(event, eventId);
	        }
		}
		
		return true;
	}

	public boolean onUp(MotionEvent event, int Id){
		for(int i = 0; i < mSubscribers.size(); i++)
		{
			mSubscribers.get(i).onUp(event, Id);
		}
		return true;
	}
	
	public boolean onDown(MotionEvent event, int Id) 
	{
		for(int i = 0; i < mSubscribers.size(); i++)
		{
			mSubscribers.get(i).onDown(event, Id);
		}
		
		return true;
	}
	
	public boolean onScroll(MotionEvent event, int Id) 
	{
		for(int i = 0; i < mSubscribers.size(); i++)
		{
			mSubscribers.get(i).onScroll(event, Id);
		}
		
		return true;
	}
}
