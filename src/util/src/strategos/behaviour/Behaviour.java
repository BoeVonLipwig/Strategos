package strategos.behaviour;


import strategos.MapLocation;
import strategos.units.*;


public interface Behaviour {

    MapLocation getPosition();

    void setPosition(MapLocation position);

    void turnTick();

    void wary();

    void entrench();

    void charge();

    boolean move();

    int attack(Unit enemy);

    int defend(Unit enemy);

    int getStrength();

    int getToughness();

}
