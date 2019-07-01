package nl.quintor.studybits.unit;

import nl.quintor.studybits.service.OsirisParser;
import nl.quintor.studybits.service.Parser;
import nl.quintor.studybits.service.ParserFactory;
import nl.quintor.studybits.service.ProgressParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
public class ParsorFactoryTest {

    @TestConfiguration
    static class ParserFactoryContextConfiguration {
        @Bean
        public ParserFactory parserFactory() {
            return new ParserFactory();
        }
    }

    @Autowired
    ParserFactory parserFactory;

    @MockBean
    OsirisParser osirisParser;

    @MockBean
    ProgressParser progressParser;

    @Test
    public void testCreateOsirisServiceSuccesFull() {
        Parser serv = parserFactory.getParser("Osiris");
        assertThat(serv, instanceOf(OsirisParser.class));
    }

//    @Test
//    public void testCreateProgressServiceSuccesFull()  {
//        Parser serv = parserFactory.getParser("Progress");
//        assertThat(serv, instanceOf(ProgressParser.class));
//    }

    @Test
    public void testCreateProgressServiceUnSuccessFull() {
        Parser serv = parserFactory.getParser("Progress");
        assertThat(serv, is(not(instanceOf(OsirisParser.class))));
    }

    @Test
    public void testCreateWhenStringIsNotCorrect() {
        Parser serv = parserFactory.getParser("0$iri$");
        assertThat(serv, is(nullValue()));
    }

    @Test
    public void testCreateWhenStringIsNull() {
        Parser serv = parserFactory.getParser("");
        assertThat(serv,  is(nullValue()));
    }
}