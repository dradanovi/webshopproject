package hr.java.web.radanovic.webShop.model;

import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class AppUserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_DETAILS_SEQ")
	@SequenceGenerator(name = "USER_DETAILS_SEQ", sequenceName = "USER_DETAILS_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "id")
	private Long id;
	@Column(name = "email")
	private String email;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "address")
	private String address;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "city_id")
	private City city;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id")
	private AppUser user;

	public AppUserDetails(String email, String firstName, String lastName, AppUser user) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
	}

	/**
	 * returns true if the object is empty and false is one of the selected
	 * variables is filled
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if ((email.isBlank() && email.contains("@")) || firstName.isBlank() || lastName.isBlank() || address.isBlank()
				|| Optional.ofNullable(city.isEmpty()).orElse(false))
			return true;
		else
			return false;
	}

	/**
	 * setter that is used for update in database
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param city
	 */
	public void setForDB(String email, String firstName, String lastName, String address, City city) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
	}

	/**
	 * returns string of the country name that is associated with the current city
	 * 
	 * @return
	 */
	public String getCityCountry() {
		return city.getName() + " " + city.getZipCode() + " " + city.getCountry().getCountryName();
	}
}
