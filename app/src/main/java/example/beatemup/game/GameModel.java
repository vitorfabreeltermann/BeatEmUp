package example.beatemup.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGPointF3D;
import example.beatemup.engine.SGTimer;
import example.beatemup.engine.SGWorld;

public class GameModel extends SGWorld 
{
	public static final int STATE_START = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_GAME_OVER = 2;
	public static final int STATE_END = 3;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_NEXT = 5;
	
	public static final int NORMAL_MAP_ID = 0;
	public static final int EASY_MAP_ID = 1;
	public static final int HARD_MAP_ID = 2;
	
	private int 			mCurrentState;
	private SGTimer 		mStartTimer;
	private int				mWhoScored;
	
	private boolean			mUpPressed;
	private boolean			mDownPressed;
	private boolean			mLeftPressed;
	private boolean			mRightPressed;
	private boolean			mUpLeftPressed;
	private boolean			mUpRightPressed;
	private boolean			mDownLeftPressed;
	private boolean			mDownRightPressed;
	@SuppressWarnings("unused")
	private boolean			mMiddlePressed;
	private boolean			mAttack;
	
	private ArrayList<Integer> mMapSequence = new ArrayList<Integer>();
	private int mCurrentMapIndex = 0;
	
	private int             mPreviousState;
	
	private Context			mContext;
	
	private ArrayList<Integer> mSounds = new ArrayList<Integer>();
	
	public GameModel(Point sceneDimensions, Context context) 
	{
		super(sceneDimensions);
		
		mContext = context;
		
		try{

			AssetManager assetManager = getContext().getAssets();
			InputStream inputStream;
			inputStream = assetManager.open("xml/world_config.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			Element elementRoot = doc.getDocumentElement();
			
			NodeList maps = elementRoot.getElementsByTagName("map");
			for (int i = 0; i < maps.getLength(); i++) {
				Element mapInfo = (Element) maps.item(i);
				int id = Integer.parseInt(mapInfo.getElementsByTagName("id").item(0).getTextContent());
				float dimensionsX = Float.parseFloat(mapInfo.getElementsByTagName("dimX").item(0).getTextContent());
				float dimensionsY = Float.parseFloat(mapInfo.getElementsByTagName("dimY").item(0).getTextContent());
				float dimensionsZ = Float.parseFloat(mapInfo.getElementsByTagName("dimZ").item(0).getTextContent());
				String music = mapInfo.getElementsByTagName("music").item(0).getTextContent();
				String image = mapInfo.getElementsByTagName("image").item(0).getTextContent();

				MapBase map = new MapBase(this, id, new SGPointF3D(0 ,0, 0),
						new SGPointF3D(dimensionsX, dimensionsY, dimensionsZ), music, image);
				
				mMapList.put(map.getId(), map);
			}
			
			String[] order = ((Element)elementRoot.getElementsByTagName("sequence").item(0)).getTextContent().split(",");
			
			for (String index : order) {
				mMapSequence.add(Integer.parseInt(index));
			}
			
			mCurrentMap = mMapList.get(mMapSequence.get(0));
			
			inputStream.close();
		}catch(Exception e){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("arquivo ");
			stringBuilder.append("xml/world_config");
			stringBuilder.append(" n�o encontrado!");
			
			Log.d("Erro", stringBuilder.toString());
		}
	}
	
	public void setup() 
	{
		mSounds.clear();
		mCurrentMap.setup();
		
		mCurrentState = STATE_START;
		
		// Timer de rein�cio
		mStartTimer = new SGTimer(0.8f);
	}
	
	public void movePlayer(float x, float y, float z)
	{
		((MapBase)mCurrentMap).movePlayer(x, y, z);
	}
	
	@Override
	public void step(float elapsedTimeInSeconds) 
	{
		mSounds.clear();
		if(elapsedTimeInSeconds > 1.0f) 
		{
			elapsedTimeInSeconds = 0.1f;
		}
		
		if(mCurrentState == STATE_RUNNING)
		{	
			int x = 0, y = 0, z = 0;
			if(mUpPressed || mUpLeftPressed || mUpRightPressed){
				z += 200;
			}else if(mDownPressed || mDownLeftPressed || mDownRightPressed){
				z -= 200;
			}
			if(mLeftPressed || mUpLeftPressed || mDownLeftPressed){
				x -= 200;
			}else if(mRightPressed || mUpRightPressed || mDownRightPressed){
				x += 200;
			}
			movePlayer(x * elapsedTimeInSeconds, y * elapsedTimeInSeconds, z * elapsedTimeInSeconds);
			if(mAttack){
				((MapBase)mCurrentMap).getPlayer().attack();
				mAttack = false;
			}
			mCurrentMap.step(elapsedTimeInSeconds);
		}
		else if(mCurrentState == STATE_START)
		{
			if(!mStartTimer.hasStarted()) {
				mStartTimer.start();
			}
			if(mStartTimer.step(elapsedTimeInSeconds) == true) {
				mStartTimer.stopAndReset();
				mCurrentState = STATE_RUNNING;
			}
		}
		else if(mCurrentState == STATE_GAME_OVER || mCurrentState == STATE_NEXT || mCurrentState == STATE_END)
		{
		}
	}
	
	public void resetWorld() 
	{
		mCurrentMap.resetMap();
	}
	
	public SGEntity getEntity(int Id) { return mCurrentMap.getEntity(Id); }
	
	public SparseArray<SGEntity> getEntities() { return mCurrentMap.getEntities(); }
	public int getCurrentState() { return mCurrentState; }
	public int getWhoScored() { return mWhoScored; }
	
	public void setCurrentState(int state) { mCurrentState = state; }
	public void setWhoScored(int whoScored) { mWhoScored = whoScored; }
	
	public void pause(){
		if(mCurrentState != GameModel.STATE_PAUSED){
			mPreviousState = mCurrentState;
			mCurrentState = STATE_PAUSED;
		}
	}
	
	public void unpause(){
		if(mCurrentState == GameModel.STATE_PAUSED){
			mCurrentState = mPreviousState;
		}
	}
	
	public void pressUp(){
		mUpPressed = true;
		mDownPressed = false;
		mLeftPressed = false;
		mRightPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressUp(){
		mUpPressed = false;
	}
	
	public void pressDown(){
		mDownPressed = true;
		mUpPressed = false;
		mLeftPressed = false;
		mRightPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressDown(){
		mDownPressed = false;
	}
	
	public void pressLeft(){
		mLeftPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressLeft(){
		mLeftPressed = false;
	}
	
	public void pressRight(){
		mRightPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mLeftPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressRight(){
		mRightPressed = false;
	}
	
	public void pressUpLeft(){
		mUpLeftPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressUpLeft(){
		mUpLeftPressed = false;
	}
	
	public void pressUpRight(){
		mUpRightPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mLeftPressed = false;
		mUpLeftPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressUpRight(){
		mUpRightPressed = false;
	}
	
	public void pressDownLeft(){
		mDownLeftPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mLeftPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownRightPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressDownLeft(){
		mDownLeftPressed = false;
	}
	
	public void pressDownRight(){
		mDownRightPressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mLeftPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mMiddlePressed = false;
	}
	
	public void unpressDownRight(){
		mDownRightPressed = false;
	}
	
	public void pressMiddle(){
		mMiddlePressed = true;
		mUpPressed = false;
		mDownPressed = false;
		mRightPressed = false;
		mLeftPressed = false;
		mUpLeftPressed = false;
		mUpRightPressed = false;
		mDownLeftPressed = false;
		mDownRightPressed = false;
	}
	
	public void unpressMiddle(){
		mMiddlePressed = false;
	}
	
	public void attack(){
		mAttack = true;
	}
	
	public void nextMap(){
		mCurrentMapIndex++;
		if(mCurrentMapIndex >= mMapSequence.size())
			setCurrentState(STATE_END);
		else{
			setCurrentState(STATE_NEXT);
			mCurrentMap = mMapList.get(mMapSequence.get(mCurrentMapIndex));
		}
	}
	
	public void addSound(int soundId){
		if(!mSounds.contains(soundId))
			mSounds.add(soundId);
	}
	
	public ArrayList<Integer> getSounds(){ return mSounds; }
	
	public Context getContext(){ return mContext; }
}