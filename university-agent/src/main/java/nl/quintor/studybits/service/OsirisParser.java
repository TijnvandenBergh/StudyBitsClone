package nl.quintor.studybits.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import nl.quintor.studybits.repository.StudentRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Slf4j
@Component
public class OsirisParser extends Parser {
    private static final String URL_PROGRESS = "http://my-json-server.typicode.com/tijn167/FakeJsonApi";
    private static final String STUDENT_BY_STUDENTID_ENDPOINT ="/student/{id}";

    @Autowired
    APIrequestService requestService;

    @Autowired
    private StudentService studentService;

    public OsirisParser(String name) {
        super(name, URL_PROGRESS);
    }

    @Override
    public String callDataSource(int id, String url, String endpoint) {
        String data = requestService.request(id, url, endpoint);
        log.debug(data);
        return data;
    }

    @Override
    public Student parseStudent(int studentid) {
        String response = callDataSource(studentid,URL_PROGRESS, STUDENT_BY_STUDENTID_ENDPOINT);
        String id = Integer.toString(studentid);
        Student currentStudent = null;
        log.debug("Studenten identiteit " + id);
        currentStudent = studentService.getStudentByStudentId(id);
        List<Transcript> retrievedTranscripts = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArrayEnrollments = jsonObject.getJSONArray("inschrijvingen");
        for(int i = 0; i < jsonArrayEnrollments.length(); i++) {
            JSONObject enrollment = jsonArrayEnrollments.getJSONObject(i);
            Transcript fullTranscript = parseTranscript(enrollment.toString());
            fullTranscript.setStudent(currentStudent);
            retrievedTranscripts.add(fullTranscript);
            JSONArray jsonArrayFases = enrollment.getJSONArray("fases");
            log.debug("Aantal fases: " + jsonArrayFases.length());
            for(int x = 0; x < jsonArrayFases.length(); x++) {
                JSONObject fase = jsonArrayFases.getJSONObject(x);
                Transcript faseTranscript = parseFaseTranscript(fase.toString());
            }
        }
        log.debug("Grootte van diploma" + retrievedTranscripts.size());
        currentStudent.setTranscriptList(retrievedTranscripts);
        studentService.saveStudent(currentStudent);
        return currentStudent;
    }

    @Override
    public Transcript parseTranscript(String data) {
        JSONObject enrollment = new JSONObject(data);
        Transcript transcript = new Transcript();

        boolean finished = (boolean) enrollment.get("judicium");
        if (finished == true) {
            transcript.setTranscriptName(enrollment.getString("name"));
            transcript.setProven(false);
            transcript.setDegree("9");
            transcript.setStatus(enrollment.getString("completionDate"));

        }
        return transcript;
    }


    @Override
    public Transcript parseFaseTranscript(String data) {
        JSONObject fase = new JSONObject(data);
        log.debug(fase.getString("faseCode"));
        return null;
    }
}
