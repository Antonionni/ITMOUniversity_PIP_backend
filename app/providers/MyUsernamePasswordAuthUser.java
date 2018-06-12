package providers;

import com.feth.play.module.pa.user.FirstLastNameIdentity;
import enumerations.RoleType;
import providers.MyUsernamePasswordAuthProvider.MySignup;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.NameIdentity;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class MyUsernamePasswordAuthUser extends UsernamePasswordAuthUser
		implements NameIdentity, FirstLastNameIdentity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final Collection<RoleType> roles;
	private final String firstName;
	private final String lastName;
	private final Date birthDate;
	private final String placeOfStudy;

	public MyUsernamePasswordAuthUser(final MySignup signup) {
		super(signup.password, signup.email);
		this.name = signup.getName();
		this.firstName = signup.getFirstName();
		this.lastName = signup.getLastName();
		this.birthDate = signup.getBirthDate();
		this.placeOfStudy = signup.getPlaceOfStudy();
		this.roles = signup.getParsedRoles();
	}

	/**
	 * Used for password reset only - do not use this to signup a user!
	 * @param password
	 */
	public MyUsernamePasswordAuthUser(final String password) {
		super(password, null);
		name = null;
		this.firstName = null;
		this.lastName = null;
		this.birthDate = null;
		this.placeOfStudy = null;
		this.roles = null;
	}

	@Override
	public String getName() {
		return name;
	}

	public Collection<RoleType> getRoles() {
		return roles;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getPlaceOfStudy() {
		return placeOfStudy;
	}
}
