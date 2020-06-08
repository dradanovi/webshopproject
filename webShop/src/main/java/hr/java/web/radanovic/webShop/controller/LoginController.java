package hr.java.web.radanovic.webShop.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.enums.Authorities;
import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.AppUserDetails;
import hr.java.web.radanovic.webShop.model.Currency;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Wallet;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * holds the method mappings that are connected with the login process
 * 
 * @author demoo
 *
 */
@Slf4j
@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	/**
	 * sets the needed attributes for the login page
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String loginStart(Model model, HttpSession session) {
		if(session.getAttribute("user") != null) {
			return "redirect:/";
		}
		model.addAttribute("user", new AppUser());
		return "login";
	}

	/**
	 * set all the objects to session after successful login
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/success")
	public String loginSuccess(Model model, HttpSession session) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		log.info("--- login success ---");

		session.setAttribute("user", username);
		session.setAttribute("currency", userService.getWalletByUser().getCurrency());
		session.setAttribute("cart", new ArrayList<Product>());
		session.setAttribute("isSeller", userService.sellerExists(username));
		session.setAttribute("back", "/");
		
		return "redirect:/index";
	}

	/**
	 * set the error attribute on the login page triggered on a failed login
	 * 
	 * @param model
	 * @param redir
	 * @return
	 */
	@GetMapping("/login-error")
	public String loginError(Model model, RedirectAttributes redir) {
		redir.addAttribute("loginError", true);
		return "redirect:/login";
	}

	/**
	 * set the needed attributes for the page for registering a new user
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/registerUser")
	public String registerUser(Model model) {
		model.addAttribute("user", new AppUser());
		model.addAttribute("details", new AppUserDetails());
		return "registerUser";
	}

	/**
	 * processes the available information that are sent for registering a new user
	 * returns error if fields are empty
	 * 
	 * @param regUser
	 * @param model
	 * @param redir
	 * @return
	 */
	@PostMapping("/registerUser")
	public String registerUserPost(@ModelAttribute("registerUser") AppUser regUser,
			@ModelAttribute("details") AppUserDetails details, Model model, RedirectAttributes redir) {
		if (regUser.getUsername().equals("") || regUser.getPassword().equals("")
				|| (details.getEmail().equals("") && !details.getEmail().contains("@"))) {
			redir.addAttribute("registerEmpty", true);
			return "redirect:/registerUser";
		}
		if (userService.checkDuplicateUser(regUser.getUsername())) {
			redir.addAttribute("message", "Duplicate Username");
			log.info("error");
			return "redirect:/registerUser";
		}
		AppUser user = new AppUser(regUser.getUsername(), new BCryptPasswordEncoder().encode(regUser.getPassword()),
				true);
		user.setRolesWithRole(userService.getRolesByAuth(Authorities.ROLE_USER));
		details.setUser(user);

		Wallet wallet = new Wallet(Currency.HRK, user);
		log.info(wallet.toString());

		userService.saveUser(user, details, wallet);

		return "redirect:/login";
	}

}
