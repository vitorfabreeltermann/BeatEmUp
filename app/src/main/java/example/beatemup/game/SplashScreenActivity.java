package example.beatemup.game;

import example.beatemup.engine.SGActivity;
import example.beatemup.engine.SGInputPublisher;
import example.beatemup.engine.SGInputSubscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends SGActivity implements SGInputSubscriber {
	
	private SplashScreenView mView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.enableKeepScreenOn();
		this.enableFullScreen();
		
		mView = new SplashScreenView(this);
		setContentView(mView);
		
		setInputPublisher(new SGInputPublisher());
		getInputPublisher().registerSubscriber(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void startNewActivity(){
		Intent intent = new Intent(this, StartScreenActivity.class);
		startActivity(intent);
		overridePendingTransition(0, 0);
		finish();
	}
	
	@Override
	public void onDown(MotionEvent event, int Id) {
	}

	@Override
	public void onScroll(MotionEvent event, int Id) {
	}

	@Override
	public void onUp(MotionEvent event, int Id) {
		startNewActivity();
	}
	
	@Override
	public void onBackPressed() {
		startNewActivity();
	}
}
