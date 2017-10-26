package terrain;

import strategos.util.GameObject;
import strategos.util.GameObjectVisitor;
import strategos.terrain.Forest;

public class ForestTestObj extends TerrainTestObj implements Forest, GameObject {
    @Override
    public void accept(GameObjectVisitor gameObjectVisitor) {
        gameObjectVisitor.visit(this);
    }
}
