package nl.quintor.studybits.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "student_table_id")
    private long id;

    @Column(unique = true)
    private String studentId;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String studentDid;

    @Lob
    private String proofRequest;

    @OneToOne
    private ExchangePosition exchangePosition;

    @Column
    private String myDid;

    @OneToMany(mappedBy = "student", cascade =  CascadeType.ALL)
    private List<Transcript> transcriptList;

    public boolean hasDid() {
        if(this.getStudentDid() != null){
            return true;
        }

        return false;
    }
}
