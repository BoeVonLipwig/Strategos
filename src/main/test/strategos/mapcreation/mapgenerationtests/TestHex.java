package strategos.mapcreation.mapgenerationtests;

import strategos.mapcreation.mapgeneration.terrain.TerrainTile;
import strategos.terrain.Terrain;
import strategos.util.Paintable;

public class TestHex implements Paintable {
    private boolean isInPlayArea;
    private TerrainTile terrain;

    TestHex(boolean isInPlayArea) {
        this.isInPlayArea = isInPlayArea;
    }

    @Override
    public boolean isInPlayArea() {
        return isInPlayArea;
    }

    @Override
    public Terrain getTerrain() {
        return terrain;
    }

    String getTerrainName() {
        return isInPlayArea ? terrain.toString() : "";
    }

    void setTerrain(TerrainTile terrain) {
        if (terrain == null) {
            throw new NullPointerException();
        }
        if (isInPlayArea && this.terrain == null) {
            this.terrain = terrain;
        }
    }

    @Override
    public void setTerrain(Terrain t) {

    }
}
