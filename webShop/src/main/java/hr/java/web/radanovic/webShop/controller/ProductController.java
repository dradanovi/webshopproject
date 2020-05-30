package hr.java.web.radanovic.webShop.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Currency;
import hr.java.web.radanovic.webShop.model.Expense;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.service.SaleService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings that are connected with processes pertaining products
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class ProductController {

	@Autowired
	private SaleService saleService;

	@Autowired
	private HttpSession session;

	/**
	 * sets the selected product to be put in a cart for later purchase and returns
	 * the user to the page it selected the product from
	 * 
	 * @param id
	 * @param cart
	 * @param session
	 * @param referer
	 * @return
	 */
	@PostMapping("/addToCart")
	public String postAddCard(@RequestParam("listingId") Long id, @SessionAttribute("cart") List<Product> cart,
			@RequestHeader String referer) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		log.info("Add to cart -> ProductId " + id);
		Product product = saleService.addToCart(id);
		if (saleService.checkAvailability(cart, product)) {
			cart.add(saleService.addToCart(id));
			session.setAttribute("cart", cart);
		}
		backUrl(referer, session);
		return "redirect:" + session.getAttribute("back");
	}

	/**
	 * displays all products that are put by the user in the cart and sums up all
	 * the prices
	 * 
	 * @param cart
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/cart")
	public String getCart(@SessionAttribute("cart") List<Product> cart, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("listings", cart);
		List<BigDecimal> bigDlist = cart.stream().map(e -> e.getCost()).collect(Collectors.toList());
		model.addAttribute("sum", bigDlist.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		model.addAttribute("cardList", saleService.getLast4());
		return "cart";
	}

	/**
	 * removes the selected product from the cart
	 * 
	 * @param id
	 * @param cart
	 * @param session
	 * @return
	 */
	@GetMapping("/removeFromCart")
	public String removeFromCart(@RequestParam("productId") Long id, @SessionAttribute("cart") List<Product> cart) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		cart.remove(saleService.addToCart(id));
		session.setAttribute("cart", cart);
		return "redirect:/cart";
	}

	/**
	 * removes all products that ate currently present in the cart
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("/emptyCart")
	public String emptyCart(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		session.setAttribute("cart", new ArrayList<Product>());
		return "redirect:/listing/1";
	}

	/**
	 * creates a bill that contains all products in the cart and reduces their
	 * availability per product purchased, lastly clears all products from the cart
	 * 
	 * @param cart
	 * @param card
	 * @param session
	 * @param redir
	 * @return
	 */
	@PostMapping("/payment")
	public String payment(@SessionAttribute("cart") List<Product> cart, @RequestParam("card") String card,
			HttpSession session, RedirectAttributes redir) {
		List<BigDecimal> bigDlist = cart.stream().map(e -> e.getCost()).collect(Collectors.toList());
		BigDecimal amount = bigDlist.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		CardInfo cardInfo = saleService.getCards().stream().filter(e -> e.getCardNumber().endsWith(card))
				.collect(Collectors.toList()).get(0);
//		log.info("amount " + amount.toString());
//		log.info("currency " + (Currency) session.getAttribute("currency"));
//		log.info("last 4 numbers " + card);
//		log.info("card id " + saleService.getCardByNumber(cardInfo.getCardNumber()).getId());
//		log.info("number of products " + saleService.updateProductAvailability(cart).size());
//		saleService.updateProductAvailability(cart).forEach(e -> log.info("product " + e.getId()));

		Expense expense = new Expense(amount, (Currency) session.getAttribute("currency"), LocalDateTime.now(),
				saleService.getCardByNumber(cardInfo.getCardNumber()), saleService.updateProductAvailability(cart));
		saleService.setExpense(expense);
		session.setAttribute("cart", new ArrayList<Product>());
		return "redirect:/listing/1";
	}

	/**
	 * displays the product page with all the current information that the seller
	 * put
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/product/p")
	public String getProduct(@RequestParam("id") Long id, Model model, @RequestHeader String referer) {
		log.info("product view id: " + id);
		Product product = saleService.getProduct(id);
		model.addAttribute("product", product);
		model.addAttribute("reviews", saleService.getReviewsByProduct(product));
		backUrl(referer, session);
		model.addAttribute("back", session.getAttribute("back"));
		return "products";
	}

	/**
	 * displays all the products that the user purchased, the rating that the user
	 * put on the product if available
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/expenses")
	public String getExpenses(Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("expenseMap", saleService.getExpenseReviewMap(null));
		return "expenses";
	}

	/**
	 * displays card associated products that the user purchased, the rating that
	 * the user put on the product if available
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/expenses/{id}")
	public String getExpensesByCard(@PathVariable("id") Long id, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("expenseMap", saleService.getExpenseReviewMap(id));
		return "expenses";
	}

	/**
	 * finalize product so it can be seen publicly
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/product/finalize")
	public String finalizeProduct(@RequestParam("product") Long id, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		Product product = saleService.getProduct(id);
		product.setEnabled(true);
		saleService.setProduct(product);
		return "redirect:/sellerDashboard/unfinishedListings";
	}

	/**
	 * calculates the url for the back button
	 * 
	 * @param referer
	 * @param session
	 */
	private void backUrl(String referer, HttpSession session) {
		String urlHead = "http://localhost:8080";
		String url = referer.substring(urlHead.length(), referer.length());
		log.info("url: " + url);
		if (url.equals("/") || url.contains("/listing/") || url.contains("/sellerDashboard/")) {
			session.setAttribute("back", url);
		}
	}
}
