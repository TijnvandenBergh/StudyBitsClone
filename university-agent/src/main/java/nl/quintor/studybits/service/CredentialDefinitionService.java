package nl.quintor.studybits.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import nl.quintor.studybits.entity.CredentialDefinitionType;
import nl.quintor.studybits.indy.wrapper.Issuer;
import org.hyperledger.indy.sdk.IndyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Component
public class CredentialDefinitionService {
    @Getter
    private HashMap<CredentialDefinitionType.Type, String> credentialDefinitionIds;
//
//    @Getter
//    private String schemaId;

    @Autowired
    private Issuer universityIssuer;

    @Autowired
    public CredentialDefinitionService() {
        credentialDefinitionIds = new HashMap<>();
    }


    public String createCredentialDefinition(String schemaId, String credentialDefinitionType) throws JsonProcessingException, IndyException, ExecutionException, InterruptedException {

       // String credentialDefinitionId = universityIssuer.defineCredential(schemaId).get();
        if(credentialDefinitionType.contains("TRANSCRIPT")) {
            String credentialDefinitionId = universityIssuer.defineCredential(schemaId).get();
            credentialDefinitionIds.put(CredentialDefinitionType.Type.TRANSCRIPT, credentialDefinitionId);
            return credentialDefinitionId;
        }
        if(credentialDefinitionType.contains("PROPEDEUSE")) {
            String credentialDefinitionId = universityIssuer.defineCredential(schemaId).get();
            credentialDefinitionIds.put(CredentialDefinitionType.Type.PROPEDEUSE, credentialDefinitionId);
            return credentialDefinitionId;
        }

        return "Bad request";
    }

    public String getCredentialDefinitionId(CredentialDefinitionType.Type type) {
        return credentialDefinitionIds.get(type);
    }
}
