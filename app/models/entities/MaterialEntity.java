package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
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
    private Timestamp createdat;
    /**
     * date and time when materials updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedat;
    /**
     * reference to ID of leson
     */
    @Basic
    @Column(name = "lessonid", nullable = false)
    private int lessonid;
    /**
     * reference of {@link LessonEntity}
     */
    @ManyToOne
    @JoinColumn(name = "lessonid", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private LessonEntity lessonByLessonid;
    /**
     * List of content which used in this materials
     */
    @ManyToMany
    @JoinTable(name = "materials_has_content",
            joinColumns = @JoinColumn(name = "materialid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "contentid", referencedColumnName = "id"))
    private Collection<ContentEntity> materialContent;

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

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    public int getLessonid() {
        return lessonid;
    }

    public void setLessonid(int lessonid) {
        this.lessonid = lessonid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialEntity that = (MaterialEntity) o;
        return id == that.id &&
                lessonid == that.lessonid &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdat, updatedat, lessonid);
    }

    public LessonEntity getLessonByLessonid() {
        return lessonByLessonid;
    }

    public void setLessonByLessonid(LessonEntity lessonByLessonid) {
        this.lessonByLessonid = lessonByLessonid;
    }

    public Collection<ContentEntity> getMaterialContent() {
        return materialContent;
    }

    public void setMaterialContent(Collection<ContentEntity> materialContent) {
        this.materialContent = materialContent;
    }
}
