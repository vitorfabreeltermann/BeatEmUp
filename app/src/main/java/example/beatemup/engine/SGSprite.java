package example.beatemup.engine;

import java.util.HashMap;

import android.util.Log;

public class SGSprite {
	private static StringBuilder	stringBuilder = new StringBuilder();
	private HashMap<String, SGAnimation> 
									mAnimations = new HashMap<String, SGAnimation>();
	private SGAnimation				mCurrentAnimation;
	private SGPointF3D				mDimensions = new SGPointF3D();
	private SGEntity				mEntity;
	private boolean					mIsVisible = true;
	private SGPointF3D				mPosition = new SGPointF3D();
	private SGTileset				mTileset = null;
	
	public SGSprite(SGSpriteDesc spriteDesc) 
	{		
		mTileset = spriteDesc.getTileset();
		mAnimations = spriteDesc.getAnimations();
	}
	
	public SGSprite(SGSpriteDesc spriteDesc, SGEntity entity) 
	{
		mEntity = entity;
		mPosition.set(mEntity.getPosition());
		mDimensions.set(mEntity.getDimensions());
		mTileset = spriteDesc.getTileset();
		mAnimations = spriteDesc.getAnimations();
	}

	public void changeDesc(SGSpriteDesc spriteDesc) 
	{
		mTileset = spriteDesc.getTileset();
		mAnimations = spriteDesc.getAnimations();
	}

	public void step(float elapsedTimeInSeconds) 
	{
		mCurrentAnimation.step(elapsedTimeInSeconds);
		
		if(mEntity != null) 
		{
			mPosition.set(mEntity.getPosition());
			mDimensions.set(mEntity.getDimensions());
		}
	}

	public SGAnimation getAnimation(String animationName) 
	{
		SGAnimation animation = mAnimations.get(animationName);
		
		if(animation == null) 
		{
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append("SGEntity.getAnimation(): A entidade '");
			stringBuilder.append(mEntity.getId());
			stringBuilder.append("' n�o possui uma anima��o de nome '");
			stringBuilder.append(animationName);
			stringBuilder.append("'");
			Log.d("SimpleGameEngine", stringBuilder.toString());
		}
		
		return animation;
	}

	public SGAnimation	getCurrentAnimation() { return mCurrentAnimation; }
	public SGPointF3D	getDimensions() { return mDimensions; }
	public SGEntity		getEntity() { return mEntity; }
	public SGImage		getImage() { return mTileset.getImage(); }
	public SGPointF3D	getPosition() { return mPosition; }	
	public SGTileset	getTileSet() { return mTileset; }
	
	public boolean		isVisible() { return mIsVisible; }
	
	public void setCurrentAnimation(String animationName) 
	{
		SGAnimation animation = mAnimations.get(animationName);
		if(animation != null)
		{
			mCurrentAnimation = animation;
		}
	}
	
	public void setCurrentAnimationAndReset(String animationName) 
	{
		SGAnimation animation = mAnimations.get(animationName);
		if(animation != null) 
		{
			mCurrentAnimation = animation;
			mCurrentAnimation.reset();
		}
	}
	
	public void setDimensions(SGPointF3D dimensions)
	{
		if(mEntity != null)
		{
			mDimensions = dimensions;
		}
	}
	
	public void setEntity(SGEntity entity) 
	{ 
		mEntity = entity;
	}
	
	public void setIsVisible(boolean isVisible) { mIsVisible = isVisible; }

	public void setPosition(SGPointF3D position)
	{
		if(mEntity != null)
		{
			mPosition = position;
		}
	}
}
