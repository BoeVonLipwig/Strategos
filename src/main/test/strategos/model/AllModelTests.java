package strategos.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import strategos.hexgrid.HexGridTests;
import strategos.model.external.ExternalTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExternalTests.class,
        HexGridTests.class,
        ModelTests.class
})
public class AllModelTests {
}
