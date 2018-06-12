package models.entities;

import be.objectify.deadbolt.java.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import enumerations.RoleType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_roles_has_users", catalog = "postgres")
public class UserRolesHasUsersEntity implements Role {
    /**
     * unique identifiator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * date and time when user get his role
     */
    @Basic
    @Column(name = "startdate", nullable = true)
    private Date startdate;
    /**
     * date and time when user lose his role
     */
    @Basic
    @Column(name = "enddate", nullable = true)
    private Date enddate;
    /**
     * Enum of roles
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", nullable = false)
    private RoleType roleType;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private UserEntity user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolesHasUsersEntity that = (UserRolesHasUsersEntity) o;
        return id == that.id &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, enddate);
    }

    @Override
    @Transient
    public String getName() {
        return roleType.getRoleName();
    }
}
