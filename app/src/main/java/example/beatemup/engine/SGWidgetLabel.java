package example.beatemup.engine;

import android.graphics.PointF;

public class SGWidgetLabel extends SGWidget {
	
	protected SGFont mFont;
	protected SGText mText;

	public SGWidgetLabel(Alignment alignment, PointF position, SGFont font, String text) {
		super("label", alignment, position, new PointF(0, 0));
		
		mFont = font;
		mText = new SGText(text);
		
		mDimensions = mFont.measureText(mText);
	}

	@Override
	public void render(SGRenderer renderer) {
		renderer.drawText(mText, mFont, mAbsolutePosition);
	}
	
	@Override
	public void update(){
		PointF textDimensions = mFont.measureText(mText);
		
		if(mAlignment == Alignment.Left){
			mAbsolutePosition.x = mRelativePosition.x;
		}
		else if(mAlignment == Alignment.Center){
			float halfOffset = (mSceneDimensions.x / 2) - (textDimensions.x / 2);
			mAbsolutePosition.x = halfOffset + mRelativePosition.x;
		}
		else{ //mAlignment == Alignment.Right
			mAbsolutePosition.x = (mSceneDimensions.x - textDimensions.x ) + mRelativePosition.x;
		}
		
		mAbsolutePosition.y = mRelativePosition.y;
		
		mArea.left = mAbsolutePosition.x;
		mArea.top = mAbsolutePosition.y;
		mArea.right = mAbsolutePosition.x + mDimensions.x;
		mArea.bottom = mAbsolutePosition.y + mDimensions.y;
	}
	
	public SGFont getFont(){ return mFont; }
	public String getString(){ return mText.getString(); }
	
	public void setString(String label){ mText.setString(label); }
}
