package models.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;

@Entity
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
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

	@Id
	public Long id;

	@Column(unique = true)
	public String token;

	@ManyToOne
	public UserEntity targetUser;

	public Type type;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date created;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date expires;

	public static final Find<Long, TokenAction> find = new Find<Long, TokenAction>(){};

	public static TokenAction findByToken(final String token, final Type type) {
		return find.where().eq("token", token).eq("type", type).findUnique();
	}

	public static void deleteByUser(final UserEntity u, final Type type) {
		QueryIterator<TokenAction> iterator = find.where()
				.eq("targetUser.id", u.id).eq("type", type).findIterate();
		while(iterator.hasNext()) {
			Ebean.delete(iterator.next());
		}
		iterator.close();
	}

	public boolean isValid() {
		return this.expires.after(new Date());
	}

	public static TokenAction create(final Type type, final String token,
			final UserEntity targetUser) {
		final TokenAction ua = new TokenAction();
		ua.targetUser = targetUser;
		ua.token = token;
		ua.type = type;
		final Date created = new Date();
		ua.created = created;
		ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
		ua.save();
		return ua;
	}
}
