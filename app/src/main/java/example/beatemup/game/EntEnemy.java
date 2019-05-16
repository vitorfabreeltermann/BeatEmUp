package example.beatemup.game;

import java.util.Random;

import example.beatemup.engine.SGCuboidF;
import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;

public class EntEnemy extends EntCharacter {

	private boolean mDead;
	
	private boolean mAttacking = true;
	private float mAccumulator = 0;
	
	private SGCuboidF mTarget = new SGCuboidF();
	private static Random	mRandom = new Random();
	private int mTimeToChange;
	
	public EntEnemy(SGMap map, int id, SGPointF3D position, SGPointF3D dimensions, String image,
			float speed, int hp, int str) {
		super(map, id, "enemy", position, dimensions, image, speed, hp, str);
		setIsActive(false);
		mTarget.set(mBoundingBox);
		mTimeToChange = 5 + mRandom.nextInt(5);
	}
	
	@Override
	public void step(float timeElapsedInSeconds) {
		if(!isActive())
			return;
		mAccumulator += timeElapsedInSeconds;
		if(mAccumulator >= mTimeToChange){
			mAccumulator = 0;
			mTimeToChange = 5 + mRandom.nextInt(5);
			mAttacking = !mAttacking;
			int x = mRandom.nextInt((int) (((MapBase)getMap()).getRightBoundery() - ((MapBase)getMap()).getLeftBoundery()));
			x += ((MapBase)getMap()).getLeftBoundery();
			int z = mRandom.nextInt((int) getMap().getDimensions().z);
			mTarget.set(x, mBoundingBox.top, z, x + mDimensions.x, mBoundingBox.bottom, z + mDimensions.z);
		}
		if(mAttacking){
			EntPlayer player = ((MapBase)getMap()).getPlayer();
			if(_willAttack(player)){
				attack();
			}
			else{
				float x = mSpeed * (mPosition.x == player.getPosition().x ? 0 :
								  mPosition.x > player.getPosition().x ? -200 : 200);
				float z = mSpeed * (mPosition.z == player.getPosition().z ? 0 :
					  mPosition.z > player.getPosition().z ? -200 : 200);
				move(x * timeElapsedInSeconds, 0, z * timeElapsedInSeconds);
			}
		}
		else{
			while(getMap().collisionTest(mBoundingBox, mTarget)){
				int x = mRandom.nextInt((int) (((MapBase)getMap()).getRightBoundery() - ((MapBase)getMap()).getLeftBoundery()));
				x += ((MapBase)getMap()).getLeftBoundery();
				int z = mRandom.nextInt((int) getMap().getDimensions().z);
				mTarget.set(x, mBoundingBox.top, z, x + mDimensions.x, mBoundingBox.bottom, z + mDimensions.z);
			}
			float x = mSpeed * (mPosition.x == mTarget.left ? 0 :
				  mPosition.x > mTarget.left ? -200 : 200);
			float z = mSpeed * (mPosition.z == mTarget.front ? 0 :
				  mPosition.z > mTarget.front ? -200 : 200);
			move(x * timeElapsedInSeconds, 0, z * timeElapsedInSeconds);
		}
	}

	@Override
	public void dealDamage() {
		EntPlayer player = ((MapBase)getMap()).getPlayer();
		if(getMap().collisionTest(mBoundingBox, player.getBoundingBox())){
			player.receiveDamage(mStr);;
		}
	}
	
	@Override
	public void die() {
		mDead = true;
		setIsActive(false);
	}

	@Override
	public void setIsActive(boolean isActive) {
		if(isActive){
			if(!mDead)
				super.setIsActive(isActive);
			int x = mRandom.nextInt((int) (((MapBase)getMap()).getRightBoundery() - ((MapBase)getMap()).getLeftBoundery()));
			x += ((MapBase)getMap()).getLeftBoundery();
			int z = mRandom.nextInt((int) getMap().getDimensions().z);
			mTarget.set(x, mBoundingBox.top, z, x + mDimensions.x, mBoundingBox.bottom, z + mDimensions.z);
		}
		else
			super.setIsActive(isActive);
	}
	
	public boolean isAlive(){
		return !mDead;
	}
}
