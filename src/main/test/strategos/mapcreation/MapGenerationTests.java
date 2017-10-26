package strategos.mapcreation;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import strategos.mapcreation.mapgenerationtests.MapTester;
import strategos.mapcreation.noisegenerationtests.NoiseTester;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NoiseTester.class,
        MapTester.class})
public class MapGenerationTests {
//Do the needful
}
