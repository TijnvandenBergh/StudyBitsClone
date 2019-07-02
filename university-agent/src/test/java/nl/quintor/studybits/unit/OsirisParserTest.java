package nl.quintor.studybits.unit;

import nl.quintor.studybits.service.OsirisParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
public class OsirisParserTest {

    @Test
    public void testOsirisParserNameAndUrl() {
        try {
            new OsirisParser("OsirisParser");
            new OsirisParser("Osiris");
            new OsirisParser("2ewr-fwe-13");
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        }
    }

}
