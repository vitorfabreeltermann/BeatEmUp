package example.beatemup.game;

import java.util.ArrayList;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGEvent;
import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;

public class EntItem extends SGEntity {

	private String mImage;
	
	public static final int STATE_STOP			= 0x01;
	
	private ArrayList<Integer> mEventsOnGot = new ArrayList<Integer>();
	
	public EntItem(SGMap map, int id, SGPointF3D position, SGPointF3D dimensions, String image) {
		super(map, id, "item", position, dimensions);
		mImage = image;
		addFlags(STATE_STOP);
	}
	
	public String getImage(){ return mImage; }

	@Override
	public void step(float timeElapsedInSeconds) {
		if(isActive()){
			EntPlayer player = ((MapBase)getMap()).getPlayer();
			if(getMap().collisionTest(mBoundingBox, player.getBoundingBox())){
				onGot();
			}
		}
	}
	
	public void onGot(){
		for (int i = 0; i < mEventsOnGot.size(); i++) {
			mEvents.get(mEventsOnGot.get(i)).execute();
		}
		setIsActive(false);
	}
	
	public void addOnGot(int id, SGEvent event){
		mEvents.put(id, event);
		mEventsOnGot.add(id);
	}
}
