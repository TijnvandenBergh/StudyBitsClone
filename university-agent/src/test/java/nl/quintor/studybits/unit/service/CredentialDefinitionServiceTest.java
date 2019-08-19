package nl.quintor.studybits.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.quintor.studybits.entity.CredentialDefinitionType;
import nl.quintor.studybits.indy.wrapper.Issuer;
import nl.quintor.studybits.service.AgentService;
import nl.quintor.studybits.service.CredentialDefinitionService;
import org.hyperledger.indy.sdk.IndyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
public class CredentialDefinitionServiceTest {

    @TestConfiguration
    static class CredentialDefinitionServiceTestConfiguration {

        @Bean
        public CredentialDefinitionService credentialDefinitionService() {
            return new CredentialDefinitionService();
        }
    }
    @Autowired
    CredentialDefinitionService sut;

    @MockBean
    private HashMap<CredentialDefinitionType.Type, String> credentialDefinitionIds;


    @MockBean
    private Issuer universityIssuer;



    @Test
    public void testCredentialDefinitionService() {
        new CredentialDefinitionService();
    }

    @Test
    public void testPropedeuse() throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
         String credDef =  sut.createCredentialDefinition("10101", "propedeuse");
         Assert.assertNotNull(credDef);
    }
}
