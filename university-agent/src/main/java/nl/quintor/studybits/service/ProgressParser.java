package nl.quintor.studybits.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Slf4j
@Component
public class ProgressParser extends Parser {
    @Override
    public String callDataSource(int id, String url, String endpoint) {
        return null;
    }

    @Override
    public Student parseStudent(int studentid) {
        return null;
    }

    @Override
    public Transcript parseTranscript(String data) {
        return null;
    }

    @Override
    public Transcript parseFaseTranscript(String data) {
        return null;
    }
}
