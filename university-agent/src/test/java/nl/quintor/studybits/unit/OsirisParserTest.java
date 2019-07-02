package nl.quintor.studybits.unit;

import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.service.APIrequestService;
import nl.quintor.studybits.service.OsirisParser;
import nl.quintor.studybits.service.StudentService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OsirisParserTest {

    @Mock
    APIrequestService apIrequestService;
    @Mock
    StudentService studentService;

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
    public void parseStudent()  {
        when(apIrequestService.request(anyInt(), anyString(), anyString())).thenReturn(TestStringClass.jsonTestString);
        when(studentService.getStudentByStudentId("1")).thenReturn(new Student());
        Assert.assertEquals(Student.class, osirisParser.parseStudent(1).getClass());
    }

}
