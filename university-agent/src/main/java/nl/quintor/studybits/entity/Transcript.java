package nl.quintor.studybits.entity;

import lombok.*;

import javax.persistence.*;

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
    
}

