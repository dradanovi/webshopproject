package hr.java.web.radanovic.webShop.categories;

import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * contains the information of the super class for pairing the product with its
 * category
 * 
 * @author demoo
 *
 */
@Data
@AllArgsConstructor
public class Categories {

	private Category category;
	private Product product;

}
