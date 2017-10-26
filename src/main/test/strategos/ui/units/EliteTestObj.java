package units;

import strategos.util.GameObject;
import strategos.util.GameObjectVisitor;
import strategos.model.UnitOwner;
import strategos.behaviour.Behaviour;
import strategos.units.Elite;

public class EliteTestObj extends UnitTestObj implements Elite, GameObject {
    public EliteTestObj(UnitOwner owner) {
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
