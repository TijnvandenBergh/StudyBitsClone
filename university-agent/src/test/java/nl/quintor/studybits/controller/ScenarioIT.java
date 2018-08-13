package nl.quintor.studybits.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.TextNode;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.specification.RequestSpecification;
import nl.quintor.studybits.indy.wrapper.*;
import nl.quintor.studybits.indy.wrapper.dto.*;
import nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelope;
import nl.quintor.studybits.indy.wrapper.util.AsyncUtil;
import nl.quintor.studybits.indy.wrapper.util.JSONUtil;
import nl.quintor.studybits.indy.wrapper.util.PoolUtils;
import nl.quintor.studybits.service.ExchangePositionService;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.indy.sdk.IndyException;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScenarioIT {
    static final String ENDPOINT_RUG = "http://localhost:8080";
    static final String ENDPOINT_GENT = "http://localhost:8081";
    static IndyPool indyPool;
    static IndyWallet stewardWallet;
    static IndyWallet studentWallet;
    static SessionFilter sessionFilter = new SessionFilter();

    static String schemaId;

    @BeforeClass
    public static void bootstrapBackend() throws InterruptedException, ExecutionException, IndyException, IOException {
        String poolName = PoolUtils.createPoolLedgerConfig(null, "testPool" + System.currentTimeMillis());
        indyPool = new IndyPool(poolName);
        stewardWallet = IndyWallet.create(indyPool, "steward_wallet" + System.currentTimeMillis(), "000000000000000000000000Steward1");
        TrustAnchor steward = new TrustAnchor(stewardWallet);

        Issuer university = new Issuer(IndyWallet.create(indyPool, "university_wallet" + System.currentTimeMillis(), StringUtils.leftPad("rug", 32, '0')));

        // Connecting newcomer with Steward
        String governmentConnectionRequest = steward.createConnectionRequest(university.getName(), "TRUST_ANCHOR").get().toJSON();

        AnoncryptedMessage newcomerConnectionResponse = university.acceptConnectionRequest(JSONUtil.mapper.readValue(governmentConnectionRequest, ConnectionRequest.class))
                .thenCompose(AsyncUtil.wrapException(university::anonEncrypt))
                .get();

        steward.anonDecrypt(newcomerConnectionResponse, ConnectionResponse.class)
                .thenCompose(AsyncUtil.wrapException(steward::acceptConnectionResponse)).get();

        // Create verinym
        AuthcryptedMessage verinym = university.authEncrypt(university.createVerinymRequest(JSONUtil.mapper.readValue(governmentConnectionRequest, ConnectionRequest.class)
                .getDid()))
                .get();

        steward.authDecrypt(verinym, Verinym.class)
                .thenCompose(AsyncUtil.wrapException(steward::acceptVerinymRequest)).get();


        Issuer stewardIssuer = new Issuer(stewardWallet);
        schemaId = stewardIssuer.createAndSendSchema("Transcript", "1.0", "degree", "status", "average").get();
        System.out.println(sessionFilter.getSessionId());
        givenCorrectHeaders(ENDPOINT_RUG)
                .when()
                .post("/bootstrap/credential_definition/{schemaId}", schemaId)
                .then()
                .assertThat().statusCode(200);

        studentWallet = IndyWallet.create(indyPool, "student_wallet" + System.currentTimeMillis(), null);
    }


    @Test
    public void test1_Connect() throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
        MessageEnvelope<ConnectionRequest> connectionRequestEnvelope = givenCorrectHeaders(ENDPOINT_RUG)
                .queryParam("student_id", "12345678")
                .post("/agent/login")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope.class);

        connectionRequestEnvelope.setIndyWallet(studentWallet);

        ConnectionRequest connectionRequest = connectionRequestEnvelope.getMessage();

        ConnectionResponse connectionResponse = studentWallet.acceptConnectionRequest(connectionRequest).get();

        MessageEnvelope connectionResponseEnvelope = MessageEnvelope.fromAnoncryptable(connectionResponse, IndyMessageTypes.CONNECTION_RESPONSE, studentWallet);

        MessageEnvelope connectionAcknowledgementEnvelope = givenCorrectHeaders(ENDPOINT_RUG)
                .body(connectionResponseEnvelope)
                .post("/agent/message")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope.class);

        assertThat(connectionAcknowledgementEnvelope.getType().getURN(), is(equalTo(IndyMessageTypes.CONNECTION_ACKNOWLEDGEMENT)));
        assertThat(connectionAcknowledgementEnvelope.getMessage(), is(equalTo("Rijksuniversiteit Groningen")));
    }

    @Test
    public void test2_obtainingCredential() throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
        MessageEnvelope<CredentialOffer>[] credentialOfferEnvelopes = givenCorrectHeaders(ENDPOINT_RUG)
                .get("/agent/credential_offer")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope[].class);

        assertThat(credentialOfferEnvelopes, arrayWithSize(equalTo(1)));

        credentialOfferEnvelopes[0].setIndyWallet(studentWallet);

        CredentialOffer credentialOffer = credentialOfferEnvelopes[0].getMessage();

        assertThat(credentialOffer.getSchemaId(), is(equalTo(schemaId)));

        Prover prover = new Prover(studentWallet, "master_secret_name");
        prover.init();

        CredentialRequest credentialRequest = prover.createCredentialRequest(credentialOffer).get();

        MessageEnvelope authcryptedCredentialRequestEnvelope = MessageEnvelope.fromAuthcryptable(credentialRequest, IndyMessageTypes.CREDENTIAL_REQUEST, studentWallet);

        MessageEnvelope<CredentialWithRequest> credentialEnvelope = givenCorrectHeaders(ENDPOINT_RUG)
                .body(authcryptedCredentialRequestEnvelope)
                .post("/agent/message")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope.class);

        assertThat(credentialEnvelope.getType().getURN(), is(equalTo(IndyMessageTypes.CREDENTIAL.getURN())));

        credentialEnvelope.setIndyWallet(prover);

        CredentialWithRequest credentialWithRequest = credentialEnvelope.getMessage();

        prover.storeCredential(credentialWithRequest).get();

        Credential credential = credentialWithRequest.getCredential();

        assertThat(credential.getValues().get("degree").get("raw").asText(), is(equalTo("Bachelor of Arts, Marketing")));
        assertThat(credential.getValues().get("average").get("raw").asText(), is(equalTo("8")));
        assertThat(credential.getValues().get("status").get("raw").asText(), is(equalTo("enrolled")));

        credentialOfferEnvelopes = givenCorrectHeaders(ENDPOINT_RUG)
                .get("/agent/credential_offer")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope[].class);

        assertThat(Arrays.asList(credentialOfferEnvelopes), hasSize(0));
    }

    @Test
    public void test3_ConnectGent() throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
        MessageEnvelope<ConnectionRequest> connectionRequestEnvelope = givenCorrectHeaders(ENDPOINT_GENT)
                .post("/agent/login")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope.class);

        ConnectionRequest connectionRequest = connectionRequestEnvelope.getMessage();

        ConnectionResponse connectionResponse = studentWallet.acceptConnectionRequest(connectionRequest).get();

        MessageEnvelope connectionResponseEnvelope = MessageEnvelope.fromAnoncryptable(connectionResponse, IndyMessageTypes.CONNECTION_RESPONSE, studentWallet);

        MessageEnvelope<String> connectionAcknowledgementEnvelope = givenCorrectHeaders(ENDPOINT_GENT)
                .body(connectionResponseEnvelope)
                .post("/agent/message")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope.class);

        connectionAcknowledgementEnvelope.setIndyWallet(studentWallet);

        assertThat(connectionAcknowledgementEnvelope.getType().getURN(), is(equalTo(IndyMessageTypes.CONNECTION_ACKNOWLEDGEMENT.getURN())));
        assertThat(connectionAcknowledgementEnvelope.getMessage(), is(equalTo("Universiteit Gent")));

        MessageEnvelope[] credentialOfferEnvelopes = givenCorrectHeaders(ENDPOINT_GENT)
                .get("/agent/credential_offer")
                .then()
                .assertThat().statusCode(200)
                .extract().as(MessageEnvelope[].class);

        assertThat(Arrays.asList(credentialOfferEnvelopes), hasSize(0));
    }

    @Test
    public void test4_getExchangePositionsAndApply() throws JsonProcessingException, IndyException, ExecutionException, InterruptedException {
        ExchangePositionService.ExchangePositionDto[] exchangePositions = givenCorrectHeaders(ENDPOINT_GENT)
                .get("/agent/exchange_position")
                .then()
                .assertThat().statusCode(200)
                .extract().as(ExchangePositionService.ExchangePositionDto[].class);

        assertThat(Arrays.asList(exchangePositions), hasSize(1));
        assertThat(exchangePositions[0].getName(), is(equalTo("MSc Marketing")));
        assertThat(exchangePositions[0].isFulfilled(), is(equalTo(false)));

        ProofRequest proofRequest = studentWallet.authDecrypt(exchangePositions[0].getAuthcryptedProofRequest(), ProofRequest.class).get();



        Prover prover = new Prover(studentWallet, "master_secret_name");
        Map<String, String> values = new HashMap<>();

        Proof proof = prover.fulfillProofRequest(proofRequest, values).get();

        MessageEnvelope proofEnvelope = MessageEnvelope.fromAuthcryptable(proof, IndyMessageTypes.PROOF, prover);

        givenCorrectHeaders(ENDPOINT_GENT)
                .body(proofEnvelope)
                .post("/agent/message")
                .then()
                .assertThat().statusCode(200);

        exchangePositions = givenCorrectHeaders(ENDPOINT_GENT)
                .get("/agent/exchange_position")
                .then()
                .assertThat().statusCode(200)
                .extract().as(ExchangePositionService.ExchangePositionDto[].class);

        assertThat(Arrays.asList(exchangePositions), hasSize(1));
        assertThat(exchangePositions[0].isFulfilled(), is(equalTo(true)));
    }

    static RequestSpecification givenCorrectHeaders(String endpoint) {
        return given()
                .baseUri(endpoint)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .filter(sessionFilter)
                .filter(new ResponseLoggingFilter());
    }
}