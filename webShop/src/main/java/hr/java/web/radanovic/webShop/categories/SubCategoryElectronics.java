package hr.java.web.radanovic.webShop.categories;

import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.enums.CategoryElectronics;
import hr.java.web.radanovic.webShop.model.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * contains information for pairing product with its associated sub category
 * electronics
 * 
 * @author demoo
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubCategoryElectronics extends Categories {

	private CategoryElectronics electronics;

	public SubCategoryElectronics(Category category, CategoryElectronics electronics, Product product) {
		super(category, product);
		this.electronics = electronics;
	}

}
