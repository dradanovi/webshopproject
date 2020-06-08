package hr.java.web.radanovic.webShop.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.enums.Authorities;
import hr.java.web.radanovic.webShop.enums.CategoryAppliances;
import hr.java.web.radanovic.webShop.enums.CategoryElectronics;
import hr.java.web.radanovic.webShop.enums.CategorySport;
import hr.java.web.radanovic.webShop.enums.CountryEnum;
import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Currency;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Seller;
import hr.java.web.radanovic.webShop.service.AreaService;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds the method mappings that are connected to sellers
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class SellerController {

	private final int NUMBER_OF_PRODUCTS_PER_PAGE = 10;

	@Autowired
	private UserService userService;

	@Autowired
	private SaleService saleService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private HttpSession session;

	/**
	 * produces the page that is responsible for selecting a existing card that will
	 * be used by the seller for selling products if a card was not designated
	 * before
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard/selectCard")
	public String getSeller(Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("seller", new Seller());
		List<String> listLast4 = saleService.getLast4();
		listLast4.forEach(e -> log.info(e.toString()));
		model.addAttribute("cardList", listLast4);
		return "seller";
	}

	/**
	 * produces the page that is responsible for reselecting a existing card that is
	 * used by a seller as a seller card if a card is already designated as such
	 * 
	 * @param model
	 * @param cardId
	 * @return
	 */
	@GetMapping("/sellerDashboard/{id}")
	public String getSellerExtra(Model model, @PathVariable("id") Long cardId) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("seller", userService.getSeller());
		List<String> listLast4 = saleService.getLast4();
		listLast4.forEach(e -> log.info(e.toString()));
		model.addAttribute("cardList", listLast4);
		return "seller";
	}

	/**
	 * saves the selected card to be the seller card
	 * 
	 * @param model
	 * @param card
	 * @return
	 */
	@PostMapping("/sellerDashboard/selectCard")
	public String postSeller(Model model, @RequestParam("card") String card) {
		Seller seller = userService.getSeller();
		AppUser user = userService.getUser();
		if (seller.isEmpty()) {
			seller = new Seller(0.0, LocalDateTime.now(), Long.valueOf(0), userService.getUser(),
					saleService.getCardByNumber(card));
			user.setRolesWithRole(userService.getRolesByAuth(Authorities.ROLE_SELLER));
			log.info("--seller is null--");
		} else {
			seller.setCard(saleService.getCardByNumber(card));
			log.info("--seller exists--");
		}
		log.info("select card -> " + seller);
		userService.setSeller(seller);
		return "redirect:/userSettings";
	}

	/**
	 * displays the front page of the seller dashboard that lets the seller all new
	 * products for sale, view and remove existing products and view all sold
	 * products to date
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard")
	public String getDash(Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("seller", userService.getSeller());
		return "sellerDashboard";
	}

	/**
	 * displays a page that takes in information needed for adding a new product
	 * that will be put for sale
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard/addListing")
	public String addListing(Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		if (!model.containsAttribute("caseOption")) {
			model.addAttribute("caseOption", "add");
		}
		model.addAttribute("seller", userService.getSeller());
		if (!model.containsAttribute("product")) {
			model.addAttribute("product", new Product());
		}
		List<String> list = new ArrayList<>();
		list.addAll((Arrays.asList(CategoryElectronics.values())).stream().map(e -> e.toString())
				.collect(Collectors.toList()));
		list.addAll((Arrays.asList(CategoryAppliances.values())).stream().map(e -> e.toString())
				.collect(Collectors.toList()));
		list.addAll(
				(Arrays.asList(CategorySport.values())).stream().map(e -> e.toString()).collect(Collectors.toList()));
		model.addAttribute("productTypeList", list);
		model.addAttribute("countryList", CountryEnum.values());
		return "sellerDashboard";
	}

	/**
	 * processes and saves all attached information that is needed for a new product
	 * to be put for sale
	 * 
	 * @param product
	 * @param curr
	 * @param redir
	 * @return
	 */
	@PostMapping("/sellerDashboard/addListing")
	public String postAddListing(@ModelAttribute("product") Product product,
			@SessionAttribute("currency") Currency curr, RedirectAttributes redir) {
		log.info("-->" + "/sellerDashboard/addListing");
		if (product.isEmpty()) {
			log.info(product.toString());
			redir.addAttribute("productEmpty", true);
			redir.addFlashAttribute("product", product);
			return "redirect:/sellerDashboard/addListing";
		}

		log.info(product.toString());
		product.getCity().setName(product.getCity().getName().toLowerCase());
		product.setCity(areaService.findDuplicate(product.getCity()));
		product.setDate(LocalDateTime.now());
		product.setSeller(userService.getSeller());
		product.setCurrency(curr);
		product.setEnabled(false);
		saleService.setProduct(product);
		log.info(product.toString());
		redir.addFlashAttribute("product", product);
		redir.addFlashAttribute("caseOption", "picture");
		return "redirect:/sellerDashboard/addListing";
	}

	/**
	 * returns the seller specific unfinished listings that the seller did not
	 * finalize
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard/unfinishedListings")
	public String unfinished(Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		log.info("-->" + "/sellerDashboard/unfinishedListings");
		model.addAttribute("caseOption", "unfinished");
		model.addAttribute("seller", userService.getSeller());
		List<Product> list = saleService.unfinished();
		List<String> picList = Arrays.asList(new File("src\\main\\resources\\static\\img\\").listFiles()).stream()
				.map(e -> e.getName()).collect(Collectors.toList());
		model.addAttribute("listings", list);
		for (Product product : list) {
			model.addAttribute("pics" + product.getId(),
					picList.stream().filter(e -> e.startsWith(product.getId() + "-")).collect(Collectors.toList()));
		}

		return "sellerDashboard";
	}

	/**
	 * displays the current products that are for sale
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard/currentListings/{page}")
	public String currentListings(@PathVariable("page") int page, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("caseOption", "current");
		model.addAttribute("seller", userService.getSeller());
		List<Product> list = saleService.getProductListBySeller();
		List<Integer> pages = saleService.getPageNumbers(page, list.size() / NUMBER_OF_PRODUCTS_PER_PAGE);
		model.addAttribute("listings", list.stream().skip((page - 1) * NUMBER_OF_PRODUCTS_PER_PAGE)
				.limit(NUMBER_OF_PRODUCTS_PER_PAGE).collect(Collectors.toList()));
		model.addAttribute("pageNums", pages);
		model.addAttribute("lastPage", pages.size());
		return "sellerDashboard";
	}

	/**
	 * removes the selected product from the active available product list
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/sellerDashboard/del/{id}")
	public String deleteListing(@PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		Seller seller = userService.getSeller((String) session.getAttribute("user"));
		Product product = saleService.getProduct(id);
		log.info("condition: " + product.getSeller().equals(seller));
		if (product.getSeller().equals(seller)) {
			saleService.deleteProduct(id);
		}
		return "redirect:/sellerDashboard/currentListings";
	}

	/**
	 * displays all completed and pending products sales to date
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/sellerDashboard/salesList/{page}")
	public String salesList(@PathVariable("page") int page, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("caseOption", "sales");
		model.addAttribute("seller", userService.getSeller());
		List<Product> list = saleService.getAllSales();
		List<Integer> pages = saleService.getPageNumbers(page, list.size() / NUMBER_OF_PRODUCTS_PER_PAGE);
		model.addAttribute("listings", list.stream().skip((page - 1) * NUMBER_OF_PRODUCTS_PER_PAGE)
				.limit(NUMBER_OF_PRODUCTS_PER_PAGE).collect(Collectors.toList()));
		model.addAttribute("pageNums", pages);
		model.addAttribute("lastPage", pages.size());
		return "sellerDashboard";
	}

}
