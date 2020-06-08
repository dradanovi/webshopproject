package hr.java.web.radanovic.webShop.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hr.java.web.radanovic.webShop.enums.Category;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.service.CategoryService;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings for listing all products or products with broad filters
 * like category and subcategory
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class IndexController {

	private final int NUMBER_OF_PRODUCTS_PER_PAGE = 10;

	@Autowired
	private SaleService saleService;

	@Autowired
	private CategoryService catService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("categorymap", catService.getNavBar());
		model.addAttribute("newestProducts", saleService.findNewestForMain());
		model.addAttribute("bestReviews", saleService.getReviewsForMain());
		model.addAttribute("recommendations", Arrays.asList(saleService.getAvailableProductList().stream().findFirst().get()));
		return "index";
	}
	
	@GetMapping("/checkSeller")
	@ResponseBody
	public boolean check(@RequestParam("user") String user) {
		return userService.getSeller(user) != null ? true : false;
	}
	
	/**
	 * displays the product list on base page after login with no filters active
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String nofilter(Model model) {
		log.info("index get OK");
		List<Product> list = saleService.getAvailableProductList();
		listingPaging(1, list, model);
		return "listings";
	}

	/**
	 * displays the product list presented by page number with no filters active
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping("/listing/{page}")
	public String getListingPage(@PathVariable("page") int page, Model model) {
		log.info("index get " + page + " OK");
		List<Product> list = saleService.getAvailableProductList();
		listingPaging(page, list, model);
		return "listings";
	}

	/**
	 * displays product list with active filter category
	 * 
	 * @param category
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping("/listing/{page}/c")
	public String getListingCategoryPage(@RequestParam("category") Category category, @PathVariable("page") int page,
			Model model) {
		log.info("prod " + category + " page " + page);
		List<String> subList = catService.findSubCategories(category);
		List<Product> list = saleService.getAvailableProductList();
		list = list.stream().filter(e -> subList.contains(e.getCategory())).collect(Collectors.toList());
		listingPaging(page, list, model);
		return "listings";
	}

	/**
	 * displays product list with active filter subCategory
	 * 
	 * @param subCategory
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping("/listing/{page}/s")
	public String getListingSubCategoryPage(@RequestParam("subCategory") String subCategory,
			@PathVariable("page") int page, Model model) {
		List<Product> list = saleService.getAvailableProductList();
		list = list.stream().filter(e -> e.getCategory().equals(subCategory)).collect(Collectors.toList());
		listingPaging(page, list, model);
		return "listings";
	}

	/**
	 * processes the filter information and returns the filtered list
	 * 
	 * @param costFrom
	 * @param costTo
	 * @param name
	 * @param page
	 * @param model
	 * @return
	 */
	@PostMapping("/listing/{page}/filter")
	public String getListingByFilter(@RequestParam("costFrom") Long costFrom, @RequestParam("costTo") Long costTo,
			@RequestParam("name") String name, @PathVariable("page") int page, Model model) {
		log.info("" + name);
		List<Product> list = saleService.filterProducts(costFrom, costTo, name);
		listingPaging(page, list, model);
		return "listings";
	}

	/**
	 * fills the model with number of viewable pages and the products
	 * 
	 * @param page
	 * @param list
	 * @param model
	 */
	private void listingPaging(int page, List<Product> list, Model model) {
		List<Integer> pages = saleService.getPageNumbers(page, list.size() / NUMBER_OF_PRODUCTS_PER_PAGE);
		model.addAttribute("categorymap", catService.getNavBar());
		model.addAttribute("listings", list.stream().skip((page - 1) * NUMBER_OF_PRODUCTS_PER_PAGE)
				.limit(NUMBER_OF_PRODUCTS_PER_PAGE).collect(Collectors.toList()));
		model.addAttribute("pageNums", pages);
		model.addAttribute("lastPage", pages.size());
	}
}
