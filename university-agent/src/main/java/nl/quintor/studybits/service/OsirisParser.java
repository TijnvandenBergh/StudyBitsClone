package nl.quintor.studybits.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import nl.quintor.studybits.repository.StudentRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@Slf4j
public class OsirisParser extends Parser {
    private static final String URL_PROGRESS = "http://my-json-server.typicode.com/tijn167/FakeJsonApi";
    private static final String STUDENT_BY_STUDENTID_ENDPOINT ="/student/{id}";


    APIrequestService requestService = new APIrequestService();

    @Autowired
    StudentRepository studentRepository;

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
    public String parseStudent(int studentid) {
        String response = callDataSource(2102241,URL_PROGRESS, STUDENT_BY_STUDENTID_ENDPOINT);
        String id = studentid + "";
        //Student currentStudent = studentRepository.getStudentByStudentId(id);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("inschrijvingen");
        for(int i = 0; i < jsonArray.length(); i++) {
            log.debug("weet niet " + jsonArray.getJSONObject(i));
        }
        return "Tijn";
    }

    @Override
    public Transcript parseTranscript(String data) {
        return null;
    }
}
