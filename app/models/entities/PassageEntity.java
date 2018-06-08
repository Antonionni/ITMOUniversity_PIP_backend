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
    @Basic
    @Column(name = "testid", nullable = false)
    private int testid;
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
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private UserEntity usersById;

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

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
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
                testid == that.testid &&
                result == that.result &&
                isRight == that.isRight &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, enddate, testid, result, isRight);
    }

    public UserEntity getUsersById() {
        return usersById;
    }

    public void setUsersById(UserEntity usersById) {
        this.usersById = usersById;
    }
}
