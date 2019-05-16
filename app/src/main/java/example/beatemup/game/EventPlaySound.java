package example.beatemup.game;

import example.beatemup.engine.SGEvent;

public class EventPlaySound implements SGEvent {

	private int mSoundId;
	private GameModel mModel;
	
	public EventPlaySound(int soundId, GameModel model) {
		mSoundId = soundId;
		mModel = model;
	}
	
	@Override
	public void execute() {
		mModel.addSound(mSoundId);
	}
}
