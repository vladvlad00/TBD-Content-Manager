package ro.uaic.info.contentmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/*
id
type -> string (header,text,image)
content -> string
 */
@Entity
public class ContentBlock {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer id;

    private String type;

    @Lob
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "courseId")
    @MapsId("courseId")
    Course course;

    public ContentBlock()
    {
    }

    public ContentBlock(String type, String content, Course course)
    {
        this.type = type;
        this.content = content;
        this.course = course;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }
}
