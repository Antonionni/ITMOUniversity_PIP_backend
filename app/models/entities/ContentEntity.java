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
    private int id;
    /**
     *  name of content
     */
    private String content;
    /**
     * reference to {@link MaterialEntity}
     */
    //private MaterialEntity attachedToMaterial;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Enum of content type
     */
    private ContentType contentType;

    @Basic
    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Enumerated
    @Column(name = "contenttype", nullable = false)
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
