package example.beatemup.game;

import example.beatemup.R;
import example.beatemup.engine.SGActivity;
import example.beatemup.engine.SGDialog;
import example.beatemup.engine.SGInputPublisher;
import example.beatemup.engine.SGInputSubscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class StartScreenActivity extends SGActivity implements SGInputSubscriber {

	private StartScreenView mView = null;
	
	private SGDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.enableKeepScreenOn();
		this.enableFullScreen();
		
		mView = new StartScreenView(this);
		setContentView(mView);
		
		setInputPublisher(new SGInputPublisher());
		getInputPublisher().registerSubscriber(this);
		
		mDialog = new SGDialog(this, R.string.dialog_game_message,
				R.string.dialog_ok, R.string.dialog_cancel){
			@Override
			public void onOk() {
				finish();
				overridePendingTransition(0, 0);
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		mDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void startNewActivity(){
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	@Override
	public void onDown(MotionEvent event, int Id) {
	}

	@Override
	public void onUp(MotionEvent event, int Id) {
		startNewActivity();
	}

	@Override
	public void onScroll(MotionEvent event, int Id) {
	}

}
