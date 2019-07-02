package nl.quintor.studybits.service;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Course;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

//
//    /**
//     *
//     * @param id the student id
//     * @param url the base-url of the endpoint
//     * @param endpoint the endpoint to call
//     * @return the JSON string with the data.
//     */
//    @Override
//    public String callDataSource(int id, String url, String endpoint) {
//        String data = requestService.request(id, url, endpoint);
//        return data;
//    }

    @Override
    public Student parseStudent(int studentid) {
        String response = requestService.request(studentid,URL_PROGRESS, STUDENT_BY_STUDENTID_ENDPOINT);
        log.debug(response);
        String id = Integer.toString(studentid);
        Student currentStudent = null;
        log.debug("Studenten identiteit " + id);
        currentStudent = studentService.getStudentByStudentId(id);
        List<Transcript> retrievedTranscripts = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            //All the enrollments the student is listed for on this university
            JSONArray jsonArrayEnrollments = jsonObject.getJSONArray("inschrijvingen");
            //For-loop to walk through all the enrollments and parse the full enrollment-transcript
            for (int i = 0; i < jsonArrayEnrollments.length(); i++) {
                JSONObject enrollment = jsonArrayEnrollments.getJSONObject(i);
                Transcript fullTranscript = parseTranscript(enrollment.toString());
                fullTranscript.setStudent(currentStudent);
                retrievedTranscripts.add(fullTranscript);
                JSONArray jsonArrayFases = enrollment.getJSONArray("fases");
                log.debug("Aantal fases: " + jsonArrayFases.length());
                //Getting the different fases belonging to the enrollment of the student
                for (int x = 0; x < jsonArrayFases.length(); x++) {
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
        } catch (JSONException ex) {
            log.debug("JSON EXCEPTION TIJDENS PARSING");
        }
        return currentStudent;
    }

    /**
     *
     * @param data
     * @return
     */
    @Override
    public Transcript parseTranscript(String data) {
        JSONObject enrollment = new JSONObject(data);
        Transcript transcript = new Transcript();
        boolean finished = (boolean) enrollment.get("judicium");
        if (finished) {
            transcript.setTranscriptName(enrollment.getString("name"));
            transcript.setProven(false);
            transcript.setReceivedDate(enrollment.getString("completionDate"));
            transcript.setStatus(enrollment.getString("completionDate"));
            transcript.setTotalEC( enrollment.getInt("totalEC"));
            transcript.setTranscriptType(enrollment.getString("type"));
            List<Course> courseList = parseCourses(enrollment.getJSONArray("grades").toString());
            log.debug("Course in transcript" + courseList.size());
            transcript.setCourses(courseList);
            for (Course course: courseList
            ) {
                course.setTranscript(transcript);
            }

        }
        return transcript;
    }


    /**
     *
     * @param data from the transcript JSON which contains a JSONArray of courses
     * @return returns each Transcript to add to
     */
    @Override
    public Transcript parseFaseTranscript(String data) {
        //Parse the string data fase to a new JSONobject
        JSONObject fase = new JSONObject(data);
        //New transcript to fill
        Transcript transcript = new Transcript();
        log.debug(fase.getString("faseCode"));
        //Statement to check if the fase does have a transcript and a check if the student received EC matches total of transcript
        if(fase.getBoolean("hasTranscript")&& fase.getInt("totalEC") == fase.getInt("receivedEC")) {
            //Filling the transcript object by parsing the JSON.
            transcript.setTranscriptName(fase.getString("faseCode"));
            transcript.setProven(false);
            transcript.setStatus("enrolled");
            transcript.setDegree("8");
            transcript.setTranscriptType(fase.getString("type"));
            transcript.setReceivedDate(fase.getString("completionDate"));
            //Getting the courses to fill the transcripts list of courses
            String coursesData = fase.getJSONArray("grades").toString();
            List<Course> coursesTranscript = parseCourses(coursesData);
            transcript.setCourses(coursesTranscript);
            //For every course set the Transcript for the right relationship in db
            for (Course course: coursesTranscript
                 ) {
                course.setTranscript(transcript);
            }
            return transcript;
        }
        return null;
    }

    /**
     * This functions parses the courses belong to the Fase-JSONObject or the Enrollment-JSONObject
     * @param  data from the transcript JSON which contains a JSONArray of courses
     * @return List<Courses> to assign to the Transcript-object
     */
    public List<Course> parseCourses(String data) {
        List<Course> courseList = new ArrayList<>();
        JSONArray courses = new JSONArray(data);
        for(int y = 0; y < courses.length(); y++) {
            JSONObject course = courses.getJSONObject(y);
            Course tempCourse = new Course();
            tempCourse.setCourseCode(course.getString("courseCode"));
            tempCourse.setCourseName(course.getString("courseName"));
            tempCourse.setGrade(course.getDouble("Grade"));
            courseList.add(tempCourse);

        }
        return courseList;
    }
}
