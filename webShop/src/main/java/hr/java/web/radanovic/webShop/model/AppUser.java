package hr.java.web.radanovic.webShop.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
	@SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	private String username;
	private String password;
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_AUTHORITY", joinColumns = @JoinColumn(name = "USERS_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "id"))
	private Set<AppRole> roles = new HashSet<>();

	public AppUser(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public void setRoles(Set<AppRole> roles) {
		this.roles = new HashSet<>(roles);
	}

	public Set<AppRole> getRoles() {
		return new HashSet<>(roles);
	}

	public void setRolesWithList(List<AppRole> roles) {
		this.roles = new HashSet<>(roles);
	}

	public void setRolesWithRole(AppRole auth) {
		if (getRoles().contains(auth))
			return;
		roles.add(auth);
	}
}
