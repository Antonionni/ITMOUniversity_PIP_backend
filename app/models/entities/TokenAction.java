package models.entities;

import java.util.Date;

import javax.persistence.*;

import play.data.format.Formats;

@Entity
@Table(name = "tokenAction", catalog = "postgres")
public class TokenAction{

	public enum Type {
		EMAIL_VERIFICATION,
		PASSWORD_RESET
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Verification time frame (until the user clicks on the link in the email)
	 * in seconds
	 * Defaults to one week
	 */
	public final static long VERIFICATION_TIME = 7 * 24 * 3600;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int id;

	@Column(name = "token", unique = true)
	public String token;

	@ManyToOne
	public UserEntity targetUser;

	@Column(name = "type", nullable = false)
	@Enumerated
	public Type type;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created")
	public Date created;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "expires")
	public Date expires;

	public boolean isValid() {
		return this.expires.after(new Date());
	}
}
