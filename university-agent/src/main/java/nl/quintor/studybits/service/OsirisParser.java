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
        JSONArray jsonArray = jsonObject.getJSONArray("inschrijvingen");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject enrollments = jsonArray.getJSONObject(i);
            Transcript fullTranscript = parseTranscript(enrollments.toString());
            fullTranscript.setStudent(currentStudent);
            log.debug("Diploma " + fullTranscript.getStudent().getFirstName());
            enrollments.getJSONArray("fases");
            retrievedTranscripts.add(fullTranscript);
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
        log.debug("diploma behaald: " + finished);
            transcript.setTranscriptName("PROPEDEUSE");
            transcript.setProven(false);
            transcript.setDegree("8");
            transcript.setStatus("enrolled");;
        return transcript;
    }


    @Override
    public Transcript parseFaseTranscript(String data) {
        return null;
    }
}
