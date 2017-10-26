package strategos.mapgenerator;

import strategos.util.Paintable;

public interface Generator {

    Paintable[][] populateMap(Paintable[][] hexMap, int seed);

    Paintable[][] populateMap(Paintable[][] hexMap);
}
