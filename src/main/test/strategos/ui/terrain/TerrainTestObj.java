package terrain;

import strategos.util.GameObject;
import strategos.util.GameObjectVisitor;
import strategos.terrain.Terrain;

public class TerrainTestObj implements Terrain, GameObject {
    @Override
    public void accept(GameObjectVisitor gameObjectVisitor) {

    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
