package models.entities;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import data.HibernateUtils;
import io.ebean.ExpressionList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users", catalog = "postgres")
public class UserEntity {
    /**
     * use unique identificator
     */
    private int id;
    /**
     * user Email
     */
    private String email;
    /**
     * secret password
     */
    private String password;
    /**
     * Name
     */
    private String firstname;
    /**
     * Family Name
     */
    private String secondname;
    /**
     * date and time when user was created
     */
    private Timestamp createdat;
    /**
     * date and time when user was update his profile
     */
    private Timestamp updatedat;
    /**
     * List of courses which student try to pass
     */
    private Collection<CourseEntity> teacherCourses;

    private Collection<UserRolesHasUsersEntity> roles;

    private Collection<LinkedAccount> linkedAccounts;

    private boolean emailValidated;

    private boolean active;

    private Date lastLogin;

    private String name;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email", nullable = false, length = -1)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false, length = -1)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "firstname", nullable = false, length = -1)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "secondname", nullable = false, length = -1)
    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
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

    @ManyToMany(mappedBy = "courseTeachers")
    public Collection<CourseEntity> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(Collection<CourseEntity> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    @OneToMany(mappedBy = "user")
    public Collection<UserRolesHasUsersEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRolesHasUsersEntity> roles) {
        this.roles = roles;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Collection<LinkedAccount> getLinkedAccounts() {
        return linkedAccounts;
    }

    public void setLinkedAccounts(Collection<LinkedAccount> linkedAccounts) {
        this.linkedAccounts = linkedAccounts;
    }

    @Column(name = "emailValidated")
    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    @Column(name = "isActive")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "lastLogin")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(secondname, that.secondname) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, email, password, firstname, secondname, createdat, updatedat);
    }


}
