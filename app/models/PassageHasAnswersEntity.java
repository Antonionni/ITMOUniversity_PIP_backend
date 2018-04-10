package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "passages_has_answers", schema = "public", catalog = "postgres")
public class PassageHasAnswersEntity {
    /**
     * uniqe identificator
     */
    private int id;
    /**
     * date and time when
     */
    private Timestamp passagesstartdate;
    /**
     * id of {@link AnswerEntity}
     */
    private int answerid;
    /**
     * reference to {@link PassageEntity}
     */
    private PassageEntity passages;
    /**
     * reference to {@link AnswerEntity}
     */
    private AnswerEntity answersByAnswerid;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "passagesstartdate", nullable = true)
    public Timestamp getPassagesstartdate() {
        return passagesstartdate;
    }

    public void setPassagesstartdate(Timestamp passagesstartdate) {
        this.passagesstartdate = passagesstartdate;
    }

    @Id
    @Column(name = "answerid", nullable = false)
    public int getAnswerid() {
        return answerid;
    }

    public void setAnswerid(int answerid) {
        this.answerid = answerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageHasAnswersEntity that = (PassageHasAnswersEntity) o;
        return id == that.id &&
                answerid == that.answerid &&
                Objects.equals(passagesstartdate, that.passagesstartdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, passagesstartdate, answerid);
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id", nullable = false), @JoinColumn(name = "passagesstartdate", referencedColumnName = "startdate")})
    public PassageEntity getPassages() {
        return passages;
    }

    public void setPassages(PassageEntity passages) {
        this.passages = passages;
    }

    @ManyToOne
    @JoinColumn(name = "answerid", referencedColumnName = "id", nullable = false)
    public AnswerEntity getAnswersByAnswerid() {
        return answersByAnswerid;
    }

    public void setAnswersByAnswerid(AnswerEntity answersByAnswerid) {
        this.answersByAnswerid = answersByAnswerid;
    }
}
