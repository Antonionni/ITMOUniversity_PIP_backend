package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "passages", catalog = "postgres")
public class PassageEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    /**
     * date and time when student start pass the test
     */
    @Column(name = "startdate", nullable = false)
    private Date startdate;
    /**
     * date and time when student end pass the test
     */
    @Basic
    @Column(name = "enddate", nullable = true)
    private Date enddate;
    /**
     * reference of {@link TestEntity}
     */
    @ManyToOne
    @JoinColumn(name = "testid", referencedColumnName = "id", nullable = false)
    private TestEntity test;
    /**
     * result score
     */
    @Basic
    @Column(name = "result", nullable = true)
    private Integer result;
    /**
     * is student pass the test
     */
    @Basic
    @Column(name = "is_right", nullable = false)
    private boolean isRight;
    /**
     * refernce to {@link UserEntity}
     */
    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "passage")
    private Collection<PassageHasAnswersEntity> passage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public TestEntity getTest() {
        return test;
    }

    public void setTest(TestEntity test) {
        this.test = test;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageEntity that = (PassageEntity) o;
        return id == that.id &&
                result.equals(that.result) &&
                isRight == that.isRight &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, enddate, result, isRight);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity usersById) {
        this.user = usersById;
    }

    public Collection<PassageHasAnswersEntity> getPassages() {
        return passage;
    }

    public void setPassage(Collection<PassageHasAnswersEntity> passage) {
        this.passage = passage;
    }
}
