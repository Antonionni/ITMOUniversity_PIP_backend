package models.serviceEntities.UserData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import enumerations.RoleType;
import models.entities.UserEntity;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Value.Style(jdkOnly = true)
public class BaseUser {
    private int id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private Date createdAt;
    private Date updatedAt;
    private List<RoleType> roles;
    private boolean emailValidated;
    private boolean active;
    private Date lastLogin;
    private Date birthDate;
    private String photoUrl;

    public BaseUser(int id, String email, String name, String firstName, String lastName, Date createdAt, Date updatedAt, List<RoleType> roles, boolean emailValidated, boolean active, Date lastLogin, Date birthDate, String photoUrl) {
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
        this.birthDate = birthDate;
        this.photoUrl = photoUrl;
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
                userEntity.getLastLogin(),
                userEntity.getBirthDate(),
                userEntity.getPhotoUrl());
    }

    public BaseUser() {}

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

    @JsonDeserialize(as = ArrayList.class, contentAs = RoleType.class)
    public List<RoleType> getRoles() {
        return roles;
    }

    @JsonDeserialize(as = ArrayList.class, contentAs = RoleType.class)
    public void setRoles(List<RoleType> roles) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
