package nl.quintor.studybits.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.quintor.studybits.entity.CredentialDefinitionType;
import nl.quintor.studybits.indy.wrapper.Issuer;
import nl.quintor.studybits.service.AgentService;
import nl.quintor.studybits.service.CredentialDefinitionService;
import org.hyperledger.indy.sdk.IndyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

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

    private String credDefId;
    private String wrongRequest;

    @Before
    public void setUp() {
        credDefId = "testCASEID";
        wrongRequest = "Bad request";

    }

    @Test
    public void testCredentialDefinitionService() {
        new CredentialDefinitionService();
    }

    @Test
    public void createCredentialDefintionPropedeuseTest() throws JsonProcessingException, IndyException, ExecutionException, InterruptedException {

        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(credDefId);
        Mockito.when(universityIssuer.defineCredential(anyString())).thenReturn(future);
        String cred = sut.createCredentialDefinition("ShemaId", "PROPEDEUSE");
        assertThat(cred).isEqualTo(credDefId);
    }

    @Test
    public void testPropedeuseFalse() throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
         String credDef =  sut.createCredentialDefinition("10101", "propedeuse");
         assertThat(credDef).isEqualTo(wrongRequest);
    }

    @Test
    public void createCredentialDefintionTranscriptTest() throws JsonProcessingException, IndyException, ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(credDefId);
        Mockito.when(universityIssuer.defineCredential(anyString())).thenReturn(future);
        String cred = sut.createCredentialDefinition("ShemaId", "TRANSCRIPT");
        assertThat(cred).isEqualTo(credDefId);
    }
}
