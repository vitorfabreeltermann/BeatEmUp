package example.beatemup.game;

import example.beatemup.engine.SGImage;
import example.beatemup.engine.SGRenderer;
import example.beatemup.engine.SGView;
import example.beatemup.engine.SGViewport;
import example.beatemup.engine.SGViewport.ScalingMode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

public class SplashScreenView extends SGView {
	
	private SGImage mImgSplashScreen;
	private PointF  mImagePosition = new PointF(0, 0);
	private PointF  mImageDimensions = new PointF();

	public SplashScreenView(Context context) {
		super(context);
		
		mImgSplashScreen = getImageFactory().createImage("images/splash_screen.png");
		mImageDimensions.set(mImgSplashScreen.getDimensions().x,
				mImgSplashScreen.getDimensions().y);
	}
	
	@Override
	protected void setup() {
		Point viewDimensions = getDimensions();
		Point sceneDimensions = new Point(480, 320);
		SGViewport viewport = new SGViewport(sceneDimensions, viewDimensions,
				ScalingMode.FULL_SCREEN_KEEP_ORIGINAL_ASPECT);
		getRenderer().setViewport(viewport);
	}
	
	@Override
	public void step(Canvas canvas, float elapsedTimeInSeconds) {
		SGRenderer renderer = getRenderer();
		
		int screenColor = Color.DKGRAY;
		int viewportColor = Color.BLACK;
		
		renderer.beginDrawing(canvas, screenColor, viewportColor);
		
		renderer.drawImage(mImgSplashScreen, null, mImagePosition, mImageDimensions);
		
		renderer.endDrawing();
	}
}
