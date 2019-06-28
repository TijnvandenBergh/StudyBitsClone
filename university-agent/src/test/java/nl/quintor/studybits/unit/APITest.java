package nl.quintor.studybits.unit;

import io.restassured.http.ContentType;
import nl.quintor.studybits.service.APIrequestService;
import org.junit.Test;

import static io.restassured.RestAssured.when;


@Run
public class APITest {

    APIrequestService apIrequestService = new APIrequestService();

    /**
     * Method to test if a non-existing student returns 404
     */
    @Test
    public void notExistingStudentTest() {
        when().
                get("http://my-json-server.typicode.com/tijn167/FakeJsonApi/student/123455555555").
                then().
                    statusCode(404);
    }

    @Test
    public void ExistingStudentTest() {
        when().
                get("http://my-json-server.typicode.com/tijn167/FakeJsonApi/student/2102241").
                then().
                statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void BadInputTest() {
        when().
                get("http://my-json-server.typicode.com/tijn167/FakeJsonApi/students/@@@e1337@@@@eeL33T").
                then().
                statusCode(404);
    }
}

