package hr.java.web.radanovic.webShop.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.webShop.enums.Authorities;
import hr.java.web.radanovic.webShop.model.AppRole;
import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.AppUserDetails;
import hr.java.web.radanovic.webShop.model.Seller;
import hr.java.web.radanovic.webShop.model.Wallet;
import hr.java.web.radanovic.webShop.repository.HibAppRoleRep;
import hr.java.web.radanovic.webShop.repository.HibAppUserDetailsRep;
import hr.java.web.radanovic.webShop.repository.HibAppUserRep;
import hr.java.web.radanovic.webShop.repository.HibSellerRep;
import hr.java.web.radanovic.webShop.repository.HibWalletRep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private HibAppUserRep userRepo;

	@Autowired
	private HibAppRoleRep roleRepo;

	@Autowired
	private HibAppUserDetailsRep detailsRepo;

	@Autowired
	private HibWalletRep walletRepo;

	@Autowired
	private HibSellerRep sellerRepo;

	@Autowired
	private HttpSession session;

	/**
	 * finds in database the user that is currently logged in
	 * 
	 * @return
	 */
	public AppUser getUser() {
		return userRepo.findByUsername((String) session.getAttribute("user")).get();
	}

	/**
	 * finds the specific user from database with user name
	 * 
	 * @param username
	 * @return
	 */
	public AppUser getUser(String username) {
		return userRepo.findByUsername(username).get();
	}

	/**
	 * checks the database if a user with a specific user name exists TRUE if user
	 * with that user name exists in database FALSE if user doesn't exist
	 * 
	 * @param username
	 * @return
	 */
	public boolean checkDuplicateUser(String username) {
		return (userRepo.findByUsername(username).isPresent()) ? true : false;
	}

	/**
	 * saves user with all needed information to database after registering
	 * 
	 * @param user
	 * @param details
	 * @param wallet
	 */
	public void saveUser(AppUser user, AppUserDetails details, Wallet wallet) {
		userRepo.save(user);
		walletRepo.save(wallet);
		detailsRepo.save(details);
	}

	/**
	 * finds the associated authority in database that corresponds with one of the
	 * enumerations Authorities
	 * 
	 * @param auth
	 * @return
	 */
	public AppRole getRolesByAuth(Authorities auth) {
		return roleRepo.findByAuthority(auth);
	}

	/**
	 * finds wallet associated with the logged in user
	 * 
	 * @return
	 */
	public Wallet getWalletByUser() {
		return walletRepo.findByUser(getUser());
	}

	/**
	 * finds the details of the associated logged in user
	 * 
	 * @return
	 */
	public AppUserDetails getDetails() {
		return detailsRepo.findByUser(getUser()).get();
	}

	/**
	 * finds the seller of the associated logged in user
	 * 
	 * @return
	 */
	public Seller getSeller() {
		return sellerRepo.findByUser(getUser()).get();
	}

	/**
	 * finds seller that is associated with a specific user
	 * 
	 * @param username
	 * @return
	 */
	public Seller getSeller(String username) {
		return sellerRepo.findByUser(getUser(username)).get();
	}

	/**
	 * saves of updates seller to database
	 * 
	 * @param seller
	 * @return
	 */
	public Seller setSeller(Seller seller) {
		log.info("set seller rating: " + seller.getRating() + " reviews: " + seller.getReviews());
		return sellerRepo.save(seller);
	}
	
	public boolean sellerExists(String username) {
		return sellerRepo.existsByUser(getUser(username));
	}
	
	public Seller updateSeller(Seller seller) {
		return sellerRepo.update(seller);
	}

	/**
	 * updates the logged in users user information
	 * 
	 * @param details
	 * @return
	 */
	public AppUserDetails updateUserDetails(AppUserDetails details) {
		AppUserDetails dbDetails = getDetails();
		dbDetails.setForDB(details.getEmail(), details.getFirstName(), details.getLastName(), details.getAddress(),
				details.getCity());
		return detailsRepo.save(dbDetails);
	}

}
