package hr.java.web.radanovic.webShop.repository;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Wallet;

public interface RepWallet extends CrudRepository<Wallet, Long> {

	Wallet findByUser(AppUser user);
	
	
}
