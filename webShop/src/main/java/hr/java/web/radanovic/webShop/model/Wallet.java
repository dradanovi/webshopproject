package hr.java.web.radanovic.webShop.model;

import javax.persistence.CascadeType;
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
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WALLET_SEQ")
	@SequenceGenerator(name = "WALLET_SEQ", sequenceName = "WALLET_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Currency currency;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id")
	private AppUser user;
	
	public Wallet(Currency currency, AppUser user) {
		super();
		this.currency = currency;
		this.user = user;
	}	
}
