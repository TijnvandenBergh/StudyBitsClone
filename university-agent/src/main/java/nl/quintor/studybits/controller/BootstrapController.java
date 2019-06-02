package nl.quintor.studybits.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Course;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import nl.quintor.studybits.repository.CourseRepository;
import nl.quintor.studybits.repository.ExchangePositionRepository;
import nl.quintor.studybits.repository.StudentRepository;
import nl.quintor.studybits.repository.TranscriptRepository;
import nl.quintor.studybits.service.CredentialDefinitionService;
import nl.quintor.studybits.service.ExchangePositionService;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.anoncreds.CredDefAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/bootstrap", produces = "application/json")
@Slf4j
public class BootstrapController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CredentialDefinitionService credentialDefinitionService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private ExchangePositionRepository exchangePositionRepository;

    @Autowired
    private ExchangePositionService exchangePositionService;


    @Value("${nl.quintor.studybits.university.name}")
    private String universityName;

    private String credDefId;

    @PostMapping("/credential_definition/{schemaId}")
    public String createCredentialDefinition(@PathVariable("schemaId") String schemaId) throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
        try {
            return credentialDefinitionService.createCredentialDefintion(schemaId);
        }
        catch (ExecutionException e) {
            // This is totally fine
            if (!(e.getCause() instanceof CredDefAlreadyExistsException)) {
                throw e;
            }
        }
        return null;
    }

    @Transactional
    @PostMapping("/create_student/{studentId}")
    public String createStudentPosition(@PathVariable("studentId") String studentId) throws JsonProcessingException {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName("Lisa");
        student.setLastName("Veren");
        student.setPassword(bCryptPasswordEncoder.encode("test1234"));
        student.setStudentDid(null);
//        List<Transcript> transcriptList = new ArrayList<>();
//        Transcript transcript =new Transcript();
//        transcript.setDegree("8");
//        transcript.setStatus("enrolled");
//        transcript.setStudent(student);
//        transcript.setProven(false);
//        transcript.setTest("BACHELOR");
//        transcriptList.add(transcript);
//        Transcript propedeuse = new Transcript();
//        propedeuse.setDegree("6");
//        propedeuse.setStatus("enrolled");
//        propedeuse.setStudent(student);
//        propedeuse.setProven(false);
//        propedeuse.setTest("PROPEDEUSE");
//        transcriptList.add(propedeuse);
//        student.setTranscriptList(transcriptList);
        studentRepository.saveAndFlush(student);

        return studentRepository.getStudentByStudentId(studentId).toString();
    }

    @PostMapping("/exchange_position/{credDefId}")
    public void createExchangePosition(@PathVariable("credDefId") String credDefId) throws JsonProcessingException {
        log.debug("Creating exchange postion {}", credDefId);
        exchangePositionService.createExchangePosition(credDefId);
        this.credDefId = credDefId;
    }

    @PostMapping("/reset")
    public void reset() throws JsonProcessingException {
        transcriptRepository.deleteAll();
        studentRepository.deleteAll();
        exchangePositionRepository.deleteAll();
        if (universityName.equals("Rijksuniversiteit Groningen")) {
            createStudentPosition("2102241");
        }
        if (credDefId  != null) {
            exchangePositionService.createExchangePosition(credDefId);
        }
    }


    @GetMapping("/ready")
    public boolean isReady() {
        return true;
    }
}
