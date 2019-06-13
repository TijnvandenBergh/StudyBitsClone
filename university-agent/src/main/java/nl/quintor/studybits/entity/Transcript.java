package nl.quintor.studybits.entity;

import io.micrometer.core.lang.Nullable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
    private String transcriptType;

    @Column
    private boolean proven;

    @Column
    private String receivedDate;

    @Column
    private int totalEC;

    @ManyToOne
    @JoinColumn
    private Student student;

    @OneToMany(mappedBy = "transcript", cascade =  CascadeType.ALL)
    @Nullable
    private List<Course> courses;

    public String coursesToString() {
    if (this.courses != null) {
        StringBuilder builder = new StringBuilder();
        for (Course crs: this.courses
             ) {
                builder.append( "\n" + "Coursecode: " +crs.getCourseCode() + "\n");
                builder.append("Coursename: " + crs.getCourseName() +"\n");
                builder.append("Grade: " + crs.getGrade() + "\n");
                log.debug("Builder" + builder.toString());
            }
        return builder.toString();
        }
        return "No courses and grades";
    }
    
}

