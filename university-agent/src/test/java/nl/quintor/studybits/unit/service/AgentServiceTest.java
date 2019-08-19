package nl.quintor.studybits.unit.service;

import net.minidev.json.JSONObject;
import nl.quintor.studybits.entity.CredentialDefinitionType;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import nl.quintor.studybits.indy.wrapper.Issuer;
import nl.quintor.studybits.indy.wrapper.Verifier;
import nl.quintor.studybits.indy.wrapper.dto.*;
import nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelope;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelopeCodec;
import nl.quintor.studybits.indy.wrapper.util.JSONUtil;
import nl.quintor.studybits.messages.StudyBitsMessageTypes;
import nl.quintor.studybits.service.*;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class AgentServiceTest {

    @TestConfiguration
    static class AgentServiceTestConfiguration {

        @Bean
        public AgentService agentService() {
            return new AgentService();
        }
    }
    @Autowired
    private AgentService sut;

    @MockBean
    Issuer universityIssuer;

    @MockBean
    Verifier universityVerifier;

    @MockBean
    StudentService studentService;

    @MockBean
    TranscriptService transcriptService;

    @MockBean
    ParserFactory parserFactory;

    @MockBean
    CredentialOffer credentialOffer;

    @MockBean
    CredentialDefinitionService credentialDefinitionService;

    @MockBean
    CredentialDefinitionType credentialDefinitionType;

    @MockBean
    ExchangePositionService exchangePositionService;

    @MockBean
    MessageEnvelopeCodec messageEnvelopeCodec;

    @MockBean
    MessageEnvelope messageEnvelope;

    @MockBean
    HashMap<CredentialDefinitionType.Type, String> credentialDefinitionIds;

    private Student student;
    private CredentialOfferList credentialOfferList;

    @BeforeClass
    public static void setUpClass() {
        StudyBitsMessageTypes.init();
    }

    @Before
    public void setUp() throws Exception {
        student = new Student();
        student.setStudentId("0");
        student.setFirstName("Tijn");
        student.setLastName("van den Bergh");
        student.setPassword("0000");
        student.setStudentDid("a1b2c3d4e5f6");
        student.setMyDid("b1c2d3e4f5g6");
        Transcript t1 = new Transcript(1, "6", "enrolled", "HBO-bachelor", "PROPEDEUSE", false, "10-12-2018", 240, "1111111", student, new ArrayList<>());
        ArrayList<Transcript> transcriptList = new ArrayList();
        transcriptList.add(t1);
        student.setTranscriptList(transcriptList);


        credentialDefinitionIds.put(CredentialDefinitionType.Type.PROPEDEUSE, "101010");
        credentialDefinitionIds.put(CredentialDefinitionType.Type.TRANSCRIPT, "010101");

        CredentialOffer credentialOffer = new CredentialOffer(student.getMyDid(), "", credentialDefinitionIds.get(CredentialDefinitionType.Type.PROPEDEUSE), null, "123");
        credentialOfferList = new CredentialOfferList();
        credentialOfferList.addCredentialOffer(credentialOffer);

        CompletableFuture<CredentialOffer> f1 = CompletableFuture.completedFuture(credentialOffer);
        Mockito.when(studentService.getStudentByStudentDid(student.getStudentDid())).thenReturn(student);
 //       Mockito.when(credentialDefinitionService.getCredentialDefinitionIds().get(CredentialDefinitionType.Type.PROPEDEUSE)).thenReturn(credentialDefinitionIds.get(CredentialDefinitionType.Type.PROPEDEUSE));
      //  Mockito.when(credentialDefinitionService.getCredentialDefinitionIds().get(CredentialDefinitionType.Type.TRANSCRIPT)).thenReturn(credentialDefinitionIds.get(CredentialDefinitionType.Type.TRANSCRIPT));

        //  doReturn(credDefId).when(credentialDefinitionService).getCredentialDefinitionIds();
        Mockito.when(transcriptService.getTranscriptByNonce(any())).thenReturn(t1);
        Mockito.when(universityIssuer.createCredentialOffer(credentialDefinitionIds.get(CredentialDefinitionType.Type.PROPEDEUSE), student.getStudentDid())).thenReturn(f1);
        Mockito.when(messageEnvelopeCodec.encryptMessage(any(CredentialOfferList.class), any(), eq(student.getStudentDid()))).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test(expected = NotImplementedException.class)
    public void unsupportedMessageTypeTest() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", student.getStudentDid());
        json.put("type", "urn:indy:sov:agent:message_type:sovrin.org/connection/1.0/verinym");
        json.put("message", "");
        MessageEnvelope messageEnvelope = MessageEnvelope.parseFromString(json.toString(), IndyMessageTypes.VERINYM);
        sut.processMessage(messageEnvelope);
    }

//    @Test
//    public void getCredentialOffersTest() throws Exception{
//        JSONObject json = new JSONObject();
//        json.put("id", student.getStudentDid());
//        json.put("type", "urn:indy:sov:agent:message_type:sovrin.org/get/1.0/get_request");
//        json.put("message", "");
//        MessageEnvelope messageEnvelope = MessageEnvelope.parseFromString(json.toString(), IndyMessageTypes.GET_REQUEST);
//        Mockito.when(messageEnvelopeCodec.decryptMessage(any())).thenReturn(CompletableFuture.completedFuture(IndyMessageTypes.CREDENTIAL_OFFERS.getURN()));
//        sut.processMessage(messageEnvelope);
//
//        //assert
//        Mockito.verify(messageEnvelopeCodec).encryptMessage(credentialOfferList, IndyMessageTypes.CREDENTIAL_OFFERS, student.getStudentDid());
//
//    }

    @Test
    public void getCredentialOffersIsProvenTest() throws Exception{
        student.getTranscriptList().get(0).setProven(true);
        sut.getCredentialOffers(student.getStudentDid());
        //assert
        Mockito.verify(messageEnvelopeCodec).encryptMessage(any(CredentialOfferList.class), eq(IndyMessageTypes.CREDENTIAL_OFFERS), eq(student.getStudentDid()));
    }

    @Test
    public void getCredentialOffersNoTranscriptTest() throws Exception{
        student.setTranscriptList(null);
        sut.getCredentialOffers(student.getStudentDid());
        //assert
        Mockito.verify(messageEnvelopeCodec).encryptMessage(any(CredentialOfferList.class), eq(IndyMessageTypes.CREDENTIAL_OFFERS), eq(student.getStudentDid()));
    }

    @Test
    public void handleCredentialRequestTest() throws Exception {
        CredentialRequest credentialRequest = new CredentialRequest("", "", credentialOfferList.getCredentialOffers().get(0));
        Mockito.when(messageEnvelopeCodec.decryptMessage(any())).thenReturn(CompletableFuture.completedFuture(credentialRequest));
        Mockito.when(studentService.getStudentByStudentDid(student.getStudentDid())).thenReturn(student);
        Mockito.doNothing().when(studentService).proveTranscript(student.getStudentDid(), "1111111");
        CredentialWithRequest credentialWithRequest = new CredentialWithRequest();
        Mockito.when(universityIssuer.createCredential(eq(credentialRequest), anyMap())).thenReturn(CompletableFuture.completedFuture(credentialWithRequest));
        Mockito.when(messageEnvelopeCodec.encryptMessage(credentialWithRequest, IndyMessageTypes.CREDENTIAL, student.getStudentDid())).thenReturn(CompletableFuture.completedFuture(null));
        JSONObject json = new JSONObject();
        json.put("id", student.getStudentDid());
        json.put("type", "urn:indy:sov:agent:message_type:sovrin.org/credential/1.0/credential_request");
        json.put("message", "");
        MessageEnvelope messageEnvelope = MessageEnvelope.parseFromString(json.toString(), IndyMessageTypes.CREDENTIAL_REQUEST);
        sut.processMessage(messageEnvelope);
        //assert
        Mockito.verify(messageEnvelopeCodec).encryptMessage(credentialWithRequest, IndyMessageTypes.CREDENTIAL, student.getStudentDid());
    }

//    @Test
//    public void handleProofTest() {
//        Mockito.when(studentService.getStudentByStudentDid(student.getStudentDid())).thenReturn(student);
//        Student studentTest = studentService.getStudentByStudentDid(student.getStudentDid());
//        try {
//            ProofRequest proofRequest = JSONUtil.mapper.readValue(student.getProofRequest(), ProofRequest.class);
//            Proof proof =
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }



}
