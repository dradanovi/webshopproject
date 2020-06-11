package hr.java.web.radanovic.webShop.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hr.java.web.radanovic.webShop.enums.Authorities;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class AppRole {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
	@SequenceGenerator(name = "roles_seq", sequenceName = "roles_seq", initialValue=1, allocationSize = 1)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Authorities authority;
	
	/**
	 * returns the string form of the authority without the prefix ROLE_
	 * @return
	 */
	public String getRole() {
		return authority.toString().subSequence(5, authority.toString().length()).toString();
	}
}
