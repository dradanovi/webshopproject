package hr.java.web.radanovic.webShop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hr.java.web.radanovic.webShop.enums.CountryEnum;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Data
@Entity
@Table(name = "country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")
	@SequenceGenerator(name = "country_seq", sequenceName = "country_seq", initialValue=1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	@Enumerated(EnumType.STRING)
	private CountryEnum countryName;
}
