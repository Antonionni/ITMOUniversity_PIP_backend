package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "passages_has_answers", catalog = "postgres")
@IdClass(PasssageHasAnswersPK.class)
public class PassageHasAnswersEntity {
    /**
     * uniqe identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    /**
     * date and time when
     */
    @Column(name = "passagesstartdate", nullable = true)
    private Timestamp passagesstartdate;
    /**
     * id of {@link AnswerEntity}
     */
    @Id
    @Column(name = "answerid")
    private Integer answerid;
    /**
     * reference to {@link PassageEntity}
     */
    @ManyToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false), @JoinColumn(name = "passagesstartdate", referencedColumnName = "startdate", insertable = false, updatable = false)})
    private PassageEntity passages;
    /**
     * reference to {@link AnswerEntity}
     */
    @ManyToOne
    @JoinColumn(name = "answerid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private AnswerEntity answersByAnswerid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getPassagesstartdate() {
        return passagesstartdate;
    }

    public void setPassagesstartdate(Timestamp passagesstartdate) {
        this.passagesstartdate = passagesstartdate;
    }

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

    public PassageEntity getPassages() {
        return passages;
    }

    public void setPassages(PassageEntity passages) {
        this.passages = passages;
    }

    public AnswerEntity getAnswersByAnswerid() {
        return answersByAnswerid;
    }

    public void setAnswersByAnswerid(AnswerEntity answersByAnswerid) {
        this.answersByAnswerid = answersByAnswerid;
    }
}
