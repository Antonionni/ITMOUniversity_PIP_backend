package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "passages_has_answers", catalog = "postgres")
public class PassageHasAnswersEntity {
    /**
     * uniqe identificator
     */
    @EmbeddedId
    private PassageHasAnswersPK id;

    @Column(name = "passagesstartdate", nullable = true)
    private Date startdate;
    /**
     * reference to {@link PassageEntity}
     */
    @ManyToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false), @JoinColumn(name = "passagesstartdate", referencedColumnName = "startdate", insertable = false, updatable = false)})
    private PassageEntity passage;

    /**
     * reference to {@link AnswerEntity}
     */
    @ManyToOne
    @JoinColumn(name = "answerid", referencedColumnName = "id", nullable = false)
    @MapsId("answerId")
    private AnswerEntity answer;

    @Column(name = "textanswer", nullable = true)
    private String textAnswer;

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date passagesstartdate) {
        this.startdate = passagesstartdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageHasAnswersEntity that = (PassageHasAnswersEntity) o;
        return id.equals(that.id) &&
                answer.getId() == (that.answer.getId()) &&
                Objects.equals(startdate, that.startdate);
    }

    public PassageHasAnswersPK getId() {
        return id;
    }

    public void setId(PassageHasAnswersPK id) {
        this.id = id;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, answer.getId());
    }

    public PassageEntity getPassage() {
        return passage;
    }

    public void setPassage(PassageEntity passages) {
        this.passage = passages;
    }

    public AnswerEntity getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerEntity answersByAnswerid) {
        this.answer = answersByAnswerid;
    }
}
