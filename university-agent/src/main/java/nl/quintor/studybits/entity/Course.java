package nl.quintor.studybits.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String courseName;

    @Column
    private String courseCode;

    @Column
    private double grade;

    @Column
    private double receivedEC;

    @Column
    private double courseEC;

    @ManyToOne
    @JoinColumn
    private Transcript transcript;
}
