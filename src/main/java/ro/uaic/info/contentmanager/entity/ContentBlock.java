package ro.uaic.info.contentmanager.entity;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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





}
