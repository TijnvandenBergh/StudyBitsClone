package nl.quintor.studybits.entity;

import io.micrometer.core.lang.Nullable;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String degree;

    @Column
    private String status;

    @Column
    private String transcriptName;

    @Column
    private boolean proven;

    @ManyToOne
    @JoinColumn
    private Student student;

    @OneToMany(mappedBy = "transcript", cascade =  CascadeType.ALL)
    @Nullable
    private List<Course> courses;
    
}

