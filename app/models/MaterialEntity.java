package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "materials", schema = "public", catalog = "postgres")
public class MaterialEntity {
    private int id;
    private String title;
    private Timestamp createdat;
    private Timestamp updatedat;
    private int lessonid;
    private LessonEntity lessonByLessonid;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = true, length = -1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "createdat", nullable = true)
    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    @Basic
    @Column(name = "updatedat", nullable = true)
    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    @Basic
    @Column(name = "lessonid", nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "lessonid", referencedColumnName = "id", nullable = false)
    public LessonEntity getLessonByLessonid() {
        return lessonByLessonid;
    }

    public void setLessonByLessonid(LessonEntity lessonByLessonid) {
        this.lessonByLessonid = lessonByLessonid;
    }
}
