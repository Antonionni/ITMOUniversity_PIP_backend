package models.serviceEntities.UserData;

import enumerations.RoleType;
import models.entities.UserEntity;

import java.util.Collection;
import java.util.Date;

public class BaseUser {
    private int id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private Date createdAt;
    private Date updatedAt;
    private Collection<RoleType> roles;
    private boolean emailValidated;
    private boolean active;
    private Date lastLogin;

    public BaseUser(int id, String email, String name, String firstName, String lastName, Date createdAt, Date updatedAt, Collection<RoleType> roles, boolean emailValidated, boolean active, Date lastLogin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roles = roles;
        this.emailValidated = emailValidated;
        this.active = active;
        this.lastLogin = lastLogin;
    }

    public BaseUser(UserEntity userEntity) {
        this(userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getFirstname(),
                userEntity.getSecondname(),
                userEntity.getCreatedat(),
                userEntity.getUpdatedat(),
                userEntity.getRoleTypes(),
                userEntity.isEmailValidated(),
                userEntity.isActive(),
                userEntity.getLastLogin());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Collection<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleType> roles) {
        this.roles = roles;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
