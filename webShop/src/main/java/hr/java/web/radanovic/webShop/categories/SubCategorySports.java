package hr.java.web.radanovic.webShop.categories;

import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.enums.CategorySport;
import hr.java.web.radanovic.webShop.model.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * contains information for pairing product with its associated sub category
 * sport
 * 
 * @author demoo
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubCategorySports extends Categories {

	private CategorySport sport;

	public SubCategorySports(Category category, CategorySport sport, Product product) {
		super(category, product);
		this.sport = sport;
	}
}
