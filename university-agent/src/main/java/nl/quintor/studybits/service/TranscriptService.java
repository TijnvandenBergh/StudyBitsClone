package nl.quintor.studybits.service;


import nl.quintor.studybits.entity.Student;
import nl.quintor.studybits.entity.Transcript;
import nl.quintor.studybits.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class TranscriptService {

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Transactional
    public Transcript getTranscriptByNonce(String nonce) {
        return transcriptRepository.getTranscriptByNonce(nonce);
    }

    @Transactional
    public void saveTranscript(Transcript transcript) {
        transcriptRepository.saveAndFlush(transcript);
    }
}

