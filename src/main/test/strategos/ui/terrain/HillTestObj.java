package terrain;

import strategos.util.GameObject;
import strategos.util.GameObjectVisitor;
import strategos.terrain.Hill;

public class HillTestObj extends TerrainTestObj implements Hill, GameObject {
    @Override
    public void accept(GameObjectVisitor gameObjectVisitor) {
        gameObjectVisitor.visit(this);
    }
}
