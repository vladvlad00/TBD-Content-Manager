package ro.uaic.info.contentmanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;


@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String pageTitle;

    @ManyToOne
    @JoinColumn(name="subjectId")
    Subject subject;

    @OneToMany(mappedBy = "course")
    @OrderColumn(name = "position")
    List<ContentBlock> courseContentBlocks;


    public Course() {
    }

    public Course(Integer id, String name, String pageTitle) {
        this.id = id;
        this.name = name;
        this.pageTitle = pageTitle;
    }

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

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;

    public List<ContentBlock> getCourseContentBlocks()
    {
        return courseContentBlocks;
    }

    public void setCourseContentBlocks(List<ContentBlock> courseContentBlocks)
    {
        this.courseContentBlocks = courseContentBlocks;
    }
}
