package nl.quintor.studybits.repository;

import nl.quintor.studybits.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    Transcript getTranscriptByNonce(String nonce);
}
