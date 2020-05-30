package hr.java.web.radanovic.webShop.model;

import java.time.LocalDate;
import java.util.Optional;

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

import hr.java.web.radanovic.webShop.enums.CardProvider;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Data
@Entity
@Table(name = "card_info")
public class CardInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARD_INFO_SEQ")
	@SequenceGenerator(name = "CARD_INFO_SEQ", sequenceName = "CARD_INFO_SEQ", initialValue = 1, allocationSize = 1)
	@Exclude
	@Column(name = "id")
	private Long id;
	@Column(name = "cardholder_first_name")
	private String cardHolderFirstName;
	@Column(name = "cardholder_last_name")
	private String cardHolderLastName;
	@Column(name = "card_number")
	private String cardNumber;
	@Column(name = "csc")
	private Integer csc;
	@Column(name = "card_provider")
	@Enumerated(EnumType.STRING)
	private CardProvider provider;
	@Column(name = "date_expired")
	// 2007-12-03T10:15:30
	private LocalDate expirationDate;
	@Exclude
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;

	public boolean isEmpty() {
		return (cardHolderFirstName.isBlank() || cardHolderLastName.isBlank() || cardNumber.isBlank()
				|| (Optional.ofNullable(csc).orElse(0) == 0) || Optional.ofNullable(provider).isEmpty()) ? true : false;
	}

	/**
	 * returns string of the last 4 digits of the card number for displaying on the
	 * site
	 * 
	 * @return
	 */
	public String getLast4() {
		return cardNumber.substring(cardNumber.length() - 4);
	}

}
