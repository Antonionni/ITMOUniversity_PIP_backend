package models;

import com.google.gson.annotations.SerializedName;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static models.User.TABLE;

@Entity
@Table(name = TABLE)
public class User extends Model {

    public static final String TABLE = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_SECOND_NAME = "second_name";

    @Id
    @Column(name = COLUMN_ID)
    public Long mId;

    @Column(name = COLUMN_LOGIN)
    @SerializedName(COLUMN_LOGIN)
    public String mLogin;

    @Column(name = COLUMN_PASSWORD)
    @SerializedName(COLUMN_PASSWORD)
    public String mPassword;

    @Column(name = COLUMN_EMAIL)
    @SerializedName(COLUMN_EMAIL)
    public String mEmail;

    @Column(name = COLUMN_FIRST_NAME)
    @SerializedName(COLUMN_FIRST_NAME)
    public String mFirstName;

    @Column(name = COLUMN_SECOND_NAME)
    @SerializedName(COLUMN_SECOND_NAME)
    public String mSecondName;

    public User(String login, String password, String email, String firstName, String secondName) {
        mLogin = login;
        mPassword = password;
        mEmail = email;
        mFirstName = firstName;
        mSecondName = secondName;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mLogin='" + mLogin + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mSecondName='" + mSecondName + '\'' +
                '}';
    }
}
