package example.beatemup.engine;

import android.view.MotionEvent;

public interface SGInputSubscriber 
{
	public void onDown(MotionEvent event, int Id);
	
	public void onScroll(MotionEvent event, int Id);
	
	public void onUp(MotionEvent event, int Id);
}
