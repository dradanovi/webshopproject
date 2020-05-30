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
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEWS_SEQ")
	@SequenceGenerator(name = "REVIEWS_SEQ", sequenceName = "REVIEWS_SEQ", initialValue = 1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private AppUser user;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;
	@Exclude
	@Column(name = "message")
	private String message;
	@Exclude
	@Column(name = "grade")
	private Long grade;

	/**
	 * returns true if the object is empty and false is one of the selected
	 * variables is filled
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (grade == null && message == null) ? true : false;
	}

}
