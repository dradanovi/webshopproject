package hr.java.web.radanovic.webShop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPENSE_SEQ")
	@SequenceGenerator(name = "EXPENSE_SEQ", sequenceName = "EXPENSE_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "id")
	private Long id;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "currency")
	@Enumerated(EnumType.STRING)
	private Currency currency;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	@Column(name = "date_closed")
	private LocalDateTime datePayed;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id")
	private CardInfo cardUsed;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "PRODUCT_EXPENSE", joinColumns = @JoinColumn(name = "EXPENSE_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "id"))
	private List<Product> products = new ArrayList<>();

	public Expense(BigDecimal amount, Currency currency, LocalDateTime dateCreated, CardInfo cardUsed,
			List<Product> products) {
		this.amount = amount;
		this.currency = currency;
		this.dateCreated = dateCreated;
		this.cardUsed = cardUsed;
		this.products = new ArrayList<>(products);
	}

	public void setProducts(List<Product> products) {
		this.products = new ArrayList<>(products);
	}

	public List<Product> getProducts() {
		return new ArrayList<>(products);
	}

	public void setProductsWithList(List<Product> products) {
		this.products = new ArrayList<>(products);
	}

	public void setProductsWithProduct(Product product) {
		if (getProducts().contains(product))
			return;
		products.add(product);
	}

	/**
	 * returns string that is representing a expense number and is made by taking
	 * the id of the expense and filling the rest of the spaces with zeros
	 * 
	 * @return
	 */
	public String getExpenseNumber() {
		String expneseNumber = "0000000000000000000";
		String id = this.id.toString();
		expneseNumber = expneseNumber.substring(0, expneseNumber.length() - id.length());
		return expneseNumber + id;
	}

	/**
	 * returns the creation date in a specific format
	 * 
	 * @return
	 */
	public String printDateCreated() {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		return dateCreated.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	/**
	 * returns the payment date in a specific format
	 * 
	 * @return
	 */
	public String printDatePayed() {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		return datePayed.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}
}
