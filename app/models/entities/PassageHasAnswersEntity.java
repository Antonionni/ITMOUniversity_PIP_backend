package models.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "passages_has_answers", catalog = "postgres")
public class PassageHasAnswersEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "passagesstartdate", nullable = true)
    private Date startdate;
    /**
     * reference to {@link PassageEntity}
     */
    @ManyToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false), @JoinColumn(name = "passagesstartdate", referencedColumnName = "startdate", insertable = false, updatable = false)})
    private PassageEntity passage;

    @ManyToOne
    private QuestionEntity questionEntity;

    /**
     * reference to {@link AnswerEntity}
     */
    @ManyToMany
    @JoinTable(name = "passage_answers",
            joinColumns = @JoinColumn(name = "passageId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "answerId", referencedColumnName = "id"))
    private Collection<AnswerEntity> answers;

    @Column(name = "textanswer", nullable = true)
    private String textAnswer;

    @Column(name = "isverified")
    private Boolean isVerifiedByTeacher;

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date passagesstartdate) {
        this.startdate = passagesstartdate;
    }

    public Boolean getVerifiedByTeacher() {
        return isVerifiedByTeacher;
    }

    public void setVerifiedByTeacher(Boolean verifiedByTeacher) {
        isVerifiedByTeacher = verifiedByTeacher;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<AnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<AnswerEntity> answers) {
        this.answers = answers;
    }

    public PassageEntity getPassage() {
        return passage;
    }

    public void setPassage(PassageEntity passages) {
        this.passage = passages;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageHasAnswersEntity that = (PassageHasAnswersEntity) o;
        return id == that.id &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(passage, that.passage) &&
                Objects.equals(questionEntity, that.questionEntity) &&
                Objects.equals(answers, that.answers) &&
                Objects.equals(textAnswer, that.textAnswer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, passage, questionEntity, answers, textAnswer);
    }
}
