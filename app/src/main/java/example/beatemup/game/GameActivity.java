package example.beatemup.game;

import android.graphics.Point;
import android.os.Bundle;

import example.beatemup.R;
import example.beatemup.engine.SGActivity;
import example.beatemup.engine.SGDialog;
import example.beatemup.engine.SGInputPublisher;

public class GameActivity extends SGActivity 
{
	private GameController mController;
	private GameModel mModel;
	private GameView mView;
	
	private SGDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		enableFullScreen();
		enableKeepScreenOn();
		
		mModel = new GameModel(new Point(480, 320), this);
		
		mView = new GameView(this, mModel);
		setContentView(mView);
		
		SGInputPublisher inputPublisher = new SGInputPublisher();		
		mController = new GameController(mModel, mView.getGui());
		inputPublisher.registerSubscriber(mController);
		setInputPublisher(inputPublisher);
		
		mDialog = new SGDialog(this, R.string.dialog_game_message,
				R.string.dialog_ok, R.string.dialog_cancel){
			@Override
			public void onOk() {
				retunToStartScreen();
			}
			
			@Override
			public void onCancel() {
				mModel.unpause();
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		if(mModel.getCurrentState() != GameModel.STATE_GAME_OVER){
			mModel.pause();
			mDialog.show();
		}
		else{
			retunToStartScreen();
		}
	}
	
	public void retunToStartScreen(){
		finish();
		overridePendingTransition(0, 0);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		mView.getMusicPlayer().release();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		mView.getMusicPlayer().pause();
		
		overridePendingTransition(0, 0);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		mView.getMusicPlayer().resume();
	}
}


