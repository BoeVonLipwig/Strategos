package strategos.ui.view;

import strategos.terrain.Terrain;
import strategos.units.*;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import static strategos.ui.config.Config.*;

/**
 * The type Grid component.
 */
public class GridComponent extends JComponent {

    private Terrain[][] terrain;
    private List<Unit> entities;
    private DrawEntity drawEntity = new DrawEntity();

    /**
     * Instantiates a new Grid component for drawing on.
     */
    public GridComponent() {
        setLayout(new BorderLayout());
        setPreferredSize(GRID_COMPONENT_SIZE);
    }

    /**
     * Gets grid panel.
     *
     * @return the grid
     */
    public JLayeredPane getGrid() {
        JLayeredPane p = new JLayeredPane();
        p.setLayout(new BorderLayout());
        p.setPreferredSize(GRID_COMPONENT_SIZE);
        return p;
    }

    protected void paintComponent(Graphics g) {
        paintHexGrid(g, Color.BLACK, terrain);
        paintUnits(g, entities);
    }

    private void paintHexGrid(Graphics g, Color c, Terrain[][] terrain) {
        g.setColor(Color.BLACK);
        for (int y = 0; y < terrain.length; y++) {
            for (int x = 0; x < terrain[0].length; x++) {
                if (y % 2 == 0) {
                    hexagon(g, getGridX(x), getGridY(y), Color.BLACK);
                } else {
                    hexagon(g, getGridX(x)+HEX_SIZE/2, getGridY(y), Color.BLACK);
                }
            }
        }
    }

    private void paintUnits(Graphics g, List<Unit> entities) {
        for (Unit unit : entities) {
            if (unit instanceof Archers) {
                drawEntity.draw((Archers)unit, g);
            } else if (unit instanceof Cavalry) {
                drawEntity.draw((Cavalry)unit, g);
            } else if (unit instanceof Elite) {
                drawEntity.draw((Elite)unit, g);
            } else if (unit instanceof Spearmen) {
                drawEntity.draw((Spearmen)unit, g);
            } else if (unit instanceof Swordsmen) {
                drawEntity.draw((Swordsmen)unit, g);
            }
         }
    }

    private void hexagon(Graphics g, int x, int y, Color c) {
        int nPoints = 6;
        int[] xPoints = {x, x+HEX_SIZE/2, x+HEX_SIZE, x+HEX_SIZE, x+HEX_SIZE/2, x, x};
        int[] yPoints = {y+HEX_SIZE/4, y, y+HEX_SIZE/4, y+HEX_SIZE/4*3, y+HEX_SIZE, y+HEX_SIZE/4*3, y+HEX_SIZE/4};
        g.setColor(c);
        g.drawPolygon(xPoints, yPoints, nPoints);
    }

    //Credit: https://www.redblobgames.com/grids/hexagons/#hex-to-pixel for logic of hex to pixels
    private int getGridY(int y) {
        return HEX_SIZE/2 * 3/2 * y;
    }

    private int getGridX(int x) {
        return x * HEX_SIZE + HEX_SIZE;
    }

    public void setEntities(List<Unit> entities) {
        this.entities = entities;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }
}
