package hr.java.web.radanovic.webShop.categories;

import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.enums.CategoryAppliances;
import hr.java.web.radanovic.webShop.model.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * contains information for pairing product with its associated sub category
 * appliances
 * 
 * @author demoo
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubCategoryAppliances extends Categories {

	private CategoryAppliances appliances;

	public SubCategoryAppliances(Category category, CategoryAppliances appliances, Product product) {
		super(category, product);
		this.appliances = appliances;
	}
}
