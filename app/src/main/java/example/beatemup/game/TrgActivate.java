package example.beatemup.game;

import java.util.ArrayList;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;
import example.beatemup.engine.SGTrigger;

public class TrgActivate extends SGTrigger{

	private ArrayList<EntEnemy> mEnemies = new ArrayList<EntEnemy>();
	private TrgBoundaryLeft mBoundaryLeft;
	private TrgBoundaryRight mBoundaryRight;
	private float mLeftPosition;
	private float mRightPosition;
	private boolean mActioned;
	private boolean mIsFinal;
	
	public TrgActivate(SGMap map, int id, SGPointF3D position, SGPointF3D dimensions, boolean isFinal,
			TrgBoundaryLeft boundaryLeft, float leftPosition, TrgBoundaryRight boundaryRight, float rightPosition) {
		super(map, id, position, dimensions);
		mIsFinal = isFinal;
		mBoundaryLeft = boundaryLeft;
		mBoundaryRight = boundaryRight;
		mLeftPosition = leftPosition;
		mRightPosition = rightPosition;
		setIsActive(true);
	}

	public void addEnemy(EntEnemy emeny){
		mEnemies.add(emeny);
	}
	
	@Override
	public void step(float elapsedTimeInSeconds) {
		if(!isActive())
			return;
		if(!mActioned)
			super.step(elapsedTimeInSeconds);
		else{
			boolean hasAnyEnemyAlive = false;
			for (EntEnemy current : mEnemies) {
				hasAnyEnemyAlive |= current.isAlive();
			}
			if(!hasAnyEnemyAlive){
				mBoundaryLeft.setPosition(-mBoundaryLeft.getDimensions().x,
						mBoundaryLeft.getPosition().y,
						mBoundaryLeft.getPosition().z);
				mBoundaryRight.setPosition(getMap().getDimensions().x,
						mBoundaryLeft.getPosition().y,
						mBoundaryLeft.getPosition().z);
				setIsActive(false);
				if(mIsFinal){
					((GameModel)getMap().getWorld()).nextMap();
				}
			}
		}
	}
	
	@Override
	public void onHit(SGEntity entity, float elapsedTimeInSeconds) {
		for (EntEnemy current : mEnemies) {
			current.setIsActive(true);
		}
		mBoundaryLeft.setPosition(mLeftPosition - mBoundaryLeft.getDimensions().x,
									mBoundaryLeft.getPosition().y,
									mBoundaryLeft.getPosition().z);
		mBoundaryRight.setPosition(mRightPosition,
									mBoundaryLeft.getPosition().y,
									mBoundaryLeft.getPosition().z);
		mActioned = true;
	}
}
