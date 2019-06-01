package nl.quintor.studybits.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;

@NoArgsConstructor
@Slf4j
public class OsirisParser extends Parser {
    private static final String URL_PROGRESS = "https://my-json-server.typicode.com/tijn167/FakseJsonApi";
    private static final String STUDENT_BY_STUDENTID_ENDPOINT ="/students/{id}";


    APIrequestService requestService = new APIrequestService();

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
        log.debug("ola");
        String response = callDataSource(studentid,URL_PROGRESS, STUDENT_BY_STUDENTID_ENDPOINT);
        log.debug("EWASAG: " + response );
        return "Tijn";
    }

    @Override
    public Transcript parseTranscript() {
        return null;
    }
}
