package hr.java.web.radanovic.webShop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Seller;

public interface RepSeller extends CrudRepository<Seller, Long> {
	
	Optional<Seller> findByUser(AppUser user);
	
}
