package hr.java.web.radanovic.webShop.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Seller;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings that show the profiles of users and sellers
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class ProfileController {

	final int NUMBER_OF_PRODUCTS_PER_PAGE = 10;

	@Autowired
	private UserService userService;

	@Autowired
	private SaleService saleServise;
	
	@Autowired
	private HttpSession session;

	/**
	 * displays a sellers profile with all of the products that are currently
	 * available and the rating of a seller
	 * 
	 * @param username
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping("/profile/{page}/p")
	public String sellerProfile(@RequestParam("seller") String username, @PathVariable int page, Model model) {
		log.info("seller profile -> " + username);
		Seller seller = userService.getSeller(username);
		model.addAttribute("seller", seller);
		List<Product> list = saleServise.getProductListBySeller(seller);
		List<Integer> pages = saleServise.getPageNumbers(page, list.size() / NUMBER_OF_PRODUCTS_PER_PAGE);
		model.addAttribute("listings",
				list.stream().skip((page - 1) * NUMBER_OF_PRODUCTS_PER_PAGE).limit(NUMBER_OF_PRODUCTS_PER_PAGE).collect(Collectors.toList()));
		model.addAttribute("pageNums", pages);
		model.addAttribute("lastPage", pages.size());
		model.addAttribute("back", (session.getAttribute("back") != null) ? session.getAttribute("back") : "/");
		return "profile";
	}

	/**
	 * displays a users profile that show its authorities and list of reviews made
	 * by that user
	 * 
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/profile/u")
	public String userProfile(@RequestParam("user") String username, Model model) {
		log.info("user profile -> " + username);
		AppUser user = userService.getUser(username);
		model.addAttribute("user", user);
		model.addAttribute("sellerExists", (userService.getSeller(username).isEmpty()) ? false : true);
		model.addAttribute("reviewList", saleServise.getReviewsByUser(user));
		model.addAttribute("back", (session.getAttribute("back") != null) ? session.getAttribute("back") : "/");
		return "profile";
	}

}
