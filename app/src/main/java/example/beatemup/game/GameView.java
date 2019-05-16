package example.beatemup.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import example.beatemup.R;
import example.beatemup.engine.SGAnimation;
import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGEvent;
import example.beatemup.engine.SGFont;
import example.beatemup.engine.SGGui;
import example.beatemup.engine.SGImage;
import example.beatemup.engine.SGImageFactory;
import example.beatemup.engine.SGMusicPlayer;
import example.beatemup.engine.SGRenderer;
import example.beatemup.engine.SGSoundPool;
import example.beatemup.engine.SGSprite;
import example.beatemup.engine.SGSpriteDesc;
import example.beatemup.engine.SGTileset;
import example.beatemup.engine.SGView;
import example.beatemup.engine.SGViewport;
import example.beatemup.engine.SGViewport.ScalingMode;
import example.beatemup.engine.SGWidget.Alignment;
import example.beatemup.engine.SGWidgetButton;
import example.beatemup.engine.SGWidgetContainer;
import example.beatemup.engine.SGWidgetDPadButton;
import example.beatemup.engine.SGWidgetLabel;

public class GameView extends SGView {
	public final static float	CHAR_SIZE_BIG = 32;
	public final static float	CHAR_SIZE_SMALL = 16;

	private GameModel 			mModel;
	private MapBase				mMap;
	
	private SGImage				mImgField;
	private Rect				mTempRectSrc = new Rect();
	private RectF				mTempRectFDest = new RectF();
	
	private SparseArray<SGSprite>
								mSprites = new SparseArray<SGSprite>();
	
	private ArrayList<SGEntity> mEntities = new ArrayList<SGEntity>();
	
	private SGFont 				mFntVisitorBig;
	private SGFont 				mFntVisitorSmall;
	
	private SGMusicPlayer 		mMusicPlayer;
	private SGSoundPool 		mSoundPool;
	private SparseIntArray		mSounds = new SparseIntArray();
	
	private SGGui               mGui;
	
	private SGWidgetContainer   mCtnrInfo;
	private SGWidgetContainer   mCtnrScore;
	private SGWidgetLabel       mLblLowerInfo;
	private SGWidgetLabel       mLblPlayerHp;
	private SGWidgetLabel       mLblUpperInfo;
	private String              mStrGameOver;
	private String				mStrEnd;
	private String              mStrPaused;
	private String              mStrPlayerHp;
	private String              mStrStart;
	private SGWidgetButton      mBtnPause;
	private SGWidgetDPadButton  mBtnDPad;
	private SGWidgetButton		mBtnAttack;
	
	private PointF 				mMapPosition2D = new PointF();
	private PointF				mMapDimensions2D = new PointF();
	
	private PointF 				mEntityPosition2D = new PointF();
	private PointF				mEntityDimensions2D = new PointF();
	
	private GameView(Context context) 
	{
		super(context);
	}
	
	public GameView(Context context, GameModel model) 
	{
		super(context);
		
		mModel = model;
		
		mSoundPool = new SGSoundPool(context);
		mMusicPlayer = new SGMusicPlayer(context);
		
		mGui = new SGGui(getRenderer(), mModel.getSceneDimensions());
	}
	
	@Override
	public void setup()
	{
		mEntities.clear();
		mSprites.clear();
		
		Point viewDimensions = getDimensions();
		SGViewport viewport = new SGViewport(mModel.getSceneDimensions(),
				viewDimensions, ScalingMode.FULL_SCREEN_KEEP_ORIGINAL_ASPECT);
		getRenderer().setViewport(viewport);
		
		mModel.setup();
		
		mMap = (MapBase) mModel.getCurrentMap();
		
		// Campo
		mImgField = getImageFactory().createImage("maps/" + mMap.getImage());
		Point fieldDimensions = mImgField.getDimensions();
		mTempRectSrc.set(0, 0, fieldDimensions.x, fieldDimensions.y);
		mTempRectFDest.set(0, 0, fieldDimensions.x, fieldDimensions.y);
		
		
		
		for (int i = 0; i < mMap.getEntities().size(); i++) {
			final SGEntity currentEntity = mMap.getEntities().get(mMap.getEntities().keyAt(i));
			
			if(currentEntity instanceof EntCharacter){
				int[] tilesStopRight = { 0, 1 };
				int[] tilesWalkingRight = { 2, 3 };
				int[] tilesAttackingRight = { 4, 5, 6, 7 };
				int[] tilesDamageRight = { 8, 9 ,10, 11 };
				int[] tilesStopLeft = { 15, 14 };
				int[] tilesWalkingLeft = { 13, 12 };
				int[] tilesAttackingLeft = { 19, 18, 17, 16 };
				int[] tilesDamageLeft = { 23, 22, 21, 20 };
				
				SGImage characterImage = getImageFactory().createImage("tilesets/" + ((EntCharacter)currentEntity).getImage());
				SGTileset tileset = new SGTileset(characterImage, new Point(4, 6), null);		
				SGSpriteDesc sprDesc = new SGSpriteDesc(tileset);
				
				SGAnimation animation = new SGAnimation(tilesStopRight, 0.1f);	
				sprDesc.addAnimation("stop_right", animation);
				
				animation = new SGAnimation(tilesStopLeft, 0.1f);	
				sprDesc.addAnimation("stop_left", animation);
				
				animation = new SGAnimation(tilesWalkingRight, 0.1f);	
				sprDesc.addAnimation("walking_right", animation);
				
				animation = new SGAnimation(tilesWalkingLeft, 0.1f);	
				sprDesc.addAnimation("walking_left", animation);
				
				SGEvent event = new SGEvent() {
					@Override
					public void execute() {
						((EntCharacter) currentEntity).finishAttak();
					}
				};
				
				animation = new SGAnimation(tilesAttackingRight, 0.3f);	
				animation.setOnFinish(event);
				sprDesc.addAnimation("attacking_right", animation);
				
				animation = new SGAnimation(tilesAttackingLeft, 0.3f);
				animation.setOnFinish(event);
				sprDesc.addAnimation("attacking_left", animation);
				
				event = new SGEvent() {
					
					@Override
					public void execute() {
						((EntCharacter) currentEntity).finishDamage();
					}
				};
				
				animation = new SGAnimation(tilesDamageRight, 0.1f);
				animation.setOnFinish(event);
				sprDesc.addAnimation("damage_right", animation);
				
				animation = new SGAnimation(tilesDamageLeft, 0.1f);
				animation.setOnFinish(event);
				sprDesc.addAnimation("damage_left", animation);
				
				mSprites.put(currentEntity.getId(), new SGSprite(sprDesc, currentEntity));
			}else if (currentEntity instanceof EntItem){
				SGImage itemImage = getImageFactory().createImage("tilesets/" + ((EntItem)currentEntity).getImage());
				SGTileset tileset = new SGTileset(itemImage, new Point(2, 1), null);		
				SGSpriteDesc sprDesc = new SGSpriteDesc(tileset);
				
				int[] tilesStop = { 0, 1 };
				
				SGAnimation animation = new SGAnimation(tilesStop, 0.1f);
				sprDesc.addAnimation("stop", animation);
				
				mSprites.put(currentEntity.getId(), new SGSprite(sprDesc, currentEntity));
			}
		}
		
		
		
		SGImageFactory imageFactory = getImageFactory();
		SGImage imgFontVisitor = imageFactory.createImage("fonts/font_visitor_white.png");
		Point tilesNum = new Point(16, 16);
		SGTileset tilesetVisitor = new SGTileset(imgFontVisitor, tilesNum, null);
		
		mFntVisitorBig = new SGFont(tilesetVisitor, new PointF(CHAR_SIZE_BIG, CHAR_SIZE_BIG));
		mFntVisitorSmall = new SGFont(tilesetVisitor, new PointF(CHAR_SIZE_SMALL, CHAR_SIZE_SMALL));
		
		
		// Efeitos sonoros
		try{
			AssetManager assetManager = getContext().getAssets();
			InputStream inputStream;
			inputStream = assetManager.open("xml/sounds_config.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			Element elementRoot = doc.getDocumentElement();
			
			NodeList config = elementRoot.getElementsByTagName("sound");
			for (int i = 0; i < config.getLength(); i++) {
				Element sound = (Element) config.item(i);
				int id = Integer.parseInt(sound.getElementsByTagName("id").item(0).getTextContent());
				String soundFile = "sounds/" + sound.getElementsByTagName("file").item(0).getTextContent();

				mSounds.put(id, mSoundPool.loadSound(soundFile));
			}
			
			inputStream.close();
		}catch(Exception e){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("arquivo ");
			stringBuilder.append("xml/sounds_config");
			stringBuilder.append(" n�o encontrado!");
			
			Log.d("Erro", stringBuilder.toString());
		}
		
		
		
		// M�sica
		mMusicPlayer.loadMusic("musics/"+ mMap.getMusic());
		mMusicPlayer.play(true, 1.0f, 1.0f);
		
		//GUI
		mStrPlayerHp = "HP: 0";
		
		SGWidgetContainer guiRoot = mGui.getRoot();
		
		PointF position = new PointF();
		PointF dimensions = new PointF();
		
		//Cont�iner - Placar
		position.set(0, 0);
		dimensions.set(0, 0); // Inicialmente sem dimens�es
		
		mCtnrScore = new SGWidgetContainer(Alignment.Center, position, dimensions, mGui);
		guiRoot.addChild("score", mCtnrScore);
		
		//Hp do jogador
		position.set(20, 14);
		mLblPlayerHp = new SGWidgetLabel(Alignment.Left, position, mFntVisitorSmall, mStrPlayerHp);
		mCtnrScore.addChild("player_hp", mLblPlayerHp);
		
		//Textos informativos
		Context context = getContext();
		
		mStrGameOver = context.getResources().getString(R.string.game_over);
		mStrEnd = context.getResources().getString(R.string.end);
		mStrPaused = context.getResources().getString(R.string.paused);
		mStrStart = context.getResources().getString(R.string.start);
		
		//Cont�iner - Info
		position.set(0, 0);
		dimensions.set(0, 0); // Inicialmente sem dimens�es
		
		mCtnrInfo = new SGWidgetContainer(Alignment.Center, position, dimensions, mGui);
		guiRoot.addChild("info", mCtnrInfo);
		
		//Informa��es - Superior
		position.set(0, 80);
		mLblUpperInfo = new SGWidgetLabel(Alignment.Center, position, mFntVisitorSmall, "");
		mCtnrInfo.addChild("upper_info", mLblUpperInfo);
		
		//Informa��es - Inferior
		position.set(0, 100);
		mLblLowerInfo = new SGWidgetLabel(Alignment.Center, position, mFntVisitorBig, "");
		mCtnrInfo.addChild("lower_info", mLblLowerInfo);
		
		
		try {
			AssetManager assetManager = getContext().getAssets();
			InputStream inputStream;
			inputStream = assetManager.open("xml/buttons_config.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			Element elementRoot = doc.getDocumentElement();
			
			//Bot�o de pausa
			Element config = (Element) elementRoot.getElementsByTagName("pause").item(0);
			int positionX = Integer.parseInt(config.getElementsByTagName("posX").item(0).getTextContent());
			int positionY = Integer.parseInt(config.getElementsByTagName("posY").item(0).getTextContent());
			int dimensionsX = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			int dimensionsY = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			
			tilesNum = new Point(2, 1);
			SGTileset tileset = new SGTileset(imageFactory.createImage("gui/button_pause.png"),
					tilesNum, null);
			position.set(positionX, positionY);
			dimensions.set(dimensionsX, dimensionsY);
			
			mBtnPause = 
					new SGWidgetButton(Alignment.Center, position, dimensions, tileset, mGui){
						@Override
						public boolean onUp(PointF position){
							if(mModel.getCurrentState() != GameModel.STATE_PAUSED &&
								mModel.getCurrentState() != GameModel.STATE_GAME_OVER){
								mModel.pause();
							}
							else if(mModel.getCurrentState() == GameModel.STATE_PAUSED){
								mModel.unpause();
							}
							return true;
						}
					};
			guiRoot.addChild("pause", mBtnPause);
			
			//bot�o d-pad
			config = (Element) elementRoot.getElementsByTagName("dPad").item(0);
			positionX = Integer.parseInt(config.getElementsByTagName("posX").item(0).getTextContent());
			positionY = Integer.parseInt(config.getElementsByTagName("posY").item(0).getTextContent());
			dimensionsX = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			dimensionsY = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			
			tilesNum = new Point(5, 2);
			tileset = new SGTileset(imageFactory.createImage("gui/button_dpad.png"),
					tilesNum, null);
			position.set(positionX, positionY);
			dimensions.set(dimensionsX, dimensionsY);
			mBtnDPad = 
					new SGWidgetDPadButton(Alignment.Left, position, dimensions, tileset, mGui){
						
						@Override
						public boolean downOnDown(PointF position) {
							mModel.pressDown();
							return true;
						}
						
						@Override
						public boolean downOnUp(PointF position){
							mModel.unpressDown();
							return true;
						}
						
						@Override
						public boolean upOnDown(PointF position) {
							mModel.pressUp();
							return true;
						}
						
						@Override
						public boolean upOnUp(PointF position) {
							mModel.unpressUp();
							return true;
						}
						
						@Override
						public boolean leftOnDown(PointF position) {
							mModel.pressLeft();
							return true;
						}
						
						@Override
						public boolean leftOnUp(PointF position) {
							mModel.unpressLeft();
							return true;
						}
						
						@Override
						public boolean rightOnDown(PointF position) {
							mModel.pressRight();
							return true;
						}
						
						@Override
						public boolean rightOnUp(PointF position) {
							mModel.unpressRight();
							return true;
						}
						
						@Override
						public boolean downLeftOnDown(PointF position) {
							mModel.pressDownLeft();
							return true;
						}
						
						@Override
						public boolean downLeftOnUp(PointF position) {
							mModel.unpressDownLeft();
							return true;
						}
						
						@Override
						public boolean downRightOnDown(PointF position) {
							mModel.pressDownRight();
							return true;
						}
						
						@Override
						public boolean downRightOnUp(PointF position) {
							mModel.unpressDownRight();
							return true;
						}
						
						@Override
						public boolean upRightOnDown(PointF position) {
							mModel.pressUpRight();
							return true;
						}
						
						@Override
						public boolean upRightOnUp(PointF position) {
							mModel.unpressUpRight();
							return true;
						}
						
						@Override
						public boolean upLeftOnDown(PointF position) {
							mModel.pressUpLeft();
							return true;
						}
						
						@Override
						public boolean upLeftOnUp(PointF position) {
							mModel.unpressUpLeft();
							return true;
						}
						
						@Override
						public boolean middleOnDown(PointF position) {
							mModel.pressMiddle();
							return true;
						}
						
						@Override
						public boolean middleOnUp(PointF position) {
							mModel.unpressMiddle();
							return true;
						}
					};
			guiRoot.addChild("dpad", mBtnDPad);
			
			// bot�o ataque
			config = (Element) elementRoot.getElementsByTagName("attack").item(0);
			positionX = Integer.parseInt(config.getElementsByTagName("posX").item(0).getTextContent());
			positionY = Integer.parseInt(config.getElementsByTagName("posY").item(0).getTextContent());
			dimensionsX = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			dimensionsY = Integer.parseInt(config.getElementsByTagName("dimX").item(0).getTextContent());
			
			tilesNum = new Point(2, 1);
			tileset = new SGTileset(imageFactory.createImage("gui/button_action.png"),
					tilesNum, null);
			position.set(positionX, positionY);
			dimensions.set(dimensionsX, dimensionsY);
			mBtnAttack = new SGWidgetButton(Alignment.Right, position, dimensions, tileset, mGui){
				@Override
				public boolean onDown(PointF position) {
					mModel.attack();
					return true;
				}
			};
			guiRoot.addChild("attack", mBtnAttack);
			
			inputStream.close();
			
		} catch (Exception e) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("arquivo ");
			stringBuilder.append("xml/buttons_config");
			stringBuilder.append(" n�o encontrado!");
			
			Log.d("Erro", stringBuilder.toString());
		}	
	}
	
	@Override 
	public void step(Canvas canvas, float elapsedTimeInSeconds) 
	{
		mModel.step(elapsedTimeInSeconds);
		
		mMap = (MapBase) mModel.getCurrentMap();
		
		SGRenderer renderer = getRenderer();		
		int screenColor = Color.LTGRAY;
		int viewportColor = Color.BLACK;
		renderer.beginDrawing(canvas, screenColor, viewportColor);
		
		if(mModel.getCurrentState() == GameModel.STATE_NEXT){
			setup();
		}
		else{
			ArrayList<SGEntity> entities = mMap.getEntitiesOrderedByZ(mEntities);
			
			mMapPosition2D.set(mMap.getPosition().x, mMap.getPosition().y + mMap.getDimensions().z - mMap.getPosition().z);
			mMapDimensions2D.set(mMap.getDimensions().x, mMap.getDimensions().y + mMap.getDimensions().z);
			
			mTempRectFDest.top = -mMapPosition2D.y;
			mTempRectFDest.left = -mMapPosition2D.x;
			mTempRectFDest.bottom = mMapDimensions2D.y - mMapPosition2D.y;
			mTempRectFDest.right = mMapDimensions2D.x - mMapPosition2D.x;

			renderer.drawImage(mImgField, mTempRectSrc, mTempRectFDest);
			
			mStrPlayerHp = "HP: "+String.valueOf(((MapBase) mModel.getCurrentMap()).getPlayer().getCurrentHp());
			mLblPlayerHp.setString(mStrPlayerHp);
			
			mCtnrInfo.setIsVisible(true);
			
			for(SGEntity currentEntity : entities)
			{
				if(!currentEntity.isActive())
					continue;
				
				SGSprite sprite = mSprites.get(currentEntity.getId());
				
				if(currentEntity instanceof EntCharacter) 
				{
					SGAnimation last = sprite.getCurrentAnimation();
					if(currentEntity.hasFlag(EntCharacter.STATE_LEFT)){
						if(currentEntity.hasFlag(EntCharacter.STATE_STOP)) 
							sprite.setCurrentAnimation("stop_left");
						else if(currentEntity.hasFlag(EntCharacter.STATE_WALKING)) 
							sprite.setCurrentAnimation("walking_left");
						else if (currentEntity.hasFlag(EntCharacter.STATE_ATTACKING))
							sprite.setCurrentAnimation("attacking_left");
						else //if (currentEntity.hasFlag(EntCharacter.STATE_DAMAGE))
							sprite.setCurrentAnimation("damage_left");
					}
					else{ //if(currentEntity.hasFlag(EntCharacter.STATE_LEFT)){
						if(currentEntity.hasFlag(EntCharacter.STATE_STOP)) 
							sprite.setCurrentAnimation("stop_right");
						else if(currentEntity.hasFlag(EntCharacter.STATE_WALKING)) 
							sprite.setCurrentAnimation("walking_right");
						else if (currentEntity.hasFlag(EntCharacter.STATE_ATTACKING))
							sprite.setCurrentAnimation("attacking_right");
						else //if (currentEntity.hasFlag(EntCharacter.STATE_DAMAGE))
							sprite.setCurrentAnimation("damage_right");
					}
					
					if(last != null && last != sprite.getCurrentAnimation())
						sprite.getCurrentAnimation().reset();
					
					
					
					SGTileset tileset = sprite.getTileSet();
					mEntityPosition2D.set(sprite.getPosition().x, sprite.getPosition().y + mMap.getDimensions().z - sprite.getPosition().z - sprite.getDimensions().z/2);
					mEntityPosition2D.y -= mMapPosition2D.y;
					mEntityPosition2D.x -= mMapPosition2D.x;
					mEntityDimensions2D.set(sprite.getDimensions().x, sprite.getDimensions().y);
					int currentTile = sprite.getCurrentAnimation().getCurrentTile();
					Rect drawingArea = tileset.getTile(currentTile);
					
					renderer.drawImage(tileset.getImage(), drawingArea,
									   mEntityPosition2D, mEntityDimensions2D);
					
					if(currentEntity.hasFlag(EntCharacter.STATE_ATTACKING) || currentEntity.hasFlag(EntCharacter.STATE_DAMAGE)){
						sprite.getCurrentAnimation().start(1);
					}
					else{
						sprite.getCurrentAnimation().start(-1);
					}
						
					sprite.step(elapsedTimeInSeconds);	
				}
				else if(currentEntity instanceof EntItem) 
				{
					sprite.setCurrentAnimation("stop");
					
					sprite.step(elapsedTimeInSeconds);
					
					SGTileset tileset = sprite.getTileSet();
					mEntityPosition2D.set(sprite.getPosition().x, sprite.getPosition().y + mMap.getDimensions().z - sprite.getPosition().z - sprite.getDimensions().z/2);
					mEntityPosition2D.y -= mMapPosition2D.y;
					mEntityPosition2D.x -= mMapPosition2D.x;
					mEntityDimensions2D.set(sprite.getDimensions().x, sprite.getDimensions().y);
					int currentTile = sprite.getCurrentAnimation().getCurrentTile();
					Rect drawingArea = tileset.getTile(currentTile);
					
					renderer.drawImage(tileset.getImage(), drawingArea,
									   mEntityPosition2D, mEntityDimensions2D);
					
					sprite.getCurrentAnimation().start(-1);
				}
			}
			
			if(mModel.getCurrentState() == GameModel.STATE_START){
				mCtnrInfo.setIsVisible(true);
				
				mLblUpperInfo.setString("");
				mLblLowerInfo.setString(mStrStart);
			}
			else if(mModel.getCurrentState() == GameModel.STATE_GAME_OVER){
				mCtnrInfo.setIsVisible(true);
				
				mLblUpperInfo.setString("");
				mLblLowerInfo.setString(mStrGameOver);
				
				mBtnPause.setIsEnabled(false);
			}
			else if(mModel.getCurrentState() == GameModel.STATE_END){
				mCtnrInfo.setIsVisible(true);
				
				mLblUpperInfo.setString("");
				mLblLowerInfo.setString(mStrEnd);
				
				mBtnPause.setIsEnabled(false);
			}
			else if(mModel.getCurrentState() == GameModel.STATE_PAUSED){
				mCtnrInfo.setIsVisible(true);
				
				mLblUpperInfo.setString("");
				mLblLowerInfo.setString(mStrPaused);
			}
			else{ // mModel.getCurrentState() == GameModel.STATE_RUNNING
				mCtnrInfo.setIsVisible(false);
			}
		}
			
		
		mGui.update();
		mGui.render();
		
		renderer.endDrawing();
		
		ArrayList<Integer> sounds = mModel.getSounds();
		for (int sound : sounds) {
			mSoundPool.playSound(mSounds.get(sound), 1, 1, 0, 1);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if(!hasWindowFocus){
			mModel.pause();
		}
	}
	
	public SGMusicPlayer getMusicPlayer() 
	{
		return mMusicPlayer;
	}
	
	public SGGui getGui(){ return mGui; }
}