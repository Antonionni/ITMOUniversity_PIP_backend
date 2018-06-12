package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "passages", catalog = "postgres")
@IdClass(PassageEntityPK.class)
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
    @Id
    @Column(name = "startdate", nullable = false)
    private Timestamp startdate;
    /**
     * date and time when student end pass the test
     */
    @Basic
    @Column(name = "enddate", nullable = true)
    private Timestamp enddate;
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
    @Column(name = "result", nullable = false)
    private int result;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public Timestamp getEnddate() {
        return enddate;
    }

    public void setEnddate(Timestamp enddate) {
        this.enddate = enddate;
    }

    public TestEntity getTest() {
        return test;
    }

    public void setTest(TestEntity test) {
        this.test = test;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
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
                result == that.result &&
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
}
