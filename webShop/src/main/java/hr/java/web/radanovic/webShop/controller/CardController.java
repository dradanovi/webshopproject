package hr.java.web.radanovic.webShop.controller;

import java.time.YearMonth;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.enums.CardProvider;
import hr.java.web.radanovic.webShop.enums.Months;
import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings for modifying, creating and deleting debit card
 * information
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class CardController {

	@Autowired
	private UserService userService;

	@Autowired
	private SaleService saleService;
	
	@Autowired
	private HttpSession session;

	/**
	 * sets the page for creating a new debit card
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/newCard")
	public String newCard(Model model) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		model.addAttribute("card_info", new CardInfo());
		model.addAttribute("providerList", CardProvider.values());
		model.addAttribute("monthList", Months.values());
		return "newCard";
	}

	/**
	 * processes the information that are necessary for creating a new debit card
	 * and saves the information to the database via a service
	 * 
	 * @param cardInfo
	 * @param month
	 * @param year
	 * @param redir
	 * @return
	 */
	@PostMapping("/newCard")
	public String postNewCard(@ModelAttribute("card_info") CardInfo cardInfo, @RequestParam("month") int month,
			@RequestParam("year") int year, RedirectAttributes redir) {
		if (cardInfo.isEmpty()) {
			redir.addAttribute("cardEmpty", true);
			return "redirect:/newCard";
		}
		int calentarYear = Calendar.getInstance().get(Calendar.YEAR);
		YearMonth ym = YearMonth.of(calentarYear - (calentarYear % 100) + year, month);
		cardInfo.setExpirationDate(ym.atEndOfMonth());
		cardInfo.setWallet(userService.getWalletByUser());
		log.info(cardInfo.toString());
		saleService.setCard(cardInfo);
		return "redirect:/userSettings";
	}

	/**
	 * deletes the card via service
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/card/{id}")
	public String deleteCard(@PathVariable Long id) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		saleService.deleteCard(id);
		return "redirect:/userSettings";
	}
}
