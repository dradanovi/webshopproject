package hr.java.web.radanovic.webShop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hr.java.web.radanovic.webShop.model.Review;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings designated for reviews
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class ReviewController {

	@Autowired
	private SaleService saleService;

	@Autowired
	private UserService userService;

	/**
	 * saves the attached information to a review via service
	 * 
	 * @param review
	 * @param id
	 * @param model
	 * @return
	 */
	@PostMapping("/review/{productId}")
	public String postReview(@ModelAttribute("review") Review review, @PathVariable("productId") Long id, Model model) {
		log.info("-------review----------");
		log.info("" + id);
		if (review.getId() != null) {
			if (Optional.ofNullable(saleService.getReview(review.getId())).isEmpty()) {
				return "redirect:/expenses";
			}
		}
		review.setProduct(saleService.getProduct(id));
		review.setUser(userService.getUser());
		saleService.setReview(review);
		log.info(review.toString());
		return "redirect:/expenses";
	}

	/**
	 * produces the page that is responsible for creating a new review
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/review/r")
	public String getReview(@RequestParam("product") Long id, Model model) {
		model.addAttribute("mode", "new");
		model.addAttribute("review", new Review());
		model.addAttribute("productId", id);
		return "review";
	}

	/**
	 * displays the selected review
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/review/s")
	public String showReview(@RequestParam("review") Long id, Model model) {
		Review review = saleService.getReview(id);
		model.addAttribute("mode", "show");
		model.addAttribute("review", review);
		model.addAttribute("productId", review.getProduct().getId());
		return "review";
	}

	/**
	 * displays the page for changing the existing review
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/review/c")
	public String changeReview(@RequestParam("review") Long id, Model model) {
		Review review = saleService.getReview(id);
		model.addAttribute("mode", "change");
		model.addAttribute("review", review);
		model.addAttribute("productId", review.getProduct().getId());
		return "review";
	}

	/**
	 * returns a list that contains all the reviews for the seller
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@GetMapping("/review/all/seller")
	public String showReviews(@RequestParam("seller") String user, Model model) {
		model.addAttribute("reviews", saleService.getAllSellerReviews(user));
		log.info(saleService.getAllSellerReviews(user).size() + "size");
		saleService.getAllSellerReviews(user).forEach(e -> log.info(e.toString()));;
		return "sellerReviews";
	}

}
