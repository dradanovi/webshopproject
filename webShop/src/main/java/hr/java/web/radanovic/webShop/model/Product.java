package hr.java.web.radanovic.webShop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ")
	@SequenceGenerator(name = "PRODUCT_SEQ", sequenceName = "PRODUCT_SEQ", initialValue = 1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "product_type")
	private String category;
	@Column(name = "cost")
	private BigDecimal cost;
	@Column(name = "currency")
	@Enumerated(EnumType.STRING)
	private Currency currency;
	@Column(name = "available")
	private Integer numberAvailable;
	@Column(name = "date_listed")
	private LocalDateTime date;
	@Column(name = "description")
	private String description;
	@Column(name = "enabled")
	private boolean enabled;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "city_id")
	private City city;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "seller_id")
	private Seller seller;

	/**
	 * returns true if the object is empty and false is one of the selected
	 * variables is filled
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (city.isEmpty() || name.isBlank() || category.isBlank() || cost == null || numberAvailable == null)
				? true
				: false;
	}

	/**
	 * returns the creation date in a specific format
	 * 
	 * @return
	 */
	public String printDate() {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}
}
