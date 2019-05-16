package example.beatemup.game;

import java.util.ArrayList;

import example.beatemup.engine.SGCuboidF;
import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGEvent;
import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;

public abstract class EntCharacter extends SGEntity {

	private String mImage;
	
	protected float mSpeed;
	protected int mMhp;
	protected int mCurrentHp;
	protected int mStr;
	
	public static final int STATE_STOP			= 0x01;
	public static final int STATE_WALKING		= 0x02;
	public static final int STATE_ATTACKING		= 0x04;
	public static final int STATE_RIGHT			= 0X08;
	public static final int STATE_LEFT			= 0x10;
	public static final int STATE_DAMAGE		= 0x20;
	
	private ArrayList<Integer> mEventsOnReceiveDamage = new ArrayList<Integer>();
	
	public EntCharacter(SGMap map, int id, String category, SGPointF3D position, SGPointF3D dimensions, String image,
			float speed, int hp, int str) {
		super(map, id, category, position, dimensions);
		mImage = image;
		mSpeed = speed;
		mMhp = hp;
		mCurrentHp = mMhp;
		mStr = str;
		addFlags(STATE_STOP);
		addFlags(STATE_RIGHT);
	}

	public String getImage(){ return mImage; }
	
	@Override
	public void move(float offsetX, float offsetY, float offsetZ) {
		
		if(hasFlag(STATE_STOP) || hasFlag(STATE_WALKING)){
			if(offsetX == 0 && offsetY == 0 && offsetZ == 0){
				if(hasFlag(STATE_WALKING)){
					removeFlags(STATE_WALKING);
					addFlags(STATE_STOP);
				}
			}
			else{
				if(hasFlag(STATE_STOP)){
					removeFlags(STATE_STOP);
					addFlags(STATE_WALKING);
				}
				if(offsetX > 0){
					if(hasFlag(STATE_LEFT)){
						removeFlags(STATE_LEFT);
						addFlags(STATE_RIGHT);
					}
				}
				else if(offsetX < 0){
					if(hasFlag(STATE_RIGHT)){
						removeFlags(STATE_RIGHT);
						addFlags(STATE_LEFT);
					}
				}
				super.move(offsetX*mSpeed, offsetY*mSpeed, offsetZ*mSpeed);
			}
		}
	}
	
	public void attack(){
		if(hasFlag(STATE_STOP) || hasFlag(STATE_WALKING)){
			if(hasFlag(STATE_STOP))
				removeFlags(STATE_STOP);
			else
				removeFlags(STATE_WALKING);
			addFlags(STATE_ATTACKING);
		}
	}
	
	public void finishAttak(){
		if(hasFlag(STATE_ATTACKING)){
			removeFlags(STATE_ATTACKING);
			addFlags(STATE_STOP);
			dealDamage();
		}
	}
	
	public void finishDamage(){
		if(hasFlag(STATE_DAMAGE)){
			removeFlags(STATE_DAMAGE);
			addFlags(STATE_STOP);
		}
	}
	
	public abstract void dealDamage();
	
	public void receiveDamage(int damage){
		if(!hasFlag(STATE_DAMAGE)){
			_chageHp(-damage);
			if(hasFlag(STATE_ATTACKING))
				removeFlags(STATE_ATTACKING);
			else if(hasFlag(STATE_STOP))
				removeFlags(STATE_STOP);
			else if(hasFlag(STATE_WALKING))
				removeFlags(STATE_WALKING);
			addFlags(STATE_DAMAGE);
			onReceiveDamage();
		}
	}
	
	public void onReceiveDamage(){
		for (int i = 0; i < mEventsOnReceiveDamage.size(); i++) {
			mEvents.get(mEventsOnReceiveDamage.get(i)).execute();
		}
	}
	
	public void addOnRecieveDamage(int id, SGEvent event){
		mEvents.put(id, event);
		mEventsOnReceiveDamage.add(id);
	}
	
	public void heal(int healing){
		_chageHp(healing);
	}
	
	private void _chageHp(int value){
		mCurrentHp += value;
		if(mCurrentHp > mMhp) mCurrentHp = mMhp;
		if(mCurrentHp <= 0) die();
	}
	
	protected boolean _willAttack(EntCharacter other){
		SGCuboidF attackArea = new SGCuboidF(hasFlag(STATE_LEFT) ? mBoundingBox.left : mBoundingBox.right - mDimensions.x/2,
											 mBoundingBox.top,
											 mBoundingBox.front,
											 hasFlag(STATE_LEFT) ? mBoundingBox.left + mDimensions.x/2 : mBoundingBox.right,
											 mBoundingBox.right,
											 mBoundingBox.back);
		return getMap().collisionTest(attackArea, other.getBoundingBox());
	}
	
	public abstract void die();
	
	public int getCurrentHp(){ return mCurrentHp; }
}
