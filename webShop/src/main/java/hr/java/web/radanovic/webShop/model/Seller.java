package hr.java.web.radanovic.webShop.model;

import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "seller")
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLERS_SEQ")
	@SequenceGenerator(name = "SELLERS_SEQ", sequenceName = "SELLERS_SEQ", initialValue = 1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@Column(name = "rating", precision = 1, scale = 1)
	private Double rating;
	@Column(name = "date_created")
	private LocalDateTime date;
	@Column(name = "reviews")
	private Long reviews;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id")
	private AppUser user;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id")
	private CardInfo card;

	public Seller(Double rating, LocalDateTime date, Long reviews, AppUser user, CardInfo card) {
		super();
		this.rating = rating;
		this.date = date;
		this.reviews = reviews;
		this.user = user;
		this.card = card;
	}

	/**
	 * returns true if the object is empty and false is one of the selected
	 * variables is filled
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (user == null || card == null) ? true : false;
	}
}
