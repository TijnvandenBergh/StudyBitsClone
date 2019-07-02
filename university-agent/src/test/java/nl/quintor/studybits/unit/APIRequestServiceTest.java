package nl.quintor.studybits.unit;

import nl.quintor.studybits.service.APIrequestService;
import nl.quintor.studybits.service.ParserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
public class APIRequestServiceTest {

//    @TestConfiguration
//    static class RequestServiceContextConfiguration {
//        @Qualifier("TestBean")
//        @Bean
//        public APIrequestService apiRequestService() {
//            return new APIrequestService();
//        }
//    }
//    @Qualifier("TestBean")
//    @Autowired
//    APIrequestService apIrequestService;

    @MockBean
    private RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        RestGatewaySupport gatewaySupport = new RestGatewaySupport();
        gatewaySupport.setRestTemplate(restTemplate);
        mockRestServiceServer = MockRestServiceServer.createServer(gatewaySupport);
    }

    @Test
    public void testGetResponseWhenRequest() {
//        mockRestServiceServer.expect(once(), requestTo("https://my-json-server.typicode.com/tijn167/fakejsonapi/student/2102241"))
//        .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
//
//        String result = apIrequestService.request(2102241, "https://my-json-server.typicode.com/tijn167/fakejsonapi", "/student" );
//        System.out.println(result);
//        mockRestServiceServer.verify();
//        assertEquals("{student: 210221}", result);
    }

}
