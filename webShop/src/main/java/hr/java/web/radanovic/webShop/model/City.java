package hr.java.web.radanovic.webShop.model;

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
import lombok.EqualsAndHashCode.Exclude;

@Data
@Entity
@Table(name = "city")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CITY_SEQ")
	@SequenceGenerator(name = "CITY_SEQ", sequenceName = "CITY_SEQ", initialValue = 1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "zip_code")
	private String zipCode;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "country_id")
	private Country country;

	/**
	 * returns true if the object is empty and false is one of the selected
	 * variables is filled
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (name.isBlank() || zipCode.isBlank()) ? true : false;

	}
}
