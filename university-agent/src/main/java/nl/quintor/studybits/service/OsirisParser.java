package nl.quintor.studybits.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Course;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
        //All the enrollments the student is listed for on this university
        JSONArray jsonArrayEnrollments = jsonObject.getJSONArray("inschrijvingen");
        //For-loop to walk through all the enrollments and parse the full enrollment-transcript
        for(int i = 0; i < jsonArrayEnrollments.length(); i++) {
            JSONObject enrollment = jsonArrayEnrollments.getJSONObject(i);
            Transcript fullTranscript = parseTranscript(enrollment.toString());
            fullTranscript.setStudent(currentStudent);
            retrievedTranscripts.add(fullTranscript);
            JSONArray jsonArrayFases = enrollment.getJSONArray("fases");
            log.debug("Aantal fases: " + jsonArrayFases.length());
            //Getting the different fases belonging to the enrollment of the student
            for(int x = 0; x < jsonArrayFases.length(); x++) {
                JSONObject fase = jsonArrayFases.getJSONObject(x);
                Transcript faseTranscript = parseFaseTranscript(fase.toString());
                if (faseTranscript != null) {
                    faseTranscript.setStudent(currentStudent);
                    retrievedTranscripts.add(faseTranscript);
                }
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
        if (finished) {
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
        Transcript transcript = new Transcript();
        log.debug(fase.getString("faseCode"));
        if(fase.getBoolean("hasTranscript")&& fase.getInt("totalEC") == fase.getInt("receivedEC")) {
            transcript.setTranscriptName(fase.getString("faseCode"));
            transcript.setProven(false);
            transcript.setStatus("enrolled");
            transcript.setDegree("8");
            String coursesData = fase.getJSONArray("grades").toString();
            List<Course> coursesTranscript = parseCourses(coursesData);
            transcript.setCourses(coursesTranscript);
            return transcript;
        }
        return null;
    }

    /**
     * This functions parses the courses belong to the Fase-JSONObject or the Enrollment-JSONObject
     * @param  data from the fase JSON which contains a JSONArray of courses
     * @return List<Courses> to assign to the Transcript-object
     */
    public List<Course> parseCourses(String data) {
        List<Course> course = new ArrayList<>();
        return course;
    }
}
