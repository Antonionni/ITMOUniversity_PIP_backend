package models;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.feth.play.module.pa.user.PicturedIdentity;

public class TelegramIdentity extends AuthUser implements FirstLastNameIdentity, PicturedIdentity {
    private String firstName;
    private String lastName;
    private String name;
    private String picture;
    private String id;
    private String provider;

    public TelegramIdentity(String id) {
        this.id = id;
        this.provider = "telegram";
    }

    public TelegramIdentity(String id, String firstName, String lastName, String name, String picture) {
        this(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.picture = picture;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getProvider() {
        return provider;
    }
}
