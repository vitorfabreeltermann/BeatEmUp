package example.beatemup.engine;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;

public class SGImage 
{
	public static enum MergeType{
		VERTICAL, HORIZONTAL
	}
	
	private Bitmap		mBitmap = null;
	private Point 		mDimensions = new Point();
	
	public SGImage(Bitmap bitmap) 
	{
		mBitmap = bitmap;
		mDimensions.set(mBitmap.getWidth(), mBitmap.getHeight());
	}
	
	public SGImage(int x, int y){
		mBitmap = Bitmap.createBitmap(x, y, Config.ARGB_8888);
	}
	
	public Bitmap 	getBitmap() { return mBitmap; }
	public Point 	getDimensions() { return mDimensions; }
}

