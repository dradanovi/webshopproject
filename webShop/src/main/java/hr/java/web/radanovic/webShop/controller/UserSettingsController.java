package hr.java.web.radanovic.webShop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.enums.CountryEnum;
import hr.java.web.radanovic.webShop.model.AppUserDetails;
import hr.java.web.radanovic.webShop.service.AreaService;
import hr.java.web.radanovic.webShop.service.SaleService;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds method mappings that are connected to the user setting such as user
 * information
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class UserSettingsController {

	@Autowired
	private UserService userService;

	@Autowired
	private SaleService saleService;

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private HttpSession session;

	/**
	 * displays the front page for the user settings segment
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("userSettings")
	public String userSettings(Model model) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		AppUserDetails details = userService.getDetails();
		if (details.getCity() != null) {
			if (details.getCity().getId() == null) {
				details.setCity(null);
			}
		}
		model.addAttribute("user_details", details);
		model.addAttribute("cardList", saleService.getCards());
		log.info("user settings -> " + userService.getSeller());
		model.addAttribute("sellerAuthorization", userService.getSeller());
		return "userSettings";
	}

	/**
	 * produces the page that requires inputs for user data such as email, address
	 * etc.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("modifyUserDetails")
	public String modifySettings(Model model) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		log.info(userService.getDetails().toString());
		model.addAttribute("user_details", userService.getDetails());
		model.addAttribute("countryList", CountryEnum.values());
		return "modifyUserDetails";
	}

	/**
	 * saves the information presented for the user information such as address,
	 * name, email etc.
	 * 
	 * @param details
	 * @param redir
	 * @return
	 */
	@PostMapping("modifyUserDetails")
	public String modifySettingsPost(@ModelAttribute("user_details") AppUserDetails details, RedirectAttributes redir) {
		if (details.isEmpty()) {
			redir.addAttribute("detailsEmpty", true);
			return "redirect:/modifyUserDetails";
		}
		details.getCity().setName(details.getCity().getName().toLowerCase());
		details.setCity(areaService.findDuplicate(details.getCity()));
		details = userService.updateUserDetails(details);

		log.info("details after db save\n" + details.toString());
		return "redirect:/userSettings";
	}

}
