package example.beatemup.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.util.Log;

public class SGRenderer 
{
	private SGViewport mViewport;
	
	private Canvas	mTempCanvas;
	private Paint 	mTempPaint = new Paint();
	private PointF	mTempPosition = new PointF();
	private RectF 	mTempDstRect = new RectF();
	
	public void beginDrawing(Canvas canvas, int screenColor, int viewportColor) 
	{
		mTempCanvas = canvas;
		canvas.drawColor(screenColor);
		
		if(mViewport != null){
			canvas.save();
			canvas.clipRect(mViewport.getDrawingArea());
			canvas.drawColor(viewportColor);
			canvas.restore();
		}
	}
	
	public void endDrawing() 
	{
	}

	public void drawRect(Rect worldDestination, int color) 
	{
		if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.left * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.top * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = (worldDestination.right * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = (worldDestination.bottom * scalingFactor.y) + offsetFromOrigin.y;
			
	    	mTempPaint.setColor(color);
			mTempPaint.setStyle(Paint.Style.FILL);
	    	mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
    }
	
	public void drawRect(RectF worldDestination, int color) 
	{
		if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.left * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.top * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = (worldDestination.right * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = (worldDestination.bottom * scalingFactor.y) + offsetFromOrigin.y;
			
	    	mTempPaint.setColor(color);
			mTempPaint.setStyle(Paint.Style.FILL);
	    	mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
    }
	
	public void drawRect(PointF worldDestination, PointF dstDimensions, int color) 
	{
		if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.x * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.y * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = ((worldDestination.x + dstDimensions.x) * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = ((worldDestination.y + dstDimensions.y) * scalingFactor.y) + offsetFromOrigin.y;
			
			mTempPaint.setColor(color);
			mTempPaint.setStyle(Paint.Style.FILL);			
			mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
	}
	
	public void drawOutlineRect(Rect worldDestination, int color) 
	{
		if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.left * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.top * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = (worldDestination.right * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = (worldDestination.bottom * scalingFactor.y) + offsetFromOrigin.y;
			
			mTempDstRect.right -= 1;
	    	mTempDstRect.bottom -= 1;
			
	    	mTempPaint.setColor(color);
	    	mTempPaint.setStyle(Paint.Style.STROKE);
	    	mTempPaint.setStrokeWidth(0);	    	
	    	
	    	mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
    }
	
    public void drawOutlineRect(RectF worldDestination, int color) 
    {
    	if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.left * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.top * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = (worldDestination.right * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = (worldDestination.bottom * scalingFactor.y) + offsetFromOrigin.y;
			
			mTempDstRect.right -= 1;
	    	mTempDstRect.bottom -= 1;
			
	    	mTempPaint.setColor(color);
	    	mTempPaint.setStyle(Paint.Style.STROKE);
	    	mTempPaint.setStrokeWidth(0);	    	
	    	
	    	mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
    }
    
    public void drawOutlineRect(PointF worldDestination, PointF dstDimensions, int color) 
    {
    	if(mViewport != null) 
		{
			Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
			PointF scalingFactor = mViewport.getScalingFactor();
			
			mTempDstRect.left = (worldDestination.x * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.top = 	(worldDestination.y * scalingFactor.y) + offsetFromOrigin.y;
			mTempDstRect.right = ((worldDestination.x + dstDimensions.x) * scalingFactor.x) + offsetFromOrigin.x;
			mTempDstRect.bottom = ((worldDestination.y + dstDimensions.y) * scalingFactor.y) + offsetFromOrigin.y;
			
			mTempDstRect.right -= 1;
	    	mTempDstRect.bottom -= 1;
			
	    	mTempPaint.setColor(color);
	    	mTempPaint.setStyle(Paint.Style.STROKE);
	    	mTempPaint.setStrokeWidth(0);	    	
	    	
	    	mTempCanvas.drawRect(mTempDstRect, mTempPaint);
		}
    	else 
    	{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
    }

    public void drawImage(SGImage image, Rect objSource, RectF worldDestination) 
    {
    	if(mViewport != null){
    		Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
    		PointF scalingFactor = mViewport.getScalingFactor();
    		
    		mTempDstRect.left = (worldDestination.left * scalingFactor.x) + offsetFromOrigin.x;
    		mTempDstRect.top = (worldDestination.top * scalingFactor.y) + offsetFromOrigin.y;
    		mTempDstRect.right = (worldDestination.right * scalingFactor.x) + offsetFromOrigin.x;
    		mTempDstRect.bottom = (worldDestination.bottom * scalingFactor.y) + offsetFromOrigin.y;
    		
    		if(image != null) 
    		{
    			Bitmap bitmap = image.getBitmap();
    			mTempCanvas.drawBitmap(bitmap, objSource, mTempDstRect, mTempPaint);
    		}
    		else 
    		{
    			drawRect(worldDestination, Color.RED);
    		}
    	}
    	else{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
	}

    public void drawImage(SGImage image, Rect objSource, PointF worldDestination, PointF dstDimensions) 
    {
    	if(mViewport != null){
    		Point offsetFromOrigin = mViewport.getOffsetFromOrigin();
    		PointF scalingFactor = mViewport.getScalingFactor();
    		
    		mTempDstRect.left = (worldDestination.x * scalingFactor.x) + offsetFromOrigin.x;
    		mTempDstRect.top = (worldDestination.y * scalingFactor.y) + offsetFromOrigin.y;
    		mTempDstRect.right = ((worldDestination.x + dstDimensions.x) * scalingFactor.x) + offsetFromOrigin.x;
    		mTempDstRect.bottom = ((worldDestination.y + dstDimensions.y) * scalingFactor.y) + offsetFromOrigin.y;
    		
    		if(image != null) 
    		{
    			Bitmap bitmap = image.getBitmap();
    			mTempCanvas.drawBitmap(bitmap, objSource, mTempDstRect, mTempPaint);
    		}
    		else 
    		{
    			drawRect(mTempDstRect, Color.RED);
    		}
    	}
    	else{
    		Log.d("SimpleGameEngine", "SGRenderer.drawImage(): viewport n�o foi definida!");
    		System.exit(1);
    	}
	}
    
    public void drawText(SGText text, SGFont font, PointF worldDestination) 
    {
    	SGTileset fontTileSet = font.getTileSet();
    	char[] textChars = text.getCharacters();
    	
    	mTempPosition.set(worldDestination);
    	for(int i = 0; i < textChars.length; i++) 
    	{
    		Rect character = fontTileSet.getTile(textChars[i]);
    		drawImage(fontTileSet.getImage(), character, mTempPosition, font.getFontDimensions());
    		
    		mTempPosition.x += font.getFontDimensions().x;
		}
	}
    
    public SGViewport getViewport(){ return mViewport; }
    public void setViewport(SGViewport viewport){ mViewport = viewport; }
}
