package ro.uaic.info.contentmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="subject")
    Set<Course> subjectCourses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getSubjectCourses() {
        return subjectCourses;
    }

    public void setSubjectCourses(Set<Course> subjectCourses) {
        this.subjectCourses = subjectCourses;
    }
}
