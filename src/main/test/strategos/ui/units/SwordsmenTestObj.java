package units;

import strategos.util.GameObject;
import strategos.util.GameObjectVisitor;
import strategos.model.UnitOwner;
import strategos.behaviour.Behaviour;
import strategos.units.Swordsmen;

public class SwordsmenTestObj extends UnitTestObj implements Swordsmen, GameObject {
    public SwordsmenTestObj(UnitOwner owner) {
        super(owner);
    }

    @Override
    public void setBehaviour(Behaviour behaviour) {

    }

    @Override
    public void accept(GameObjectVisitor gameObjectVisitor) {
        gameObjectVisitor.visit(this);
    }
}
