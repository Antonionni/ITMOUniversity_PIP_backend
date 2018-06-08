package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "passages_has_answers", catalog = "postgres")
public class PassageHasAnswersEntity {
    /**
     * uniqe identificator
     */
    private Integer id;
    /**
     * date and time when
     */
    private Timestamp passagesstartdate;
    /**
     * id of {@link AnswerEntity}
     */
    private Integer answerid;
    /**
     * reference to {@link PassageEntity}
     */
    private PassageEntity passages;
    /**
     * reference to {@link AnswerEntity}
     */
    private AnswerEntity answersByAnswerid;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "passagesstartdate", nullable = true)
    public Timestamp getPassagesstartdate() {
        return passagesstartdate;
    }

    public void setPassagesstartdate(Timestamp passagesstartdate) {
        this.passagesstartdate = passagesstartdate;
    }

    //TODO kek eto primary key
    @Column(name = "answerid", nullable = false)
    public Integer getAnswerid() {
        return answerid;
    }

    public void setAnswerid(Integer answerid) {
        this.answerid = answerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageHasAnswersEntity that = (PassageHasAnswersEntity) o;
        return id.equals(that.id) &&
                answerid.equals(that.answerid) &&
                Objects.equals(passagesstartdate, that.passagesstartdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, passagesstartdate, answerid);
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false), @JoinColumn(name = "passagesstartdate", referencedColumnName = "startdate", insertable = false, updatable = false)})
    public PassageEntity getPassages() {
        return passages;
    }

    public void setPassages(PassageEntity passages) {
        this.passages = passages;
    }

    @ManyToOne
    @JoinColumn(name = "answerid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public AnswerEntity getAnswersByAnswerid() {
        return answersByAnswerid;
    }

    public void setAnswersByAnswerid(AnswerEntity answersByAnswerid) {
        this.answersByAnswerid = answersByAnswerid;
    }
}
