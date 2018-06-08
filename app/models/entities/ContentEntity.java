package models.entities;

import enumerations.ContentType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "content", catalog = "postgres")
public class ContentEntity {
    /**
     * unique indentificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     *  name of content
     */
    @Basic
    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR")
    private String content;
    /**
     * reference to {@link MaterialEntity}
     */
    //private MaterialEntity attachedToMaterial;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Enum of content type
     */
    @Enumerated
    @Column(name = "contenttype", nullable = false)
    private ContentType contentType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentEntity that = (ContentEntity) o;
        return id == that.id &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content);
    }
}
