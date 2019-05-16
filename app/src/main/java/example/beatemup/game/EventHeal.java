package example.beatemup.game;

import example.beatemup.engine.SGEvent;

public class EventHeal implements SGEvent {

	private EntPlayer		mHealed;
	private int			mHeal;
	
	public EventHeal(EntPlayer healed, int heal){
		mHealed = healed;
		mHeal = heal;
	}
	
	@Override
	public void execute() {
		mHealed.heal(mHeal);
	}
}
