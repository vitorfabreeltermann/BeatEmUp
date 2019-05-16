package example.beatemup.game;

import example.beatemup.engine.SGEntity;
import example.beatemup.engine.SGMap;
import example.beatemup.engine.SGPointF3D;

public class EntPlayer extends EntCharacter {

	public EntPlayer(SGMap map, int id, SGPointF3D position, SGPointF3D dimensions, String image,
			float speed, int hp, int str) {
		super(map, id, "player", position, dimensions, image, speed, hp, str);
	}

	@Override
	public void dealDamage() {
		for (int i = 0; i < ((MapBase)getMap()).getEntities().size(); i++) {
			SGEntity currentEntity = getMap().getEntities().get(getMap().getEntities().keyAt(i));
			if(currentEntity instanceof EntEnemy &&
			   _willAttack((EntCharacter) currentEntity)){
				((EntEnemy) currentEntity).receiveDamage(mStr);
			}
		}
	}
	
	@Override
	public void die() {
		((GameModel)getMap().getWorld()).setCurrentState(GameModel.STATE_GAME_OVER);
	}
}
