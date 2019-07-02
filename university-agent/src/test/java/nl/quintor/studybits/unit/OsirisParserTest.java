package nl.quintor.studybits.unit;

import nl.quintor.studybits.service.APIrequestService;
import nl.quintor.studybits.service.OsirisParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OsirisParserTest {


    @Autowired
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

    @Test
    public void testOsirisParserWithoutParams() {
        try  {
            new OsirisParser();
        } catch( Exception e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void testCallingDataSource() {
        APIrequestService apIrequestService = mock(APIrequestService.class);
        doReturn("Test").when(apIrequestService).request(2102241, "", "");
        String result = apIrequestService.request(2102241, "", "");
        Assert.assertEquals("Test", result);
    }

}
