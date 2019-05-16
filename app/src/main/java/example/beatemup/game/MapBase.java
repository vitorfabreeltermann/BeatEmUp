package example.beatemup.game;

import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;
import example.beatemup.engine.SGTrigger;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGWorld;

import android.content.res.AssetManager;
import android.util.Log;

public class MapBase extends SGMap{
	
	private EntPlayer		mPlayer;
	
	private TrgBoundaryLeft mBoundaryLeft;
	private TrgBoundaryRight mBoundaryRight;
	
	private String mMusic;
	private String mImage;
	
	public MapBase(SGWorld world, int id, SGPointF3D position, SGPointF3D dimensions, String music, String image) {
		super(world, id, position, dimensions);
		mMusic = music;
		mImage = image;
	}

	@Override
	public void step(float timeElapsedInSeconds) {
		for (int i = 0; i < mEntities.size(); i++) {
			mEntities.valueAt(i).step(timeElapsedInSeconds);
		}
		EntPlayer player = mPlayer;
		float x = player.getPosition().x + player.getDimensions().x / 2 <= mBoundaryLeft.getPosition().x + mBoundaryLeft.getDimensions().x + mWorld.getSceneDimensions().x / 2 ? mBoundaryLeft.getPosition().x + mBoundaryLeft.getDimensions().x :
			player.getPosition().x + player.getDimensions().x / 2 >= mBoundaryRight.getPosition().x - mWorld.getSceneDimensions().x / 2 ? mBoundaryRight.getPosition().x - mWorld.getSceneDimensions().x :
			player.getPosition().x + player.getDimensions().x / 2 - mWorld.getSceneDimensions().x / 2;
		float y = player.getPosition().y + player.getDimensions().y / 2 <= mWorld.getSceneDimensions().y / 2 ? 0 :
				player.getPosition().y + player.getDimensions().y / 2 >= mDimensions.y - mWorld.getSceneDimensions().y / 2 ? mDimensions.y - mWorld.getSceneDimensions().y :
				player.getPosition().y + player.getDimensions().y / 2 - mWorld.getSceneDimensions().y / 2;
		float z = player.getPosition().z + player.getDimensions().z/2 - mDimensions.z/2 > 0 ? player.getPosition().z + player.getDimensions().z/2 - mDimensions.z/2 : 0;
		mPosition.set(x, y, z);
	}

	@Override
	public void setup() {
		
		SGPointF3D worldDimensions = getDimensions();
		
		try{
			AssetManager assetManager = ((GameModel) getWorld()).getContext().getAssets();
			InputStream inputStream;
			inputStream = assetManager.open("xml/map"+mId+"_config.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			Element elementRoot = doc.getDocumentElement();
			
			// jogador
			Element playerConfig = (Element)elementRoot.getElementsByTagName("player").item(0);
			int id = 0;
			float positionX = Float.parseFloat(playerConfig.getElementsByTagName("posX").item(0).getTextContent());
			float positionZ = Float.parseFloat(playerConfig.getElementsByTagName("posZ").item(0).getTextContent());
			float dimensionsX = Float.parseFloat(playerConfig.getElementsByTagName("dimX").item(0).getTextContent());
			float dimensionsY = Float.parseFloat(playerConfig.getElementsByTagName("dimY").item(0).getTextContent());
			float dimensionsZ = Float.parseFloat(playerConfig.getElementsByTagName("dimZ").item(0).getTextContent());
			String image = playerConfig.getElementsByTagName("image").item(0).getTextContent();
			float speed = Float.parseFloat(playerConfig.getElementsByTagName("speed").item(0).getTextContent());
			int hp = Integer.parseInt(playerConfig.getElementsByTagName("hp").item(0).getTextContent());
			int str = Integer.parseInt(playerConfig.getElementsByTagName("str").item(0).getTextContent());
			
			SGPointF3D tempPosition = new SGPointF3D(positionX, mDimensions.y -dimensionsY , positionZ);
			SGPointF3D tempDimensions = new SGPointF3D(dimensionsX, dimensionsY, dimensionsZ);
			mPlayer = new EntPlayer(this, id, tempPosition, tempDimensions, image, speed, hp, str);
			
			int eventId = 0;
			Element onRceiveDamage = (Element) elementRoot.getElementsByTagName("onRecieveDamage").item(0);
			NodeList playSounds = onRceiveDamage.getElementsByTagName("sound");
			for (int i = 0; i < playSounds.getLength(); i++) {
				int soundId = Integer.parseInt(((Element)playSounds.item(i)).getTextContent());
				
				EventPlaySound eventSound = new EventPlaySound(soundId, (GameModel) getWorld());
				mPlayer.addOnRecieveDamage(eventId, eventSound);
				eventId++;
			}
			NodeList heals = onRceiveDamage.getElementsByTagName("heal");
			for (int i = 0; i < heals.getLength(); i++) {
				int heal = Integer.parseInt(((Element)heals.item(i)).getTextContent());
				
				EventHeal eventHeal = new EventHeal(mPlayer,heal);
				mPlayer.addOnRecieveDamage(eventId, eventHeal);
				eventId++;
			}
			
			mEntities.put(mPlayer.getId(), mPlayer);
			
			
			
			// trigger fronteira esquerda
			tempPosition.set(-200, -200, -200);
			tempDimensions.set(200, worldDimensions.y+400, worldDimensions.z+400);
			mBoundaryLeft =  new TrgBoundaryLeft(this, 1, tempPosition, tempDimensions);
			mBoundaryLeft.addObservedEntity(mPlayer);
			mEntities.put(mBoundaryLeft.getId(), mBoundaryLeft);
			
			
			//trigger fronteira direita
			tempPosition.set(worldDimensions.x, -200, -200);
			tempDimensions.set(200, worldDimensions.y+400, worldDimensions.z+400);
			mBoundaryRight =  new TrgBoundaryRight(this, 2, tempPosition, tempDimensions);
			mBoundaryRight.addObservedEntity(mPlayer);
			mEntities.put(mBoundaryRight.getId(), mBoundaryRight);
			
			
			// inimigos
			NodeList enemies = elementRoot.getElementsByTagName("enemy");
			for (int i = 0; i < enemies.getLength(); i++) {
				Element enemyConfig = (Element)enemies.item(i);
				id = Integer.parseInt(enemyConfig.getElementsByTagName("id").item(0).getTextContent());
				positionX = Float.parseFloat(enemyConfig.getElementsByTagName("posX").item(0).getTextContent());
				positionZ = Float.parseFloat(enemyConfig.getElementsByTagName("posZ").item(0).getTextContent());
				dimensionsX = Float.parseFloat(enemyConfig.getElementsByTagName("dimX").item(0).getTextContent());
				dimensionsY = Float.parseFloat(enemyConfig.getElementsByTagName("dimY").item(0).getTextContent());
				dimensionsZ = Float.parseFloat(enemyConfig.getElementsByTagName("dimZ").item(0).getTextContent());
				image = enemyConfig.getElementsByTagName("image").item(0).getTextContent();
				speed = Float.parseFloat(enemyConfig.getElementsByTagName("speed").item(0).getTextContent());
				hp = Integer.parseInt(enemyConfig.getElementsByTagName("hp").item(0).getTextContent());
				str = Integer.parseInt(enemyConfig.getElementsByTagName("str").item(0).getTextContent());
				
				tempPosition.set(positionX, mDimensions.y -dimensionsY, positionZ);
				tempDimensions.set(dimensionsX, dimensionsY, dimensionsZ);
				EntEnemy enemy = new EntEnemy(this, id, tempPosition, tempDimensions, image, speed, hp, str);
				
				eventId = 0;
				onRceiveDamage = (Element) elementRoot.getElementsByTagName("onRecieveDamage").item(0);
				playSounds = onRceiveDamage.getElementsByTagName("sound");
				for (int j = 0; j < playSounds.getLength(); j++) {
					int soundId = Integer.parseInt(((Element)playSounds.item(j)).getTextContent());
					
					EventPlaySound eventSound = new EventPlaySound(soundId, (GameModel) getWorld());
					enemy.addOnRecieveDamage(eventId, eventSound);
					eventId++;
				}
				heals = onRceiveDamage.getElementsByTagName("heal");
				for (int j = 0; j < heals.getLength(); j++) {
					int heal = Integer.parseInt(((Element)heals.item(j)).getTextContent());
					
					EventHeal eventHeal = new EventHeal(mPlayer,heal);
					enemy.addOnRecieveDamage(eventId, eventHeal);
					eventId++;
				}
				
				mEntities.put(enemy.getId(), enemy);
			}
			
			
			// itens
			NodeList items = elementRoot.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Element itemConfig = (Element)items.item(i);
				id = Integer.parseInt(itemConfig.getElementsByTagName("id").item(0).getTextContent());
				positionX = Float.parseFloat(itemConfig.getElementsByTagName("posX").item(0).getTextContent());
				positionZ = Float.parseFloat(itemConfig.getElementsByTagName("posZ").item(0).getTextContent());
				dimensionsX = Float.parseFloat(itemConfig.getElementsByTagName("dimX").item(0).getTextContent());
				dimensionsY = Float.parseFloat(itemConfig.getElementsByTagName("dimY").item(0).getTextContent());
				dimensionsZ = Float.parseFloat(itemConfig.getElementsByTagName("dimZ").item(0).getTextContent());
				image = itemConfig.getElementsByTagName("image").item(0).getTextContent();
				
				tempPosition.set(positionX, mDimensions.y -dimensionsY, positionZ);
				tempDimensions.set(dimensionsX, dimensionsY, dimensionsZ);
				EntItem item = new EntItem(this, id, tempPosition, tempDimensions, image);
				
				eventId = 0;
				onRceiveDamage = (Element) elementRoot.getElementsByTagName("onGot").item(0);
				playSounds = onRceiveDamage.getElementsByTagName("sound");
				for (int j = 0; j < playSounds.getLength(); j++) {
					int soundId = Integer.parseInt(((Element)playSounds.item(j)).getTextContent());
					
					EventPlaySound eventSound = new EventPlaySound(soundId, (GameModel) getWorld());
					item.addOnGot(eventId, eventSound);
					eventId++;
				}
				heals = onRceiveDamage.getElementsByTagName("heal");
				for (int j = 0; j < heals.getLength(); j++) {
					int heal = Integer.parseInt(((Element)heals.item(j)).getTextContent());
					
					EventHeal eventHeal = new EventHeal(mPlayer,heal);
					item.addOnGot(eventId, eventHeal);
					eventId++;
				}
				
				mEntities.put(item.getId(), item);
			}
			
			//trigger a esquerda
			tempPosition.set(-200, -200, -200);
			tempDimensions.set(200, worldDimensions.y+400, worldDimensions.z+400);
			TrgLeftWall leftWall =  new TrgLeftWall(this, 3, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					leftWall.addObservedEntity(currentEntity);
			}
			mEntities.put(leftWall.getId(), leftWall);
			
			
			//trigger a direita
			tempPosition.set(worldDimensions.x, -200, -200);
			tempDimensions.set(200, worldDimensions.y+400, worldDimensions.z+400);
			TrgRightWall rightWall =  new TrgRightWall(this, 4, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					rightWall.addObservedEntity(currentEntity);
			}
			mEntities.put(rightWall.getId(), rightWall);
			
			
			//trigger a cima
			tempPosition.set(-200, -200, -200);
			tempDimensions.set(worldDimensions.x+400, 200, worldDimensions.z+400);
			TrgUpperWall upperWall =  new TrgUpperWall(this, 5, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					upperWall.addObservedEntity(currentEntity);
			}
			mEntities.put(upperWall.getId(), upperWall);
			
			
			//trigger a baixo
			tempPosition.set(-200, worldDimensions.y, -200);
			tempDimensions.set(worldDimensions.x+400, 200, worldDimensions.z+400);
			TrgLowerWall lowerWall =  new TrgLowerWall(this, 6, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					lowerWall.addObservedEntity(currentEntity);
			}
			mEntities.put(lowerWall.getId(), lowerWall);
			
			//trigger a frente
			tempPosition.set(-200, -200, -200);
			tempDimensions.set(worldDimensions.x+400, worldDimensions.y+400, 200);
			TrgFrontWall frontWall =  new TrgFrontWall(this, 7, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					frontWall.addObservedEntity(currentEntity);
			}
			mEntities.put(frontWall.getId(), frontWall);
			
			
			//trigger ao fundo
			tempPosition.set(-200, -200, worldDimensions.z);
			tempDimensions.set(worldDimensions.x+400, worldDimensions.y+400, 200);
			TrgBackWall backWall =  new TrgBackWall(this, 8, tempPosition, tempDimensions);
			for (int i = 0; i < getEntities().size(); i++) {
				SGEntity currentEntity = getEntities().get(getEntities().keyAt(i));
				if(!(currentEntity instanceof SGTrigger))
					backWall.addObservedEntity(currentEntity);
			}
			mEntities.put(backWall.getId(), backWall);
			
			// trigger ativador
			NodeList activators = elementRoot.getElementsByTagName("activator");
			for (int i = 0; i < activators.getLength(); i++) {
				Element activatorConfig = (Element)activators.item(i);
				id = Integer.parseInt(activatorConfig.getElementsByTagName("id").item(0).getTextContent());
				positionX = Float.parseFloat(activatorConfig.getElementsByTagName("posX").item(0).getTextContent());
				dimensionsX = Float.parseFloat(activatorConfig.getElementsByTagName("dimX").item(0).getTextContent());
				boolean isFinal = Boolean.parseBoolean(activatorConfig.getElementsByTagName("final").item(0).getTextContent());
				float left = Float.parseFloat(activatorConfig.getElementsByTagName("left").item(0).getTextContent());
				float right = Float.parseFloat(activatorConfig.getElementsByTagName("right").item(0).getTextContent());
				
				tempPosition.set(positionX, -200, -200);
				tempDimensions.set(dimensionsX, worldDimensions.y+400, worldDimensions.z+400);
				TrgActivate activate = new TrgActivate(this, id, tempPosition, tempDimensions, isFinal, mBoundaryLeft, left, mBoundaryRight, right);
				
				String[] enemiesList = ((Element)activatorConfig.getElementsByTagName("enemies").item(0)).getTextContent().split(",");
				
				for (String index : enemiesList) {
					EntEnemy current = (EntEnemy) mEntities.get(Integer.parseInt(index));
					activate.addEnemy(current);
					activate.addObservedEntity(mPlayer);
				}
				
				mEntities.put(activate.getId(), activate);
			}
			
			inputStream.close();
		}catch(Exception e){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("arquivo ");
			stringBuilder.append("xml/map"+mId+"_config");
			stringBuilder.append(" n�o encontrado!");
			
			Log.d("Erro", stringBuilder.toString());
		}
		
	}
	
	public void movePlayer(float x, float y, float z)
	{
		mPlayer.move(x, y, z);
	}
	
	@Override
	public void resetMap() {
		mEntities.clear();
		setup();
	}
	
	public ArrayList<SGEntity> getEntitiesOrderedByZ(ArrayList<SGEntity> list){
		list.clear();
		for (int i = 0; i < mEntities.size(); i++)
		{
			boolean added = false;
			
			SGEntity currentEntity = mEntities.get(mEntities.keyAt(i));
			
			for (int j = 0; !added && j < list.size(); j++) {
				if(currentEntity.getPosition().z > list.get(j).getPosition().z){
					list.add(j, currentEntity);
					added = true;
				}
			}
			if(!added){
				list.add(currentEntity);
			}
		}
		
		return list;
	}
	
	public float getLeftBoundery(){ return mBoundaryLeft.getPosition().x + mBoundaryLeft.getDimensions().x; }
	public float getRightBoundery(){ return mBoundaryRight.getPosition().x; }
	public EntPlayer getPlayer(){ return mPlayer; }
	public String getMusic(){ return mMusic; }
	public String getImage(){ return mImage; }
}
