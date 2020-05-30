package hr.java.web.radanovic.webShop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.webShop.categories.Categories;
import hr.java.web.radanovic.webShop.categories.SubCategoryAppliances;
import hr.java.web.radanovic.webShop.categories.SubCategoryElectronics;
import hr.java.web.radanovic.webShop.categories.SubCategorySports;
import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.enums.CategoryAppliances;
import hr.java.web.radanovic.webShop.enums.CategoryElectronics;
import hr.java.web.radanovic.webShop.enums.CategorySport;
import hr.java.web.radanovic.webShop.model.Product;

@Service
public class CategoryService {

	/**
	 * finds and returns the category of the received Product object or it returns
	 * null if no matches found
	 * 
	 * @param product
	 * @return
	 */
	public Categories findCategory(Product product) {
		for (CategoryElectronics subCategory : CategoryElectronics.values()) {
			if (subCategory.toString().equals(product.getCategory())) {
				return new SubCategoryElectronics(Category.Electronics, subCategory, product);
			}
		}
		for (CategoryAppliances subCategory : CategoryAppliances.values()) {
			if (subCategory.toString().equals(product.getCategory())) {
				return new SubCategoryAppliances(Category.Appliances, subCategory, product);
			}
		}
		for (CategorySport subCategory : CategorySport.values()) {
			if (subCategory.toString().equals(product.getCategory())) {
				return new SubCategorySports(Category.Sports, subCategory, product);
			}
		}
		return null;
	}

	/**
	 * returns Map<String, List<String>> of categories and sub categories that is
	 * used in the navigation bar
	 * 
	 * @return
	 */
	public Map<String, List<String>> getNavBar() {
		Map<String, List<String>> navBar = new HashMap<String, List<String>>();
		navBar.put(Category.Electronics.toString(), Arrays.asList(CategoryElectronics.values()).stream()
				.map(e -> e.toString()).collect(Collectors.toList()));
		navBar.put(Category.Appliances.toString(), Arrays.asList(CategoryAppliances.values()).stream()
				.map(e -> e.toString()).collect(Collectors.toList()));
		navBar.put(Category.Sports.toString(),
				Arrays.asList(CategorySport.values()).stream().map(e -> e.toString()).collect(Collectors.toList()));
		return navBar;
	}

	/**
	 * finds the associated category for the received sub category
	 * 
	 * @param sub
	 * @return
	 */
	public Categories findRelevantCategory(String sub) {
		for (CategoryElectronics subCategory : CategoryElectronics.values()) {
			if (subCategory.toString().equals(sub)) {
				return new SubCategoryElectronics(Category.Electronics, subCategory, new Product());
			}
		}
		for (CategoryAppliances subCategory : CategoryAppliances.values()) {
			if (subCategory.toString().equals(sub)) {
				return new SubCategoryAppliances(Category.Appliances, subCategory, new Product());
			}
		}
		for (CategorySport subCategory : CategorySport.values()) {
			if (subCategory.toString().equals(sub)) {
				return new SubCategorySports(Category.Sports, subCategory, new Product());
			}
		}

		return null;
	}

	/**
	 * finds all sub categories that are associated with the received category and
	 * if category has no associated sub categories returns an empty list of strings
	 * 
	 * @param category
	 * @return
	 */
	public List<String> findSubCategories(Category category) {
		if (category.equals(Category.Electronics)) {
			return (Arrays.asList(CategoryElectronics.values())).stream().map(e -> e.toString())
					.collect(Collectors.toList());
		} else if (category.equals(Category.Appliances)) {
			return (Arrays.asList(CategoryAppliances.values())).stream().map(e -> e.toString())
					.collect(Collectors.toList());
		} else if (category.equals(Category.Sports)) {
			return (Arrays.asList(CategorySport.values())).stream().map(e -> e.toString()).collect(Collectors.toList());
		}
		return new ArrayList<String>();
	}
}
