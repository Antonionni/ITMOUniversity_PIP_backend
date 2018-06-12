package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "materials", catalog = "postgres")
public class MaterialEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * title of material
     */
    @Basic
    @Column(name = "title", nullable = true)
    private String title;
    /**
     * date and time when material created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Date createdAt;
    /**
     * date and time when materials updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Date updatedAt;
    /**
     * reference of {@link LessonEntity}
     */
    @ManyToOne
    @JoinColumn(name = "lessonid", referencedColumnName = "id", nullable = false)
    private LessonEntity lessons;
    /**
     * List of content which used in this materials
     */
    @ManyToMany
    @JoinTable(name = "materials_has_content",
            joinColumns = @JoinColumn(name = "materialid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "contentid", referencedColumnName = "id"))
    private Collection<ContentEntity> materialContents;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdat) {
        this.createdAt = createdat;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedat) {
        this.updatedAt = updatedat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialEntity that = (MaterialEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdAt, updatedAt);
    }

    public LessonEntity getLessons() {
        return lessons;
    }

    public void setLessons(LessonEntity lessonByLessonid) {
        this.lessons = lessonByLessonid;
    }

    public Collection<ContentEntity> getMaterialContents() {
        return materialContents;
    }

    public void setMaterialContents(Collection<ContentEntity> materialContent) {
        this.materialContents = materialContent;
    }
}
