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
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn
    private Transcript transcript;
}
