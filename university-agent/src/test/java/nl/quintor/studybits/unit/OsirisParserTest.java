package nl.quintor.studybits.unit;

import nl.quintor.studybits.service.APIrequestService;
import nl.quintor.studybits.service.OsirisParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OsirisParserTest {

    @Mock
    APIrequestService apIrequestService;

    @InjectMocks
    OsirisParser osirisParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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
      when(apIrequestService.request(1,"","")).thenReturn("TestCall");
      Assert.assertEquals("TestCall", osirisParser.callDataSource(1, "", ""));
    }

    @Test
    public void parseStudent() {
        when(osirisParser.callDataSource(1, "", "")).thenReturn("TestStudent");

    }

}
